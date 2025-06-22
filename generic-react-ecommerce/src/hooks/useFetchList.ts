import {useEffect, useState} from "react";
import {Api} from "../services/api.ts";
import {useSelector} from "react-redux";

export interface useFetchProps {
    url: string
    search?: string
    type: string
    limit?: number
    skip?: number
}

export function useFetchList<T>({url, search, type, limit, skip}: useFetchProps) {

    const [data, setData] = useState<T[]>([])
    const [total, setTotal] = useState(0)

    const searchRef = useSelector((state: any) => state.search.query)


    async function getProductList() {
        const params = {
            q: searchRef,
            limit: limit,
            skip: skip
        }

        try {
            const response = await Api.get(url, params)
            if (response.status === 200) {
                if (type === 'products') {
                    setData(response.data?.products)
                    setTotal(response.data?.total)
                } else setData(response.data)

            }
        } catch (error) {
            console.log(error)
        }
    }


    useEffect(() => {
        getProductList().then()

    }, [searchRef, skip])

    return {data, total}
}