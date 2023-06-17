
function acceptFriend(uid) {
    uid = uid.replaceAll(" ", "");
    fetch(`/api/user/acceptFriend?friend_uid=${uid}`)
        .then(response => response.json())
        .then(data => {
            if (data["code"] === 0) {
                location.reload();
            } else {
                // 请求失败，弹出提示框
                alert("Failed to accept friend request.");
            }
        })
        .catch(error => console.log(error));
}

function deleteFriend(uid) {
    uid = uid.replaceAll(" ", "");
    fetch(`/api/user/deleteFriend?uid=${uid}`)
        .then(response => response.json())
        .then(data => {
            if (data["code"] === 0) {
                location.reload();
            } else {
                // 请求失败，弹出提示框
                alert("Failed to delete friend.");
            }
        })
        .catch(error => console.log(error));
}

function refuseFriend(uid) {
    uid = uid.replaceAll(" ", "");
    fetch(`/api/user/refuseFriend?friend_uid=${uid}`)
        .then(response => response.json())
        .then(data => {
            if (data["code"] === 0) {
                location.reload();
            } else {
                // 请求失败，弹出提示框
                alert("Failed to refuse friend request.");
            }
        })
        .catch(error => console.log(error));
}


function loadFriends() {
    fetch("/api/user/getFriends")
        .then(response => response.json())
        .then(data => {
            var friendsContainer = document.getElementById("friends-container");
            friendsContainer.innerHTML = "";

            if (data["code"] === 0) {
                var uids = data["data"]["uids"];
                var unames = data["data"]["unames"];
                var applyUids = data["applications"]["uids"];
                var applyUnames = data["applications"]["unames"];

                friendsContainer.innerHTML += "<p>Friend Applications</p>";
                for (let i = 0; i < applyUids.length; i++) {
                    var applyFriendItem = document.createElement("div");
                    applyFriendItem.setAttribute("class", "user-item");
                    applyFriendItem.innerHTML = `
          <div class="avatar-container">
            <img src="/api/user/avatar/${applyUids[i]}">
          </div>
          <div class="user-item-info">
            <h4>${applyUnames[i]}</h4>
            <p>@${applyUids[i]}</p>
          </div>
          <div class="user-item-opt">
          <svg onclick="acceptFriend('${applyUids[i]}')" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M358.8544 660.5632L277.8496 579.5584l81.4592-81.4592L440.32 579.104l267.1936-267.1872 90.5088 90.5088-349.1072 349.1008L358.4 661.0176l0.4544-0.448zM512 1024c-282.7712 0-512-229.2288-512-512S229.2288 0 512 0s512 229.2288 512 512-229.2288 512-512 512z m0-128c212.0768 0 384-171.9232 384-384s-171.9232-384-384-384-384 171.9232-384 384 171.9232 384 384 384z"></path></svg>
          <svg onclick="refuseFriend('${applyUids[i]}')" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M828.8 828.8c174.912-174.976 174.912-458.688 0-633.6-174.976-174.976-458.688-174.976-633.6 0-174.912 174.912-174.976 458.624 0 633.6C370.176 1003.776 653.888 1003.776 828.8 828.8zM373.824 427.584c-14.848-14.72-14.912-38.784-0.128-53.696 14.72-14.848 38.784-14.976 53.696-0.192L512 458.24l84.416-84.352c14.72-14.848 38.784-14.976 53.696-0.192 14.912 14.784 14.976 38.784 0.256 53.696L565.696 512l84.608 84.544c14.72 14.848 14.656 38.912-0.256 53.696-14.848 14.784-38.912 14.656-53.696-0.128L512 565.696 427.392 650.24c-14.848 14.784-38.912 14.72-53.696-0.192C358.912 635.2 358.976 611.136 373.824 596.352L458.24 512 373.824 427.584z"></path></svg>
          </div>
        `;
                    friendsContainer.appendChild(applyFriendItem);
                }
                friendsContainer.innerHTML += `<div style="width:100%;height:1px;background-color:#efefef;margin-top:5px"></div>`;

                friendsContainer.innerHTML += "<p>Friends:";
                for (let i = 0; i < uids.length; i++) {
                    var friendItem = document.createElement("div");
                    friendItem.setAttribute("class", "user-item");
                    friendItem.setAttribute("id", "user-item-" + uids[i]);
                    friendItem.innerHTML = `
          <div class="avatar-container">
            <img src="/api/user/avatar/${uids[i]}">
          </div>
          <div class="user-item-info">
            <h4>${unames[i]}</h4>
            <p>@${uids[i]}</p>
          </div>
          <div class="user-item-opt">
          <svg viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M853.333333 85.333333H170.666667C123.52 85.333333 85.76 123.52 85.76 170.666667L85.333333 938.666667l170.666667-170.666667h597.333333c47.146667 0 85.333333-38.186667 85.333334-85.333333V170.666667c0-47.146667-38.186667-85.333333-85.333334-85.333334zM256 384h512v85.333333H256v-85.333333z m341.333333 213.333333H256v-85.333333h341.333333v85.333333z m170.666667-256H256v-85.333333h512v85.333333z"></path></svg>
          <svg viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" onclick="deleteFriend('${uids[i]}')"><path d="M924.43026 238.663658 770.369466 238.663658l0-104.254435c0-38.566364-31.397081-69.990051-69.963445-69.990051L323.606259 64.419172c-38.566364 0-69.949118 31.423687-69.949118 69.990051l0 104.3363-154.334018 0.054235c-9.286504 0-18.013259 3.619434-24.595164 10.228969-6.568602 6.5553-10.188037 15.308661-10.160407 24.581861 0 19.173688 15.59621 34.81083 34.783201 34.81083l78.594009-0.013303L177.944761 889.430118c0 38.566364 31.382754 69.990051 69.963445 69.990051l528.225543 0c38.566364 0 69.963445-31.423687 69.963445-69.990051L846.097194 330.860477l-0.163729 0-0.013303-22.560832 78.539774-0.013303c19.188015 0 34.783201-15.637142 34.783201-34.851763C959.213461 254.259868 943.603949 238.663658 924.43026 238.663658zM412.347372 822.007543c-19.188015 0-34.783201-15.637142-34.783201-34.81083L377.399419 414.779771c0-19.173688 15.59621-34.824133 34.797527-34.824133 19.188015 0 34.783201 15.650445 34.783201 34.824133l0.163729 372.361683C447.143876 806.316166 431.521061 821.966611 412.347372 822.007543zM611.842962 822.007543c-19.201318 0-34.81083-15.637142-34.81083-34.81083L576.868403 414.779771c0-19.173688 15.59621-34.824133 34.783201-34.824133 19.201318 0 34.797527 15.650445 34.797527 34.824133l0.163729 372.361683C646.627187 806.316166 631.030977 821.966611 611.842962 822.007543zM323.401598 177.427992c0-23.844058 19.405979-43.210128 43.223431-43.210128l290.763247 0c23.844058 0 43.25106 19.365046 43.25106 43.210128l0 61.277622-377.237737 0.040932L323.401598 177.427992z"></path></svg>
          </div>
        `;
                    friendsContainer.appendChild(friendItem);
                }
            } else {
                friendsContainer.innerHTML = `<p style="text-align: center;">Failed to load friends.</p>`;
            }
        })
        .catch(error => console.log(error));
}

function filterFriends(input) {
    keyword = input.value;

    var divs = document.getElementsByClassName('user-item');

    for (var i = 0; i < divs.length; i++) {
        var div = divs[i];
        var username = div.querySelector('.user-item-info h4').textContent.toLowerCase();
        var uid = div.getAttribute('id').replace('user-item-', '').toLowerCase();

        if (username.includes(keyword.toLowerCase()) || uid.includes(keyword.toLowerCase())) {
            div.style.display = 'block';
        } else {
            div.style.display = 'none';
        }
    }
}

window.onload = function () {
    loadFriends();
}