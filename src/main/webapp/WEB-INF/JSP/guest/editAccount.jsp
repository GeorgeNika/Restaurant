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
            <td width="60%">
                <h2>PROFILE PAGE</h2>
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/guest/mainPage'">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">
    <table class="common_table">
        <tr>
            <td> Data</td>
        </tr>
        <tr>
            <td class="text_block">
                <form:form method="POST" action="${context}/guest/editAccountAction"
                           commandName="editAccountForm">
                    <table>
                        <tr>
                            <h1> Edit account id - ${editAccount.idAccount} :: Login - ${editAccount.login}</h1>
                        </tr>
                        <tr>
                            <td colspan="2" class="errors"><form:errors path="*"/></td>
                        </tr>
                        <tr>
                            <td>Password</td>
                            <td><form:password id="pass1" path="password"/> <span id="comparePass1"></span></td>
                        </tr>
                        <tr>
                            <td>Confirm password</td>
                            <td><form:password id="pass2" path="confirmPassword"/> <span id="comparePass2"></span></td>
                        </tr>
                        <tr>
                            <td>E-mail</td>
                            <td><form:input path="email"/></td>
                        </tr>
                        <tr>
                            <td>First Name</td>
                            <td><form:input path="firstName"/></td>
                        </tr>
                        <tr>
                            <td>Middle Name</td>
                            <td><form:input path="middleName"/></td>
                        </tr>
                        <tr>
                            <td>Last Name</td>
                            <td><form:input path="lastName"/></td>
                        </tr>
                        <tr>
                            <td>Phone</td>
                            <td><form:input path="phone"/></td>
                        </tr>

                        <tr>
                            <td colspan="2" style="text-align:match-parent;">
                                <input type="submit" class="simple_button" value="Edit"/>
                            </td>
                        </tr>
                    </table>
                </form:form>
            </td>

        </tr>
    </table>
</div>

<script>
    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/profile.png");
        $("span").css('color', '#ff0000');
    });
</script>
</body>
</html>
