import {Card, CardContent, Grid, Rating, Typography} from "@mui/material";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import moment from "moment";

export interface UserReviewProps {
    reviewerName: string;
    comment: string
    rating: number
    date: string
    isMobile?: boolean
}

export function UserReview({reviewerName, comment, date, rating, isMobile}: UserReviewProps) {

    const toIsoDate = (date: string) => moment(date).format('DD/MM/YYYY');
    const flexDirection = isMobile ? 'column' : 'row'

    return (
        <Card sx={{my: 3}}>
            <CardContent>
                <Grid item display={'flex'} alignItems={'center'} flexDirection={flexDirection}
                      justifyContent={isMobile ? 'center' : 'flex-start'}>
                    <AccountCircleIcon/>
                    <Typography sx={{mx: 2}}>{reviewerName}</Typography>
                    <Rating readOnly value={rating}></Rating>
                </Grid>


                <Typography variant='subtitle2' sx={{mt: 2}}>{toIsoDate(date)}</Typography>
                <Typography>{comment}</Typography>
            </CardContent>
        </Card>
    )
}