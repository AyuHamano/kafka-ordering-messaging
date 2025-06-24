import {
    Button,
    Container,
    Divider,
    Grid,
    MenuItem,
    Rating,
    Select,
    Typography,
    useMediaQuery,
    useTheme
} from "@mui/material";
import {ProductType} from "../../types/ProductType.ts";
import UseFetchItem from "../../hooks/UseFetchItem.tsx";
import {getProductById} from "../../services/apiUrls.ts";
import {useParams} from "react-router-dom";
import {UserReview} from "./components/UserReview.tsx";
import {useDispatch} from "react-redux";
import {addToCart} from "../../redux/slices/cartSlice.ts";
import {useState} from "react";

export function ProductView() {

    const [quantity, setQuantity] = useState(1);

    const {id} = useParams()

    const dispatch = useDispatch();

    const url = getProductById(id)

    const {data} = UseFetchItem<ProductType>(url)

    const theme = useTheme()

    const isMobile = useMediaQuery(theme.breakpoints.down('md'));

    const handleAddShoppingCart = () => {
        dispatch(addToCart({id: data?.id, name: data?.title, price: data?.price, quantity: quantity, thumbnail: data?.thumbnail}))
    }

    const handleSelectChange = (e: any) => {
        
        setQuantity(e.target.value)
    }

    return (
        (!!data ?
    (<Grid container spacing={6} sx={{my: 10}} display={'flex'} alignItems="center" justifyContent={'center'}>
        <Container maxWidth="lg" sx={{
            display: 'flex',
            marginTop: 5,
            // marginInline: 5,
            flexDirection: isMobile ? 'column' : 'row',
            alignItems: 'center'
        }}>

            <Grid item xs={8} display={'flex'} justifyContent={'center'}>
                <img src={data?.thumbnail} width={350} height={400} style={{objectFit: 'cover'}}/>
            </Grid>
            <Grid item xs={10} display={'flex'} flexDirection='column' justifyContent={'center'}>
                <Grid sx={{my: 2}}>
                    <Typography variant="h5">
                        {data?.title}
                    </Typography>
                    <Typography>
                        {data?.description}
                    </Typography>
                </Grid>

                <Grid display={'flex'}>
                    <Typography sx={{mr: 1}}>{data?.rating}</Typography>
                    <Rating name="read-only" value={data?.rating ?? 0} readOnly/>
                </Grid>

                <Typography variant='subtitle2'>
                    {data?.reviews?.length + ' users reviews'}
                </Typography>

                <Grid sx={{mt: 2}} display={'flex'} alignItems={'center'}>
                    <Typography variant='h5' sx={{mr: 3}}>$ {data?.price}</Typography>
                    <Typography color={'error'}>- {data?.discountPercentage}%</Typography>
                </Grid>


                <Grid sx={{mb: 2}} display={'flex'} alignItems={'center'}>
                    <Typography variant='subtitle2' sx={{mr: 1}}>Quantity:</Typography>
                    <Select onChange={handleSelectChange}  size={'small'} value={quantity}>
                        <MenuItem value={1}>1</MenuItem>
                        <MenuItem value={2}>2</MenuItem>
                        <MenuItem value={3}>3</MenuItem>
                    </Select>
                </Grid>

                <Grid>
                    <Button onClick={handleAddShoppingCart} style={{borderRadius: 20}} variant={'contained'}>Add to
                        basket</Button>
                </Grid>
            </Grid>
        </Container>

        <Container>

            <Divider sx={{my: 2}}/>

            <Grid item xs={12} display={'flex'} justifyContent={'space-around'}
                  flexDirection={isMobile ? 'column' : 'row'} sx={{mx: 10}}>

                <Grid item xs={12}>
                    <Typography variant={'h5'} sx={{mt: 2}}>Reviews</Typography>

                    {
                        data?.reviews?.map((item, index) => (
                            <UserReview rating={item.rating} reviewerName={item.reviewerName}
                                        comment={item.comment}
                                        date={item.date} isMobile={isMobile}/>
                        ))
                    }
                </Grid>

                <Grid item xs={12} display={'flex'} alignItems={isMobile ? 'center' : 'flex-end'}
                      flexDirection={'column'}>
                    <Typography variant={'h5'} sx={{mb: 2}}>Product specification</Typography>

                    <Grid item xs={12} display={'flex'} alignItems={'flex-start'} flexDirection={'column'}>

                        <Typography>Weight: {data?.weight}</Typography>
                        <Typography>Dimensions: {data?.dimensions?.depth} x {data?.dimensions?.width} x {data?.dimensions?.height} cm</Typography>
                        <Typography>Return Policy: {data?.returnPolicy}</Typography>
                        <Typography>Warranty Information: {data?.warrantyInformation}</Typography>
                    </Grid>
                </Grid>
            </Grid>
        </Container>
    </Grid>)
            : null))
}


