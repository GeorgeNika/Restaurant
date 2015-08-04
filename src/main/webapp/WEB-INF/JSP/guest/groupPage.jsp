<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="headerInPage">
    <table width="95%">
        <tr>
            <td width="5%"></td>
            <td width="15%">
                <h2>GROUP PAGE</h2>
            </td>
            <td width="25%">
                <h4>Id - ${idGroup}</h4>
                <h4>Name - ${groupName}</h4>
                <h4>Owner - ${ownerLogin}</h4>
            </td>
            <td width="10%">
                <div id="orderShowButton" hidden>
                    <img src="${context}/resources/images/show_group_orders.png" alt="show" class="round_button"
                         onclick="window.location.href ='${context}/guest/listGroupOrderPage/${idGroup}'">
                </div>
            </td>
            <td width="10%">
                <div id="orderAddButton" hidden>
                    <img src="${context}/resources/images/add_group_order.png" alt="add" class="round_button"
                         onclick="window.location.href ='${context}/guest/addNewGroupOrderAction/${idGroup}'">
                </div>
            </td>
            <td width="10%">
                <div id="editGroup" hidden>
                    <img src="${context}/resources/images/edit_group_name.png" alt="edit" class="round_button"
                         onclick="window.location.href ='${context}/guest/editGroupPage/${idGroup}'">
                </div>
            </td>
            <td width="10%">
                <div id="disbandGroup" hidden>
                    <img src="${context}/resources/images/disband_group.png" alt="dosband" class="round_button"
                         onclick="window.location.href ='${context}/guest/disbandGroupAction/${idGroup}'">
                </div>
                <div id="leaveGroup" hidden>
                    <img src="${context}/resources/images/leave_group.png" alt="leave" class="round_button"
                         onclick="window.location.href ='${context}/guest/leaveGroupAction/${idGroup}'">
                </div>

            </td>
            <td width="15%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/guest/listIOwnerOfGroupPage'">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">
    <div id="ownerDiv" hidden>
        Requests for membership
        <table id="requestFMSTable" class="common_table">
            <tr>
                <td width="5%">№</td>
                <td width="30%">name</td>
                <td width="40%">info</td>
                <td width="10%">created</td>
                <td width="15%">action</td>
            </tr>
        </table>
    </div>
    <div id="memberDiv" hidden>
        <br/>
        Members of group
        <table id="memberTable" class="common_table">
            <tr>
                <td width="5%">№</td>
                <td width="50%">Login</td>
                <td width="15%">FirstName</td>
                <td width="15%">LastName</td>
                <td width="10%">created</td>
                <td width="5%">action</td>
            </tr>
        </table>
    </div>
    <div id="visitorDiv" hidden>
        <br/>
        <form:form method="POST" id="requestFMSForm" commandName="requestFMSForm">
            <table class="common_table">
                <tr>
                    <td colspan="2" style="text-align:match-parent;"> additional information to you request</td>
                </tr>
                <tr>
                    <td>Info</td>
                    <td><form:input size="110" path="info"/></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:match-parent;">
                        <c:choose>
                            <c:when test="${iSentRequestFMS}">
                                <button id="sendRequestFMSButton" class="left_button">
                                    EDIT request for membership
                                </button>
                                <button id="deleteRequestFMSButton" class="right_button">
                                    DELETE request for membership
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button id="sendRequestFMSButton" class="simple_button">
                                    SEND NEW request for membership
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>

