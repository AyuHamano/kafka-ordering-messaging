import {createBrowserRouter} from "react-router-dom";
import Home from "../pages/home/Home.tsx";
import Layout from "../layout/Layout.tsx";
import {Login} from "../pages/Login.tsx";
import {ProductView} from "../pages/product/ProductView.tsx";
import {CartView} from "../pages/cart/CartView.tsx";

const routerPaths = createBrowserRouter([
    {
        path: "generic-react-ecommerce",
        element: <Layout/>,
        children: [
            {
                path: '', element: <Home/>
            }
            , {
                path: 'login', element: <Login/>
            },
            {
                path: 'home',
                element: <Home/>
            },
            {
                path: 'products',
                element: <Home/>
            }, {
                path: 'products/category/:category',
                element: <Home/>
            },
            {
                path: 'products/:id',
                element: <ProductView/>
            },
            {
                path: 'cart',
                element: <CartView/>
            }]
    }

])

export default routerPaths
