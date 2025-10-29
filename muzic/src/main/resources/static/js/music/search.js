$(document).ready(function () {
    const $input = $("#search-input");
    const $preview = $("#search-preview");
    let timer = null;

    // 🔸 입력 이벤트 (디바운스 0.5초)
    $input.on("input", function () {
        clearTimeout(timer);
        const keyword = $input.val().trim();

        if (keyword.length < 2) {
            $preview.hide();
            return;
        }

        timer = setTimeout(() => {
            $.ajax({
                url: "/music/search/preview",
                method: "GET",
                data: { keyword: keyword },
                success: function (data) {
                    renderPreview(data);
                },
                error: function () {
                    $preview.hide();
                }
            });
        }, 500);
    });

    // 🔸 엔터 입력 시 전체 검색 페이지 이동
    $input.on("keypress", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            const keyword = $input.val().trim();
            if (keyword.length >= 2) {
                window.location.href = "/music/search/list?keyword=" + encodeURIComponent(keyword);
            }
        }
    });

    // 🔸 미리보기 렌더링
    function renderPreview(data) {
        $preview.empty();

        const categories = [
            { key: "music_title", label: "🎵 곡 검색결과" },
            { key: "music_artist", label: "👤 가수 검색결과" },
            { key: "music_album", label: "💿 앨범 검색결과" }
        ];

        categories.forEach(({ key, label }) => {
            const section = $("<div>").addClass("preview-section");
            section.append("<h4>" + label + "</h4>");

            const list = data && data[key] ? data[key] : [];

            if (list.length > 0) {
                list.forEach(item => {
                    let text = "";

                    if (key === "music_title") {
                        text = `${item.musicTitle} - ${item.musicArtist} - ${item.musicAlbum || "앨범없음"}`;
                    } else if (key === "music_artist") {
                        text = `${item.musicArtist} - ${item.musicTitle} - ${item.musicAlbum || "앨범없음"}`;
                    } else if (key === "music_album") {
                        text = `${item.musicAlbum} - ${item.musicTitle} - ${item.musicArtist}`;
                    }

                    section.append(
                        $("<div>")
                            .addClass("preview-item")
                            .text(text)
                            .on("click", function () {
                                window.location.href = "/music/detail?musicNo=" + item.musicNo;
                            })
                    );
                });
            } else {
                section.append(
                    $("<div>")
                        .addClass("preview-empty")
                        .text("검색 결과가 없습니다.")
                );
            }

            $preview.append(section);
        });

        $preview.show();
    }

    // ✅ 🔸 외부 클릭 시 미리보기 닫기
    $(document).on("click", function (e) {
        // 검색창(input)이나 미리보기 영역 클릭이 아니라면 닫기
        if (!$(e.target).closest("#search-input, #search-preview").length) {
            $preview.hide();
        }
    });

    // ✅ 🔸 미리보기 내부 클릭 시 닫히지 않게
    $preview.on("click", function (e) {
        e.stopPropagation();
    });
	
	$input.on("focus", function () {
	    const keyword = $input.val().trim();
	    if (keyword.length >= 2) {
	        // 다시 AJAX 호출
	        $.ajax({
	            url: "/music/search/preview",
	            method: "GET",
	            data: { keyword: keyword },
	            success: function (data) {
	                renderPreview(data);
	            },
	            error: function () {
	                $preview.hide();
	            }
	        });
	    }
	});
	
});