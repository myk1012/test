package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl  implements UserService {
    private  UserDao userDao = new UserDaoImpl();
   /*注册用户*/
    @Override
    public boolean regist(User user) {
//        1.更具用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());

        if (u !=null){
            return false;
        }

/*2保存用户信息
* 2.1设置激活码,唯一字符码
* 2.2 设置激活码*/

        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        userDao.save(user);

//        3.激活邮件发送,邮件正文
        String  content="<a href='http://localhost/travel/user/activ?code="+user.getCode()+"'>点击激活[考学论坛]</a>";
        MailUtils.sendMail(user.getEmail(),content, "激活邮件");
        return true;
    }
//激活用户
    @Override
    public boolean active(String code) {
//        1.根据激活码查询用户对象
         User user=   userDao.finByCode(code);
        if (user != null){
//        2.调用dao的修改激活状态方法
            userDao.updateStatus(user);
            return true;
        }else {
            return  false;
        }

    }
/*登录方法*/
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
