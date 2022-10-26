package Servlet;

import DAO.*;
import Tmall_Bean.*;
import Util.Page;
import org.apache.commons.lang.math.RandomUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "ForeServlet", value = "/ForeServlet")
public class ForeServlet extends BaseForeServlet {
    //主页
    public String home(HttpServletRequest req, HttpServletResponse resp, Page page) {
        List<Category> list = categoryDAO.list();
        productDAO.fill(list);
        productDAO.fillByRow(list);
        req.setAttribute("list", list);
        return "home.jsp";
    }

    //注册
    public String register(HttpServletRequest req, HttpServletResponse resp, Page page) {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        boolean b = userDAO.isExist(name);
        if (b) {
            req.setAttribute("msg", "用户名已被使用");
            return "register.jsp";
        }
        User u = new User();
        u.setName(name);
        u.setPassword(password);
        userDAO.add(u);
        System.out.println(name + ": 注册成功");
        return "@registerSuccess.jsp";
    }

    //登录
    public String login(HttpServletRequest req, HttpServletResponse resp, Page page) {
        String name = req.getParameter("name");
        String password = req.getParameter("password");

        User user = userDAO.get(name, password);
        if (null == user) {
            req.setAttribute("msg", "账号密码错误");
            return "loginPage.jsp";
        }
        req.getSession().setAttribute("user", user);
        return "@forehome";
    }

    //退出
    public String logout(HttpServletRequest req, HttpServletResponse resp, Page page) {
        req.getSession().removeAttribute("user");

        return "@forehome";
    }

    //产品页面
    public String product(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int pid = Integer.parseInt(req.getParameter("pid"));
        Product p = productDAO.get(pid);

        List<ProductImage> productSingleImages = productImageDAO.list(p, ProductImageDAO.type_single);
        List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);

        List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
        List<Review> reviews = reviewDAO.list(p.getId());
        productDAO.setSaleAndReviewNumber(p);

        req.setAttribute("p", p);
        req.setAttribute("reviews", reviews);
        req.setAttribute("pvs", pvs);

