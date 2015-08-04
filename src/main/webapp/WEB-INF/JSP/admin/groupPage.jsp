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
            <td width="20%"></td>
            <td width="20%">
                <h2>GROUP PAGE</h2>
            </td>
            <td width="40%">
                <h4>Id - ${idGroup}</h4>
                <h4>Name - ${groupName}</h4>
                <h4>Owner - ${ownerLogin}</h4>
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/admin/allGroupPage'">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">

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
</div>

<script>
    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/group.png");
        $("#memberDiv").show();
        getMemberInfo();
    });
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
    function useObtainedDataForMember(data) {
        var entityList = data;
        var dateCreate;
        $("#memberTable").find("tr:gt(0)").remove();
        for (var ind in entityList) {
            var actionDel = "";
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
        }
    }
</script>
</body>
</html>
