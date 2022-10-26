package DAO;

import Tmall_Bean.Product;
import Tmall_Bean.Review;
import Tmall_Bean.User;
import Util.DBUtil;
import Util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDAO {
    public int getTotal(){
        int total = 0;
        String sql = "select count(*) from Review";
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()){
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total= rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return total;
    }
    public int getTotal(int pid){
        int total = 0;
        String sql = "select count(*) from Review where pid ="+pid;
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
    public void add(Review r){
        String sql = "insert into Review values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,r.getContent());
            ps.setInt(2,r.getUser().getId());
            ps.setInt(3,r.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(r.getCreateDate()));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                r.setId(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void update(Review r) {

        String sql = "update Review set content= ?, uid=?, pid=? , createDate = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getContent());
            ps.setInt(2, r.getUser().getId());
            ps.setInt(3, r.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(r.getCreateDate()));
            ps.setInt(5, r.getId());
            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    public void delete(int id) {
        String sql = "delete from Review where id = " + id;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Review get(int id){
        Review r = new Review();
        String sql = "select * from Review where id ="+id;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()){
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
                int uid = rs.getInt("uid");
                int pid = rs.getInt("pid");
                String content = rs.getString("content");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Product p = new ProductDAO().get(pid);
                User u = new UserDAO().get(uid);
                r.setContent(content);
                r.setProduct(p);
                r.setUser(u);
                r.setId(id);
                r.setCreateDate(createDate);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return r;
    }
    public int getCount(int pid) {
        String sql = "select count(*) from Review where pid = ? ";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return 0;
    }
    public List<Review> list(int pid) {
        return list(pid, 0, Short.MAX_VALUE);
    }
    public List<Review> list(int pid, int start, int count) {
        List<Review> list = new ArrayList<>();
        String sql = "select * from Review where pid = ? order by id  asc limit ?,?";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,pid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Review r = new Review();
                String content  = rs.getString("content");
                int id = rs.getInt("id");
                int uid = rs.getInt("uid");
                User u = new UserDAO().get(uid);
                Product p = new ProductDAO().get(pid);
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                r.setId(id);
                r.setProduct(p);
                r.setUser(u);
                r.setContent(content);
                r.setCreateDate(createDate);
                list.add(r);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public boolean isExist(String content, int pid) {


        String sql = "select * from Review where content = ? and pid = ?";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, pid);


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }


        return false;
    }
}
