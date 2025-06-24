# Sistema de Mensageria com Apache Kafka e Spring Boot

## Visão Geral

Este projeto implementa um sistema de mensageria distribuído utilizando Apache Kafka como broker de mensagens e Spring Boot para os serviços produtores e consumidores. O sistema foi desenvolvido com foco em escalabilidade, tolerância à falha e idempotência.

## Arquitetura dos Serviços

### Componentes Principais

1. **Producer Service** - Serviço responsável por publicar mensagens nos tópicos Kafka
2. **Consumer Service** - Serviço que consome e processa mensagens dos tópicos
3. **Apache Kafka** - Broker de mensagens distribuído
4. **Apache Zookeeper** - Coordenação e configuração do cluster Kafka

### Estrutura do Projeto

```

```

## Como Executar a Solução

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker e Docker Compose
- Git

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone <url-do-repositorio>
   cd sistema-mensageria-kafka
   ```

4. **Execute os serviços:**
   ```bash
   # Terminal 1 - Consumer Service
   cd consumer-service
   mvn spring-boot:run
   
   # Terminal 2 - Producer Service
   cd producer-service
   mvn spring-boot:run
   ```

5. **Teste o sistema:**
   ```bash
   curl -X POST http://localhost:8001/api/messages \
   -H "Content-Type: application/json" \
   -d '{
    "customerName": "ayu",
    "email": "seuemail@gmail.com",
    "items":[
        {"productId": 4, "quantity": 1}
    ]

   ```

## Questões Técnicas

### 1. Escalabilidade

**Como conseguir escalabilidade com o Kafka:**

A escalabilidade no Apache Kafka é alcançada através de várias estratégias:

**Particionamento Horizontal:**
- Cada tópico pode ser dividido em múltiplas partições
- Partições são distribuídas entre diferentes brokers
- Permite processamento paralelo das mensagens
- Exemplo de configuração:
  ```java
  @Component
  public class KafkaTopicConfig {
      @Bean
      public NewTopic orderTopic() {
          return TopicBuilder.name("orders")
                  .partitions(10) // 10 partições para paralelismo
                  .replicas(3)    // 3 réplicas para redundância
                  .build();
      }
  }
  ```

**Escalabilidade de Consumidores:**
- Consumer Groups permitem múltiplos consumidores processarem o mesmo tópico
- Cada partição é consumida por apenas um consumidor do grupo
- Adição dinâmica de consumidores para aumentar throughput
- Configuração no Spring Boot:
  ```yaml
  spring:
    kafka:
      consumer:
        group-id: order-processing-group
        auto-offset-reset: earliest
        max-poll-records: 100
  ```

**Escalabilidade de Brokers:**
- Cluster Kafka pode ser expandido adicionando novos brokers
- Redistribuição automática de partições
- Load balancing entre brokers
- Alta disponibilidade através de replicação

**Estratégias de Particionamento:**
```java
@Service
public class MessageProducer {
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public void sendMessage(String orderId, Order order) {
        // Particionamento por chave para garantir ordem
        kafkaTemplate.send("orders", orderId, order);
    }
}
```

### 2. Tolerância à Falha

**Definição:**
Tolerância à falha é a capacidade do sistema continuar operando corretamente mesmo quando alguns componentes falham. No contexto do Kafka, isso significa manter a disponibilidade e integridade dos dados mesmo com falhas de brokers, rede ou consumidores.

**Situação de Falha - Exemplo Prático:**

**Cenário:** Um broker do cluster Kafka falha durante o processamento de pedidos de e-commerce.

**Impacto sem tolerância à falha:**
- Perda de mensagens não replicadas
- Indisponibilidade de partições específicas
- Interrupção do processamento

**Como o Kafka trata a falha:**

1. **Replicação de Dados:**
   ```java
   @Bean
   public NewTopic orderTopic() {
       return TopicBuilder.name("orders")
               .partitions(6)
               .replicas(3) // 3 réplicas - tolera falha de 2 brokers
               .config("min.insync.replicas", "2") // Mínimo 2 réplicas sincronizadas
               .build();
   }
   ```

2. **Configuração de Produtor Resiliente:**
   ```yaml
   spring:
     kafka:
       producer:
         acks: all # Aguarda confirmação de todas as réplicas
         retries: 3
         retry-backoff-ms: 1000
         delivery-timeout-ms: 30000
   ```

3. **Consumer com Retry e Dead Letter Queue:**
   ```java
   @Component
   public class OrderConsumer {
       
       @KafkaListener(topics = "orders")
       public void processOrder(Order order) {
           try {
               orderService.processOrder(order);
           } catch (Exception e) {
               // Rejeita mensagem para retry automático
               throw new RetryableException("Falha temporária", e);
           }
       }
       
       @KafkaListener(topics = "orders.DLT")
       public void handleFailedOrder(Order order) {
           // Processa mensagens que falharam múltiplas vezes
           errorService.handleFailedOrder(order);
       }
   }
   ```

4. **Configuração de Retry:**
   ```yaml
   spring:
     kafka:
       consumer:
         enable-auto-commit: false
       listener:
         ack-mode: manual_immediate
   ```

**Mecanismos de Recuperação:**
- **Leader Election:** Kafka automaticamente elege novo leader para partições órfãs
- **ISR (In-Sync Replicas):** Mantém lista de réplicas sincronizadas
- **Broker Discovery:** Clientes automaticamente descobrem novos brokers
- **Offset Management:** Committs manuais para garantir processamento

### 3. Idempotência

**Definição:**
Idempotência garante que processar a mesma mensagem múltiplas vezes produz o mesmo resultado que processá-la uma única vez. É crucial em sistemas distribuídos onde duplicações podem ocorrer devido a retries, falhas de rede ou rebalanceamento.

**Implementação da Idempotência:**

1. **Idempotência no Produtor:**
   ```yaml
   spring:
     kafka:
       producer:
         enable-idempotence: true # Evita duplicatas no nível do broker
         max-in-flight-requests-per-connection: 1
         retries: 3
   ```

2. **Chaves Idempotentes na Aplicação:**
   ```java
   @Entity
   public class ProcessedMessage {
       @Id
       private String messageId;
       private LocalDateTime processedAt;
       private String status;
   }
   
   @Service
   public class IdempotentOrderProcessor {
       
       @Autowired
       private ProcessedMessageRepository processedRepo;
       
       @Transactional
       public void processOrder(Order order) {
           String messageId = order.getId() + "-" + order.getTimestamp();
           
           // Verifica se já foi processada
           if (processedRepo.existsById(messageId)) {
               log.info("Mensagem {} já processada, ignorando", messageId);
               return;
           }
           
           try {
               // Processa o pedido
               orderService.createOrder(order);
               
               // Marca como processada
               ProcessedMessage processed = new ProcessedMessage();
               processed.setMessageId(messageId);
               processed.setProcessedAt(LocalDateTime.now());
               processed.setStatus("SUCCESS");
               processedRepo.save(processed);
               
           } catch (Exception e) {
               // Registra falha para debug
               ProcessedMessage failed = new ProcessedMessage();
               failed.setMessageId(messageId);
               failed.setProcessedAt(LocalDateTime.now());
               failed.setStatus("FAILED");
               processedRepo.save(failed);
               throw e;
           }
       }
   }
   ```

3. **Headers para Deduplicação:**
   ```java
   @Component
   public class MessageProducer {
       
       public void sendOrder(Order order) {
           ProducerRecord<String, Order> record = new ProducerRecord<>(
               "orders", 
               order.getId(), 
               order
           );
           
           // Adiciona headers para deduplicação
           record.headers().add("idempotency-key", 
               (order.getId() + "-" + System.currentTimeMillis()).getBytes());
           record.headers().add("correlation-id", 
               order.getCorrelationId().getBytes());
           
           kafkaTemplate.send(record);
       }
   }
   ```

4. **Estratégia com Redis para Cache de Deduplicação:**
   ```java
   @Service
   public class DeduplicationService {
       
       @Autowired
       private RedisTemplate<String, String> redisTemplate;
       
       public boolean isAlreadyProcessed(String messageId) {
           String key = "processed:" + messageId;
           return redisTemplate.hasKey(key);
       }
       
       public void markAsProcessed(String messageId) {
           String key = "processed:" + messageId;
           // TTL de 24 horas para limpeza automática
           redisTemplate.opsForValue().set(key, "true", Duration.ofHours(24));
       }
   }
   ```

**Padrões de Idempotência:**
- **Exactly-Once Semantics:** Configuração no Kafka para garantia de entrega única
- **Database Constraints:** Unique constraints para evitar duplicatas
- **Versionamento:** Control de versão de entidades para detecção de mudanças
- **Timestamps:** Verificação de ordem temporal das mensagens

## Monitoramento e Observabilidade

### Métricas do Kafka
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### Health Checks
```java
@Component
public class KafkaHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        try {
            // Verifica conectividade com Kafka
            kafkaAdmin.listTopics();
            return Health.up()
                    .withDetail("kafka", "Connected")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("kafka", "Disconnected")
                    .withException(e)
                    .build();
        }
    }
}
```

## Configurações de Produção

### Segurança
```yaml
spring:
  kafka:
    security:
      protocol: SASL_SSL
    ssl:
      trust-store-location: /path/to/truststore.jks
      trust-store-password: password
    jaas:
      enabled: true
```

### Performance Tuning
```yaml
spring:
  kafka:
    producer:
      batch-size: 16384
      linger-ms: 5
      buffer-memory: 33554432
    consumer:
      fetch-min-size: 1
      fetch-max-wait: 500
      max-poll-records: 500
```

## Conclusão

Este sistema de mensageria implementa as melhores práticas para sistemas distribuídos de alta disponibilidade, garantindo escalabilidade através do particionamento, tolerância à falha via replicação e retry mechanisms, e idempotência através de chaves de deduplicação e controle transacional.

A arquitetura suporta crescimento horizontal tanto de produtores quanto consumidores, mantendo a consistência e confiabilidade dos dados mesmo em cenários de falha.