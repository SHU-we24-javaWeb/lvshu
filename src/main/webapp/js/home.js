document.addEventListener('DOMContentLoaded', function () {
    loadUserProfile();
});

function loadUserProfile() {
    fetch('/lvshu/hotGuideServlet')
        .then(response => response.json())
        .then(data => {
            console.log('API Response:', data);
            if (data.success) {

                // 更新我的攻略列表
                const myGuidesContainer = document.getElementById('myGuides');
                myGuidesContainer.innerHTML = data.guides.map(guide => `
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

                                        </div>
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