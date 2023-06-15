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

    fetch('./api/user/get_profile')
        .then(response => response.json())
        .then(data => {
            if (data.code === 0) {
                const profileData = data.data;
                document.getElementById('email_input').value = profileData.email;
                document.getElementById('uid_input').value = profileData.uid;
                document.getElementById('username_input').value = profileData.username;
                document.getElementById('phone_input').value = profileData.phone;
                document.getElementById('add_input').value = profileData.address;
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
          })
          .catch(error => console.log('Error:', error));
      }    

}