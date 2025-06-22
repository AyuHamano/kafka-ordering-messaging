
import {Api} from "../services/api.ts";


interface OrdemItem { 
    id: number
    quantity: number
}


export async function usePostCart({url, body} : {url: string, body: {customerName: string, email: string, items: OrdemItem}}) {

      
        try {
            const response = await Api.post(url, body)
            if (response.status === 200) {
               console.log("sucesso")

            }
        } catch (error) {
            console.log(error)
        }
    

}