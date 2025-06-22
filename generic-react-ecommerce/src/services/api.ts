import axios from "axios";

const axiosInstance = axios.create({
    baseURL: 'https://dummyjson.com',
    timeout: 15000
})


export const Api = {
    get: (url: string, params?: { q: string | undefined }) => axiosInstance.get(url, {params}),
    post: (url: string, body: any ) => axiosInstance.post(url, body)
}