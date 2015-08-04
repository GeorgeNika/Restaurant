<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<body>
<div width="90%" class="common_table">
    <table>
        <form:form method="POST" id="form" action=""
                   commandName="registerNewAccountForm">
            <tr>
                <td width="40%"></td>
                <td width="60%">Please enter registration data</td>
            </tr>
            <tr>
                <td colspan="2" class="errors"><form:errors path="*"/></td>
            </tr>
            <tr>
                <td>Login<span>*</span></td>
                <td><form:input id="login" path="login"/> <span id="compareLogin"></span></td>
            </tr>
            <tr>
                <td>Password<span>*</span></td>
                <td><form:password id="pass1" path="password"/> <span id="comparePass1"></span></td>
            </tr>
            <tr>
                <td>Confirm Password<span>*</span></td>
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
                    <button id="emailButton" class="simple_button">
                        Register with email verification
                    </button>
                </td>
            </tr>
            <tr id="reCaptchaDataTr" hidden>
                <td></td>
                <td><form:input id="reCaptchaData" path="reCaptchaData"/></td>
            </tr>
            <tr id="reCaptchaTr" hidden>
                <td colspan="2">
                    <div id="reCaptcha" class="g-recaptcha"
                         data-sitekey="6LducwoTAAAAAKyj9sOskLORPY9ajB736-lylByf"
                         data-callBack="reCaptchaSubmit"></div>
                </td>
            </tr>
            <tr id="reCaptchaButtonTr">
                <td colspan="2" style="text-align:match-parent;">
                    <button id="reCaptchaButton" class="simple_button" type="button">
                        Register with reCAPTCHA
                    </button>
                </td>
            </tr>
        </form:form>
    </table>
</div>

<script>
    function reCaptchaSubmit(dat) {
        $("#reCaptchaData").val(dat);
        $("#form").attr('action', '${context}/registerNewAccountWithoutEmailAction').submit();
    }
    $(document).ready(function () {
        $("span").css('color', '#ff0000');

        $("#reCaptchaTr").hide();
        $("#reCaptchaDataTr").hide();
        $("#emailButton").click(emailChoice);
        $("#reCaptchaButton").click(reCaptchaChoice);
    });
    function emailChoice() {
        $("#form").attr('action', '${context}/registerNewAccountAction').submit();
    }
    function reCaptchaChoice() {
        $("#reCaptchaTr").show();
        $("#reCaptchaButton").attr('onclick', '').unbind('click');
        $("#reCaptchaButtonTr").hide()
    }
</script>
</body>




