import {Avatar, Grid, Typography} from "@mui/material"
import {ReactNode} from "react";

interface CategoryPosterProps {
    id?: number
    name: string
    children?: ReactNode
}


export function CategoryPoster({name, children}: CategoryPosterProps) {

    return (
        <Grid item xs={2} justifyContent={'center'}>
            <Grid item>
                <Avatar sx={{bgcolor: 'secondary.main', width: 80, height: 80}}>
                    {children}
                </Avatar>
            </Grid>
            <Typography sx={{mt: 2}} textAlign={'center'} variant={'body1'} color={"primary.main"}>
                {name}
            </Typography>
        </Grid>
    )
}