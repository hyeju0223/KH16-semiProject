$(function () {
    // 1. 첫 페이지만 보이게
    $(".page").hide().first().show();

    // 2. 다음 버튼 클릭 시
    $(".btn-next").click(function () {
        const page = $(this).closest(".page");

        // 🚫 실패한 필드가 있는 경우 이동 막기
        const invalid = page.find(".field.fail, .field.fail2, .field:invalid").length > 0;

        if (invalid) {
            alert("입력 형식을 다시 확인해주세요.");
            return;
        }

        // ✅ 다음 단계 표시
        const next = page.next(".page");
        if (next.length) {
            page.hide();
            next.show();
        }
        refreshPage();
    });

    // 3. 이전 버튼 클릭 시
    $(".btn-prev").click(function () {
        const page = $(this).closest(".page");
        const prev = page.prev(".page");

        if (prev.length) {
            page.hide();
            prev.show();
        }
        refreshPage();
    });

    // 4. 진행바 갱신
    function refreshPage() {
        const totalPage = $(".page").length;
        const currentPage = $(".page").index($(".page:visible")) + 1;
        const percent = currentPage * 100 / totalPage;
        $(".progressbar .guage").css("width", percent + "%");
    }

    // 초기 상태 갱신
    refreshPage();
});
