<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="headerInPage">
    <table width="95%">
        <tr>
            <td width="20%"></td>
            <td width="40%">
                <h2>ALL ACCOUNT PAGE</h2>
            </td>
            <td width="20%">
                <img src="${context}/resources/images/add_new_admin.png" alt="add_admin" class="round_button"
                     id="addAdmin">
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/admin/mainPage'">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">
    <div class="common_table">
        <table id="mainTable">
            <tr>
                <td width="5%"></td>
                <td width="5%" id="idSort">№</td>
                <td width="20%" id="nameSort">Name</td>
                <td width="9%">First Name</td>
                <td width="9%">Middle Name</td>
                <td width="9%">Last Name</td>
                <td width="13%">email</td>
                <td width="2%"></td>
                <td width="10%">Phone</td>
                <td width="9%">created</td>
                <td width="9%">updated</td>
            </tr>
        </table>
    </div>
    <div class="common_table">
        <tag:ajaxPaging></tag:ajaxPaging>
    </div>
</div>
<div id="modal_form" hidden>
    <span id="modal_close">X</span> <!-- Кнoпкa зaкрыть -->
    <br/>
    <table class="common_table">
        <form method="POST" id="form" action="">
            <tr>
                <td width="40%"></td>
                <td width="60%">Please enter registration data</td>
            </tr>

            <tr>
                <td>Login<span>*</span></td>
                <td><input id="login" path="login"/> <span id="compareLogin"></span></td>
            </tr>
            <tr>
                <td>Password<span>*</span></td>
                <td><input type="password" id="pass1" path="password"/> <span id="comparePass1"></span></td>
            </tr>
            <tr>
                <td>Confirm Password<span>*</span></td>
                <td><input type="password" id="pass2" path="confirmPassword"/> <span id="comparePass2"></span></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:match-parent;">
                    <button id="addAdminButton" class="simple_button">
                        Add admin
                    </button>
                </td>
            </tr>
        </form>
    </table>
</div>
<div id="overlay" hidden></div>

<script src="${context}/resources/js/common_modal.js"></script>
<script>
    function addAdmin() {
        $.ajax({
            url: '${context}/admin/addAdminAction',
            type: 'POST',
            datatype: 'json',
            data: {login: $("#login").val(), password: $("#pass1").val()},
            success: function (response) {
                if (response == true) {
                    alert('ok');
                } else {
                    alert('ne ok');
                }
            }
        });
    }
    function useObtainedData(data) {
        $('#pageNumber').html(data.page);
        $('#idLike').val(data.idLike);
        $('#nameLike').val(data.nameLike);
        $("#mainTable").find("tr:gt(0)").remove();
        var entityList = data.entityList;
        var accountActive, emailActive;
        var dateCreate, dateUpdated;
        for (var ind in entityList) {
            if (entityList[ind].active) {
                accountActive = "<img src='${context}/resources/images/yes.png' id='idI" + entityList[ind].idAccount + "'>";
            } else {
                accountActive = "<img src='${context}/resources/images/no.png' id='idI" + entityList[ind].idAccount + "'>";
            }
            if (entityList[ind].emailVerified) {
                emailActive = "<img src='${context}/resources/images/yes.png' id='idE" + entityList[ind].idAccount + "'>";
            } else {
                emailActive = "<img src='${context}/resources/images/no.png' id='idE" + entityList[ind].idAccount + "'>";
            }
            dateCreate = getOnlyDateFromTimeStamp(entityList[ind].created);
            dateUpdated = getOnlyDateFromTimeStamp(entityList[ind].updated);

            $('#mainTable tr:last').after(""
                    + "<tr id='idA" + entityList[ind].idAccount + "'>"
                    + "<td>" + accountActive + "</td>"
                    + "<td>" + entityList[ind].idAccount + "</td>"
                    + "<td>" + entityList[ind].login + "</td>"
                    + "<td>" + entityList[ind].firstName + "</td>"
                    + "<td>" + entityList[ind].middleName + "</td>"
                    + "<td>" + entityList[ind].lastName + "</td>"
                    + "<td>" + entityList[ind].email + "</td>"
                    + "<td>" + emailActive + "</td>"
                    + "<td>" + entityList[ind].phone + "</td>"
                    + "<td>" + dateCreate + "</td>"
                    + "<td>" + dateUpdated + "</td>"
                    + "</tr>"
            );
            $("#idI" + entityList[ind].idAccount).addClass('common_table_active_account');
            $("#idE" + entityList[ind].idAccount).addClass('common_table_active_email');
        }
    }
    function ajaxClick(sendData) {
        $.ajax({
            url: '${context}/admin/listAllAccountAjax',
            type: 'POST',
            datatype: 'json',
            data: sendData,
            success: function (response) {
                useObtainedData(response);
            }
        });
    }
    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/account.png");
        $("span").css('color', '#ff0000');
        $("#addAdminButton").attr('onclick', '').click(addAdmin);
        ajaxClick();
    });
</script>
</body>
</html>

