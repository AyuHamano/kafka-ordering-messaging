# Sistema de Mensageria com Apache Kafka e Spring Boot

## Visão Geral

Este projeto implementa um sistema de mensageria distribuído utilizando Apache Kafka como broker de mensagens e Spring Boot para os serviços produtores e consumidores. O sistema foi desenvolvido com foco em escalabilidade, tolerância à falha e idempotência.

## Arquitetura dos Serviços

### Componentes Principais

1. **Order Service** - Serviço responsável por persistir os pedidos no banco de dados e publicar eles no tópico "orders"
2. **Inventory Service** - Serviço que consome e processa mensagens do tópico "orders", publica resultado do estoque em "inventory-events"
2. **Notification Service** - Serviço que consome e processa mensagens dos tópico "inventory-events" e manda um e-mail de confirmação
3. **Apache Kafka** - Broker de mensagens distribuído
4. **Apache Zookeeper** - Coordenação e configuração do cluster Kafka

### Estrutura do Projeto


| Directory Name            | Projeto      | 
|---------------------------|--------------------|
| generic-react-ecommerce   | Front-end com React   | 
| inventory-service         | Java Spring Boot com Kafka |
| notification-service      | Java Spring Boot com Kafka  | 
| order-service             | Java Spring Boot com Kafka   | 


## Como Executar a Solução

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Kafka 3.71
- Git

### Passo a Passo

1. **Clone o repositório:**

2. **Execute os serviços dentro dapasta do kafka (para windows)**
    ```
   .\bin\windows\kafka-server-start.bat .\config\server.properties
    ```
  
    ```
    .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
    ```

4. **Execute os serviços:**
   ```
   # Terminal 1 - Order Service
   cd order-service
   mvn spring-boot:run
   
   # Terminal 2 - Inventory Service
   cd inventory-service
   mvn spring-boot:run

   # Terminal 3 - Notification Service
   cd notification-service
   mvn spring-boot:run
   ```

5. **Teste o sistema:**
   ```bash
   curl -X POST http://localhost:8001/api/orders \
   -H "Content-Type: application/json" \
   -d '{
    "customerName": "seu nome",
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
- Exemplo de configuração feita no Order-sevice e inventory-service:
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
- Configuração no Spring Boot de inventory-service e notification-service
  ```yaml
  spring:
    kafka:
      consumer:
        group-id: inventory-group
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
 @Retryable(value = {Exception.class})
    public void sendOrder(OrderDto order) throws ExecutionException, InterruptedException, TimeoutException {
        try{
            kafkaTemplate.send("orders", order.id().toString(), order).get(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.out.println(("Erro ao enviar pedido: " +  e.getMessage()));
            throw e;
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
               .partitions(10)
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

3. **Producer com Retry e Recover:**
  O padrão Retry e Recover é uma estratégia essencial para lidar com falhas temporárias em sistemas distribuídos, garantindo que operações que dependem de recursos externos (como bancos de dados, APIs ou brokers de mensagens) tenham múltiplas chances de serem executadas antes de serem consideradas falhas definitivas.
   ```java
   @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendOrder(OrderDto order) throws ExecutionException, InterruptedException, TimeoutException {
        try{
            kafkaTemplate.send("orders", order.id().toString(), order).get(5, TimeUnit.SECONDS);
            logger.info("Pedido {} enviado com sucesso", order.id());

        } catch (Exception e) {
            logger.error(("Erro ao enviar pedido: " +  e.getMessage()));
            throw e;
        }
    }

    @Recover
    public void sendOrderToDlq(Exception e, OrderDto order) {
        logger.error("Enviando pedido {} para DLQ após falhas: {}", order.id(), e.getMessage());
        kafkaTemplate.send("orders.DLQ", order.id().toString(), order);
    }
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
   ```java
   //habilitar a idempotência
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
   ```

2. **Implementação no Consumidor**
   ```java
     props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed"); // Importante!
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
   ```

3. **Implementando Chaves Únicas**
   ```java
   public void sendOrder(OrderDto order) throws ExecutionException, InterruptedException, TimeoutException {
        try{

          //order id é a chave única
            kafkaTemplate.send("orders", order.id().toString(), order).get(5, TimeUnit.SECONDS);
            logger.info("Pedido {} enviado com sucesso", order.id());

        }
        catch (Exception e) {
            logger.error(("Erro ao enviar pedido: " +  e.getMessage()));
            throw e;
        }
    }
   ```


**Padrões de Idempotência:**
- **Exactly-Once Semantics:** Configuração no Kafka para garantia de entrega única
- **Database Constraints:** Unique constraints para evitar duplicatas
- **Versionamento:** Control de versão de entidades para detecção de mudanças
- **Timestamps:** Verificação de ordem temporal das mensagens


## Conclusão

Este sistema de mensageria implementa as melhores práticas para sistemas distribuídos de alta disponibilidade, garantindo escalabilidade através do particionamento, tolerância à falha via replicação e retry mechanisms, e idempotência através de chaves de deduplicação e controle transacional.

A arquitetura suporta crescimento horizontal tanto de produtores quanto consumidores, mantendo a consistência e confiabilidade dos dados mesmo em cenários de falha.
