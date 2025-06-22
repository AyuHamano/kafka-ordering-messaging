import { Grid, Pagination, Typography} from "@mui/material";

import {useState} from "react";
import {useFetchList, useFetchProps} from "../../hooks/useFetchList.ts";
import {ProductPoster} from "./components/ProductPoster.tsx";
import {ProductType} from "../../types/ProductType.ts";
import {getCategoryProducts, getProductsSearch} from "../../services/apiUrls.ts";
import {useSelector} from "react-redux";

function ProductsListSearch({ category}: { category?: string }) {
    const url = category ? getCategoryProducts(category) : getProductsSearch()

    const [page, setPage] = useState(1)

    const searchRef = useSelector((state: any) => state.search.query)


    const params: useFetchProps = {
        url: url,
        search: searchRef,
        type: 'products',
        limit: 20,
        skip: !searchRef ? (page - 1) * 20 : 0,
    }
    const {data, total} = useFetchList<ProductType>(params)

    const pagesNumber = () => {
        const number = total / 20
        return Math.floor(number)
    }

    return (
        <Grid item container spacing={1} justifyContent={'center'} display={'flex'} sx={{my: 15, height: '100%'}}>
            {data?.length > 0 ?
                (<>
                        {data?.map(item =>

                            <ProductPoster product={item}/>
                        )}
                        <Grid item xs={12} justifyContent={'center'} display={'flex'}>
                            {total > 20 && <Pagination count={pagesNumber()} color="primary"
                                                       onChange={(e, value) => {
                                                           setPage(value)
                                                           console.log(e)
                                                       }}/>}
                        </Grid>
                    </>
                ) :
                <Grid item xs={12} justifyContent={'center'} display={'flex'} sx={{my: 10}}><Typography>There's no items to show</Typography></Grid>   }
        </Grid>
    )
}

export default ProductsListSearch