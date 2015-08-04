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
            <td width="80%"></td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/guest/listIOwnerOfGroupPage'">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">
    <table width="95%">
        <tr>
            <td width="15%"><br/></td>
            <td width="70%">
                <h1>Create new GROUP</h1>
                <form:form method="POST" action="${context}/guest/createNewGroupAction" commandName="groupForm">
                    <div class="common_table">
                        <table>
                            <tr>
                                <td><br/></td>
                                <td><br/></td>
                            </tr>
                            <tr>
                                <td>Name</td>
                                <td><form:input size="110" path="name"/></td>
                            </tr>
                            <tr>
                                <td colspan="2" style="text-align:match-parent;">
                                    <input type="submit" class="simple_button" value="Create new   GROUP"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form:form>
            </td>
            <td width="15%"><br/></td>
        </tr>
    </table>
</div>

<script>
    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/group.png");
    });
</script>
</body>
</html>
