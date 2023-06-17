function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
    }
    return "";
}


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

function parseText(text) {
    var lines = text.split('\n');
    var parsedText = '';

    lines.forEach(function (line) {
        if (line.trim() !== '') {
            var parsedLine = line.trim().replace(/@(\w+)/g, '<a href="/user/$1">@$1</a>');
            parsedText += '<p>' + parsedLine + '</p>';
        }
    });

    return parsedText;
}

function show_forward(forward_btn) {
    if(forward_btn.getAttribute("id").endsWith("-ON")) {
        var uid = forward_btn.getAttribute("id").replace("forward_", "");
        uid = uid.substring(0, uid.length - 3);
        document.getElementById("forward_editor_" + uid).remove();
        forward_btn.setAttribute("id", "forward_"+uid);
    }else {
        var uid = forward_btn.getAttribute("id").replace("forward_", "");
        var forward_editor = document.createElement("div");
        forward_editor.setAttribute("class", "forward_editor");
        forward_editor.setAttribute("id", "forward_editor_" + uid);
        forward_editor.innerHTML = `<div class="forward_user_avatar">
    <img id="user-avatar" src="/api/user/avatar/${getCookie("uid")}">
</div>
<div class="forward_edit">
    <div class="forward_textinput">
        <textarea placeholder="Say something to forward..." oninput="adjustTextareaHeight(this)"
            maxlength="500" id="forward_textarea${uid}"></textarea>
        <div class="forward_bottom">
            <p id="forward_word_count${uid}">0/500</p>
            <button class="forward_btn" id="forward_btn${uid}" onclick="forward(this)">Forward</button>
        </div>
    </div>
</div>`;
        post_content = document.getElementById('post_content_' + uid);
        post_content.appendChild(forward_editor);
        forward_btn.setAttribute("id", forward_btn.getAttribute("id") + "-ON");
    }
}

function parseQuote(content, uid, username, posttime, views, comments, forwards, likes, postId) {
    var content = JSON.parse((JSON.parse(content.replaceAll("\"", "\\\"").replace("\\\"content\\\":\\\"", "\"content\":\"").replace("}\\\"", "}\""))["content"]));
    var album = content["imgs"];
    var card_general = `
    <div class="post_user_avatar">
        <img src="/api/user/avatar/${uid}" onclick="location.href = '/user/${uid}';">
    </div>
    <div class="post_content" id="post_content_${postId}">
        <div class="post_info">
            <div class="post_user_info">
                <h3 onclick="location.href = '/user/${uid}';">${username}</h3>
                <p onclick="location.href = '/user/${uid}';">${uid}</p>
            </div>
        </div>
        <div class="post_detail">
            ${parseText(content["text"])}
            ${album}
        <div class="post_statistic">
            <p>${posttime}</p>
            <p>·</p>
            <b>${views}</b>
            <p>Views</p>
        </div>
    </div>`
    return card_general;
}

