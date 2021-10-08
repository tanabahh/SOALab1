package servlet;

import javax.servlet.annotation.WebServlet;
import model.User;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;
import service.UserService;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "userServlet", value = "/users/*")
public class UserSimpleServlet extends HttpServlet {

    private UserService service = new UserService();

//    public void init(ServletConfig servletConfig) {
//        try {
//            super.init(servletConfig);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("hello in servlet");
        List<User> users = service.findAllUsers();
        User user = new User();
        user.setAge(12);
        user.setName("kkkl");
        users.add(user);
        req.setAttribute("users", users);
        RequestDispatcher dispatcher = req.getRequestDispatcher("showVehicle.jsp");
        dispatcher.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        String name = req.getParameter("name");
        int age = Integer.parseInt(req.getParameter("age"));
        User user = new User(name, age);
        service.saveUser(user);
        resp.sendRedirect("/users");

    }

    @Override
    protected void  doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        User user = service.findUser(id);
        user.setName(req.getParameter("name"));
        user.setAge(Integer.parseInt(req.getParameter("age")));
        service.updateUser(user);
        resp.sendRedirect("/users");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        service.deleteUser(service.findUser(id));
        resp.sendRedirect("/users");
    }
}
