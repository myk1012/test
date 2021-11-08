package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet( "/user/*")// user/add/user/find
public class UserServlet extends BaseServlet {
    private  UserService service=new UserServiceImpl();
    /*注册功能
    *
    *
    *
    * */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证校验
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码使用一次
        //比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){

            ResultInfo info= new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            ObjectMapper mapper= new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;

        }
        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();
// 2.封装对象
        User user= new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        3.调用service完成
       // UserService service=new UserServiceImpl();
        boolean flag= service.regist(user);
        ResultInfo info= new ResultInfo();
//        响应
        if (flag){
//            注册成功
            info.setFlag(true);
        }else{
//            注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }
//        将info对象序列化为json
        ObjectMapper mapper= new ObjectMapper();
        String json = mapper.writeValueAsString(info);
//        将json数据写回客户端
//        设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }
/*登录功能
*
*
*
* */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //            1.获取用户名和密码
        Map<String, String[]> map = request.getParameterMap();
//    封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        调用Service查询
//        UserService service = new UserServiceImpl();
        User u = service.login(user);
        ResultInfo info = new ResultInfo();
//        4.判断用户存在
        if (u == null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
//        5判断用户是否激活
        if ( u != null && !"Y".equals(u.getStatus())){
//            用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活,请激活");
        }
//        6.判断登录成功
        if (u != null && "Y".equals(u.getStatus())){
            info.setFlag(true);
            request.getSession().setAttribute("user", u);
        }
//        ObjectMapper mapper = new ObjectMapper();
//
//        response.setContentType("application/json;charset:utf-8");
//        mapper.writeValue(response.getOutputStream(),info);
        writeValue(info,response);

    }

/*查找单个对象
*
*
*
* */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
//        ObjectMapper mapper=new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(), user);
        writeValue(user, response);

    }
/*退出功能
*
*
*
* */

    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+"/login.html");
    }
/*激活功能
*
*
*
* */
    public void activ(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取激活码
        String code = request.getParameter("code");
        if (code != null){
//            2.调用service完成激活
//            UserService service=new UserServiceImpl();
            boolean flag= service.active(code);
//           3.判断标记
            String msg=null;

            if (flag){
                msg="激活成功,请<a href='login.html'>登录</a>";
            }else {
                msg="激活失败,请联系管理员";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }


    }
