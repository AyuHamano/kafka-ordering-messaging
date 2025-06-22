import {Button, Grid, Toolbar, useTheme} from "@mui/material";

export default function CategoriesHorizontalMenu() {
    const theme = useTheme()
    return (
        <Grid sx={{backgroundColor: theme.palette.primary.light}}>
            <Toolbar style={{minHeight: 50, gap: 30}}>
                <Button color="inherit" size={'small'}>Eletronics</Button>
                <Button color="inherit" size={'small'}>Self Care</Button>
                <Button color="inherit" size={'small'}>Home Decoration</Button>
                <Button color="inherit" size={'small'}>Grosseries</Button>
                <Button color="inherit" size={'small'}>Women</Button>
                <Button color="inherit" size={'small'}>Men</Button>
            </Toolbar>

        </Grid>
    )
}