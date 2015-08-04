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
            <td width="10%"></td>
            <td width="20%">
                <h2>MENU PAGE</h2>
            </td>
            <td width="20%">
                <select class="cs-select cs-skin-circular">
                    <option value="" disabled>Выберите элемент</option>
                    <option value="1" selected>1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                </select>
            </td>
            <td width="10%"></td>
            <td width="20%">
                <img src="${context}/resources/images/add_menu_item.png" alt="logout" class="round_button"
                     onclick="window.location.href ='${context}/admin/addMenuItemAction'">
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/admin/mainPage'">
            </td>
        </tr>
    </table>
</div>
<div class="articleInPage">
    <div class="common_table">
        <table id="mainTable">
            <tr>
                <td width="5%"></td>
                <td width="5%" id="idSort">№</td>
                <td width="40%" id="nameSort">Name</td>
                <td width="5%">Weight</td>
                <td width="10%">Price</td>
                <td width="10%">created</td>
                <td width="10%">updated</td>
                <td width="10%">photo</td>
                <td width="5%">action</td>
            </tr>
        </table>
    </div>
    <div class="common_table">
        <tag:ajaxPaging></tag:ajaxPaging>
    </div>
</div>

<script src="${context}/resources/js/classie.js"></script>
<script src="${context}/resources/js/selectFx.js"></script>
<script>
    (function () {
        [].slice.call(document.querySelectorAll('select.cs-select')).forEach(function (el) {
            new SelectFx(el, {
                stickyPlaceholder: false,
                onChange: function (val) {
                    var imgName = 'menu_group' + val;
                    var img = document.createElement('img');
                    img.src = '${context}/resources/images/' + imgName + '.png';
                    img.onload = function () {
                        document.querySelector('span.cs-placeholder').style.backgroundImage =
                                'url(${context}/resources/images/' + imgName + '.png)';
                    };
                }
            });
        });
    })();
</script>
<script>
    function accountHref(data) {
        var tempId = $(data.target).parent().attr('id');
        var searchId = tempId.substring(3, tempId.length);
        window.location.href = '${context}/admin/menuItemPage/' + searchId;
    }
    function changeEnableClick(data) {
        var tempId = $(data.target).attr('id');
        var searchId = tempId.substring(3, tempId.length);
        $.ajax({
            url: '${context}/admin/changeEnableMenuItemAjax',
            type: 'POST',
            datatype: 'json',
            data: {idMenuItem: searchId},
            success: function () {
                ajaxClick();
            }
        });
    }
    function useObtainedData(data) {
        $('#pageNumber').html(data.page);
        $('#idLike').val(data.idLike);
        $('#nameLike').val(data.nameLike);
        $("#mainTable").find("tr:gt(0)").remove();
        var entityList = data.entityList;
        var accountActive, actionEnable;
        var dateCreate, dateUpdated;
        for (var ind in entityList) {
            if (entityList[ind].active) {
                accountActive = "<img src='${context}/resources/images/yes.png' id='idI" + entityList[ind].idMenuItem + "'>";
                actionEnable = "<img src='${context}/resources/images/no.png' id='idE" + entityList[ind].idMenuItem + "'>";
            } else {
                accountActive = "<img src='${context}/resources/images/no.png' id='idI" + entityList[ind].idMenuItem + "'>";
                actionEnable = "<img src='${context}/resources/images/yes.png' id='idE" + entityList[ind].idMenuItem + "'>";
            }
            dateCreate = getOnlyDateFromTimeStamp(entityList[ind].created);
            dateUpdated = getOnlyDateFromTimeStamp(entityList[ind].updated);

            $('#mainTable tr:last').after(""
                    + "<tr id='idA" + entityList[ind].idMenuItem + "'>"
                    + "<td>" + accountActive + "</td>"
                    + "<td>" + entityList[ind].idMenuItem + "</td>"
                    + "<td>" + entityList[ind].menuItemName + "</td>"
                    + "<td>" + entityList[ind].weight + "</td>"
                    + "<td>" + getPriceFromInteger(entityList[ind].price) + "</td>"
                    + "<td>" + dateCreate + "</td>"
                    + "<td>" + dateUpdated + "</td>"
                    + "<td>" + entityList[ind].photo + "</td>"
                    + "<td>" + actionEnable + "</td>"
                    + "</tr>"
            );
            $(document).off("click", "#idA" + entityList[ind].idMenuItem);
            $(document).on("click", "#idA" + entityList[ind].idMenuItem, accountHref);
            $("#idI" + entityList[ind].idMenuItem).addClass('common_table_active_account');
            $(document).off("click", "#idE" + entityList[ind].idMenuItem);
            $(document).on("click", "#idE" + entityList[ind].idMenuItem, changeEnableClick);
            $("#idE" + entityList[ind].idMenuItem).addClass('common_table_active_account');
        }
    }
    function ajaxClick(sendData) {
        $.ajax({
            url: '${context}/ajax/listMenuItemAjax',
            type: 'POST',
            datatype: 'json',
            data: sendData,
            success: function (response) {
                useObtainedData(response);
            }
        });
    }
    function changeMenuGroup(event) {
        $.ajax({
            url: '${context}/ajax/changeMenuGroupAjax',
            type: 'POST',
            datatype: 'json',
            data: {idMenuGroup: $(event.target).attr('data-value')},
            success: function (response) {
                toFirstPage();
            }
        });
    }
    $(document).ready(function () {
        $('#topLeftIcon').attr("src", "${context}/resources/images/menu.png");

        $('[data-value = 1]').click(changeMenuGroup);
        $('[data-value = 2]').click(changeMenuGroup);
        $('[data-value = 3]').click(changeMenuGroup);
        $('[data-value = 4]').click(changeMenuGroup);
        $('[data-value = 5]').click(changeMenuGroup);
        $('[data-value = 6]').click(changeMenuGroup);
        $('[data-value = 7]').click(changeMenuGroup);
        $('[data-value = 8]').click(changeMenuGroup);

        var menuGroupSelected =${menuGroupSelected};
        if (menuGroupSelected != 0) {
            ajaxClick();
            var imgName = 'menu_group' + menuGroupSelected;
            document.querySelector('span.cs-placeholder').style.backgroundImage =
                    'url(${context}/resources/images/' + imgName + '.png)';
        } else {
            $('span.cs-placeholder').click();
            $('[data-value = 1]').click();
        }
    });
</script>
</body>
</html>

