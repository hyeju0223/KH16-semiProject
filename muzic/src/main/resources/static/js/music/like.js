$(function () {

    // 로그인 아닌 경우 JS 실행 안함
    if (window.isLogin !== "true") return;

    // 공통 UI 렌더 함수
    function updateUI(icon, countTag, isLike, cnt) {
        icon.toggleClass("fa-solid liked", isLike)
            .toggleClass("fa-regular", !isLike);
        countTag.text(cnt);
    }

    $(".detail-like, .list-like").each(function () {
        var btn = $(this);

        // 비활성 버튼이면 무시
        if (btn.hasClass("disabled-like")) return;

        var musicNo = btn.data("music-no");
        var icon = btn.find("i");
        var countTag = btn.find(".like-count");

        // ✅ 초기 일시 숨김 (UI 깜빡임 방지)
        icon.css("opacity", "0.5");

        // ✅ 초기 상태
        $.get("/rest/music/check?musicNo=" + musicNo, function (res) {
            updateUI(icon, countTag, res.like, res.likeCount);
            icon.css("opacity", "1");
        });

        // ✅ 좋아요 토글
        btn.on("click", function (e) {
            e.stopPropagation();
            $.get("/rest/music/toggle?musicNo=" + musicNo, function (res) {
                updateUI(icon, countTag, res.like, res.likeCount);
            });
        });
    });

});