<%@ page import="ua.george_nika.restaurant.util.RestaurantConstant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<link rel="shortcut icon" href="${context}/favicon.ico" type="image/x-icon">
<html>
<head>
    <link rel="stylesheet" href="${context}/resources/css/common_page.css" type="text/css">
    <link rel="stylesheet" href="${context}/resources/css/common_table.css" type="text/css">

    <title></title>

    <script src="${context}/resources/js/common.js"></script>
    <script src="https://www.google.com/recaptcha/api.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
</head>
<body>
<div class="header">
    <table width="100%">
        <tr>
            <td width="20%"></td>
            <td width="60%">
                <img src="${context}/resources/images/top_guest.png" alt="logo" class="logo" >
            </td>
            <td width="20%"></td>
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
                <div>
                    <br/>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <hr class="line"/>
</div>
<div class="loginLeft">
    <jsp:include page="../account/loginBlock.jsp"/>
</div>
<section class="loginArticle">
    <decorator:body>

    </decorator:body>
</section>

</body>
</html>


