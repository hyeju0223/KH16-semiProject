// COVER
var coverInput = document.getElementById("coverInput");
var coverPreview = document.getElementById("coverPreview");
var coverPlaceholder = document.getElementById("coverPlaceholder");
var coverResetBtn = document.getElementById("coverResetBtn");

coverInput.onchange = function(e){
    var file = e.target.files[0];
    if(!file) return;
    coverPreview.src = URL.createObjectURL(file);
    coverPreview.style.display = "block";
    coverPlaceholder.style.display = "none";
    coverResetBtn.style.display = "flex";
};

coverResetBtn.onclick = function(e){
    e.stopPropagation();
    coverInput.value = "";
    coverPreview.style.display = "none";
    coverPlaceholder.style.display = "block";
    coverResetBtn.style.display = "none";
};


// AUDIO
var audioInput = document.getElementById("musicFileInput");
var audioText = document.getElementById("audioText");
var durationText = document.getElementById("musicDuration");
var audioResetBtn = document.getElementById("audioResetBtn");

audioInput.onchange = function(e){
    var file = e.target.files[0];
    if(!file) return;

    audioText.textContent = file.name;
    audioResetBtn.style.display = "flex";

    var audio = new Audio(URL.createObjectURL(file));
    audio.onloadedmetadata = function(){
        var sec = String(Math.floor(audio.duration % 60)).padStart(2,"0");
        var min = Math.floor(audio.duration / 60);
        durationText.textContent = `${min}:${sec}`;
    };
};

audioResetBtn.onclick = function(e){
    e.stopPropagation();
    audioInput.value = "";
    audioText.textContent = "음악 파일을 업로드하세요";
    durationText.textContent = "";
    audioResetBtn.style.display = "none";
};