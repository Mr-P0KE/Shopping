package Servlet;

import DAO.*;
import Util.Page;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@WebServlet(name = "BaseBackServlet", value = "/BaseBackServlet")
public abstract class BaseBackServlet extends HttpServlet {
    public abstract String add(HttpServletRequest req, HttpServletResponse resp, Page page);
    public abstract String delete(HttpServletRequest req, HttpServletResponse resp, Page page);
    public abstract String update(HttpServletRequest req, HttpServletResponse resp, Page page);
    public abstract String edit(HttpServletRequest req, HttpServletResponse resp, Page page);
    public abstract String list(HttpServletRequest req, HttpServletResponse resp, Page page);
    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int start = 0;
            int count = 5;
            try {
                start = Integer.parseInt(req.getParameter("page.start"));
            }catch (Exception e){
            }
            try {
                count = Integer.parseInt(req.getParameter("page.count"));
            }catch (Exception e){
            }
            Page page = new Page(start,count);
            String method = (String) req.getAttribute("method");
            System.out.println("我们");
            Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
                    javax.servlet.http.HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this,req,resp,page).toString();
            //System.out.println(redirect);
            if(redirect.startsWith("@")){
                resp.sendRedirect(redirect.substring(1));
            }else if(redirect.startsWith("%")){
                resp.getWriter().print(redirect.substring(1));
            }else
                req.getRequestDispatcher(redirect).forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public InputStream parseUpload(HttpServletRequest req, Map<String,String> params){
        InputStream is = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            factory.setSizeThreshold(1024*10240);//10M
            List items = upload.parseRequest(req);
            Iterator iter = items.iterator();
            while (iter.hasNext()){
                FileItem item = (FileItem) iter.next();
                if(!item.isFormField()){
                    System.out.println("上传文件：进入获取输入流。为文件表单");
                    is = item.getInputStream();
                }else {
                    System.out.println("上传文件：未进入获取输入流。为普通表单");
                    String name = item.getFieldName();
                    String value = item.getString();
                    System.out.println(name +":"+value);
                    value = new String(value.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
                    params.put(name,value);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }
}
