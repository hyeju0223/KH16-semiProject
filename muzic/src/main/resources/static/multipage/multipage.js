$(function () {
/*<<<<<<< HEAD
    //1. 첫 페이지만 보이게 하는 것
    $(".page").hide().first().show();

    //2. 다음버튼을 누르면 현재페이지를 숨기고 다음페이지를 표시
    $(".btn-next").on("click", function () {
        $(this).closest(".page").hide();//현재페이지 숨김
        $(this).closest(".page").next().show();//다음페이지 표시
        refreshPage();
    });

    //3. 이전버튼을 누르면 현재페이지를 숨기고 이전페이지를 표시
    $(".btn-prev").on("click", function () {
        $(this).closest(".page").hide();//현재페이지 숨김
        $(this).closest(".page").prev().show();//이전페이지 표시
        refreshPage();
    });

    //첫 페이지 상태 계산
    refreshPage();

    function refreshPage() {
        //4. 총 페이지 수를 출력
        var totalPage = $(".page").length;
        $(".total-page").text(totalPage);

        //5. 현재 페이지 위치를 출력
        //var currentPage = 전체 페이지 중에서 표시되는 페이지의 위치;
        var currentPage = $(".page").index($(".page:visible")) + 1;
        $(".current-page").text(currentPage);

        //6. 진행상황 계산
        var percent = currentPage * 100 / totalPage;
        $(".progressbar > .guage").css("width", percent + "%");
    }
});
=======*/
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
/*>>>>>>> refs/remotes/origin/2025-10-24minsulee*/
