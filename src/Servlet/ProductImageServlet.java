package Servlet;

import DAO.ProductImageDAO;
import Tmall_Bean.Product;
import Tmall_Bean.ProductImage;
import Util.DBUtil;
import Util.Image;
import Util.Page;
import com.sun.imageio.plugins.common.ImageUtil;

import javax.imageio.ImageIO;
import javax.servlet.*;
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

@WebServlet(name = "ProductImageServlet", value = "/ProductImageServlet")
public class ProductImageServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest req, HttpServletResponse resp, Page page) {
      Map<String,String> map = new HashMap<>();
      InputStream is = super.parseUpload(req,map);
      int pid = Integer.parseInt(map.get("pid"));
      String type = map.get("type");
      Product p = productDAO.get(pid);
      ProductImage pi = new ProductImage();
      pi.setType(type);
      pi.setProduct(p);
      productImageDAO.add(pi);

      String fileName = pi.getId() + ".jpg";
      String imageFolder;
      String imageFolder_small = null;
      String imageFolder_middle = null;
      if(ProductImageDAO.type_single.equals(pi.getType())){
          imageFolder = req.getSession().getServletContext().getRealPath("img/productSingle");
          imageFolder_small = req.getSession().getServletContext().getRealPath("img/productSingle_small");
          imageFolder_middle = req.getSession().getServletContext().getRealPath("img/productSingle_middle");
      }else {
          imageFolder = req.getSession().getServletContext().getRealPath("img/productDetail");
      }
      File f = new File(imageFolder,fileName);
      f.getParentFile().mkdirs();
      try {
          if(null != is && 0!= is.available()){
              try (FileOutputStream fos = new FileOutputStream(f)){
                  byte[] b = new byte[1024*1024];
                  int length = 0;
                  while (-1 != (length = is.read(b))){
                      fos.write(b,0,length);
                  }
                  fos.flush();

                  BufferedImage img = Image.change2jpg(f);
                  ImageIO.write(img,"jpg",f);
                  if(ProductImageDAO.type_single.equals(pi.getType())){
                      File f_small = new File(imageFolder_small,fileName);
                      File f_middle = new File(imageFolder_middle,fileName);
                      Image.resizeImage(f,56,56,f_small);
                      Image.resizeImage(f,217,190,f_middle);
                  }
              }catch (IOException e){
                  e.printStackTrace();
              }
          }
      }catch (IOException e){
          e.printStackTrace();
      }
      return "@admin_productImage_list?pid="+p.getId();
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductImage pi = productImageDAO.get(id);
        productImageDAO.delete(id);

        if (ProductImageDAO.type_single.equals(pi.getType())) {
            String imageFolder_single = request.getSession().getServletContext().getRealPath("img/productSingle");
            String imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");

            File f_single = new File(imageFolder_single, pi.getId() + ".jpg");
            f_single.delete();
            File f_small = new File(imageFolder_small, pi.getId() + ".jpg");
            f_small.delete();
            File f_middle = new File(imageFolder_middle, pi.getId() + ".jpg");
            f_middle.delete();

        } else {
            String imageFolder_detail = request.getSession().getServletContext().getRealPath("img/productDetail");
            File f_detail = new File(imageFolder_detail, pi.getId() + ".jpg");
            f_detail.delete();
        }
        return "@admin_productImage_list?pid=" + pi.getProduct().getId();
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
        int pid = Integer.parseInt(req.getParameter("pid"));
        Product p = productDAO.get(pid);
        List<ProductImage> list = productImageDAO.list(p,ProductImageDAO.type_single);
        List<ProductImage> list1 = productImageDAO.list(p,ProductImageDAO.type_detail);

        req.setAttribute("p",p);
        req.setAttribute("list",list);
        req.setAttribute("list1",list1);
        return "admin/listProductImage.jsp";
    }
}
