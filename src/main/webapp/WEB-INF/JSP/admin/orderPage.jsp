<%@ page import="ua.george_nika.restaurant.util.RestaurantConstant" %>
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
            <td width="20%">
                <div id="idOrder"></div>
                <div id="ownerName"></div>
                <div id="orderTime">
                    Order time <input type="text" value="2015/12/12 18:00" id="datetimepicker" disabled/>
                </div>
            </td>
            <td width="20%">
                <h2>ORDER</h2>
            </td>
            <td width="20%">
                <div id="allPositions"></div>
                <div id="allCost"></div>
            </td>
            <td width="20%">
                <img src="${context}/resources/images/send_order.png" alt="order" id="sendOrderButton">
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/admin/allOrderPage'">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">
    <div class="common_table">
        <table id="mainTable">
            <tr>
                <td width="5%">№</td>
                <td width="60%">Name</td>
                <td width="5%">Weight</td>
                <td width="10%">Price</td>
                <td width="15%">Quantity</td>
                <td width="15%">Cost</td>
            </tr>
        </table>
    </div>
</div>

<script src="${context}/resources/js/common_modal.js"></script>

<script>
    function getOrderInfo() {
        $.ajax({
            url: '${context}/ajax/getOrderAjax',
            type: 'POST',
            datatype: 'json',
            data: {idOrder: ${idOrder}},
            success: function (response) {
                useObtainedDataForOrder(response);
            }
        });
    }
    function useObtainedDataForOrder(data) {
        var entityList = data.entityList;
        $("#idOrder").html("Order number - " + data.idOrder);
        $("#ownerName").html("Order to - " + data.owner);
        $("#allPositions").html("Positions - " + data.allPositions);
        $("#allCost").html("Total cost - " + getPriceFromInteger(data.allCost));
        $("#datetimepicker").val(data.orderTime);
        if (data.status == <%=RestaurantConstant.ORDER_STATUS_DONE%>) {
            $("#sendOrderButton").attr("src", "${context}/resources/images/order_done.png").attr("onClick", '');
        }

        $("#mainTable").find("tr:gt(0)").remove();
        for (var ind in entityList) {
            if (entityList[ind].menuGroup) {
                $('#mainTable tr:last').after(""
                        + "<tr>"
                        + "<td colspan = '7' style='text-align: center'>" + entityList[ind].menuItemName + "</td>"
                        + "</tr>"
                );
            } else {
                $('#mainTable tr:last').after(""
                        + "<tr id='idO" + entityList[ind].idMenuItem + "'>"
                        + "<td>" + entityList[ind].idMenuItem + "</td>"
                        + "<td>" + entityList[ind].menuItemName + "</td>"
                        + "<td>" + entityList[ind].weight + "</td>"
                        + "<td>" + getPriceFromInteger(entityList[ind].price) + "</td>"
                        + "<td>" + entityList[ind].allQuantity + "</td>"
                        + "<td>" + getPriceFromInteger(entityList[ind].allQuantity * entityList[ind].price) + "</td>"
                        + "</tr>"
                );
            }
        }
    }


    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/order.png");
        getOrderInfo();
    });
</script>
</body>
</html>
