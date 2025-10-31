// 🔍 검색 미리보기 JS (jQuery 필요)
$(document).ready(function () {

    var $input = $("#mz-search-input");
    var $preview = $("#mz-search-preview");
    var timer = null;

    // 입력 이벤트 (0.5초 디바운스)
    $input.on("input", function () {
        clearTimeout(timer);
        var keyword = $input.val().trim();

        if (keyword.length < 2) {
            $preview.hide();
            return;
        }

        timer = setTimeout(function () {
            $.ajax({
                url: "/music/search/preview",
                method: "GET",
                data: { keyword: keyword},
                success: function (data) {
                    renderPreview(data);
                },
                error: function () {
                    $preview.hide();
                }
            });
        }, 500);
    });

    // 엔터 검색 → 검색결과 페이지 이동
    $input.on("keypress", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            var keyword = $input.val().trim();
            if (keyword.length >= 2) {
                window.location.href = "/music/search/list?keyword=" + encodeURIComponent(keyword);
            }
        }
    });

    // ✅ 미리보기 렌더링
    function renderPreview(data) {

        $preview.empty();

        var sectionTitle = {
            music_title: "🎵 곡",
            music_artist: "👤 아티스트",
            music_album: "💿 앨범"
        };

        $.each(data, function (column, list) {

            var $title = $("<div>").addClass("mz-preview-title").text(sectionTitle[column]);
            $preview.append($title);

            $.each(list, function (i, item) {

                var text = "";

                if (column === "music_title") {
                    text = item.musicTitle + " · " + item.musicArtist;
                } else if (column === "music_artist") {
                    text = item.musicArtist + " · " + item.musicTitle;
                } else if (column === "music_album") {
                    text = item.musicAlbum + " · " + item.musicArtist;
                }

                var $row = $("<div>")
                    .addClass("mz-preview-item")
                    .text(text)
                    .on("click", function () {
                        window.location.href = "/music/detail?musicNo=" + item.musicNo;
                    });

                $preview.append($row);
            });
        });

        $preview.show();
    }

    // 외부 클릭 → 미리보기 닫기
    $(document).on("click", function (e) {
        if (!$(e.target).closest("#mz-search-input, #mz-search-preview").length) {
            $preview.hide();
        }
    });

    // 미리보기 내부 클릭 시 닫힘 방지
    $preview.on("click", function (e) {
        e.stopPropagation();
    });

});