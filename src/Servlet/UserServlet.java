package Servlet;

import DAO.UserDAO;
import Tmall_Bean.User;
import Util.Page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends BaseBackServlet {

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
        List<User> list = userDAO.list(page.getStart(), page.getCount());
        int total = userDAO.getTotal();
        page.setTotal(total);
        req.setAttribute("list",list);
        req.setAttribute("page",page);
        return "admin/listUser.jsp";
    }
}
