import {Container, Grid} from "@mui/material";
import {useState} from "react";
import {Outlet} from "react-router";
import HorizontalMenu from "./horizontal-menu/HorizontalMenu.tsx";
import ProductsListSearch from "../pages/product/ProductsList.tsx";
import {useSelector} from "react-redux";

function Layout() {
    const searchRef = useSelector((state: any) => state.search.query);

    return (
        <div  style={{height:"100%", width: "100%"}}>
            <HorizontalMenu/>
           
                {searchRef ?
                    <ProductsListSearch/>
                    : <Outlet/>
                }
            
        </div>
    )

}

export default Layout