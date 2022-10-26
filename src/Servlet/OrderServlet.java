package Servlet;

import DAO.OrderDAO;
import Tmall_Bean.Order;
import Util.Page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "OrderServlet", value = "/OrderServlet")
public class OrderServlet extends BaseBackServlet {

    @Override
    public String add(HttpServletRequest req, HttpServletResponse resp, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse resp, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest req, HttpServletResponse resp, Page page) {
        return null;
    }

    @Override
    public String edit(HttpServletRequest req, HttpServletResponse resp, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest req, HttpServletResponse resp, Page page) {
        List<Order> list = orderDAO.list(page.getStart(),page.getCount());
        orderItemDAO.fill(list);
        int total = orderDAO.getTotal();
        page.setTotal(total);
        req.setAttribute("page",page);
        req.setAttribute("list",list);
        return "admin/listOrder.jsp";
    }
    public String delivery(HttpServletRequest req,HttpServletResponse resp,Page page){
        int id = Integer.parseInt(req.getParameter("id"));

        Order order = orderDAO.get(id);
        order.setDeliveryDate(new Date());
        order.setStatus(OrderDAO.waitConfirm);
        orderDAO.update(order);
        return "@admin_order_list";
    }
}
