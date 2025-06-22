export const BASEURL = 'https://dummyjson.com/products'

export const getProductById = (id: string | undefined) => {
    return BASEURL + '/' + id
}

export const getCategoryProducts = (category: string) => {
    return BASEURL + '/category/' + category
}

export const getProductsSearch = () => {
    return BASEURL + '/search'
}