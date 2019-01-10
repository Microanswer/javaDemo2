function so(n) {
    if (n < 10) {
        return "0" + n;
    } else {
        return ""+n;
    }
}

window.onload = function () {
    var pi = document.getElementsByTagName("img");
    var oDte = new Date();
    var str = so(oDte.getHours()) + so(oDte.getMinutes()) + so(oDte.getSeconds());
    for (var i = 0; i < pi.length; i++) {
        pi[i].src = "img/" + str[i] + "png";
    }
}