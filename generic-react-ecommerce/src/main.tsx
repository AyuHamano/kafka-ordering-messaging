import {createRoot} from "react-dom/client";
import {RouterProvider} from "react-router-dom";
import {createTheme, ThemeProvider} from "@mui/material";
import routerPaths from "./routes/RouterPaths.tsx";
import {grey} from "@mui/material/colors";
import {store} from "./redux/store.ts";
import {Provider} from "react-redux";


const theme = createTheme({
    palette: {
        primary: {
            main: '#21094E',
            light: '#511281',
        },
        secondary: {
            main: '#511281',
            contrastText: grey[500]
        }
    },
})

createRoot(document.getElementById('root')!).render(
    <ThemeProvider theme={theme}>
        <Provider store={store}>
            <RouterProvider router={routerPaths}/>
        </Provider>
    </ThemeProvider>
)
