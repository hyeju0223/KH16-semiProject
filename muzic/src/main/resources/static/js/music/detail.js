$(function() {

	var audio = $("#audio-player")[0];
	var $playBtn = $("#play-btn");
	var $duration = $(".duration-box");

	/** 🕓 오디오 메타데이터 로드 시 재생시간 표시 */
	audio.onloadedmetadata = function() {
		var totalSec = Math.floor(audio.duration);
		var min = Math.floor(totalSec / 60);
		var sec = String(totalSec % 60).padStart(2, "0");
		$duration.text(`${min}:${sec}`);
	};

	/** ▶️⏸ 플레이 / 일시정지 버튼 */
	$playBtn.on("click", function() {
		if (audio.paused) {
			audio.play();
			$(this).html('<i class="fa-solid fa-pause"></i>');
		} else {
			audio.pause();
			$(this).html('<i class="fa-solid fa-play"></i>');
		}
	});

	/** 🎧 다른 곳 클릭하면 일시정지 (선택사항) */
	$(document).on("click", function(e) {
		if (!$(e.target).closest(".album-box").length) {
			if (!audio.paused) {
				audio.pause();
				$playBtn.html('<i class="fa-solid fa-play"></i>');
			}
		}
	});

});