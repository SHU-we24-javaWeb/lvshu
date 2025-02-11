// carousel.js
document.addEventListener('DOMContentLoaded', function() {
    const carouselInner = document.getElementById('carouselInner');

    // 定义图片和标题内容
    const slides = [
        {
            img: 'https://img.zcool.cn/community/031rqdfipqluron1tur9uls3935.png',
            alt: '北京',
            title: '北京三日游完全攻略'
        },
        {
            img: 'https://th.bing.com/th/id/R.3cf01ec05f272a9e9dd166b76534fcac?rik=ugRSZ%2fSeMYjFmA&pid=ImgRaw&r=0',
            alt: '上海',
            title: '魔都上海精选攻略'
        },
        {
            img: 'https://th.bing.com/th/id/R.b56aa4bb51cabc689de0e62b44e9ded1?rik=68%2bi6nBs8vAjaA&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50062%2f5802.jpg_wh1200.jpg&ehk=fapBxRe0u39jJ8t3P3G2Qobt2aqqpWZaTACd54Ycyzc%3d&risl=&pid=ImgRaw&r=0',
            alt: '杭州',
            title: '杭州西湖游玩攻略'
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