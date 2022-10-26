package Servlet;

import Tmall_Bean.Category;
import Tmall_Bean.Product;
import Tmall_Bean.Property;
import Tmall_Bean.PropertyValue;
import Util.Page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", value = "/ProductServlet")
public class ProductServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest req, HttpServletResponse resp, Page page) {
       int cid = Integer.parseInt(req.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        Product p = new Product();
        String name = req.getParameter("name");
        String subTitle = req.getParameter("subTitle");
        float originalPrice = Float.parseFloat(req.getParameter("originalPrice"));
        float promotePrice = Float.parseFloat(req.getParameter("promotePrice"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        p.setOriginalPrice(originalPrice);
        p.setName(name);
        p.setStock(stock);
        p.setCategory(c);
        p.setPromotePrice(promotePrice);
        p.setSubTitle(subTitle);

        productDAO.add(p);
        return "@admin_product_list?cid="+cid;
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse resp, Page page) {
        int id = Integer.parseInt(req.getParameter("id"));
        Product p = productDAO.get(id);
        productDAO.delete(id);
        return "@admin_product_list?cid="+p.getCategory().getId();
    }

    @Override
    public String update(HttpServletRequest req, HttpServletResponse resp, Page page) {
      int id = Integer.parseInt(req.getParameter("id"));
      int cid = Integer.parseInt(req.getParameter("cid"));
      int stock = Integer.parseInt(req.getParameter("stock"));
      float originalPrice = Float.parseFloat(req.getParameter("originalPrice"));
      float promotePrice = Float.parseFloat(req.getParameter("promotePrice"));
      String subtitle = req.getParameter("subTitle");
      String name = req.getParameter("name");
      Category c = categoryDAO.get(cid);
      Product p = new Product();
      p.setId(id);
      p.setCategory(c);
      p.setSubTitle(subtitle);
      p.setStock(stock);
      p.setName(name);
      p.setPromotePrice(promotePrice);
      p.setOriginalPrice(originalPrice);
      productDAO.update(p);
      return "@admin_product_list?cid="+cid;
    }

    @Override
    public String edit(HttpServletRequest req, HttpServletResponse resp, Page page) {
       int id = Integer.parseInt(req.getParameter("id"));
       Product p = productDAO.get(id);
       req.setAttribute("p",p);
       return "admin/editProduct.jsp";
    }

    @Override
    public String list(HttpServletRequest req, HttpServletResponse resp, Page page) {
       int cid = Integer.parseInt(req.getParameter("cid"));
       Category c = categoryDAO.get(cid);
       List list = productDAO.list(cid,page.getStart(), page.getCount());
       int total = productDAO.getTotal(cid);
       page.setTotal(total);
       page.setParam("#cid="+cid);
       req.setAttribute("list",list);
       req.setAttribute("c",c);
       req.setAttribute("page",page);

       return "admin/listProduct.jsp";
    }
    public String editPropertyValue(HttpServletRequest req, HttpServletResponse resp, Page page) {
     int id = Integer.parseInt(req.getParameter("id"));
     Product p = productDAO.get(id);
     req.setAttribute("p",p);

     propertyValueDAO.init(p);
     List<PropertyValue> pv = propertyValueDAO.list(p.getId());
     req.setAttribute("pv",pv);
     return "admin/editProductValue.jsp";
    }
    public String updatePropertyValue(HttpServletRequest req, HttpServletResponse resp, Page page){
        int pvid = Integer.parseInt(req.getParameter("pvid"));
        String value = req.getParameter("value");
        PropertyValue pv = propertyValueDAO.get(pvid);

        pv.setValue(value);
        propertyValueDAO.update(pv);
        System.out.println("使用update");
        return "%success";
    }
}
