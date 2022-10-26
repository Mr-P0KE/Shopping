<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/3/24
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../include/admin/adminHeader.jsp" %>
<%@include file="../include/admin/adminNavigator.jsp" %>

<html>
<head>
    <title>编辑属性值</title>
</head>
<body>
<script>
    $(function (){
        $("input.pvValue").keyup(function (){
           var value = $(this).val();
           var page = "admin_product_updatePropertyValue";
           var pvid =  $(this).attr("pvid");
           var parentSpan = $(this).parent("span");
           parentSpan.css("border","1px solid blue");
           $.post(
               page,
               {"value": value, "pvid": pvid},
               function (result){
                   if("success" == result){
                       parentSpan.css("border", "1px solid green");
                       alert("修改成功");
                   }else {
                       parentSpan.css("border", "1px solid red");
                       alert("修改失败，请重新修改");
                   }
               }
           );
        });
    })
</script>
<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a> </li>
        <li class="active">${p.category.name}</li>
        <li><a href="admin_product_list?cid=${p.category.id}">${p.name}</a> </li>
        <li class="active">编辑产品属性</li>
    </ol>
    <div class="editDiv">
        <c:forEach items="${pv}" var="p">
            <div class="eachPV">
                <span class="pvName">${p.property.name}</span>
                <span class="pvValue"><input type="text" class="pvValue form-control" pvid="${p.id}" value="${p.value}"></span>
            </div>
        </c:forEach>

        <div style="clear:both"></div>
    </div>
</div>
</body>
</html>
