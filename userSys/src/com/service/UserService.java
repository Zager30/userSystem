package com.service;

import com.domain.User;
import com.utils.JDBCUtils;
import com.utils.MD5Utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    /**
     * 登录
     * @param name
     * @param pwd
     * @return
     */
    public boolean getUserByIdAndPwd(String name, String pwd) {
        boolean res = false;
        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from user where uname = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.execute();

            resultSet= preparedStatement.getResultSet();
            if (resultSet.next()) { //如果查询到一条记录，则说明该管理存在

                String uname = resultSet.getString(3);
                String upwd = resultSet.getString(4);

                String encoderByMd5 = MD5Utils.EncoderByMd5(pwd);
                if (name.equals(uname) && encoderByMd5.equals(upwd)){//校验密码和用户名是否正确
                    res = true;
                    System.out.println("登录成功");
                }

            } else {
                System.out.println("登录失败");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(resultSet,preparedStatement,connection);
        }

    return res;

    }

    /**
     * 添加记录
     * @param uid
     * @param uname
     * @param upwd
     * @param umajor
     * @param utype
     * @return
     */
    public boolean add(String uid, String uname, String upwd, String umajor, String utype) {
        boolean res = false;
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into user(uid,uname,upwd,umajor,utype) values (?,?,?,?,?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,uid);
            preparedStatement.setString(2,uname);
            preparedStatement.setString(3,MD5Utils.EncoderByMd5(upwd));
            preparedStatement.setString(4,umajor);
            preparedStatement.setString(5,utype);
            int i = preparedStatement.executeUpdate();
            if (i>0){
                res = true;
             }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
            return res;
    }

    /**
     * 显示
     */
    public void listUser() {

        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from user";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();

            resultSet= preparedStatement.getResultSet();
            System.out.println("uid\t\t名字\t\t专业\t\t类别");
            while (resultSet.next()) {
                String uid = resultSet.getString(2);
                String uname = resultSet.getString(3);
                String umajor = resultSet.getString(5);
                String utype = resultSet.getString(6);
                System.out.println(uid+"\t"+uname+"\t"+umajor+"\t"+utype);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(resultSet,preparedStatement,connection);
        }

    }

    /**
     * 修改密码
     * @param name
     * @param upwd
     * @return
     */
    public boolean updatePwd(String name, String upwd) {
        boolean res = false;
        Connection connection = JDBCUtils.getConnection();
        String sql = "update `user` set upwd = ? where uname = ? ";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,MD5Utils.EncoderByMd5(upwd));
            preparedStatement.setString(2,name);
            int i = preparedStatement.executeUpdate();
            if (i>0){
                res = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;

    }

    /**
     * 修改姓名以外的信息
     * @param uid
     * @param uname
     * @param upwd
     * @param umajor
     * @param utype
     * @return
     */
    public boolean updateInfo(String uid, String uname, String upwd, String umajor, String utype) {
        boolean res = false;
        Connection connection = JDBCUtils.getConnection();
        
        //根据输入的名字查找，若找到则更新其它项，提示成功，否则失败
        String sql = "update `user` set uid = ?,upwd = ? ,umajor=?,utype=? where uname = ? ";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,uid);

            preparedStatement.setString(2,MD5Utils.EncoderByMd5(upwd));
            preparedStatement.setString(3,umajor);
            preparedStatement.setString(4,utype);
            preparedStatement.setString(5,uname);
            int i = preparedStatement.executeUpdate();
            if (i>0){
                res = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;

    }

    /**
     * 删除
     * @param uid
     * @return
     */
    public boolean delete(String uid) {

        boolean res = false;
        Connection connection = JDBCUtils.getConnection();
        String sql = "delete from user where uid = ?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,uid);

            int i = preparedStatement.executeUpdate();
            if (i>0){
                res = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;


    }

    /**
     * 获取全部记录
     * @return
     */
    public List<User> getList() {
        ArrayList<User> users = new ArrayList<>();
        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from user";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();

            resultSet= preparedStatement.getResultSet();
            while (resultSet.next()) { //如果查询到一条记录，则说明该管理存在

                String uid = resultSet.getString(2);
                String uname = resultSet.getString(3);
                String upwd = resultSet.getString(4);
                String umajor = resultSet.getString(5);
                String utype = resultSet.getString(6);
                User user = new User(uid, uname, upwd, umajor, utype);
                users.add(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(resultSet,preparedStatement,connection);
        }
        return users;

    }
}
