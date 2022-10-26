package DAO;

import Tmall_Bean.Product;
import Tmall_Bean.Property;
import Tmall_Bean.PropertyValue;
import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyValueDAO {
    public int getTotal(){
        int total = 0;
        String sql = "select count(*) from PropertyValue ";
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()){
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return total;
    }
    public void add(PropertyValue p){
        String sql = "insert into PropertyValue values(null,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,p.getProduct().getId());
            ps.setInt(2,p.getProperty().getId());
            ps.setString(3,p.getValue());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = rs.getInt(1);
                p.setId(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void update(PropertyValue p){
        String sql = "update PropertyValue set pid = ?,ptid =?,value = ? where id = ? ";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,p.getProduct().getId());
            ps.setInt(2,p.getProperty().getId());
            ps.setString(3,p.getValue());
            ps.setInt(4,p.getId());
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "delete from PropertyValue where id ="+id;
        try (Connection c = DBUtil.getConnection();
        Statement s = c.createStatement()){
            s.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public PropertyValue get(int id){
        PropertyValue p = null;
        String sql = "select * from PropertyValue where id ="+id;
        try (Connection c = DBUtil.getConnection();
        Statement s = c.createStatement()){
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
                p = new PropertyValue();
                p.setId(id);
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String ss = rs.getString("value");
                Product p1 = new ProductDAO().get(pid);
                Property p2 = new PropertyDAO().get(ptid);
                p.setProduct(p1);
                p.setProperty(p2);
                p.setValue(ss);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return p;
    }
    public PropertyValue get(int ptid,int pid){
        PropertyValue p = null;
        String sql = "select * from PropertyValue where ptid = ? and pid = ?";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,ptid);
            ps.setInt(2,pid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                p = new PropertyValue();
              int id = rs.getInt("pid");
              String value = rs.getString("value");
              Product p1 = new ProductDAO().get(pid);
              Property p2 = new PropertyDAO().get(ptid);
              p.setId(id);
              p.setValue(value);
              p.setProperty(p2);
              p.setProduct(p1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return p;
    }
    public List<PropertyValue> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<PropertyValue> list(int start, int count) {
        List<PropertyValue> list = new ArrayList<>();
        String sql = "select * from PropertyValue order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue p = new PropertyValue();
                int id = rs.getInt(1);

                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                p.setProduct(product);
                p.setProperty(property);
                p.setValue(value);
                p.setId(id);
                list.add(p);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }
    //初始化，在新属性加入时，创建新的新属性对象
    public void init(Product p){
        List<Property> list = new PropertyDAO().list(p.getCategory().getId());
        for(Property pr : list){
            PropertyValue pv = get(pr.getId(),p.getId());
            if(pv == null){
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.setProperty(pr);
                this.add(pv);
            }
        }
    }
    public List<PropertyValue> list(int pid){
        List<PropertyValue> list = new ArrayList<>();
        String sql = "select * from PropertyValue where pid = ? order by ptid asc";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id  = rs.getInt("id");
                PropertyValue pv = new PropertyValue();
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");
                Product p = new ProductDAO().get(pid);
                Property pt = new PropertyDAO().get(ptid);
                pv.setProperty(pt);
                pv.setProduct(p);
                pv.setValue(value);
                pv.setId(id);
                list.add(pv);
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        return list;
    }

}
