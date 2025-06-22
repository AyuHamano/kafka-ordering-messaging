import {ReviewType} from "./ReviewType.ts";
import {DimensionType} from "./DimensionType.ts";

export interface ProductType {
    id?: number
    title: string
    thumbnail: string
    description: string
    price?: number
    discountPercentage?: number
    rating?: number
    brand?: string
    category?: string
    reviews?: ReviewType[]
    dimensions?: DimensionType;
    returnPolicy?: string;
    warrantyInformation?: string;
    weight?: number
    quantity?: number
}