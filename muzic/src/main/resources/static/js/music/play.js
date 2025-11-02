$(document).ready(function () {

    var musicNo = $(".music-detail-card").data("music-no");
    var audio = document.getElementById("music-player");

    // 음악 재생 시작될 때 서버로 카운트 요청
    audio.addEventListener("play", function () {
        $.ajax({
            url: "/rest/music/play",
            method: "POST",
            data: { musicNo: musicNo }
        });
    });

});