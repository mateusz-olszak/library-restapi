$(document).ready(function(){

    $('.input').focus(function(){
        $(this).parent().find(".label-txt").addClass('label-active');
    });

    $(".input").focusout(function(){
        if ($(this).val() == '') {
            $(this).parent().find(".label-txt").removeClass('label-active');
        };
    });

    $("#fileImage").change(function () {
        fileSize = this.files[0].size;
        if (fileSize > 1048576) {
            this.setCustomValidity("File size greater than 1MB. Plese choose file below 1MB.")
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
};

