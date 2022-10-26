package Servlet;

import DAO.CategoryDAO;
import Tmall_Bean.Category;
import Util.Image;
import Util.Page;
import javax.imageio.ImageIO;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CategoryServlet", value = "/CategoryServlet")
public class CategoryServlet extends BaseBackServlet {

    @Override
    public String add(HttpServletRequest req, HttpServletResponse resp, Page page) {
        Map<String,String> params = new HashMap<>();
        InputStream is = super.parseUpload(req,params);
        String name = params.get("name");
        Category c = new Category();
        c.setName(name);
        categoryDAO.add(c);
        File image = new File(req.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(image,c.getId()+".jpg");
        try {
            if(null != is && 0!= is.available()){
                try (FileOutputStream fos = new FileOutputStream(file)){
                  byte[] bytes = new byte[1024*1024];
                  int length = 0;
                  while(-1 != (length = is.read(bytes))){
                      fos.write(bytes,0,length);
                  }
                  fos.flush();
                  BufferedImage img = Image.change2jpg(file);
                    ImageIO.write(img,"jpg",file);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse resp, Page page)
    {
        int id = Integer.parseInt(req.getParameter("id"));
        categoryDAO.delete(id);
        return "@admin_category_list";
    }
    @Override
    public String update(HttpServletRequest req, HttpServletResponse resp, Page page)
    {
        Map<String,String> parms = new HashMap<>();
        InputStream is = parseUpload(req,parms);
        String name = parms.get("name");
        int id = Integer.parseInt(parms.get("id"));

        Category c = new Category();
        c.setId(id);
        c.setName(name);
        categoryDAO.update(c);

        File file = new File(req.getSession().getServletContext().getRealPath("img/category"));
        File file1 = new File(file,c.getId()+".jpg");
        file1.getParentFile().mkdirs();
        try {
            if(null != is && 0 != is.available()){
                try (FileOutputStream fos = new FileOutputStream(file1)){
                    byte[] b = new byte[1024*1024];
                    int length = 0;
                    while(-1 != (length = is.read(b))){
                        fos.write(b,0,length);
                    }
                    fos.flush();
                    BufferedImage image = Image.change2jpg(file1);
                    ImageIO.write(image,"jpg",file1);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String edit(HttpServletRequest req, HttpServletResponse resp, Page page) {

        int id = Integer.parseInt(req.getParameter("id"));
        Category c = new CategoryDAO().get(id);
        req.setAttribute("c",c);
        return "admin/editCategory.jsp";
    }

    @Override
    public String list(HttpServletRequest req, HttpServletResponse resp, Page page){
        List<Category> list = categoryDAO.list(page.getStart(),page.getCount());
        int total = categoryDAO.getTotal();
        page.setTotal(total);

        req.setAttribute("list",list);
        req.setAttribute("page",page);
        return "admin/listCategory.jsp";
    }
}
