$(function () {

    var audio = $("#music-player").get(0);
    var playBtn = $(".music-play-btn");
    var icon = playBtn.find("i");

    var seek = $("#seek-bar");
    var cur = $("#current-time");
    var tot = $("#total-time");

    function fmt(sec) {
        if (isNaN(sec)) return "--:--";
        var m = Math.floor(sec / 60);
        var s = Math.floor(sec % 60);
        return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }

    // Play / Pause
    playBtn.on("click", function () {
        if (audio.paused) {
            audio.play();
            icon.fadeOut(120, function () {
                icon.removeClass("fa-circle-play").addClass("fa-circle-pause");
            }).fadeIn(120);
        } else {
            audio.pause();
            icon.fadeOut(120, function () {
                icon.removeClass("fa-circle-pause").addClass("fa-circle-play");
            }).fadeIn(120);
        }
    });

    audio.addEventListener("ended", function () {
        icon.removeClass("fa-circle-pause").addClass("fa-circle-play");
    });

    audio.onloadedmetadata = function () {
        tot.text(fmt(audio.duration));
    };

    audio.ontimeupdate = function () {
        seek.val((audio.currentTime / audio.duration) * 100);
        cur.text(fmt(audio.currentTime));
    };

    seek.on("input", function () {
        audio.currentTime = (seek.val() / 100) * audio.duration;
    });

});