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
                    Order time <input type="text" value="2015/12/12 18:00" id="datetimepicker"/>
                </div>
            </td>
            <td width="20%">
                <select class="cs-select cs-skin-circular">
                    <option value="" disabled selected>Выберите элемент</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                </select>
            </td>
            <td width="20%">
                <div id="myPositions"></div>
                <div id="myCost"></div>
            </td>
            <td width="20%">
                <img src="${context}/resources/images/send_order.png" alt="order" class="round_button"
                     id="sendOrderButton"
                     onclick="window.location.href ='${context}/guest/sendOrderAction/${editPersonOrder.idOrder}'">
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/guest/listMyOrderPage'">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">
    <div class="common_table">
        <table id="mainTable">
            <tr>
                <td width="5%">№</td>
                <td width="40%">Name</td>
                <td width="5%">Weight</td>
                <td width="10%">Price</td>
                <td width="10%">Quantity</td>
                <td width="15%">Cost</td>
                <td width="15%">Action</td>
            </tr>
        </table>
    </div>
</div>
<div hidden>
    <button id="addAdmin"></button>
</div>
<div id="modal_form" hidden class="common_table" style="overflow: auto">
    <table id="selectMenuItemTable">
        <tr>
            <td colspan="2" id="menuGroupItemName">
                Type
            </td>
            <td>weight</td>
            <td>price</td>
        </tr>
    </table>
</div>
<div id="overlay" hidden></div>

<script src="${context}/resources/js/common_modal.js"></script>
<script src="${context}/resources/js/classie.js"></script>
<script src="${context}/resources/js/selectFx.js"></script>

<script>
    (function () {
        [].slice.call(document.querySelectorAll('select.cs-select')).forEach(function (el) {
            new SelectFx(el, {
                stickyPlaceholder: false
            });
        });
    })();
</script>

