import {AppBar, Badge, Box, InputBase, Toolbar, Typography, useMediaQuery, useTheme} from "@mui/material";
import AcUnitIcon from '@mui/icons-material/AcUnit';
import SearchIcon from '@mui/icons-material/Search';
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import CategoriesHorizontalMenu from "./CategoriesHorizontalMenu.tsx";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {setSearchQuery} from "../../redux/slices/searchSlice.ts";

export default function HorizontalMenu() {
    const theme = useTheme()
    const dispatch = useDispatch();
    const items = useSelector((state) => state.cart.items)

    const isMiddle = useMediaQuery(theme.breakpoints.down("md"))
    const isMobile = useMediaQuery(theme.breakpoints.down("sm"))

    const navigation = useNavigate()
    const goTo = () => {
        navigation(`/generic-react-ecommerce/products`)
    }

    const goToCart = () => {
        navigation(`/generic-react-ecommerce/cart`)
    }

    const searchRef = useSelector(state => state.search.query)

    const handleSearch = (event) => {
        const query = event.target.value;
        dispatch(setSearchQuery(query));
    };

    return (
        <AppBar sx={{
            backgroundColor: theme.palette.primary.main,
        }}>
            <Toolbar sx={{display: "flex", justifyContent: "space-between", alignItems: "center"}}>

                <Box
                    sx={{ml: 4, display: "flex", justifyContent: "flex-start", alignItems: "center", cursor: 'pointer'}}
                    onClick={() => goTo()}>
                    <AcUnitIcon fontSize='large'/>
                    {!isMobile && <Typography variant='h4'>
                        LoopStore
                    </Typography>}
                </Box>
                <Box sx={{display: "flex", alignItems: "center"}}>

                    <InputBase

                        style={{
                            minWidth: isMiddle ? 250 : 500,
                            borderRadius: 5,
                            backgroundColor: "white",
                            borderColor: theme.palette.primary.main,
                            marginInline: 4
                        }}
                        placeholder={"Search by title..."}
                        value={searchRef}
                        onChange={handleSearch}
                        startAdornment={<SearchIcon fontSize="small" sx={{marginInline: 1}}/>}/>
                </Box>

                <Box sx={{display: "flex", alignItems: "center", gap: 2}}>
                    <Badge badgeContent={items.length} color="error">
                        <ShoppingCartIcon onClick={() => goToCart()} fontSize="large" style={{cursor: 'pointer'}}/>
                    </Badge>
                    <AccountCircleIcon fontSize="large"/>
                </Box>

            </Toolbar>

            <CategoriesHorizontalMenu/>
        </AppBar>
    )
}
