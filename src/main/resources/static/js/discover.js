function ApplyFriend(uid) {
    uid = uid.replaceAll(" ", "");
    fetch(`/api/user/applyFriend?friend_uid=${uid}`)
        .then(response => response.json())
        .then(data => {
            if (data["code"] === 0) {
                alert("Applied, waiting for accepting.");
            } else {
                alert("Already sent.");
            }
        })
        .catch(error => console.log(error));
}

window.onload = function () {
    var poststab = document.getElementById("search-tabs-posts");
    var userstab = document.getElementById("search-tabs-users");

    poststab.onclick = function () {
        poststab.setAttribute("class", "selected-searchtab");
        userstab.removeAttribute("class");
    }
    userstab.onclick = function () {
        userstab.setAttribute("class", "selected-searchtab");
        poststab.removeAttribute("class");
    }
}

function search(event) {
    event.preventDefault();
    var poststab = document.getElementById("search-tabs-posts");
    var keyword = document.getElementById("search-box").value;
    if (poststab.getAttribute("class") == "selected-searchtab") {
    }
    else {
        fetch(`/api/user/searchUser?keyword=${keyword}`)
            .then(response => response.json())
            .then(data => {
                var search_result = document.getElementById("search_results")
                if(data["data"]["uids"].length == 0)
                    search_result.innerHTML=`<p style="text-align:center;">No results found</p>`;
                else
                    search_result.innerHTML=""
                for(let i = 0;i < data["data"]["uids"].length;i ++) {
                    var newUseritem = document.createElement("div");
                    newUseritem.setAttribute("class", "user-item");
                    newUseritem.innerHTML = `<div class="avatar-container"><img src="/api/user/avatar/` + data["data"]["uids"][i] + `"></div><div class="user-item-info"><h4>` + data["data"]["unames"][i] + `</h4><p>@` + data["data"]["uids"][i] +`</p></div><div class="user-item-opt"><svg onclick="ApplyFriend('${data["data"]["uids"][i]}')" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M864 800h-64v-60.8c0-19.2-12.8-32-32-32s-32 12.8-32 32V800h-64c-19.2 0-32 12.8-32 32s12.8 32 32 32h64v67.2c0 19.2 12.8 32 32 32s32-12.8 32-32V864h64c19.2 0 32-12.8 32-32s-12.8-32-32-32z"></path><path d="M704 931.2V896h-32c-35.2 0-64-28.8-64-64s28.8-64 64-64h32v-28.8c0-25.6 12.8-44.8 32-54.4-22.4-22.4-64-38.4-86.4-48 86.4-51.2 147.2-144 147.2-249.6 0-160-128-288-288-288s-288 128-288 288c0 108.8 60.8 201.6 147.2 249.6-121.6 48-214.4 153.6-240 288-3.2 9.6 0 19.2 6.4 25.6 6.4 6.4 16 12.8 25.6 12.8h553.6c-3.2-9.6-9.6-22.4-9.6-32z"></path></svg></div>`;
                    search_result.appendChild(newUseritem);
                }
            })
            .catch(error => console.log(error));
    }
}
