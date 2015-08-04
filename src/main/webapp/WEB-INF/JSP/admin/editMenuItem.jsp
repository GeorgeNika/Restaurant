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
                <h2>EDIT MENU ITEM</h2>
            </td>
            <td width="20%">
                <img src="${context}/resources/images/close_page.png" alt="close" class="round_button"
                     onclick="window.location.href ='${context}/admin/menuPage'">
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
                <form:form method="POST" action="${context}/admin/editMenuItemAction/${editMenuItem.idMenuItem}"
                           commandName="editMenuItemForm">
                    <table>
                        <tr>
                            <h1> Edit menu item id - ${editMenuItem.idMenuItem} :: name
                                - ${editMenuItem.menuItemName}</h1>
                        </tr>
                        <tr>
                            <td colspan="2" class="errors"><form:errors path="*"/></td>
                        </tr>
                        <tr>
                            <td>Name</td>
                            <td><form:input path="menuItemName"/></td>
                        </tr>
                        <tr>
                            <td>Weight</td>
                            <td><form:input path="weight"/></td>
                        </tr>
                        <tr>
                            <td>Price cent</td>
                            <td><form:input path="price"/></td>
                        </tr>
                        <tr>
                            <td>Photo</td>
                            <td><form:input path="photo"/></td>
                        </tr>
                        <tr>
                            <td colspan="2" style="text-align:match-parent;">
                                <input type="submit" class="simple_button" value="Save"/>
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
        $('#topLeftIcon').attr("src", "${context}/resources/images/menu.png");
        $("span").css('color', '#ff0000');
    });
</script>
</body>
</html>
