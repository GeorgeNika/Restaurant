<%@ page import="ua.george_nika.restaurant.util.RestaurantConstant" %>
<%@ page import="ua.george_nika.restaurant.entity.AccountEntity" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<link rel="shortcut icon" href="${context}/favicon.ico" type="image/x-icon">
<html>
<head>
    <link rel="stylesheet" href="${context}/resources/css/common_page.css" type="text/css">
    <link rel="stylesheet" href="${context}/resources/css/common_table.css" type="text/css">
    <link rel="stylesheet" href="${context}/resources/css/common_modal.css" type="text/css">
    <link rel="stylesheet" href="${context}/resources/css/slide.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="${context}/resources/css/cs-select.css"/>
    <link rel="stylesheet" type="text/css" href="${context}/resources/css/cs-skin-circular.css"/>
    <link rel="stylesheet" type="text/css" href="${context}/resources/css/cs-skin-overlay.css"/>
    <!--[if IE]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <link rel="stylesheet" href="${context}/resources/css/jquery.datetimepicker.css" type="text/css">

    <title></title>

    <script src="https://www.google.com/recaptcha/api.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
</head>
<body class="body">
<div class="header">
    <table width="100%">
        <tr>
            <td width="10%">
                <img id="topLeftIcon" src="${context}/resources/images/empty_action.png">
            </td>
            <td width="76%">
                <img src="${context}/resources/images/top2.png" alt="logo" class="logo">
            </td>
            <td width="4%"></td>
            <td width="10%">
                <img src="${context}/resources/images/logout.png" alt="logout" class="round_button"
                     onclick="window.location.href ='${context}/logoutAction'">
            </td>
        </tr>
    </table>
    <hr class="line"/>
    <div>
        <c:set var="request_info" value="<%=RestaurantConstant.REQUEST_INFO%>"/>
        <c:set var="request_error" value="<%=RestaurantConstant.REQUEST_ERROR%>"/>
        <c:choose>
            <c:when test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION != null }">
                <div class="error_line" id="info_div">
                        ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message }
                </div>
            </c:when>
            <c:when test="${not empty requestScope.get(request_error)}">
                <div class="error_line" id="info_div">
                        ${requestScope.get(request_error)}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.get(request_info)}">
                <div class="info_line" id="info_div">
                        ${requestScope.get(request_info)}
                </div>
            </c:when>
            <c:otherwise>
                <div id="info_div">
                    <br/>
                </div>
            </c:otherwise>
        </c:choose>
        <c:set var="account_entity"
               value="<%=((AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT))%>"/>
        <div id="accountName" hidden>
            ${account_entity.login}
        </div>
    </div>
    <hr class="line"/>
</div>
<div class="left">
    <img src="${context}/resources/images/order.png" alt="order" class="round_button"
         onclick="window.location.href='${context}/guest/listMyOrderPage'">
    <br/>
    <img src="${context}/resources/images/group.png" alt="group" class="round_button"
         onclick="window.location.href='${context}/guest/listIOwnerOfGroupPage'">
    <br/>
    <img src="${context}/resources/images/profile.png" alt="account" class="round_button"
         onclick="window.location.href='${context}/guest/editAccountPage'">
</div>
<div class="article">
    <section>
        <decorator:body>

        </decorator:body>
    </section>
</div>
<script src="${context}/resources/js/common.js"></script>
<script src="${context}/resources/js/jquery.datetimepicker.js"></script>

</body>
</html>



