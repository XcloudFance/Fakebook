window.onload = function() {
    var poststab = document.getElementById("search-tabs-posts");
    var userstab = document.getElementById("search-tabs-users");

    poststab.onclick = function() {
        poststab.setAttribute("class", "selected-searchtab");
        userstab.removeAttribute("class");
    }
    userstab.onclick = function() {
        userstab.setAttribute("class", "selected-searchtab");
        poststab.removeAttribute("class");
    }
}