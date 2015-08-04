<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="common_table">
    <table>
        <tr>
            <td></td>
            <td> Please login</td>
        </tr>

        <form:form method="POST" action="${context}/loginHandler" commandName="loginForm">
            <tr>
                <td>Login</td>
                <td><form:input path="j_username"/></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><form:password path="j_password"/></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input type="checkbox" value="true" name="_spring_security_remember_me" checked>remember me
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" class="simple_button" value="Login"/>
                </td>
            </tr>
        </form:form>
        <tr>
            <td colspan="2" style="text-align: match-parent;">
                <button class="simple_button" onclick="window.location.href ='${context}/backupPasswordPage'">
                    Backup password
                </button>
            </td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: match-parent;">
                <button class="simple_button" onclick="window.location.href ='${context}/registerNewAccountPage'">
                    Register new Account
                </button>
            </td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: match-parent;">
                <button onclick="window.location.href='${context}/fbLoginAction'" class="simple_button">
                    <img src="${context}/resources/images/Facebook-Button.png" class="facebook_button"/>
                    Login with Facebook
                </button>
            </td>
        </tr>
    </table>
</div>



