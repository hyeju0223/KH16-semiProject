// 검색 미리보기
$(document).ready(function() {

	var $input = $("#mz-search-input");
	var $preview = $("#mz-search-preview");
	var timer = null;

	// 공통 ajax preview 실행 함수
	function triggerPreview(keyword) {
		$.ajax({
			url: "/music/search/preview",
			method: "GET",
			data: { keyword: keyword },
			success: function(data) {
				renderPreview(data);
			},
			error: function() {
				$preview.hide();
			}
		});
	}

	// 입력 이벤트 (0.5초 디바운스)
	$input.on("input", function() {
		clearTimeout(timer);
		var keyword = $input.val().trim();

		if (keyword.length < 2) {
			$preview.hide();
			return;
		}

		timer = setTimeout(function() {
			triggerPreview(keyword);
		}, 500);
	});

	// 검색창 포커스 시 이전 검색어로 preview 자동 호출
	$input.on("focus", function() {
		var keyword = $input.val().trim();
		if (keyword.length >= 2) {
			triggerPreview(keyword);
		}
	});

	// 엔터 검색 처리 (빈 값 / 1글자 / 정상 검색)
	$input.on("keypress", function(e) {
		if (e.key === "Enter") {
			e.preventDefault();
			var keyword = $input.val().trim();

			if (keyword.length === 0) {
				// 0글자 → 전체 리스트 페이지 이동
				window.location.href = "/music/list";
				return;
			}

			if (keyword.length < 2) {
				// 1글자 → 경고
				alert("2글자 이상 검색해주세요 🎧");
				return;
			}

			// 정상 검색
			window.location.href = "/music/search/list?keyword=" + encodeURIComponent(keyword); // 특수기호 보존처리
		}
	});

	// 미리보기 렌더링
	function renderPreview(data) {

		$preview.empty();

		var sectionTitle = {
			music_title: "🎵 곡으로 검색한 음원",
			music_artist: "👤 아티스트로 검색한 음원",
			music_album: "💿 앨범으로 검색한 음원"
		};

		var hasResult = false; // 결과 체크 변수
		
		$.each(data, function(column, list) {

			var $title = $("<div>").addClass("mz-preview-title").text(sectionTitle[column]);
			$preview.append($title);

			if (!list || list.length === 0) {
			            return; // 다음 섹션
			        }

			        hasResult = true; // 하나라도 결과 있으면 true
			
			$.each(list, function(i, item) {

				var text = "";

				if (column === "music_title") {
					text = `🎵 ${item.musicTitle} / 👤 ${item.musicArtist} / 💿 ${item.musicAlbum}`;
				} else if (column === "music_artist") {
					text = `👤 ${item.musicArtist} / 🎵 ${item.musicTitle} / 💿 ${item.musicAlbum}`;
				} else if (column === "music_album") {
					text = `💿 ${item.musicAlbum} / 🎵 ${item.musicTitle} / 👤 ${item.musicArtist}`;
				}

				var $row = $("<div>")
					.addClass("mz-preview-item")
					.text(text)
					.on("click", function() {
						window.location.href = "/music/detail?musicNo=" + item.musicNo;
					});

				$preview.append($row);
			});
		});
		
		// 전체 결과 없을 때 메시지 출력
		    if (!hasResult) {
		        $preview.append(
		            $("<div>")
		                .addClass("mz-preview-empty")
		                .html("🧐 찾는 음악이 없어요! <br> 다른 키워드로 검색해보세요! 🎶💜")
		        );
		    }

		$preview.show();
	}

	// 외부 클릭 → 미리보기 닫기
	$(document).on("click", function(e) {
		if (!$(e.target).closest("#mz-search-input, #mz-search-preview").length) {
			$preview.hide();
		}
	});

	// 미리보기 내부 클릭 시 닫힘 방지
	$preview.on("click", function(e) {
		e.stopPropagation();
	});

});
