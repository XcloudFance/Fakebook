window.onload = function () {
    login_btn = document.getElementById('login_btn');

    login_btn.onclick = function () {
        login_edit = document.getElementById("login_edit");
        password_edit = document.getElementById("password_edit");

        uname = login_edit.value;
        password = password_edit.value;

        var request = new XMLHttpRequest();
        request.open("get", "./api/user/login?uid=" + uname + "&password=" + password);
        request.send();
        request.onload = function () {
            if (request.status == 200) {
                console.log(request.responseText);
                var json = JSON.parse(request.responseText);
                head.appendChild(csslink)
                alert(json);
            }
            else {
                alert("Login Failed!");
            }
        }
    }
}