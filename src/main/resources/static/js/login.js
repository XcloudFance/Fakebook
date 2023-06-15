window.onload = function () {
    login_btn = document.getElementById('login_btn');

    login_btn.onclick = function () {
        login_edit = document.getElementById("login_edit");
        password_edit = document.getElementById("password_edit");

        uname = login_edit.value;
        login_edit.setAttribute("disabled", "disabled");
        password = password_edit.value;
        password_edit.setAttribute("diabled", "disabled");
        login_btn.innerHTML = "Logining";

        fetch("./api/user/login?uidOrEmail=" + uname + "&password=" + password, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                return response.json();
            })
            .then(json => {
                if (json.code === 0) {
                    for (let i = 0;i < json.cookie.split(";").length;i ++)
                        document.cookie = json.cookie.split(";")[i];
                    login_btn.innerHTML = "success";
                    location.href = "http://127.0.0.1:8080";
                } else {
                    alert("Login Failed!");
                    location.reload();
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("Login Failed!");
                location.reload();
            });

    }
}