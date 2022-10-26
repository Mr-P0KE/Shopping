<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/3/27
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../include/admin/adminHeader.jsp" %>
<%@include file="../include/admin/adminNavigator.jsp" %>

<html>
<head>
    <title>用户管理</title>
</head>
<body>
<div class="workingArea">
    <div class="label label-info">用户管理</div>
    <br/>
    <br/>
    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr class="success">
                <td>ID</td>
                <td>用户名</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="l">
                <tr>
                    <td>${l.id}</td>
                    <td>${l.name}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp"%>
    </div>
</div>
<%@ include file="../include/admin/adminFooter.jsp"%>
</body>
</html>
