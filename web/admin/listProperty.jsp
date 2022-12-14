<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/3/22
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>
<html>
<body>
<script >
    $(function (){
        $("addForm").submit(function (){
            if(checkEmpty("name","属性名称")){
                return true;
            }
            return false;
        });
    })
</script>
<title>属性管理</title>
<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a> </li>
        <li>${c.name}</li>
        <li class="active">属性管理</li>
    </ol>
    <div class="listDataTableDiv">
        <table class="table table-bordered table-hover table-condensed table-striped">
            <thead>
            <tr class="success">
                <th>ID</th>
                <th>属性名称</th>
                <th>编辑</th>
                <th>删除</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="v">
                <tr>
                    <td>${v.id}</td>
                    <td>${v.name}</td>
                    <td><a href="admin_property_edit?id=${v.id}"><span class="glyphicon glyphicon-edit"></span> </a> </td>
                    <td><a deleteLink="true" href="admin_property_delete?id=${v.id}"><span class="glyphicon glyphicon-trash"></span> </a> </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
<div class="pageDiv">
    <%@include file="../include/admin/adminPage.jsp"%>
</div>
<div class="panel panel-warning addDiv">
    <div class="panel-heading">新增属性</div>
    <div class="panel-body">
        <form method="post" action="admin_property_add" id="addForm">
            <table class="addTable">
                <tr>
                    <td>属性名称</td>
                    <td><input type="text" name="name" id="name" class="form-control"></td>
                </tr>
                <tr class="submitTR">
                    <td colspan="2" align="center">
                        <input type="hidden" name="cid" value="${c.id}">
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
