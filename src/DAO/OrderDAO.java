package DAO;

import Tmall_Bean.Order;
import Tmall_Bean.User;
import Util.DBUtil;
import Util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";
    public int getTotal(){
        int total = 0;
        String sql = "select count(*) from Order_";
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
    public void add(Order o) {
        String sql = "insert into order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, o.getOrderCode());
            ps.setString(2, o.getAddress());
            ps.setString(3, o.getPost());
            ps.setString(4, o.getReceiver());
            ps.setString(5, o.getMobile());
            ps.setString(6, o.getUserMessage());

            ps.setTimestamp(7, DateUtil.d2t(o.getCreateDate()));
            ps.setTimestamp(8, DateUtil.d2t(o.getPayDate()));
            ps.setTimestamp(9, DateUtil.d2t(o.getDeliveryDate()));
            ps.setTimestamp(10, DateUtil.d2t(o.getConfirmDate()));
            ps.setInt(11, o.getUser().getId());
            ps.setString(12, o.getStatus());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                o.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    public void update(Order o) {
        String sql = "update order_ set address= ?, post=?, receiver=?,mobile=?,userMessage=? ,createDate = ? , payDate =? , deliveryDate =?, confirmDate = ? , orderCode =?, uid=?, status=? where id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, o.getAddress());
            ps.setString(2, o.getPost());
            ps.setString(3, o.getReceiver());
            ps.setString(4, o.getMobile());
            ps.setString(5, o.getUserMessage());
            ps.setTimestamp(6, DateUtil.d2t(o.getCreateDate()));
            ps.setTimestamp(7, DateUtil.d2t(o.getPayDate()));
            ps.setTimestamp(8, DateUtil.d2t(o.getDeliveryDate()));
            ps.setTimestamp(9, DateUtil.d2t(o.getConfirmDate()));
            ps.setString(10, o.getOrderCode());
            ps.setInt(11, o.getUser().getId());
            ps.setString(12,o.getStatus());
            ps.setInt(13, o.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "delete from Order_ where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Order get(int id) {
        Order o = new Order();
        String sql = "select * from Order_ where id = " + id;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                String orderCode = rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                int uid = rs.getInt("uid");
                java.util.Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                java.util.Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                java.util.Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));

                o.setOrderCode(orderCode);
                o.setAddress(address);
                o.setPost(post);
                o.setReceiver(receiver);
                o.setMobile(mobile);
                o.setUserMessage(userMessage);
                o.setCreateDate(createDate);
                o.setPayDate(payDate);
                o.setDeliveryDate(deliveryDate);
                o.setConfirmDate(confirmDate);
                User user = new UserDAO().get(uid);
                o.setUser(user);
                o.setStatus(status);
                o.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return o;
    }
    public List<Order> list() {
        return list(0, Short.MAX_VALUE);
    }
    public List<Order> list(int start, int count) {
        List<Order> list = new ArrayList<>();
        String sql = "select * from Order_ order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                String orderCode = rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                int uid = rs.getInt("uid");

                int id = rs.getInt("id");
                o.setId(id);
                o.setOrderCode(orderCode);
                o.setAddress(address);
                o.setPost(post);
                o.setReceiver(receiver);
                o.setMobile(mobile);
                o.setUserMessage(userMessage);
                o.setCreateDate(createDate);
                o.setPayDate(payDate);
                o.setDeliveryDate(deliveryDate);
                o.setConfirmDate(confirmDate);
                User user = new UserDAO().get(uid);
                o.setUser(user);
                o.setStatus(status);
               list.add(o);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }
    public List<Order> list(int uid, String excludedStatus) {
        return list(uid, excludedStatus, 0, Short.MAX_VALUE);
    }

    public List<Order> list(int uid, String excludedStatus, int start, int count) {
        List<Order> list = new ArrayList<>();
        String sql = "select * from Order_ where uid = ? and status != ? order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setString(2, excludedStatus);
            ps.setInt(3, start);
            ps.setInt(4, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order();
                String orderCode = rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));

                int id = rs.getInt("id");
                o.setId(id);
                o.setOrderCode(orderCode);
                o.setAddress(address);
                o.setPost(post);
                o.setReceiver(receiver);
                o.setMobile(mobile);
                o.setUserMessage(userMessage);
                o.setCreateDate(createDate);
                o.setPayDate(payDate);
                o.setDeliveryDate(deliveryDate);
                o.setConfirmDate(confirmDate);
                User user = new UserDAO().get(uid);
                o.setStatus(status);
                o.setUser(user);
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
