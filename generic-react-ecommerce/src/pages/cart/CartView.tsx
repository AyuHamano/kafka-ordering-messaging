import {Alert, Button, Card, CardContent, CardHeader, Container, Dialog, DialogActions, DialogContent, DialogTitle, Divider, Grid, IconButton, TextField, Typography} from "@mui/material";
import {useDispatch, useSelector} from "react-redux";
import DeleteIcon from '@mui/icons-material/Delete';
import CheckIcon from '@mui/icons-material/Check';
import { ProductType } from "../../types/ProductType";
import { clearCart, clearTotalValue, removeFromCart, updateQuantity } from "../../redux/slices/cartSlice";
import { useState } from "react";
import { usePostCart } from "../../services/kafka-cart-api";

export function CartView() {
    const cartItems = useSelector((state: any) => state.cart.items);
    const totalValue = useSelector((state: any) => state.cart.totalValue);
    const dispatch = useDispatch()
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [openFinishDialog, setOpenFinishDialog] = useState(false);
    const [alert, setAlert] = useState(false);
    const [message, setMessage] = useState('')

    async function postCart() {
        const body = {
            items: cartItems.map((item:ProductType) => ({
                productId: item.id,
                quantity: item.quantity
            })),
            email: email,
            customerName: name
        }
        setAlert(true)
        dispatch(clearCart())
        try {
            const response: any = await usePostCart({url: "http://localhost:8001/api/orders", body: body})
            setMessage(response)
            setOpenFinishDialog(false)
        } catch {

        }
    }

    const handleRemoveItem = (itemId: number, price: number) => {
        dispatch(removeFromCart(itemId));
        dispatch(clearTotalValue(price))
    };

    const handleQuantityChange = (itemId: number, newQuantity: number) => {
        const quantity = Math.max(1, newQuantity);
        dispatch(updateQuantity({ id: itemId, quantity }));
    };

    return (
        <Grid container sx={{backgroundColor: '#ede7e7', height: '100vh', width: '100%', padding: 20}} >
            <Grid item xs={12}>
                {alert &&
                <Alert icon={<CheckIcon fontSize="inherit" />} severity="success">
                   {message}
                </Alert>}
                <Card>
                    <CardContent>
                        {/* Cart Items List */}
                        {cartItems.map((item: any) => (
                            <Grid container key={item.id} sx={{py: 2}} alignItems="center">
                                <Grid item xs={2}>
                                    <img width={90} height={90} src={item.thumbnail}/>
                                </Grid>
                                
                                <Grid item xs={2}>
                                    <Typography variant="body1">
                                        {item.name}
                                    </Typography>
                                </Grid>
                                
                                <Grid item xs={2}>
                                    <TextField
                                        type="number"
                                        variant="outlined"
                                        size="small"
                                        value={item.quantity}
                                        onChange={(e) => {
                                            console.log(e.target.value)
                                            const newValue = parseInt(e.target.value); // Se for inválido, usa 1
                                            handleQuantityChange(item.id, Math.max(1, newValue));}}
                                        inputProps={{
                                            min: 1,
                                            style: { textAlign: 'center' }
                                        }}
                                        sx={{ width: 80 }}
                                    />
                                </Grid>
                                
                                <Grid item xs={2}>
                                    <Typography variant="body1">
                                        ${item.price.toFixed(2)}
                                    </Typography>
                                </Grid>
                                
                                <Grid item xs={2}>
                                    <Typography variant="body1" textAlign="right">
                                        ${(item.price * item.quantity || 0).toFixed(2)}
                                    </Typography>
                                </Grid>
                                
                                <Grid item xs={1} textAlign="right">
                                    <IconButton
                                        onClick={() => handleRemoveItem(item.id, item.price)}
                                        color="error"
                                        aria-label="remove from cart"
                                    >
                                        <DeleteIcon />
                                    </IconButton>
                                </Grid>
                            </Grid>
                        ))}
                        
                        <Divider sx={{my: 2}} />
                        
                        {/* Total */}
                        <Grid container sx={{py: 2}}>
                            <Grid item xs={8}>
                                <Typography variant="h6">
                                    Total:
                                </Typography>
                            </Grid>
                            {cartItems.length > 0 && <Grid item xs={4}>
                                <Typography variant="h6" textAlign="right">
                                    ${totalValue.toFixed(2)}
                                    <Button onClick={() => setOpenFinishDialog(true)} sx={{ml: 10}} variant="contained">Finish Order</Button>
                                </Typography>
                            </Grid>}
                        </Grid>
                    </CardContent>
                </Card>
            </Grid>

            <Dialog open={openFinishDialog} onClose={() => setOpenFinishDialog(false)}>
                <DialogTitle>Finalizar Compra</DialogTitle>
                <DialogContent>
                    <Typography gutterBottom>
                        Por favor, insira seu e-mail para receber os detalhes da compra:
                    </Typography>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Name"
                        fullWidth
                        variant="standard"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        label="Email"
                        type="email"
                        fullWidth
                        variant="standard"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpenFinishDialog(false)}>Cancelar</Button>
                    <Button 
                        onClick={postCart}
                        disabled={!email.includes('@') || !name}
                        color="primary"
                    >
                        Confirmar
                    </Button>
                </DialogActions>
            </Dialog>
        </Grid>
    )
}