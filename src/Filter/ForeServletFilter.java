package Filter;

import DAO.CategoryDAO;
import DAO.OrderItemDAO;
import Tmall_Bean.Category;
import Tmall_Bean.OrderItem;
import Tmall_Bean.User;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ForeServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String contextPath = req.getServletContext().getContextPath();

        User user = (User) req.getSession().getAttribute("user");
        int cartTotal = 0;
        if(null != user){
            List<OrderItem> list = new OrderItemDAO().listByUser(user.getId());
            for(OrderItem o : list){
                cartTotal += o.getNumber();
            }
        }
        req.setAttribute("cartTotal",cartTotal);

        List<Category> categories = (List<Category>) req.getAttribute("categories");
        if(null == categories){
            categories = new CategoryDAO().list();
            req.setAttribute("categories",categories);
        }

        String uri = req.getRequestURI();
        uri = StringUtils.remove(uri,contextPath);
        System.out.println("uri"+uri);
        if(uri.equals("/")){
            String method = "home";
            req.setAttribute("method",method);
            req.getRequestDispatcher("/foreServlet").forward(req,resp);
            return;
        }
        if (uri.startsWith("/fore") && !uri.startsWith("/foreServlet")) {
            String method = StringUtils.substringAfterLast(uri,"/fore");
            req.setAttribute("method",method);
            req.getRequestDispatcher("/foreServlet").forward(req,resp);
            return;
        }
        filterChain.doFilter(req,resp);
    }

    @Override
    public void destroy() {

    }
}
