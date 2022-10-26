<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/3/22
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>
<html>
<head>
    <script>
        $(function (){
            $("#editFrom").submit(function (){
                if(!checkEmpty("name","分类名称"))
                    return false;
                return true;
            });
        });
    </script>
    <title>编辑属性</title>
</head>
<body>
<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a> </li>
        <li class="active">${p.category.name}</li>
        <li><a href="admin_property_list?cid=${p.category.id}">${p.name}</a> </li>
        <li class="active">编辑属性</li>
    </ol>
    <div class="panel panel-warning editDiv">
        <div class="panel-heading">编辑属性</div>
        <div class="panel-body">
            <form method="post" action="admin_property_update" id="editForm">
                <table class="editTable">
                    <tr>
                        <td>属性名称</td>
                        <td><input type="text" id="name" name="name" value="${p.name}"class="form-control"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input type="hidden" name="id" value="${p.id}">
                            <input type="hidden" name="cid" value="${p.category.id}">
                            <button type="submit" class="btn btn-success">提 交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<%@include file="../include/admin/adminFooter.jsp"%>