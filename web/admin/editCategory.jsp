<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/3/19
  Time: 18:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

    <title>编辑分类</title>
<script>
    $(function (){
      $("#editFrom").submit(function (){
          if(!checkEmpty("name","分类名称"))
              return false;
          return true;
      });
    });
</script>
<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a> </li>
        <li class="active">编辑分类 </li>
    </ol>
    <div class="panel panel-warning editDiv">
        <div class="panel-heading">编辑分类</div>
        <div class="panel-body">
            <form action="admin_category_update" method="post" id="editFrom" enctype="multipart/form-data">
                <table class="editTable">
                    <tr>
                        <td>分类名称</td>
                        <td><input type="text" id="name" value="${c.name}" name="name" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>分类图片</td>
                        <td><input type="file" id="categoryPic" accept="image/*" name="filepath"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input type="hidden" name="id" value="${c.id}">
                            <button type="submit" class="btn btn-success">提 交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
