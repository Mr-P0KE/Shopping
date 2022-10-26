package Servlet;

import Tmall_Bean.Category;
import Tmall_Bean.Property;
import Util.Page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PropertyServlet", value = "/PropertyServlet")
public class PropertyServlet extends BaseBackServlet{

    @Override
    public String add(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int cid  = Integer.parseInt(req.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        String name = req.getParameter("name");
        Property p = new Property();
        p.setCategory(c);
        p.setName(name);
        propertyDAO.add(p);
        return "@admin_property_list?cid="+cid;
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse resp, Page page) {
       int id = Integer.parseInt(req.getParameter("id"));
        Property p = propertyDAO.get(id);
       propertyDAO.delete(id);
       return "@admin_property_list?cid="+p.getCategory().getId();
    }

    @Override
    public String update(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int cid = Integer.parseInt(req.getParameter("cid"));
        String name = req.getParameter("name");
        int id = Integer.parseInt(req.getParameter("id"));
        Category c = categoryDAO.get(cid);
        Property p = new Property();
        p.setName(name);
        p.setCategory(c);
        p.setId(id);
        propertyDAO.update(p);
        return "@admin_property_list?cid="+cid;
    }

    @Override
    public String edit(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int id = Integer.parseInt(req.getParameter("id"));
        Property p = propertyDAO.get(id);
        req.setAttribute("p",p);
        return "admin/editProperty.jsp";
    }

    @Override
    public String list(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int cid = Integer.parseInt(req.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        List<Property> ps = propertyDAO.list(cid, page.getStart(),page.getCount());
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid="+c.getId());

        req.setAttribute("page",page);
        req.setAttribute("c",c);
        req.setAttribute("list",ps);
        return "admin/listProperty.jsp";
    }
}