        return "product.jsp";
    }

    //判断是否登录
    public String checkLogin(HttpServletRequest req, HttpServletResponse resp, Page page) {
        User user = (User) req.getSession().getAttribute("user");
        if (null != user) {
            return "%success";
        }
        return "%fail";
    }

    //AJAX进行登录的数据处理
    public String loginAjax(HttpServletRequest req, HttpServletResponse resp, Page page) {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        User user = userDAO.get(name, password);
        if (null == user) {
            return "%fail";
        }
        req.getSession().setAttribute("user", user);
        return "%success";
    }

    //分类页面数据处理
    public String category(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int cid = Integer.parseInt(req.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        productDAO.fill(category);
        productDAO.setSaleAndReviewNumber(category.getProducts());

        String sort = req.getParameter("sort");
        if (null != sort) {
            switch (sort) {
                case "review":
                    Collections.sort(category.getProducts(), new Comparator<Product>() {
                        @Override
                        public int compare(Product p1, Product p2) {
                            return p2.getReviewCount() - p1.getReviewCount();
                        }
                    });
                    break;
                case "date":
                    Collections.sort(category.getProducts(), new Comparator<Product>() {
                        @Override
                        public int compare(Product p1, Product p2) {
                            return p1.getCreateDate().compareTo(p2.getCreateDate());
                        }
                    });
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(), new Comparator<Product>() {
                        @Override
                        public int compare(Product p1, Product p2) {
                            return p1.getSaleCount() - p2.getSaleCount();
                        }
                    });
                    break;
                case "price":
                    Collections.sort(category.getProducts(), new Comparator<Product>() {
                        @Override
                        public int compare(Product p1, Product p2) {
                            return (int) (p1.getPromotePrice() - p2.getPromotePrice());
                        }
                    });
                    break;
                // 按照评论数量和已售数量综合（乘积）降序排列
                case "all":
                    Collections.sort(category.getProducts(), new Comparator<Product>() {
                        @Override
                        public int compare(Product p1, Product p2) {
                            return p2.getReviewCount() * p2.getSaleCount() - p1.getReviewCount() * p1.getSaleCount();
                        }
                    });
                    break;
            }
        }
        req.setAttribute("category", category);
        return "category.jsp";
    }

    //搜索页
    public String search(HttpServletRequest req, HttpServletResponse resp, Page page) {
        String keyword = req.getParameter("keyword");
        List<Product> ps = productDAO.search(keyword, 0, 20);
        req.setAttribute("ps", ps);
        return "searchResult.jsp";
    }

    //在商品界面--点击数量--点击购买按钮
    public String buyone(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int pid = Integer.parseInt(req.getParameter("pid"));
        int num = Integer.parseInt(req.getParameter("num"));
        Product p = productDAO.get(pid);
        int orderItem = 0;

        User user = (User) req.getSession().getAttribute("user");
        // 遍历此用户对应的所有订单项，如果其产品id等于p.getId()，就对对应订单项的数量追加，不等于，则新建订单项
        boolean found = false;
        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        for (OrderItem o : orderItems) {
            if (o.getProduct().getId() == p.getId()) {
                o.setNumber(o.getNumber() + num);
                orderItemDAO.update(o);
                found = true;
                orderItem = o.getId();
                break;
            }
        }
        if (!found) {
            OrderItem o = new OrderItem();
            o.setUser(user);
            o.setNumber(num);
            o.setProduct(p);
            orderItemDAO.add(o);
            orderItem = o.getId();
        }

        return "@forebuy?orderItem=" + orderItem;
    }

    //点击购买
    public String buy(HttpServletRequest req, HttpServletResponse resp, Page page) {
        String[] oiids = req.getParameterValues("orderItem");
        List<OrderItem> list = new ArrayList<>();
        float total = 0;
        for (String o : oiids) {
            int id = Integer.parseInt(o);
            OrderItem orderItem = orderItemDAO.get(id);
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            list.add(orderItem);
        }
        req.getSession().setAttribute("orderItems", list);
        req.setAttribute("total", total);
        return "buy.jsp";
    }

    //添加购物车
    public String addCart(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int pid = Integer.parseInt(req.getParameter("pid"));
        Product product = productDAO.get(pid);
        int num = Integer.parseInt(req.getParameter("num"));
        User user = (User) req.getSession().getAttribute("user");
        boolean found = false;
        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == product.getId()) {
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemDAO.update(orderItem);
                found = true;
                break;
            }
        }
        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setNumber(num);
            orderItem.setProduct(product);
            orderItemDAO.add(orderItem);
        }
        return "%success";
    }

    //购物车
    public String cart(HttpServletRequest req, HttpServletResponse resp, Page page) {
        User u = (User) req.getSession().getAttribute("user");
        List<OrderItem> orderItems = orderItemDAO.listByUser(u.getId());
        req.setAttribute("orderItems", orderItems);
        return "cart.jsp";
    }

    //改变订单项-订单数量
    public String changeOrderItem(HttpServletRequest req, HttpServletResponse resp, Page page) {
        User u = (User) req.getSession().getAttribute("user");
        if (null == u) {
            return "%fail";
        }
        int pid = Integer.parseInt(req.getParameter("pid"));
        int number = Integer.parseInt(req.getParameter("number"));
        List<OrderItem> o = orderItemDAO.listByUser(u.getId());
        for (OrderItem oi : o) {
            if (oi.getProduct().getId() == pid) {
                oi.setNumber(number);
                orderItemDAO.update(oi);
                break;
            }
        }
        return "%success";
    }

    //AJAX删除订单
    public String deleteOrderItem(HttpServletRequest req, HttpServletResponse resp, Page page) {
        User u = (User) req.getSession().getAttribute("user");
        if (null == u) {
            return "%false";
        }
        int orderItemId = Integer.parseInt(req.getParameter("orderItem"));
        orderItemDAO.delete(orderItemId);
        return "%success";
    }

    //下单操作数据处理
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> orderItems = (List<OrderItem>) request.getSession().getAttribute("orderItems");
        if (orderItems.isEmpty()) {
            return "@login.jsp";
        }
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");

        Order order = new Order();
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);

        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderDAO.waitPay);

        orderDAO.add(order);
        float total = 0;
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
            orderItemDAO.update(orderItem);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }

        return "@forealipay?oid=" + order.getId() + "&total=" + total;
    }
    //支付宝付款界面
    public String alipay(HttpServletRequest req,HttpServletResponse resp,Page page){
        return "alipay.jsp";
    }
    //付款成功界面
    public String payed(HttpServletRequest req,HttpServletResponse resp,Page page){
        int oid = Integer.parseInt(req.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.waitDelivery);
        o.setPayDate(new Date());
        orderDAO.update(o);
        req.setAttribute("o",o);
        return "payed.jsp";
    }
    //已购买页面
    public String bought(HttpServletRequest req,HttpServletResponse resp,Page page){
        User u = (User) req.getSession().getAttribute("user");
        List<Order> list = orderDAO.list(u.getId(),orderDAO.delete);
        orderItemDAO.fill(list);
        req.setAttribute("os",list);
        return "bought.jsp";
    }
    //确认付款页面处理
    public String confirmPay(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int oid = Integer.parseInt(req.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        req.setAttribute("o", o);
        req.setAttribute("o", o);
        return "confirmPay.jsp";
    }
    //确认收货的页面数据处理
    public String orderConfirmed(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int oid = Integer.parseInt(req.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.waitReview);
        o.setConfirmDate(new Date());
        orderDAO.update(o);

        return "orderConfirmed.jsp";
    }
    //删除订单操作数据处理
    public String deleteOrder(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int oid = Integer.parseInt(req.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.delete);
        orderDAO.update(o);

        return "%success";
    }
    //点击评论数据处理
    public String review(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int oid = Integer.parseInt(req.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewDAO.list(p.getId());
        productDAO.setSaleAndReviewNumber(p);
        req.setAttribute("p", p);
        req.setAttribute("o", o);
        req.setAttribute("reviews", reviews);

        return "review.jsp";
    }
    //评论页面数据处理
    public String doreview(HttpServletRequest req,HttpServletResponse resp,Page page){
        int oid = Integer.parseInt(req.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.finish);
        orderDAO.update(o);
        int pid = Integer.parseInt(req.getParameter("pid"));
        Product p = productDAO.get(pid);
        String content = req.getParameter("content");
        User u = (User) req.getSession().getAttribute("user");
        Review r = new Review();
        r.setContent(content);
        r.setUser(u);
        r.setProduct(p);
        r.setCreateDate(new Date());
        reviewDAO.add(r);

        return "@forereview?oid=" + oid + "&showonly=true";
    }
}
