<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/4/5
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<script>
    var deleteOrder = false;
    var deleteOrderid = 0;

    $(function () {
        $("a[orderStatus]").click(function () {
            var orderStatus = $(this).attr("orderStatus");
            if ('all' == orderStatus) {
                $("table[orderStatus]").show();
            }
            else {
                $("table[orderStatus]").hide();
                $("table[orderStatus=" + orderStatus + "]").show();
            }

            $("div.orderType div").removeClass("selectedOrderType");
            $(this).parent("div").addClass("selectedOrderType");
        });

        $("a.deleteOrderLink").click(function () {
            deleteOrderid = $(this).attr("oid");
            deleteOrder = false;
            $("#deleteConfirmModal").modal("show");
        });

        $("button.deleteConfirmButton").click(function () {
            deleteOrder = true;
            $("#deleteConfirmModal").modal('hide');
        });

        $('#deleteConfirmModal').on('hidden.bs.modal', function (e) {
            if (deleteOrder) {
                var page = "foredeleteOrder";
                $.post(
                    page,
                    {"oid": deleteOrderid},
                    function (result) {
                        if ("success" == result) {
                            $("table.orderListItemTable[oid=" + deleteOrderid + "]").hide();
                        }
                        else {
                            location.href = "login.jsp";
                        }
                    }
                );

            }
        });

        $(".ask2delivery").click(function () {
            var link = $(this).attr("link");
            $(this).hide();
            page = link;
            $.ajax({
                url: page,
                success: function (result) {
                    alert("???????????????????????????????????????????????????????????????")
                }
            });

        });
    });

</script>

<div class="boughtDiv">
    <div class="orderType">
        <div class="selectedOrderType"><a orderStatus="all" href="#nowhere">????????????</a></div>
        <div><a orderStatus="waitPay" href="#nowhere">?????????</a></div>
        <div><a orderStatus="waitDelivery" href="#nowhere">?????????</a></div>
        <div><a orderStatus="waitConfirm" href="#nowhere">?????????</a></div>
        <div><a orderStatus="waitReview" href="#nowhere" class="noRightborder">?????????</a></div>
        <div class="orderTypeLastOne"><a class="noRightborder"> </a></div>
    </div>
    <div style="clear:both"></div>
    <div class="orderListTitle">
        <table class="orderListTitleTable">
            <tr>
                <td>??????</td>
                <td width="100px">??????</td>
                <td width="100px">??????</td>
                <td width="120px">?????????</td>
                <td width="100px">????????????</td>
            </tr>
        </table>

    </div>

    <div class="orderListItem">
        <c:forEach items="${os}" var="o">
            <table class="orderListItemTable" orderStatus="${o.status}" oid="${o.id}">
                <tr class="orderListItemFirstTR">
                    <td colspan="2">
                        <b><fmt:formatDate value="${o.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></b>
                        <span>?????????: ${o.orderCode}
                    </span>
                    </td>
                    <td colspan="2"><img width="13px" src="img/site/orderItemTmall.png">????????????</td>
                    <td colspan="1">
                        <a class="wangwanglink" href="#nowhere">
                            <div class="orderItemWangWangGif"></div>
                        </a>

                    </td>
                    <td class="orderItemDeleteTD">
                        <a class="deleteOrderLink" oid="${o.id}" href="#nowhere">
                            <span class="orderListItemDelete glyphicon glyphicon-trash"></span>
                        </a>

                    </td>
                </tr>
                <c:forEach items="${o.orderItems}" var="oi" varStatus="st">
                    <SCRIPT>console.log("st" + ${st.count})</SCRIPT>
                    <tr class="orderItemProductInfoPartTR">
                        <td class="orderItemProductInfoPartTD"><img width="80" height="80"
                                                                    src="img/productSingle_middle/${oi.product.firstProductImage.id}.jpg">
                        </td>
                        <td class="orderItemProductInfoPartTD">
                            <div class="orderListItemProductLinkOutDiv">
                                <a href="foreproduct?pid=${oi.product.id}">${oi.product.name}</a>
                                <div class="orderListItemProductLinkInnerDiv">
                                    <img src="img/site/creditcard.png" title="?????????????????????">
                                    <img src="img/site/7day.png" title="?????????????????????,??????7?????????">
                                    <img src="img/site/promise.png" title="?????????????????????,??????????????????">
                                </div>
                            </div>
                        </td>
                        <td class="orderItemProductInfoPartTD" width="100px">

                            <div class="orderListItemProductOriginalPrice">???<fmt:formatNumber type="number"
                                                                                              value="${oi.product.originalPrice}"
                                                                                              minFractionDigits="2"/></div>
                            <div class="orderListItemProductPrice">???<fmt:formatNumber type="number"
                                                                                      value="${oi.product.promotePrice}"
                                                                                      minFractionDigits="2"/></div>

                        </td>
                        <c:if test="${st.count==1}">

                            <td valign="top" rowspan="${fn:length(o.orderItems)}"
                                class="orderListItemNumberTD orderItemOrderInfoPartTD" width="100px">
                                <span class="orderListItemNumber">${o.totalNumber}</span>
                            </td>
                            <td valign="top" rowspan="${fn:length(o.orderItems)}" width="120px"
                                class="orderListItemProductRealPriceTD orderItemOrderInfoPartTD">
                                <div class="orderListItemProductRealPrice">???<fmt:formatNumber minFractionDigits="2"
                                                                                              maxFractionDigits="2"
                                                                                              type="number"
                                                                                              value="${o.total}"/></div>
                                <div class="orderListItemPriceWithTransport">(???????????????0.00)</div>
                            </td>
                            <td valign="top" rowspan="${fn:length(o.orderItems)}"
                                class="orderListItemButtonTD orderItemOrderInfoPartTD" width="100px">
                                <c:if test="${o.status=='waitConfirm' }">
                                    <a href="foreconfirmPay?oid=${o.id}">
                                        <button class="orderListItemConfirm">????????????</button>
                                    </a>
                                </c:if>
                                <c:if test="${o.status=='waitPay' }">
                                    <a href="alipay.jsp?oid=${o.id}&total=${o.total}">
                                        <button class="orderListItemConfirm">??????</button>
                                    </a>
                                </c:if>

                                <c:if test="${o.status=='waitDelivery' }">
                                    <span>?????????</span>
                                    <%--                                     <button class="btn btn-info btn-sm ask2delivery" link="admin_order_delivery?id=${o.id}">???????????????</button> --%>

                                </c:if>

                                <c:if test="${o.status=='waitReview' }">
                                    <a href="forereview?oid=${o.id}">
                                        <button class="orderListItemReview">??????</button>
                                    </a>
                                </c:if>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>

            </table>
        </c:forEach>

    </div>

</div>