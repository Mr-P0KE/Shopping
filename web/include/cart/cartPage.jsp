<%--
  Created by IntelliJ IDEA.
  User: 56207
  Date: 2022/4/1
  Time: 21:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<script>
    var deleteOrderItem = false;
    var deleteOrderItemid = 0;
    $(function () {

        $("a.deleteOrderItem").click(function () {
            deleteOrderItem = false;
            var orderItem = $(this).attr("orderItem");
            deleteOrderItemid = orderItem;
            $("#deleteConfirmModal").modal('show');
        });
        $("button.deleteConfirmButton").click(function () {
            deleteOrderItem = true;
            $("#deleteConfirmModal").modal('hide');
        });

        $('#deleteConfirmModal').on('hidden.bs.modal', function (e) {
            if (deleteOrderItem) {
                var page = "foredeleteOrderItem";
                $.post(
                    page,
                    {"orderItem": deleteOrderItemid},
                    function (result) {
                        if ("success" == result) {
                            $("tr.cartProductItemTR[orderItem=" + deleteOrderItemid + "]").hide();
                        }
                        else {
                            location.href = "login.jsp";
                        }
                    }
                );

            }
        });

        $("img.cartProductItemIfSelected").click(function () {
            var selectit = $(this).attr("selectit");
            if ("selectit" == selectit) {
                $(this).attr("src", "img/site/cartNotSelected.png");
                $(this).attr("selectit", "false");
                $(this).parents("tr.cartProductItemTR").css("background-color", "#fff");
            }
            else {
                $(this).attr("src", "img/site/cartSelected.png");
                $(this).attr("selectit", "selectit");
                $(this).parents("tr.cartProductItemTR").css("background-color", "#FFF8E1");
            }
            syncSelect();
            syncCreateOrderButton();
            calcCartSumPriceAndNumber();
        });
        $("img.selectAllItem").click(function () {
            var selectit = $(this).attr("selectit");
            if ("selectit" == selectit) {
                $("img.selectAllItem").attr("src", "img/site/cartNotSelected.png");
                $("img.selectAllItem").attr("selectit", "false");
                $(".cartProductItemIfSelected").each(function () {
                    $(this).attr("src", "img/site/cartNotSelected.png");
                    $(this).attr("selectit", "false");
                    $(this).parents("tr.cartProductItemTR").css("background-color", "#fff");
                });
            }
            else {
                $("img.selectAllItem").attr("src", "img/site/cartSelected.png");
                $("img.selectAllItem").attr("selectit", "selectit");
                $(".cartProductItemIfSelected").each(function () {
                    $(this).attr("src", "img/site/cartSelected.png");
                    $(this).attr("selectit", "selectit");
                    $(this).parents("tr.cartProductItemTR").css("background-color", "#FFF8E1");
                });
            }
            syncCreateOrderButton();
            calcCartSumPriceAndNumber();


        });

        $(".orderItemNumberSetting").keyup(function () {
            var pid = $(this).attr("pid");
            var stock = $("span.orderItemStock[pid=" + pid + "]").text();
            var price = $("span.orderItemPromotePrice[pid=" + pid + "]").text();

            var num = $(".orderItemNumberSetting[pid=" + pid + "]").val();
            num = parseInt(num);
            if (isNaN(num))
                num = 1;
            if (num <= 0)
                num = 1;
            if (num > stock)
                num = stock;

            syncPrice(pid, num, price);
        });

        $(".numberPlus").click(function () {

            var pid = $(this).attr("pid");
            console.log("pid=" + pid);
            console.log("no no cache");
            var stock = $("span.orderItemStock[pid=" + pid + "]").text();
            console.log("stock=" + stock);
            var price = $("span.orderItemPromotePrice[pid=" + pid + "]").text();
            console.log("price=" + price);
            var num = $(".orderItemNumberSetting[pid=" + pid + "]").val();
            num++;
            if (num > stock)
                num = stock;
            syncPrice(pid, num, price);
        });
        $(".numberMinus").click(function () {
            var pid = $(this).attr("pid");
            var stock = $("span.orderItemStock[pid=" + pid + "]").text();
            var price = $("span.orderItemPromotePrice[pid=" + pid + "]").text();
            var num = $(".orderItemNumberSetting[pid=" + pid + "]").val();
            --num;
            if (num <= 0)
                num = 1;
            syncPrice(pid, num, price);
        });

        $("button.createOrderButton").click(function () {
            var params = "";
            $(".cartProductItemIfSelected").each(function () {
                if ("selectit" == $(this).attr("selectit")) {
                    var orderItem = $(this).attr("orderItem");
                    params += "&orderItem=" + orderItem;
                }
            });
            params = params.substring(1);
            location.href = "forebuy?" + params;
        });


    });

    function syncCreateOrderButton() {
        var selectAny = false;
        $(".cartProductItemIfSelected").each(function () {
            if ("selectit" == $(this).attr("selectit")) {
                selectAny = true;
            }
        });

        if (selectAny) {
            $("button.createOrderButton").css("background-color", "#C40000");
            $("button.createOrderButton").removeAttr("disabled");
        }
        else {
            $("button.createOrderButton").css("background-color", "#AAAAAA");
            $("button.createOrderButton").attr("disabled", "disabled");
        }

    }

    function syncSelect() {
        var selectAll = true;
        $(".cartProductItemIfSelected").each(function () {
            if ("false" == $(this).attr("selectit")) {
                selectAll = false;
            }
        });

        if (selectAll)
            $("img.selectAllItem").attr("src", "img/site/cartSelected.png");
        else
            $("img.selectAllItem").attr("src", "img/site/cartNotSelected.png");


    }

    function calcCartSumPriceAndNumber() {
        var sum = 0;
        var totalNumber = 0;
        $("img.cartProductItemIfSelected[selectit='selectit']").each(function () {
            var orderItem = $(this).attr("orderItem");
            var price = $(".cartProductItemSmallSumPrice[orderItem=" + orderItem + "]").text();
            price = price.replace(/,/g, "");
            price = price.replace(/???/g, "");
            sum += Number(price);

            var num = $(".orderItemNumberSetting[orderItem=" + orderItem + "]").val();
            totalNumber += Number(num);

        });

        $("span.cartSumPrice").html("???" + formatMoney(sum));
        $("span.cartTitlePrice").html("???" + formatMoney(sum));
        $("span.cartSumNumber").html(totalNumber);
    }

    function syncPrice(pid, num, price) {
        $(".orderItemNumberSetting[pid=" + pid + "]").val(num);
        var cartProductItemSmallSumPrice = formatMoney(num * price);
        $(".cartProductItemSmallSumPrice[pid=" + pid + "]").html("???" + cartProductItemSmallSumPrice);
        calcCartSumPriceAndNumber();

        var page = "forechangeOrderItem";
        $.post(
            page,
            {"pid": pid, "number": num},
            function (result) {
                if ("success" != result) {
                    location.href = "login.jsp";
                }
            }
        );

    }
