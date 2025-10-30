function toggleLike(el, e) {
    e.stopPropagation();
    var icon = el.querySelector("i");

    if (el.classList.contains("liked")) {
        el.classList.remove("liked");
        icon.classList.replace("fa-solid", "fa-regular");
    } else {
        el.classList.add("liked");
        icon.classList.replace("fa-regular", "fa-solid");
    }
}