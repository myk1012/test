package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;
/*注册用户*/
public interface UserService {
//1.根据用户查询用户对象

//    2.保存用户信息

    boolean regist(User user);

    boolean active(String code);

    User login(User user);
}
