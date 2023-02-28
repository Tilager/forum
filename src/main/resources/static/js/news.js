$(document).on("click", "#add_news_button", function (e) {
    e.preventDefault();

    let name = $("#news-name");
    let description = $("#news-description");
    let fileInput = $("#add-news-attach-file_button");

    const formData = new FormData();

    formData.append("name", name.val());
    formData.append("description", description.val());
    formData.append("logo", fileInput[0].files[0]);

    $.ajax({
        type: "POST",
        url: '/api/saveNews',
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function (data) {
            $("#news").prepend
            (`<div class="item">
                <div class="edit-news-content">
                    <div class="edit-news-button">
                        <button class="edit__button"><i class="fas fa-ellipsis-v" aria-hidden="true"></i></button>
                        <div class="edit-news">
                            <div class="edit-news-form">
                                <input type="text" name="text" placeholder="Измените название новости">
                                <textarea name="description" cols="30" rows="5" placeholder="Измените описание новости"></textarea>
                                <button id="${data.uuid}" class="edit-news__button">Подтвердить</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="item-photo">
                    <img src="/news/${data.fileName}" alt="photo">
                </div>
                <div class="item-text">
                    <div class="item-title">
                        <h2>${name.val()}</h2>
                    </div>
                    <div class="item-description">
                        <p>${description.val()}</p>
                    </div>
                </div>
              </div>`);

            name.val("");
            description.val("");
            fileInput.val(null);
        }
    })
})

$(document).on("click", ".edit-news__button", function (e) {
    e.preventDefault();

    let button = $(this);
    let uuid = button.prop('id');
    let name = button.parent().find('input');
    let description = button.parent().find('textarea');

    $.ajax({
        url: '/api/editNews',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            'uuid': uuid,
            'name': name.val(),
            'description': description.val()
        }),
        success: function (data) {
            location.reload();
        },
        error: function (e) {
            console.error(e);
        }
    })

})