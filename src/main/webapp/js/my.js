// 标签页切换
document.querySelectorAll('.tab-btn').forEach(button => {
    button.addEventListener('click', function () {
        // 移除所有标签的active类
        document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
        // 为当前点击的标签添加active类
        this.classList.add('active');

        // 切换内容显示
        const targetId = this.getAttribute('data-target');
        document.querySelectorAll('.tab-pane').forEach(pane => {
            pane.classList.remove('show', 'active');
        });
        document.querySelector(targetId).classList.add('show', 'active');
    });
});

// 头像预览
document.querySelector('input[name="avatar"]').addEventListener('change', function (e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const preview = document.getElementById('avatarPreview');
            preview.src = e.target.result;
            preview.style.display = 'block';
        }
        reader.readAsDataURL(file);
    }
});

// // 更新个人资料
// function updateProfile() {
//     const formData = new FormData(document.getElementById('editProfileForm'));
//
//     // 这里添加向后端发送数据的逻辑
//     fetch('/api/user/update-profile', {
//         method: 'POST',
//         body: formData
//     })
//         .then(response => response.json())
//         .then(data => {
//             if (data.success) {
//                 alert('个人资料更新成功！');
//                 $('#editProfileModal').modal('hide');
//                 // 刷新页面或更新显示
//                 location.reload();
//             } else {
//                 alert('更新失败：' + data.message);
//             }
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             alert('更新失败，请稍后重试');
//         });
// }

// 编辑攻略
function editGuide(guideId) {
    window.location.href = `edit-guide.html?id=${guideId}`;
}

// 在页面加载完成后获取用户数据
document.addEventListener('DOMContentLoaded', function () {
    loadUserProfile();
});

// 更新用户信息
function loadUserProfile() {
    fetch('/lvshu/profileServlet')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 更新用户基本信息
                const user = data.data.user;
                document.getElementById('username').textContent = user.username;
                document.getElementById('userAvatar').src = user.avatar || 'images/default-avatar.jpg';
                document.getElementById('userSignature').textContent = user.signature || '这个人很懒，什么都没写~';
                document.getElementById('gender').textContent = user.gender == 'O' ? '沃尔玛购物袋' : (user.gender == 'F' ? '女' : '男');
                document.getElementById('email').textContent = user.email || '这个人很懒，什么都没写~';
                document.getElementById('mobilePhone').textContent = user.mobilePhone || '这个人很懒，什么都没写~';
                document.getElementById('major').textContent = user.major || '这个人很懒，什么都没写~';
                document.getElementById('nativePlace').textContent = user.nativePlace || '这个人很懒，什么都没写~';

                // 更新统计数据
                const stats = data.data.stats;
                document.getElementById('favoriteCount').textContent = stats.favoriteCount;
                document.getElementById('followersCount').textContent = stats.followersCount;
                document.getElementById('followingCount').textContent = stats.followingCount;

                // 更新我的攻略列表
                const myGuidesContainer = document.getElementById('myGuides');
                myGuidesContainer.innerHTML = data.data.guides.map(guide => `
                            <div class="collection-card">
                                <img src="${guide.coverImage || 'images/default-cover.jpg'}"
                                     alt="${escapeHtml(guide.title)}"
                                     class="collection-image">
                                <div class="collection-content">
                                    <h3>${escapeHtml(guide.title)}</h3>
                                    <p>${escapeHtml(guide.content.substring(0, 100))}...</p>
                                    <div class="collection-meta">
                                        <span><i class="bi bi-eye"></i> ${guide.viewCount}</span>
                                        <span><i class="bi bi-heart"></i> ${guide.favoriteCount}</span>
                                        <div class="action-buttons">
                                            <a href="guide-detail.html?id=${guide.guideId}"
                                               class="edit-btn">
                                                查看详情
                                            </a>
                                            <a href="edit-guide.html?id=${guide.guideId}" 
                                               class="edit-btn">
                                                <i class="bi bi-pencil"></i> 编辑
                                            </a>
                                            <form action="/lvshu/deleteGuideServlet" method="post" 
                                                  style="display: inline-block;"
                                                  onsubmit="return confirm('确定要删除这篇攻略吗？');">
                                                <input type="hidden" name="guideId" value="${guide.guideId}">
                                                <button type="submit" class="edit-btn">
                                                    <i class="bi bi-trash"></i> 删除
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        `).join('');

                // 更新我的收藏列表
                const myFavoritesContainer = document.getElementById('myFavorites');
                myFavoritesContainer.innerHTML = data.data.favorites.map(guide => `
                            <div class="collection-card">
                                <img src="${guide.coverImage || 'images/default-cover.jpg'}"
                                     alt="${escapeHtml(guide.title)}"
                                     class="collection-image">
                                <div class="collection-content">
                                    <h3>${escapeHtml(guide.title)}</h3>
                                    <p>${escapeHtml(guide.content.substring(0, 100))}...</p>
                                    <div class="collection-meta">
                                        <span><i class="bi bi-eye"></i> ${guide.viewCount}</span>
                                        <span><i class="bi bi-heart"></i> ${guide.favoriteCount}</span>
                                        <a href="guide-detail.html?id=${guide.guideId}"
                                           class="edit-btn">
                                            查看详情
                                        </a>
                                    </div>
                                </div>
                            </div>
                        `).join('');
            } else {
                alert('加载用户信息失败：' + (data.message || '未知错误'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('加载用户信息失败，请稍后重试');
        });
}

// 辅助函数：HTML转义
function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// 退出登录逻辑
function logout() {
    // 这里你可以通过API调用后端进行退出登录操作
    fetch('/lvshu/logoutServlet', {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 成功退出，跳转到登录页面
                alert('退出成功');
                window.location.href = 'login.html';  // 假设登录页面是 login.html
            } else {
                alert('退出失败，请稍后重试');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('退出失败，请稍后重试');
        });
}

document.getElementById('followButton').addEventListener('click', function() {
    loadUserList('following');
});

document.getElementById('favoriteButton').addEventListener('click', function() {
    loadUserList('followers'); // 假设后端有一个类似的接口获取收藏列表
});

// 加载关注/收藏列表
function loadUserList(type) {
    fetch(`/lvshu/loadUserList?type=${type}`)  // 后端接口，type取'following' 或 'favorites'
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const userListContainer = document.getElementById('userList');
                const modalTitle = document.getElementById('modalTitle');
                userListContainer.innerHTML = '';

                modalTitle.textContent = (type === 'following') ? '关注的人' : '粉丝列表';

                data.users.forEach(user => {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <div class="user-info">
                            <img src="${user.avatar || 'images/default-avatar.jpg'}" alt="${user.username}'s avatar" class="user-avatar">
                            <div class="user-details">
                                <h3>${user.username}</h3>
                                <p><strong>性别：</strong>${user.gender == 'O' ? '沃尔玛购物袋' : (user.gender == 'F' ? '女' : '男') || '未填写'}</p>
                                <p><strong>地区：</strong>${user.nativePlace || '未填写'}</p>
                                <p><strong>电话：</strong>${user.mobilePhone || '未填写'}</p>
                                <p><strong>邮箱：</strong>${user.email || '未填写'}</p>
                                <p><strong>专业：</strong>${user.major || '未填写'}</p>
                            </div>
                        </div>
                    `;
                    userListContainer.appendChild(li);
                });

                // 显示弹窗
                document.getElementById('modal').style.display = 'flex';
            } else {
                alert('加载用户列表失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('加载用户列表失败，请稍后重试');
        });
}

// 关闭弹窗
document.getElementById('closeModal').addEventListener('click', function() {
    document.getElementById('modal').style.display = 'none';
});