</script>

<div class="cartDiv">
    <div class="cartTitle pull-right">
        <span>????????????  (????????????)</span>
        <span class="cartTitlePrice">???0.00</span>
        <button class="createOrderButton" disabled="disabled">??? ???</button>
    </div>


    <div class="cartProductList">
        <table class="cartProductTable">
            <thead>
            <tr>
                <th class="selectAndImage">
                    <img selectit="false" class="selectAllItem" src="img/site/cartNotSelected.png">
                    ??????

                </th>
                <th>????????????</th>
                <th>??????</th>
                <th>??????</th>
                <th width="120px">??????</th>
                <th class="operation">??????</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${orderItems }" var="oi">
                <tr orderItem="${oi.id}" class="cartProductItemTR">
                    <td>
                        <img selectit="false" orderItem="${oi.id}" class="cartProductItemIfSelected"
                             src="img/site/cartNotSelected.png">
                        <a style="display:none" href="#nowhere"><img src="img/site/cartSelected.png"></a>
                        <img class="cartProductImg"
                             src="img/productSingle_middle/${oi.product.firstProductImage.id}.jpg">
                    </td>
                    <td>
                        <div class="cartProductLinkOutDiv">
                            <a href="foreproduct?pid=${oi.product.id}" class="cartProductLink">${oi.product.name}</a>
                            <div class="cartProductLinkInnerDiv">
                                <img src="img/site/creditcard.png" title="?????????????????????">
                                <img src="img/site/7day.png" title="?????????????????????,??????7?????????">
                                <img src="img/site/promise.png" title="?????????????????????,??????????????????">
                            </div>
                        </div>

                    </td>
                    <td>
                        <span class="cartProductItemOringalPrice">???${oi.product.originalPrice}</span>
                        <span class="cartProductItemPromotionPrice">???${oi.product.promotePrice}</span>

                    </td>
                    <td>

                        <div class="cartProductChangeNumberDiv">
                            <span class="hidden orderItemStock " pid="${oi.product.id}">${oi.product.stock}</span>
                            <span class="hidden orderItemPromotePrice "
                                  pid="${oi.product.id}">${oi.product.promotePrice}</span>
                            <a pid="${oi.product.id}" class="numberMinus" href="#nowhere">-</a>
                            <input pid="${oi.product.id}" orderItem="${oi.id}" class="orderItemNumberSetting"
                                   autocomplete="off" value="${oi.number}">
                            <a stock="${oi.product.stock}" pid="${oi.product.id}" class="numberPlus"
                               href="#nowhere">+</a>
                        </div>

                    </td>
                    <td>
							<span class="cartProductItemSmallSumPrice" orderItem="${oi.id}" pid="${oi.product.id}">
							???<fmt:formatNumber type="number" value="${oi.product.promotePrice*oi.number}"
                                               minFractionDigits="2"/>
							</span>

                    </td>
                    <td>
                        <a class="deleteOrderItem" orderItem="${oi.id}" href="#nowhere">??????</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
    </div>

    <div class="cartFoot">
        <img selectit="false" class="selectAllItem" src="img/site/cartNotSelected.png">
        <span>??????</span>
        <!-- 		<a href="#">??????</a> -->

        <div class="pull-right">
            <span>???????????? <span class="cartSumNumber">0</span> ???</span>

            <span>?????? (????????????): </span>
            <span class="cartSumPrice">???0.00</span>
            <button class="createOrderButton" disabled="disabled">??? ???</button>
        </div>

    </div>

</div>