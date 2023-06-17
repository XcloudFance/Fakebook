function clearAllCookie() {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
        for (var i = keys.length; i--;)
            document.cookie = keys[i] + '=0;expires=' + new Date(0).toUTCString()
    }
}

window.onload = function () {
    function getCookie(name) {
        const cookieValue = document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)');
        return cookieValue ? cookieValue.pop() : '';
    }

    function setAvatar(uid) {
        const avatarElement = document.getElementById('avatar');

        const avatarUrl = `/api/user/avatar/${uid}`;

        avatarElement.src = avatarUrl;
    }

    const uid = getCookie('uid');

    setAvatar(uid);

    document.getElementById("saveProfile_btn").onclick = function () {
        profileData = {
            email: document.getElementById('email_input').value,
            uid: document.getElementById('uid_input').value,
            username: document.getElementById('username_input').value,
            phone: document.getElementById('phone_input').value,
            address: document.getElementById('addr_input').value,
            jobs: document.getElementById('jobs_input').value,
            hobbies: document.getElementById('hobbies_input').value,
            birthday: document.getElementById('birthday_input').value,
            gender: document.getElementById('gender_input').value
        }

        fetch('/api/user/set_profile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(profileData)
        })
            .then(response => response.json())
            .then(data => {
                // 处理响应数据
                console.log(data);
                alert("Success");
                location.reload();
            })
            .catch(error => {
                // 处理错误
                console.error(error);
            });
    }

    fetch('./api/user/get_profile')
        .then(response => response.json())
        .then(data => {
            if (data.code === 0) {
                const profileData = data.data;
                document.getElementById('email_input').value = profileData.email;
                document.getElementById('uid_input').value = profileData.uid;
                document.getElementById('username_input').value = profileData.username;
                document.getElementById('phone_input').value = profileData.phone;
                document.getElementById('addr_input').value = profileData.address;
                document.getElementById('jobs_input').value = profileData.jobs;
                document.getElementById('hobbies_input').value = profileData.hobbies;
                document.getElementById('birthday_input').value = profileData.birthday;
                document.getElementById('gender_input').value = profileData.gender;
            }
        })
        .catch(error => console.log('Error:', error));

    const chooseBtn = document.querySelector('.avatar-conatiner h4');
    chooseBtn.addEventListener('click', () => {
        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.accept = 'image/.png';
        fileInput.addEventListener('change', handleFileSelect);
        fileInput.click();
    });

    function handleFileSelect(event) {
        const file = event.target.files[0];
        const formData = new FormData();
        formData.append('avatar', file);

        fetch('/api/user/uploadAvatar', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                console.log('Upload Response:', data);
                const avatarElement = document.getElementById('avatar');
                avatarElement.src += "?v=" + Math.floor(Math.random() * 114514) + 1;;
            })
            .catch(error => console.log('Error:', error));
    }

    document.getElementById("logout_btn").onclick = function() {
        clearAllCookie();
        location.reload();
    }

}