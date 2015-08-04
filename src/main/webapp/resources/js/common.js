$(document).ready(function () {
    $("#pass1").attr('onchange', '').change(comparePassword);
    $("#pass2").attr('onchange', '').change(comparePassword).keyup(comparePassword);
    $("#login").attr('onchange', '').change(compareLogin);

    $("#toFirstPage").attr('onclick', '').click(toFirstPage);
    $("#toPrevPage").attr('onclick', '').click(toPrevPage);
    $("#toNextPage").attr('onclick', '').click(toNextPage);
    $("#toLastPage").attr('onclick', '').click(toLastPage);
    $("#idLike").attr('onchange', '').change(idLike);
    $("#nameLike").attr('onchange', '').change(nameLike);
    $("#clearLike").attr('onclick', '').click(clearLike);
    $("#idSort").attr('onclick', '').click(idSort);
    $("#nameSort").attr('onclick', '').click(nameSort);
    $("#clearSort").attr('onclick', '').click(clearSort);

});
function clearInfoLine() {
    $("#info_div").removeClass().addClass("body").html($("#accountName").html() + '<br/>');
}
$(document).ready(function () {
    setTimeout(clearInfoLine, 5000);
});
function comparePassword() {
    if ($("#pass1").val() == $("#pass2").val()) {
        $("#comparePass1").empty();
        $("#comparePass2").empty();
    } else {
        $("#comparePass1").html("password");
        $("#comparePass2").html("mismatch");
    }
}
function compareLogin() {
    $.ajax({
        url: '${context}/compareLoginAction',
        type: 'POST',
        datatype: 'json',
        data: {login: $("#login").val()},
        success: function (response) {
            if (response == true) {
                $("#compareLogin").empty();
            } else {
                $("#compareLogin").html(" login busy ");
            }
        }
    });
}

function toFirstPage() {
    ajaxClick({page: 'start'});
}
function toPrevPage() {
    ajaxClick({page: 'prev'});
}
function toNextPage() {
    ajaxClick({page: 'next'});
}
function toLastPage() {
    ajaxClick({page: 'end'});
}
function idLike() {
    ajaxClick({idLike: $("#idLike").val()});
}
function nameLike() {
    ajaxClick({nameLike: $("#nameLike").val()});
}
function clearLike() {
    ajaxClick({idLike: "", nameLike: ""});
}
function idSort() {
    ajaxClick({sort: "id"});
}
function nameSort() {
    ajaxClick({sort: "name"});
}
function clearSort() {
    ajaxClick({sort: "clear"});
}

function getPriceFromInteger(price) {
    if (price == null) {
        return "";
    }
    ;
    if (price == 0) {
        return "0.00";
    }
    ;
    var str = price.toString();
    newPrice = str.substring(0, str.length - 2) + "." + str.substring(str.length - 2, str.length);
    if (price < 100) {
        newPrice = "0" + newPrice;
    }
    return newPrice;
}

function getOnlyDateFromTimeStamp(timeStampDate) {
    if (timeStampDate == null) {
        return "";
    }
    if (timeStampDate == 0) {
        return "";
    }
    date = new Date(timeStampDate);
    var days, months, formattedDate;

    days = (date.getDate() < 10) ? "0" + date.getDate() : date.getDate();
    months = ((date.getMonth() + 1) < 10) ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1);
    formattedDate = days + "-" + months + "-" + date.getFullYear();
    return formattedDate;
}

function getDateAndTimeFromTimeStamp(timeStampDate) {
    if (timeStampDate == null) {
        return "";
    }
    if (timeStampDate == 0) {
        return "";
    }
    date = new Date(timeStampDate);
    var days, months, formattedDate;
    var hours, minutes, formattedTime;
    days = (date.getDate() < 10) ? "0" + date.getDate() : date.getDate();
    months = ((date.getMonth() + 1) < 10) ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1);
    formattedDate = days + "-" + months + "-" + date.getFullYear();
    hours = (date.getHours() < 10) ? "0" + date.getHours() : date.getHours();
    minutes = ((dateOrder.getMinutes() + 1) < 10) ? "0" + (date.getMinutes() + 1) : (date.getMinutes() + 1);
    formattedTime = hours + "-" + minutes;
    return formattedTime + " * " + formattedDate;
}

$(document).ready(function () { // вся мaгия пoсле зaгрузки стрaницы
    $('#addAdmin').click(function (event) { // лoвим клик пo ссылки с id="go"
        event.preventDefault(); // выключaем стaндaртную рoль элементa
        $('#overlay').fadeIn(400, // снaчaлa плaвнo пoкaзывaем темную пoдлoжку
            function () { // пoсле выпoлнения предъидущей aнимaции
                $('#modal_form')
                    .css('display', 'block') // убирaем у мoдaльнoгo oкнa display: none;
                    .animate({opacity: 1, top: '50%'}, 200); // плaвнo прибaвляем прoзрaчнoсть oднoвременнo сo съезжaнием вниз
            });
    });
    /* Зaкрытие мoдaльнoгo oкнa, тут делaем тo же сaмoе нo в oбрaтнoм пoрядке */
    $('#modal_close, #overlay').click(function () { // лoвим клик пo крестику или пoдлoжке
        $('#modal_form')
            .animate({opacity: 0, top: '45%'}, 200,  // плaвнo меняем прoзрaчнoсть нa 0 и oднoвременнo двигaем oкнo вверх
            function () { // пoсле aнимaции
                $(this).css('display', 'none'); // делaем ему display: none;
                $('#overlay').fadeOut(400); // скрывaем пoдлoжку
            }
        );
    });

});

