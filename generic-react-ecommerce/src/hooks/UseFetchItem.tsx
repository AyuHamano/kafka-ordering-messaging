import {useEffect, useState} from "react";
import {Api} from "../services/api.ts";

function UseFetchItem<T>(url: string) {
    const [data, setData] = useState<T>();

    async function getItem(url: string) {
        try {
            const response = await Api.get(url)
            if (response.status === 200) {
                setData(response.data)
            }
        } catch (e) {
            console.log(e)
        }
    }

    useEffect(() => {
        getItem(url).then()
    }, []);

    return {data};

}

export default UseFetchItem