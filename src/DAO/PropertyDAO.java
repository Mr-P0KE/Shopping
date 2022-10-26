package DAO;

import Tmall_Bean.Category;
import Tmall_Bean.Property;
import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {
    public int getTotal(int cid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {

            String sql = "select count(*) from Property where cid =" + cid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }
    public void add(Property property){
        String sql = "insert into property values(null,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,property.getCategory().getId());
            ps.setString(2, property.getName());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                property.setId(rs.getInt(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void update(Property property){
        String sql = "update property set cid = ? ,name = ? where id = ?";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,property.getCategory().getId());
            ps.setString(2,property.getName());
            ps.setInt(3,property.getId());
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "delete from property where id ="+id;
        try (Connection c = DBUtil.getConnection();
        Statement s = c.createStatement()){
            s.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Property get(int id){
        Property property = new Property();
        String sql = "select * from property where id="+id;
        try (Connection c = DBUtil.getConnection();
        Statement s = c.createStatement()){
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
                property.setId(id);
                property.setName(rs.getString("name"));
                Category category = new CategoryDAO().get(rs.getInt("cid"));
                property.setCategory(category);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return property;
    }
    public List<Property> list(int cid){
        return list(cid,0,Short.MAX_VALUE);
    }
    public List<Property> list(int cid,int start,int end){
        List<Property> list = new ArrayList<>();
        String sql = "select * from property where cid = ? order by id asc limit ?,?";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,cid);
            ps.setInt(2,start);
            ps.setInt(3,end);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Property p = new Property();
                p.setId(rs.getInt(1));
                p.setName(rs.getString("name"));
                Category category = new CategoryDAO().get(rs.getInt("cid"));
                p.setCategory(category);
                list.add(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
