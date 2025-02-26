// carousel.js
document.addEventListener('DOMContentLoaded', function() {
    const carouselInner = document.getElementById('carouselInner');

    // 定义图片和标题内容
    const slides = [
        {
            img: 'images/1.png',
            alt: '模块1',
            title: '模块1介绍'
        },
        {
            img: 'images/2.png',
            alt: '模块2',
            title: '模块2介绍'
        },
        {
            img: 'images/3.png',
            alt: '模块3',
            title: '模块3介绍'
        }
    ];

    // 动态创建滑动项
    slides.forEach((slide, index) => {
        const div = document.createElement('div');
        div.classList.add('carousel-item');
        if (index === 0) div.classList.add('active'); // 第一个项添加 active 类

        const img = document.createElement('img');
        img.src = slide.img;
        img.alt = slide.alt;
        img.classList.add('d-block', 'w-100');

        div.appendChild(img);
        carouselInner.appendChild(div);
    });
});