<%@ page import="ua.george_nika.restaurant.util.RestaurantConstant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="headerInPage">
    <table width="95%">
        <tr>
            <td width="5%"></td>
            <td width="15%"><h2>${clientName}</h2></td>
            <td width="40%">
                <h2>ORDERS PAGE</h2>
            </td>
            <td width="20%">
                <img src="${context}/resources/images/add_person_order.png.png"
                     alt="addOrder" class="round_button" id="addOrderButton">
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button" id="closeButton">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">
    <div class="common_table">
        <table id="mainTable">
            <tr>
                <td width="5%">Status</td>
                <td width="5%" id="idSort">№</td>
                <td width="30%" id="nameSort">Name - OrderTime</td>
                <td width="15%">Positions</td>
                <td width="15%">Cost</td>
                <td width="15%">created</td>
                <td width="15%">updated</td>
            </tr>
        </table>
    </div>
    <div class="common_table">
        <tag:ajaxPaging></tag:ajaxPaging>
    </div>
</div>

<script>
    function accountHref(data) {
        var tempId = $(data.target).parent().attr('id');
        var searchId = tempId.substring(3, tempId.length);
        var group = ${idGroup};
        if (group == 0){
            window.location.href = '${context}/guest/personOrderPage/' + searchId;
        }else{
            window.location.href = '${context}/guest/groupOrderPage/' + searchId;
        }
    }
    function useObtainedData(data) {
        $('#pageNumber').html(data.page);
        $('#idLike').val(data.idLike);
        $('#nameLike').val(data.nameLike);
        $("#mainTable").find("tr:gt(0)").remove();
        var entityList = data.entityList;
        var accountActive;
        var dateCreate, dateUpdate, orderTime;
        for (var ind in entityList) {

            if (entityList[ind].status == <%=RestaurantConstant.ORDER_STATUS_DONE%>) {
                accountActive = "<img src='${context}/resources/images/yes.png' id='idI" + entityList[ind].idOrder + "'>";
            } else {
                accountActive = "<img src='${context}/resources/images/no.png' id='idI" + entityList[ind].idOrder + "'>";
            }

            dateCreate = getOnlyDateFromTimeStamp(entityList[ind].created);
            dateUpdate = getOnlyDateFromTimeStamp(entityList[ind].updated);
            orderTime = entityList[ind].orderTime;

            $('#mainTable tr:last').after(""
                    + "<tr id='idA" + entityList[ind].idOrder + "'>"
                    + "<td>" + accountActive + "</td>"
                    + "<td>" + entityList[ind].idOrder + "</td>"
                    + "<td>" + orderTime + "</td>"
                    + "<td>" + entityList[ind].positions + "</td>"
                    + "<td>" + getPriceFromInteger(entityList[ind].cost) + "</td>"
                    + "<td>" + dateCreate + "</td>"
                    + "<td>" + dateUpdate + "</td>"
                    + "</tr>"
            );
            $(document).off("click", "#idA" + entityList[ind].idOrder);
            $(document).on("click", "#idA" + entityList[ind].idOrder, accountHref);
            $("#idI" + entityList[ind].idOrder).addClass('common_table_active_account');
        }
    }
    function ajaxClick(sendData) {
        $.ajax({
            url: '${context}/guest/listOrderAjax',
            type: 'POST',
            datatype: 'json',
            data: sendData,
            success: function (response) {
                useObtainedData(response);
            }
        });
    }
    $(document).ready(function () {
        ajaxClick();
        $('#topLeftIcon').attr("src", "${context}/resources/images/order.png");

        var group = ${idGroup};
        if (group == 0){
            $("#closeButton").attr('onclick',"window.location.href ='${context}/guest/mainPage'");
            $("#addOrderButton").attr("src","${context}/resources/images/add_person_order.png")
                    .attr('onclick',"window.location.href ='${context}/guest/addNewPersonOrderAction'");
        }else{
            $("#closeButton").attr('onclick',"window.location.href ='${context}/guest/groupPage/${idGroup}'");
            $("#addOrderButton").attr("src","${context}/resources/images/add_group_order.png")
                    .attr('onclick',"window.location.href ='${context}/guest/addNewGroupOrderAction/${idGroup}'");
        }
    });
</script>
</body>
</html>
