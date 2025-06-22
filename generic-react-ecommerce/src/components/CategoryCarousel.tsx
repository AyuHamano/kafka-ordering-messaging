import Carousel from "react-multi-carousel";

export function CategoryCarousel() {

    return (
        <Carousel
            additionalTransfrom={0}
            arrows
            className=""
            dotListClass=""
            draggable
            infinite
            itemClass=""
            keyBoardControl
            pauseOnHover
            shouldResetAutoplay
            showDots={false}
            sliderClass=""
            slidesToSlide={1}
            swipeable
            responsive={{

                desktop: {
                    breakpoint: {
                        max: 3000,
                        min: 1024
                    },
                    items: 3,
                    partialVisibilityGutter: 40
                },
                mobile: {
                    breakpoint: {
                        max: 464,
                        min: 0
                    },
                    items: 1,
                    partialVisibilityGutter: 30
                },
                tablet: {
                    breakpoint: {
                        max: 1024,
                        min: 464
                    },
                    items: 2,
                    partialVisibilityGutter: 30
                }
            }}>
            <img
                src={"https://img.goodfon.com/original/1920x1080/8/f7/apple-watch-series-2-apple-watch-series-2-apple-smartwatch-i.jpg"}
                width={500} height={500}/>
            <img
                src={"https://img.goodfon.com/original/1920x1080/8/f7/apple-watch-series-2-apple-watch-series-2-apple-smartwatch-i.jpg"}
                width={500} height={500}/>
            <img
                src={"https://img.goodfon.com/original/1920x1080/8/f7/apple-watch-series-2-apple-watch-series-2-apple-smartwatch-i.jpg"}
                width={500} height={500}/>
            <img
                src={"https://img.goodfon.com/original/1920x1080/8/f7/apple-watch-series-2-apple-watch-series-2-apple-smartwatch-i.jpg"}
                width={500} height={500}/>
            <img
                src={"https://img.goodfon.com/original/1920x1080/8/f7/apple-watch-series-2-apple-watch-series-2-apple-smartwatch-i.jpg"}
                width={500} height={500}/>

        </Carousel>
    )
}

