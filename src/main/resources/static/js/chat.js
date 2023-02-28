let stompClient = null;
let authUUID = '';
$.ajax({
    type: "get",
    url: "/api/getAuthUserUUID",
    async: false,
    success: function(data) {
        authUUID = data;
    }
});

function connect() {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function(greeting) {
            showGreeting(JSON.parse(greeting.body));
        }, function (e) {
            alert(e);
        });
    } );
}

function showGreeting(message) {
    if (authUUID === message.fromUser.uuid) {
        $("#messages").append
        (`<div class="your-message">
           <div class="your-message-text"><p class="text">${message.contentText}</p></div>
           <div class="your-profile-photo"><img src="/files/${authUUID}/logo.png" alt=""></div>
          </div>`
        );
    } else {
        $("#messages").append
        (`<div class="other-message">
              <div class="other-profile-photo"><img src="/files/${message.fromUser.uuid}/logo.png" alt=""></div>
              <div class="other-message-text">${message.contentText}</div>
          </div>`
        );
    }
}

let dt = new DataTransfer();
function removeFilesItem(target){
    let name = $(target).prev().text();
    let input = $("#file-input");
    $(target).closest('.input-file-list-item').remove();
    for(let i = 0; i < dt.items.length; i++){
        if(name === dt.items[i].getAsFile().name){
            dt.items.remove(i);
        }
    }
    input[0].files = dt.files;
}

function removeAllFilesItem(){
    let input = $("#file-input");
    $('.input-file-list-item').remove();
    for(let i = 0; i < dt.items.length; i++){
        dt.items.remove(i);
    }
    input[0].files = dt.files;
}

$('#file-input').on('change', function(){
    let $files_list = $('#file-list');
    $files_list.empty();

    for(let i = 0; i < this.files.length; i++){
        let new_file_input = '<div class="input-file-list-item">' +
            '<span class="input-file-list-name">' + this.files.item(i).name + '</span>' +
            '<a href="#" onclick="removeFilesItem(this); return false;" class="input-file-list-remove">x</a>' +
            '</div>';
        $files_list.append(new_file_input);
        dt.items.add(this.files.item(i));
    }
    this.files = dt.files;
});

$(document).on('click', '#sendMsg', function () {
    let messageArea = $('#message-area');
    let inputFiles =  $('#file-input')[0]
    const formData = new FormData();
    formData.append('message', messageArea.val());
    formData.append('file',inputFiles.files[0])

    $.ajax({
        type: "POST",
        url: '/api/sendMessage',
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function (e) {
            messageArea.val('');
            removeAllFilesItem();
        }
    })
})

$(document).on("click", "#logout", function(e) {
    e.preventDefault();
    $.post("/logout");

    window.location.href = "/login";
    return false;
})

window.addEventListener("load", function(event) {
    connect();
});

