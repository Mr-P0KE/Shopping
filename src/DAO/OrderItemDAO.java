package DAO;

import Tmall_Bean.Order;
import Tmall_Bean.OrderItem;
import Tmall_Bean.Product;
import Tmall_Bean.User;
import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "select count(*) from OrderItem";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public void add(OrderItem o) {
        String sql = "insert into OrderItem values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getProduct().getId());
            if (null == o.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, o.getOrder().getId());
            ps.setInt(3, o.getUser().getId());
            ps.setInt(4, o.getNumber());
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
    public void update(OrderItem o){
        String sql = "update OrderItem set pid = ? ,oid = ?,uid = ?,number = ? where id= ?";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,o.getProduct().getId());
            if (null == o.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, o.getOrder().getId());
            ps.setInt(3,o.getUser().getId());
            ps.setInt(4,o.getNumber());
            ps.setInt(5,o.getId());
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "delete from OrderItem where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public OrderItem get(int id) {
        OrderItem o = new OrderItem();
        String sql = "select * from OrderItem where id = " + id;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);
                o.setProduct(product);
                o.setUser(user);
                o.setNumber(number);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    o.setOrder(order);
                }
                o.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return o;
    }
    public List<OrderItem> listByUser(int uid) {
        return listByUser(uid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByUser(int uid, int start, int count) {
        List<OrderItem> list = new ArrayList<>();
        // oid=-1说明这个OrderItem还没有对应的订单，所以显示在购物车。当oid有值得时候，就不在购物车了。
        String sql = "select * from OrderItem where uid = ? and oid= -1 order by id asc limit ?,? ";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem o = new OrderItem();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    o.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                o.setProduct(product);
                o.setUser(user);
                o.setNumber(number);
                o.setId(id);
                list.add(o);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }
    public List<OrderItem> listByOrder(int oid) {
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByOrder(int oid, int start, int count) {
        List<OrderItem> beans = new ArrayList<>();
        String sql = "select * from OrderItem where oid = ? order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, oid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public void fill(List<Order> list){
        for(Order o : list){
            List<OrderItem> list1 = listByOrder(o.getId());
            float total = 0;
            int totalNumber = 0;
            for(OrderItem oi : list1){
                total += oi.getNumber() *oi.getProduct().getPromotePrice();
                totalNumber = oi.getNumber();
            }
            o.setTotal(total);
            o.setOrderItems(list1);
            o.setTotalNumber(totalNumber);
        }
    }
    public void fill(Order o){
        List<OrderItem> list1 = listByOrder(o.getId());
        float total = 0;
        for(OrderItem oi : list1){
            total += oi.getNumber() *oi.getProduct().getPromotePrice();
        }
        o.setTotal(total);
        o.setOrderItems(list1);
    }
    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid, 0, Short.MAX_VALUE);
    }
    public List<OrderItem> listByProduct(int pid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "select * from OrderItem where pid = ? order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);
                int uid = rs.getInt("uid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public int getSaleCount(int pid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {

            String sql = "select sum(number) from OrderItem where pid = " + pid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }
}