<script>
    function getOrderInfo() {
        $.ajax({
            url: '${context}/ajax/getOrderAjax',
            type: 'POST',
            datatype: 'json',
            data: {idOrder: ${editPersonOrder.idOrder}},
            success: function (response) {
                useObtainedDataForOrder(response);
            }
        });
    }
    function useObtainedDataForOrder(data) {
        var entityList = data.entityList;
        var inputDisable;
        var orderDone = false;
        $("#idOrder").html("Order number - " + data.idOrder);
        $("#ownerName").html("Order to - " + data.owner);
        $("#myPositions").html("Positions - " + data.myPositions);
        $("#myCost").html("Total cost - " + getPriceFromInteger(data.myCost));
        $("#datetimepicker").datetimepicker({value: data.orderTime});
        if (data.status == <%=RestaurantConstant.ORDER_STATUS_DONE%>) {
            $("#sendOrderButton").attr("src", "${context}/resources/images/order_done.png").attr("onClick", '')
                    .removeClass();
            $("#datetimepicker").attr('disabled', true);
            inputDisable = "disabled";
            orderDone = true;
        }

        $("#mainTable").find("tr:gt(0)").remove();
        for (var ind in entityList) {
            var actionDel = "";
            if (entityList[ind].menuGroup) {
                $('#mainTable tr:last').after(""
                        + "<tr>"
                        + "<td colspan = '7' style='text-align: center'>" + entityList[ind].menuItemName + "</td>"
                        + "</tr>"
                );
            } else {
                if (!orderDone) {
                    actionDel = "<img src='${context}/resources/images/no.png' id='idD"
                            + entityList[ind].idMenuItem + "'>";
                }
                $('#mainTable tr:last').after(""
                        + "<tr id='idO" + entityList[ind].idMenuItem + "'>"
                        + "<td>" + entityList[ind].idMenuItem + "</td>"
                        + "<td>" + entityList[ind].menuItemName + "</td>"
                        + "<td>" + entityList[ind].weight + "</td>"
                        + "<td>" + getPriceFromInteger(entityList[ind].price) + "</td>"
                        + "<td> <input type='number' " + inputDisable + " value='" + entityList[ind].myQuantity
                        + "' id='idQ" + entityList[ind].idMenuItem + "'></td>"
                        + "<td>" + getPriceFromInteger(entityList[ind].myQuantity * entityList[ind].price) + "</td>"
                        + "<td>" + actionDel + "</td>"
                        + "</tr>"
                );
                if (!orderDone) {
                    $(document).off("click", "#idD" + entityList[ind].idMenuItem);
                    $(document).on("click", "#idD" + entityList[ind].idMenuItem, orderItemDelClick);
                    $("#idQ" + entityList[ind].idMenuItem).attr('onchange', '').change(orderItemQuantityChange);
                }
            }
        }
    }
    function orderItemQuantityChange(event) {
        var tempId = $(event.target).attr('id');
        var searchId = tempId.substring(3, tempId.length);
        orderItemChangeQuantity(searchId, $(event.target).val());
    }
    function orderItemDelClick(data) {
        var tempId = $(data.target).attr('id');
        var searchId = tempId.substring(3, tempId.length);
        orderItemChangeQuantity(searchId, 0);
    }
    function orderItemChangeQuantity(idMenuItem, quantity) {
        $.ajax({
            url: '${context}/guest/orderItemChangeQuantityAjax',
            type: 'POST',
            datatype: 'json',
            data: {idOrder: ${editPersonOrder.idOrder}, idMenuItem: idMenuItem, quantity: quantity},
            success: function () {
                getOrderInfo();
            }
        });
    }
    function changeOrderTime(event) {
        $.ajax({
            url: '${context}/guest/orderTimeChangeAjax',
            type: 'POST',
            datatype: 'json',
            data: {idOrder: ${editPersonOrder.idOrder}, orderTime: $(event.target).val()},
            success: function () {
            }
        });
    }
    function selectMenuItem(data) {
        var tempId = $(data.target).parent().attr('id');
        var searchId = tempId.substring(3, tempId.length);
        $.ajax({
            url: '${context}/guest/addMenuItemToOrderAjax',
            type: 'POST',
            datatype: 'json',
            data: {idOrder: ${editPersonOrder.idOrder}, idMenuItem: searchId},
            success: function () {
                getOrderInfo();
            }
        });
        $('#modal_close, #overlay').click();

    }
    function useObtainedDataForSelectMenuItem(data) {
        $("#menuGroupItemName").html(data.menuItemName);
        var entityList = data.entityList;
        $("#selectMenuItemTable").find("tr:gt(0)").remove();
        for (var ind in entityList) {
            $('#selectMenuItemTable tr:last').after(""
                    + "<tr id='idM" + entityList[ind].idMenuItem + "'>"
                    + "<td>" + entityList[ind].idMenuItem + "</td>"
                    + "<td>" + entityList[ind].menuItemName + "</td>"
                    + "<td>" + entityList[ind].weight + "</td>"
                    + "<td>" + getPriceFromInteger(entityList[ind].price) + "</td>"
                    + "</tr>"
            );
            $(document).off("click", "#idM" + entityList[ind].idMenuItem);
            $(document).on("click", "#idM" + entityList[ind].idMenuItem, selectMenuItem);
        }
        $('#addAdmin').click();
    }
    function changeMenuGroup(event) {
        $.ajax({
            url: '${context}/ajax/getMenuGroupItemListAjax',
            type: 'POST',
            datatype: 'json',
            data: {idMenuGroup: $(event.target).attr('data-value')},
            success: function (response) {
                useObtainedDataForSelectMenuItem(response);
            }
        });
    }
    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/order.png");
        $('#datetimepicker').datetimepicker();
        $('#datetimepicker').attr('onchange', '').change(changeOrderTime);
        getOrderInfo();
        if (!${done}) {
            $('[data-value = 1]').click(changeMenuGroup);
            $('[data-value = 2]').click(changeMenuGroup);
            $('[data-value = 3]').click(changeMenuGroup);
            $('[data-value = 4]').click(changeMenuGroup);
            $('[data-value = 5]').click(changeMenuGroup);
            $('[data-value = 6]').click(changeMenuGroup);
            $('[data-value = 7]').click(changeMenuGroup);
            $('[data-value = 8]').click(changeMenuGroup);
        }
    });
</script>
</body>
</html>
