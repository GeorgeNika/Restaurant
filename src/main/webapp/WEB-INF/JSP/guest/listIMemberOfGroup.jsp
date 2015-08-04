<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                <img src="${context}/resources/images/group_member.png" alt="member" >
            </td>
            <td width="20%">
                <h2>PAGE</h2>
            </td>
            <td width="10%">
                <img src="${context}/resources/images/all_group.png" alt="all" class="round_button"
                     onclick="window.location.href ='${context}/guest/listAllGroupPage'">
            </td>
            <td width="10%">
                <img src="${context}/resources/images/group_owner.png" alt="owner" class="round_button"
                     onclick="window.location.href ='${context}/guest/listIOwnerOfGroupPage'">
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
                <td width="5%">№</td>
                <td width="55%">Name</td>
                <td width="20%">Owner</td>
                <td width="5%">Members</td>
                <td width="10%">created</td>
            </tr>
            <c:forEach var="group" items="${groupList}">
                <tr onclick="window.location.href='${context}/guest/groupPage/${group.idClient}'">
                    <td>
                        <c:choose>
                            <c:when test="${group.active}">
                                <img src="${context}/resources/images/yes.png" class="common_table_active_account">
                            </c:when>
                            <c:otherwise>
                                <img src="${context}/resources/images/no.png" class="common_table_active_account">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${group.idClient}</td>
                    <td>${group.clientName}</td>
                    <td>${group.accountOwner.login}</td>
                    <td>${group.memberList.size()}</td>
                    <td>${group.created}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/group.png");
    });
</script>
</body>
</html>
