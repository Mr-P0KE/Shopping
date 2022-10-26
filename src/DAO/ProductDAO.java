package DAO;

import Tmall_Bean.Category;
import Tmall_Bean.Product;
import Tmall_Bean.ProductImage;
import Util.DBUtil;
import Util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDAO {
    public int getTotal(int cid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "select count(*) from Product where cid = " + cid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public void add(Product p) {
        String sql = "insert into Product values(null,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getSubTitle());
            ps.setFloat(3, p.getOriginalPrice());
            ps.setFloat(4, p.getPromotePrice());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(p.getCreateDate()));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                p.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(Product bean) {
        String sql = "update Product set name= ?, subTitle=?, originalPrice=?,promotePrice=?,stock=?, cid = ?, createDate=? where id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getSubTitle());
            ps.setFloat(3, bean.getOriginalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(8, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "delete from Product where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Product get(int id) {
        Product p = new Product();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from Product where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                p.setName(name);
                p.setSubTitle(subTitle);
                p.setOriginalPrice(originalPrice);
                p.setPromotePrice(promotePrice);
                p.setStock(stock);
                Category category = new CategoryDAO().get(cid);
                p.setCategory(category);
                p.setCreateDate(createDate);
                p.setId(id);
                //在查找时显示第一张展示图
                setFirstProductImage(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }
    public List<Product> list(int cid){
        return list(cid,0,Short.MAX_VALUE);
    }
    public List<Product> list(int cid,int start,int end){
        List<Product> list = new ArrayList<>();
        Category category = new CategoryDAO().get(cid);
        String sql = "select * from Product where cid = ? order by id asc limit ?,?";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,cid);
            ps.setInt(2,start);
            ps.setInt(3,end);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Product p = new Product();
                int id = rs.getInt("id");
                String subTitle = rs.getString("subTitle");
                String name = rs.getString("name");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                p.setId(id);
                p.setSubTitle(subTitle);
                p.setName(name);
                p.setOriginalPrice(originalPrice);
                p.setPromotePrice(promotePrice);
                p.setStock(stock);
                p.setCreateDate(createDate);
                p.setCategory(category);
                setFirstProductImage(p);
                list.add(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<Product> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Product> list(int start, int count) {
        List<Product> list = new ArrayList<Product>();
        String sql = "select * from Product limit ?,? ";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                int id = rs.getInt(1);
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                p.setName(name);
                p.setSubTitle(subTitle);
                p.setOriginalPrice(originalPrice);
                p.setPromotePrice(promotePrice);
                p.setStock(stock);
                p.setCreateDate(createDate);
                p.setId(id);
                Category category = new CategoryDAO().get(cid);
                p.setCategory(category);
                list.add(p);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }
    public void fill(List<Category> categories) {
        for (Category c : categories)
            fill(c);
    }
    //将同一分类的product，一次性填充到category对象c中
    public void fill(Category c){
       List<Product> list = this.list(c.getId());
       c.setProducts(list);
    }
    public void fillByRow(List<Category> categories){
        int Row = 8;
        for (Category c : categories){
            List<Product> list = c.getProducts();
          List<List<Product>> productsByRow = new ArrayList<>();
          for(int i = 0;i < list.size();i += Row){
              int size = i + Row;
              size = Math.min(size, list.size());
              List<Product> list1 = list.subList(i,size);
              productsByRow.add(list1);
          }
          c.setProductByRow(productsByRow);
        }
    }
    public void setFirstProductImage(Product p){
        List<ProductImage> list = new ProductImageDAO().list(p,ProductImageDAO.type_single);
        if(!list.isEmpty()){
            p.setFirstProductImage(list.get(0));
        }
    }
    public void setSaleAndReviewNumber(Product p) {
        int saleCount = new OrderItemDAO().getSaleCount(p.getId());
        p.setSaleCount(saleCount);

        int reviewCount = new ReviewDAO().getCount(p.getId());
        p.setReviewCount(reviewCount);

    }
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product p : products) {
            setSaleAndReviewNumber(p);
        }
    }
    public List<Product> search(String keyword, int start, int count) {
        List<Product> list = new ArrayList<>();

        if (null == keyword || 0 == keyword.trim().length())
            return list;
        String sql = "select * from Product where name like ? limit ?,? ";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.trim() + "%");
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                int id = rs.getInt(1);
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                p.setName(name);
                p.setSubTitle(subTitle);
                p.setOriginalPrice(originalPrice);
                p.setPromotePrice(promotePrice);
                p.setStock(stock);
                p.setCreateDate(createDate);
                p.setId(id);

                Category category = new CategoryDAO().get(cid);
                p.setCategory(category);
                setFirstProductImage(p);
                list.add(p);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }
}
