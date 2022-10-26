<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/4/1
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="productReviewDiv">
    <div class="productReviewTopPart">
        <a href="#nowhere" class="productReviewTopPartSelectedLink">商品详情</a>
        <a href="#nowhere" class="selected">累计评价 <span class="productReviewTopReviewLinkNumber">${p.reviewCount}</span>
        </a>
    </div>

    <div class="productReviewContentPart">
        <c:forEach items="${reviews}" var="r">
            <div class="productReviewItem">

                <div class="productReviewItemDesc">
                    <div class="productReviewItemContent">
                            ${r.content }
                    </div>
                    <div class="productReviewItemDate"><fmt:formatDate value="${r.createDate}"
                                                                       pattern="yyyy-MM-dd"/></div>
                </div>
                <div class="productReviewItemUserInfo">

                        ${r.user.anonymousName}<span class="userInfoGrayPart">（匿名）</span>
                </div>

                <div style="clear:both"></div>

            </div>
        </c:forEach>
    </div>

</div>