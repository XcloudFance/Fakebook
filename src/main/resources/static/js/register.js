window.onload = function () {
    login_btn = document.getElementById('login__button');

    login_btn.onclick = function () {
        email_input = document.getElementById("email_input");
        phone_input = document.getElementById("phone_input");
        uname_input = document.getElementById("uname_input");
        uid_input = document.getElementById("uid_input");
        passwrod_input = document.getElementById("password_input");
        repassword_input = document.getElementById("repassword_input");

        email = email_input.value;
        phone = phone_input.value;
        uname = uname_input.value;
        uid = uid_input.value;
        password = passwrod_input.value;
        repassword = repassword_input.value;

        function disable_inputs(disable) {
            if (disable == "disabled") {
                email = email_input.value;
                phone = phone_input.value;
                uname = uname_input.value;
                uid = uid_input.value;
                password = passwrod_input.value;
                repassword = repassword_input.value;
                email_input.setAttribute("disabled", "disabled");
                phone_input.setAttribute("disabled", "disabled");
                uname_input.setAttribute("disabled", "disabled");
                uid_input.setAttribute("disabled", "disabled");
                passwrod_input.setAttribute("disabled", "disabled");
                repassword_input.setAttribute("disabled", "disabled");
                login_btn.innerHTML = "Loading...";
            }
            else {
                email = email_input.value;
                phone = phone_input.value;
                uname = login_edit.value;
                uid = uid_input.value;
                password = passwrod_input.value;
                repassword = repassword_input.value;
                email_input.removeAttribute("disabled");
                phone_input.removeAttribute("disabled");
                uname_input.removeAttribute("disabled");
                uid_input.removeAttribute("disabled");
                passwrod_input.removeAttribute("disabled");
                repassword_input.removeAttribute("disabled");
                login_btn.innerHTML = "Start";
            }
        }

        disable_inputs("disabled");

        const isAnyStringEmpty = (...strings) => strings.some(str => !str || str.trim() === '');

        if (isAnyStringEmpty(email, phone, uname, uid, password, repassword)) {
            alert("Please input all the materials needed!!");
            disable_inputs("");
            return;
        }

        if (password != repassword) {
            alert("2 passwords don't match.");
            disable_inputs("");
            return;
        }



        var jsonData = {
            email: email,
            phone_number: phone.toString(),
            uid: uid,
            username: uname,
            password: password
        };

        fetch("./api/user/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(jsonData)
        })
            .then(response => response.json())
            .then(json => {
                if (json.code === 0) {
                    for (let i = 0;i < json.cookie.split(";").length;i ++)
                        document.cookie = json.cookie.split(";")[i];
                    login_btn.innerHTML = "success";
                    location.href = "http://127.0.0.1:8000";
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