function forward(forward_btn) {
    var uid = forward_btn.getAttribute("id").replace("forward_btn", "");
    var content = `{"text":"` + document.getElementById("forward_textarea" + uid).value + `", "imgs":\"\", "quote":["${uid}"]}`;
    fetch('/api/user/createPost', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            content: content
        }),
        credentials: 'same-origin'
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                alert('Error: ' + response.status);
            }
        })
        .then(data => {
            console.log(data);
            if (data.code === 0) {
                console.log('Post created successfully!');
                location.reload();
            } else {
                console.log('Failed to create post:', data.msg);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function parsePOST(content, uid, username, posttime, views, comments, forwards, likes, postId) {
    var content = JSON.parse((JSON.parse(content.replaceAll("\"", "\\\"").replace("\\\"content\\\":\\\"", "\"content\":\"").replace("}\\\"", "}\""))["content"]));
    var album = content["imgs"];
    var card_general = `
    <div class="post_user_avatar">
        <img src="/api/user/avatar/${uid}" onclick="location.href = '/user/${uid}';">
    </div>
    <div class="post_content" id="post_content_${postId}">
        <div class="post_info">
            <div class="post_user_info">
                <h3 onclick="location.href = '/user/${uid}';">${username}</h3>
                <p onclick="location.href = '/user/${uid}';">${uid}</p>
            </div>
        </div>
        <div class="post_detail" id="post_detail_${postId}">
            ${parseText(content["text"])}
            ${album}
        <div class="post_statistic" id="post_statistic${postId}">
            <p>${posttime}</p>
            <p>·</p>
            <b>${views}</b>
            <p>Views</p>
        </div>
        <div class="post_likes">
            <svg viewBox="0 0 24 24" aria-hidden="true" fill="#566370" class="post_comment" id="comments_${postId}">
                <path d="M1.751 10c0-4.42 3.584-8 8.005-8h4.366c4.49 0 8.129 3.64 8.129 8.13 0 2.96-1.607 5.68-4.196 7.11l-8.054 4.46v-3.69h-.067c-4.49.1-8.183-3.51-8.183-8.01zm8.005-6c-3.317 0-6.005 2.69-6.005 6 0 3.37 2.77 6.08 6.138 6.01l.351-.01h1.761v2.3l5.087-2.81c1.951-1.08 3.163-3.13 3.163-5.36 0-3.39-2.744-6.13-6.129-6.13H9.756z">
                </path>
            </svg>
            <p>${comments}</p>
            <svg viewBox="0 0 24 24" aria-hidden="true" fill="#566370" class="post_forward" onclick="show_forward(this)" id="forward_${postId}">
                <path d="M4.5 3.88l4.432 4.14-1.364 1.46L5.5 7.55V16c0 1.1.896 2 2 2H13v2H7.5c-2.209 0-4-1.79-4-4V7.55L1.432 9.48.068 8.02 4.5 3.88zM16.5 6H11V4h5.5c2.209 0 4 1.79 4 4v8.45l2.068-1.93 1.364 1.46-4.432 4.14-4.432-4.14 1.364-1.46 2.068 1.93V8c0-1.1-.896-2-2-2z">
                </path>
            </svg>
            <p>${forwards}</p>
            <svg viewBox="0 0 24 24" aria-hidden="true" fill="#566370" class="post_like" onclick="do_like(this)" id="like_${postId}">
                <path d="M16.697 5.5c-1.222-.06-2.679.51-3.89 2.16l-.805 1.09-.806-1.09C9.984 6.01 8.526 5.44 7.304 5.5c-1.243.07-2.349.78-2.91 1.91-.552 1.12-.633 2.78.479 4.82 1.074 1.97 3.257 4.27 7.129 6.61 3.87-2.34 6.052-4.64 7.126-6.61 1.111-2.04 1.03-3.7.477-4.82-.561-1.13-1.666-1.84-2.908-1.91zm4.187 7.69c-1.351 2.48-4.001 5.12-8.379 7.67l-.503.3-.504-.3c-4.379-2.55-7.029-5.19-8.382-7.67-1.36-2.5-1.41-4.86-.514-6.67.887-1.79 2.647-2.91 4.601-3.01 1.651-.09 3.368.56 4.798 2.01 1.429-1.45 3.146-2.1 4.796-2.01 1.954.1 3.714 1.22 4.601 3.01.896 1.81.846 4.17-.514 6.67z">
                </path>
            </svg>
            <p>${likes}</p>
        </div>

    </div>`
    return card_general;
}

function adjustTextareaHeight(textarea) {
    textarea.style.height = 'auto'; // 先将高度设置为自动，以便重新计算
    textarea.style.height = textarea.scrollHeight + 'px'; // 根据内容重新设置高度
    if (textarea.getAttribute("id").startsWith("forward_textarea")) {
        uid = textarea.getAttribute("id").replace("forward_textarea", "");
        var word_count = document.getElementById("forward_word_count" + uid);
        word_count.innerHTML = textarea.value.length + "/500";
    } else {
        var word_count = document.getElementById("post_word_count");
        word_count.innerHTML = textarea.value.length + "/500";
    }
}

function checkQuote(content, postID) {
    var content = JSON.parse((JSON.parse(content.replaceAll("\"", "\\\"").replace("\\\"content\\\":\\\"", "\"content\":\"").replace("}\\\"", "}\""))["content"]));
    if (content["quote"].length == 0)
        return;
    fetch("/api/user/getPost?postID=" + content["quote"][0], {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(data => {
            // 处理返回的数据
            if (data.code === 0) {
                // 请求成功
                const post = data;
                const postId = post.postId;
                const content = post.content;
                const likes = post.likes;
                const views = post.views;
                const comments = post.comments;
                const username = post.post_username;
                const uid = post.uid;
                const forwards = post.forwards;
                const posttime = post.posttime;
                var parsed_quote = parseQuote(content, uid, username, posttime, views, comments, forwards, likes, postId);
                var quote = document.createElement("div");
                quote.setAttribute("class", "post_card_quote");
                quote.innerHTML = parsed_quote;
                document.getElementById("post_detail_" + postID).insertBefore(quote, document.getElementById("post_statistic" + postID));
            } else {
                // 请求失败
                const errorMsg = data.msg;
                console.log(`Error: ${errorMsg}`);
            }
        })
        .catch(error => {
            console.log("Error occurred during API request:", error);
        });

}

function search(event) {
    event.preventDefault();
    var poststab = document.getElementById("search-tabs-posts");
    var search_result = document.getElementById("search_results");
    search_result.innerHTML = "";
    var keyword = document.getElementById("search-box").value;
    if (poststab.getAttribute("class") == "selected-searchtab") {
        fetch("/api/user/searchPost?keyword=" + keyword, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                // 处理返回的数据
                if (data.code === 0) {
                    // 请求成功
                    const posts = data.data;

                    // 处理 posts 数据
                    if(posts.length == 0)
                        document.getElementById("search_results").innerHTML = `<p style="text-align:center;">No results found</p>`;
                    for (const post of posts) {
                        const postId = post.postId;
                        const content = post.content;
                        const likes = post.likes;
                        const views = post.views;
                        const comments = post.comments;
                        const username = post.post_username;
                        const uid = post.uid;
                        const forwards = post.forwards;
                        const posttime = post.posttime;
                        var parsed_post = parsePOST(content, uid, username, posttime, views, comments, forwards, likes, postId);
                        var newPost = document.createElement("div");
                        newPost.setAttribute("class", "post_card");
                        newPost.innerHTML = parsed_post;
                        var search_result = document.getElementById("search_results");
                        search_result.appendChild(newPost);
                        checkQuote(content, postId);
                    }
                } else {
                    // 请求失败
                    const errorMsg = data.msg;
                    console.log(`Error: ${errorMsg}`);
                }
            })
            .catch(error => {
                // 处理请求错误
                console.log("Error occurred during API request:", error);
            });
    }
    else {
        fetch(`/api/user/searchUser?keyword=${keyword}`)
            .then(response => response.json())
            .then(data => {
                var search_result = document.getElementById("search_results");
                if (data["data"]["uids"].length == 0)
                    search_result.innerHTML = `<p style="text-align:center;">No results found</p>`;
                else
                    search_result.innerHTML = ""
                for (let i = 0; i < data["data"]["uids"].length; i++) {
                    var newUseritem = document.createElement("div");
                    newUseritem.setAttribute("class", "user-item");
                    newUseritem.innerHTML = `<div class="avatar-container"><img src="/api/user/avatar/` + data["data"]["uids"][i] + `"></div><div class="user-item-info"><h4>` + data["data"]["unames"][i] + `</h4><p>@` + data["data"]["uids"][i] + `</p></div><div class="user-item-opt"><svg onclick="ApplyFriend('${data["data"]["uids"][i]}')" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M864 800h-64v-60.8c0-19.2-12.8-32-32-32s-32 12.8-32 32V800h-64c-19.2 0-32 12.8-32 32s12.8 32 32 32h64v67.2c0 19.2 12.8 32 32 32s32-12.8 32-32V864h64c19.2 0 32-12.8 32-32s-12.8-32-32-32z"></path><path d="M704 931.2V896h-32c-35.2 0-64-28.8-64-64s28.8-64 64-64h32v-28.8c0-25.6 12.8-44.8 32-54.4-22.4-22.4-64-38.4-86.4-48 86.4-51.2 147.2-144 147.2-249.6 0-160-128-288-288-288s-288 128-288 288c0 108.8 60.8 201.6 147.2 249.6-121.6 48-214.4 153.6-240 288-3.2 9.6 0 19.2 6.4 25.6 6.4 6.4 16 12.8 25.6 12.8h553.6c-3.2-9.6-9.6-22.4-9.6-32z"></path></svg></div>`;
                    search_result.appendChild(newUseritem);
                }
            })
            .catch(error => console.log(error));
    }
}
