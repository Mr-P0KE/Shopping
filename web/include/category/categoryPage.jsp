<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/4/1
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div id="category">
    <div class="categoryPageDiv">
        <img src="img/category/${category.id}.jpg">
        <%@include file="sortBar.jsp" %>
        <%@include file="productsByCategory.jsp" %>
    </div>

</div>