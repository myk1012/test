package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template= new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public User findByUsername(String username) {
        User user = null;

        try {
//        1.定义sql语句
            String sql="select * from tab_user where username = ?";
//        执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {

        }

        return user;
    }

    @Override
    public void save(User user) {
//        1
        String sql="insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code)" + "values(?,?,?,?,?,?,?,?,?)";


        template.update(sql,user.getUsername(),
                            user.getPassword(),
                            user.getName(),
                            user.getBirthday(),
                            user.getSex(),
                            user.getTelephone(),
                            user.getEmail(),
                            user.getStatus(),
                            user.getCode()
        );
    }
/*根据激活码查询用户对象*/
    @Override
    public User finByCode(String code) {
        User user = null;
        try {
            String sql="select * from tab_user where code = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;


    }
    /*修改指定用户激活状态*/

    @Override
    public void updateStatus(User user) {
        String  sql=" update tab_user set status = 'Y' where uid=?";
        template.update(sql, user.getUid());
    }
// 根据用户名和密码查询的方法
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;

        try {
//        1.定义sql语句
            String sql="select * from tab_user where username = ? and  password = ?";
//        执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username,password);
        } catch (Exception e) {

        }

        return user;
    }
}