<script>
    function sendRequestFMSChoice() {
        $("#requestFMSForm").attr('action', '${context}/guest/sendRequestFMSAction/${idGroup}').submit();
    }
    function deleteRequestFMSChoice() {
        $("#requestFMSForm").attr('action', '${context}/guest/deleteRequestFMSAction/${idGroup}').submit();
    }
    function getRequestFMSInfo() {
        $.ajax({
            url: '${context}/guest/listRequestFMSAjax',
            type: 'POST',
            datatype: 'json',
            data: {idGroup: ${idGroup}},
            success: function (response) {
                useObtainedDataForRequestFMS(response);
            }
        });
    }
    function getMemberInfo() {
        $.ajax({
            url: '${context}/ajax/listMembersOfGroupAjax',
            type: 'POST',
            datatype: 'json',
            data: {idGroup: ${idGroup}},
            success: function (response) {
                useObtainedDataForMember(response);
            }
        });
    }
    function useObtainedDataForRequestFMS(data) {
        var entityList = data;
        var dateCreate;
        $("#requestFMSTable").find("tr:gt(0)").remove();
        for (var ind in entityList) {

            var actionYes = "<img src='${context}/resources/images/yes.png' id='idY"
                    + entityList[ind].idRequestFMS + "'>";
            var actionNo = "<img src='${context}/resources/images/no.png' id='idN"
                    + entityList[ind].idRequestFMS + "'>";
            dateCreate = getOnlyDateFromTimeStamp(entityList[ind].created);

            $('#requestFMSTable tr:last').after(""
                    + "<tr id='idR" + entityList[ind].idRequestFMS + "'>"
                    + "<td>" + entityList[ind].idRequestFMS + "</td>"
                    + "<td>" + entityList[ind].account + "</td>"
                    + "<td>" + entityList[ind].info + "</td>"
                    + "<td>" + dateCreate + "</td>"
                    + "<td>" + actionYes + " " + actionNo + "</td>"
                    + "</tr>"
            );
            $(document).off("click", "#idY" + entityList[ind].idRequestFMS);
            $(document).on("click", "#idY" + entityList[ind].idRequestFMS, requestFMSYesClick);
            $(document).off("click", "#idN" + entityList[ind].idRequestFMS);
            $(document).on("click", "#idN" + entityList[ind].idRequestFMS, requestFMSNoClick);
            $("#idY" + entityList[ind].idRequestFMS).addClass('common_table_active_account');
            $("#idN" + entityList[ind].idRequestFMS).addClass('common_table_active_account');
        }
    }
    function requestFMSYesClick(data) {
        var tempId = $(data.target).attr('id');
        var searchId = tempId.substring(3, tempId.length);
        $.ajax({
            url: '${context}/guest/acceptRequestFMSAjax',
            type: 'POST',
            datatype: 'json',
            data: {idRequestFMS: searchId},
            success: function () {
                getRequestFMSInfo();
                getMemberInfo();
            }
        });
    }
    function requestFMSNoClick(data) {
        var tempId = $(data.target).attr('id');
        var searchId = tempId.substring(3, tempId.length);
        $.ajax({
            url: '${context}/guest/rejectRequestFMSAjax',
            type: 'POST',
            datatype: 'json',
            data: {idRequestFMS: searchId},
            success: function () {
                getRequestFMSInfo();
            }
        });
    }
    function useObtainedDataForMember(data) {
        var entityList = data;
        var dateCreate;
        $("#memberTable").find("tr:gt(0)").remove();
        for (var ind in entityList) {
            if (${iOwner}) {
                var actionDel = "<img src='${context}/resources/images/no.png' id='idD"
                        + entityList[ind].idAccount + "'>";
            } else {
                var actionDel = "";
            }
            dateCreate = getOnlyDateFromTimeStamp(entityList[ind].created);

            $('#memberTable tr:last').after(""
                    + "<tr id='idM" + entityList[ind].idAccount + "'>"
                    + "<td>" + entityList[ind].idAccount + "</td>"
                    + "<td>" + entityList[ind].login + "</td>"
                    + "<td>" + entityList[ind].firstName + "</td>"
                    + "<td>" + entityList[ind].lastName + "</td>"
                    + "<td>" + dateCreate + "</td>"
                    + "<td>" + actionDel + "</td>"
                    + "</tr>"
            );
            $(document).off("click", "#idD" + entityList[ind].idAccount);
            $(document).on("click", "#idD" + entityList[ind].idAccount, memberDelClick);
            $("#idD" + entityList[ind].idAccount).addClass('common_table_active_account');
        }
    }
    function memberDelClick(data) {
        var tempId = $(data.target).attr('id');
        var searchId = tempId.substring(3, tempId.length);
        $.ajax({
            url: '${context}/guest/deleteMemberAjax',
            type: 'POST',
            datatype: 'json',
            data: {idAccount: searchId, idGroup: ${idGroup}},
            success: function () {
                getMemberInfo();
            }
        });
    }
    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/group.png");
        if (${iOwner}) {
            $("#ownerDiv").show();
            $("#memberDiv").show();
            $("#orderShowButton").show();
            $("#orderAddButton").show();
            $("#editGroup").show();
            $("#disbandGroup").show();
            getRequestFMSInfo();
            getMemberInfo();
        } else if (${iMember}) {
            $("#memberDiv").show();
            $("#orderShowButton").show();
            $("#orderAddButton").show();
            $("#leaveGroup").show();
            getMemberInfo();
        } else {
            $("#visitorDiv").show();
        }
        $("#sendRequestFMSButton").click(sendRequestFMSChoice);
        $("#deleteRequestFMSButton").click(deleteRequestFMSChoice);
    });
</script>
</body>
</html>

