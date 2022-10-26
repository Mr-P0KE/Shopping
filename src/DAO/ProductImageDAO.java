package DAO;

import Tmall_Bean.Product;
import Tmall_Bean.ProductImage;
import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDAO {
    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    public int getTotal(){
        int total = 0;
        String sql = "select count(*) from ProductImage";
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()){
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                total = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return total;
    }
    public void add(ProductImage p){
        String sql = "insert into ProductImage values(null,?,?)";
        try (Connection c= DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,p.getProduct().getId());
            ps.setString(2,p.getType());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                p.setId(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void update(ProductImage productImage){

    }
    public void delete(int id){
        String sql = "delete from ProductImage where id = "+id;
        try (Connection c = DBUtil.getConnection();
        Statement  s = c.createStatement()){
            s.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public ProductImage get(int id){
       ProductImage productImage = new ProductImage();
        String sql = "select * from ProductImage where id = "+id;
        try (Connection c = DBUtil.getConnection();
        Statement s = c.createStatement()){
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
                int pid  = rs.getInt("pid");
                String type = rs.getString("type");
                Product p = new ProductDAO().get(pid);
                productImage.setId(id);
                productImage.setType(type);
                productImage.setProduct(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productImage;
    }
    public List<ProductImage> list(Product p, String type){
        return list(p,type,0,Short.MAX_VALUE);
    }
    public List<ProductImage> list(Product p, String type, int start, int end){
        List<ProductImage> list = new ArrayList<>();
        String sql = "select * from ProductImage where pid =? and type =? order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
           ps.setInt(1,p.getId());
           ps.setString(2,type);
           ps.setInt(3,start);
           ps.setInt(4,end);
           ResultSet rs = ps.executeQuery();
           while (rs.next()){
               ProductImage productImage = new ProductImage();
               productImage.setProduct(p);
               productImage.setId(rs.getInt("id"));
               productImage.setType(type);
               list.add(productImage);
           }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
