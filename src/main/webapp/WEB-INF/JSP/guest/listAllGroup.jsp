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
            <td width="10%"></td>
            <td width="10%">
                <img src="${context}/resources/images/all_group.png" alt="all" >
            </td>
            <td width="20%">
                <h2>PAGE</h2>
            </td>
            <td width="10%">
                <img src="${context}/resources/images/group_owner.png" alt="owner" class="round_button"
                     onclick="window.location.href ='${context}/guest/listIOwnerOfGroupPage'">
            </td>
            <td width="10%">
                <img src="${context}/resources/images/group_member.png" alt="member" class="round_button"
                     onclick="window.location.href ='${context}/guest/listIMemberOfGroupPage'">
            </td>
            <td width="20%">
                <img src="${context}/resources/images/create_group.png" alt="create" class="round_button"
                     onclick="window.location.href ='${context}/guest/createNewGroupPage'">
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/guest/mainPage'">
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
                <td width="50%" id="nameSort">Name</td>
                <td width="20%">Owner</td>
                <td width="10%">Members</td>
                <td width="10%">created</td>
            </tr>
        </table>
    </div>
    <div class="common_table">
        <tag:ajaxPaging></tag:ajaxPaging>
    </div>
</div>

<script>
    function accountHref(data) {
        var tempId = $(data.target).parent().attr('id');
        var searchId = tempId.substring(3, tempId.length);
        window.location.href = '${context}/guest/groupPage/' + searchId;
    }
    function useObtainedData(data) {
        $('#pageNumber').html(data.page);
        $('#idLike').val(data.idLike);
        $('#nameLike').val(data.nameLike);
        $("#mainTable").find("tr:gt(0)").remove();
        var entityList = data.entityList;
        var accountActive;
        var dateCreate;
        for (var ind in entityList) {

            if (entityList[ind].active) {
                accountActive = "<img src='${context}/resources/images/yes.png' id='idI" + entityList[ind].idClient + "'>";
            } else {
                accountActive = "<img src='${context}/resources/images/no.png' id='idI" + entityList[ind].idClient + "'>";
            }

            dateCreate = getOnlyDateFromTimeStamp(entityList[ind].created);

            $('#mainTable tr:last').after(""
                    + "<tr id='idA" + entityList[ind].idClient + "'>"
                    + "<td>" + accountActive + "</td>"
                    + "<td>" + entityList[ind].idClient + "</td>"
                    + "<td>" + entityList[ind].clientName + "</td>"
                    + "<td>" + entityList[ind].clientOwner + "</td>"
                    + "<td>" + entityList[ind].groupSize + "</td>"
                    + "<td>" + dateCreate + "</td>"
                    + "</tr>"
            );
            $(document).off("click", "#idA" + entityList[ind].idClient);
            $(document).on("click", "#idA" + entityList[ind].idClient, accountHref);
            $("#idI" + entityList[ind].idClient).addClass('common_table_active_account');
        }
    }
    function ajaxClick(sendData) {
        $.ajax({
            url: '${context}/ajax/listAllGroupAjax',
            type: 'POST',
            datatype: 'json',
            data: sendData,
            success: function (response) {
                useObtainedData(response);
            }
        });
    }
    $(document).ready(function () {
        ajaxClick();
        $('#topLeftIcon').attr("src", "${context}/resources/images/group.png");
    });
</script>
</body>
</html>

