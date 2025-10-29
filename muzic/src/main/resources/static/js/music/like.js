$(function() {

    // 좋아요 버튼 클릭
    $(".btn-like").on("click", function() {
        const $btn = $(this);
        const musicNo = $btn.data("music-no");

        $.ajax({
            url: "/rest/music/toggle",
            method: "GET",
            data: { musicNo: musicNo },
            success: function(result) {
                if (result && result.liked) {
                    $btn.addClass("liked").text("❤️");
                } else {
                    $btn.removeClass("liked").text("🤍");
                }

                // 좋아요 수 실시간 갱신
                $btn.closest(".music-item").find(".like-count").text(result.likeCount);
            },
            error: function() {
                alert("로그인이 필요합니다.");
            }
        });
    });

    // 재생 버튼 클릭 시 재생수 증가
    $(".btn-play").on("click", function() {
        const musicNo = $(this).data("music-no");

        $.ajax({
            url: "/rest/music/play",
            method: "GET",
            data: { musicNo: musicNo }
        });
    });

});