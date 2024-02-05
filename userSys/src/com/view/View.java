package com.view;

import com.domain.User;
import com.service.UserService;
import com.utils.Reader;

import com.utils.Utility;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

public class View {
    //控制是否退出菜单
    private boolean loop = true;
    private String key = ""; //接收用户的选择

    private UserService userService = new UserService();

    public static void main(String[] args) throws SQLException {
      Reader.reader();//将user.txt导入数据库，只需要第一遍运行时执行
        new View().mainMenu();
    }

    private  boolean checkPwd(String password) {
        //密码至少包含：大小写英文字母、数字，密码长度大于4位，小于10位
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,10}$";
        boolean matches = Pattern.matches(regex, password);
        return  matches;
    }

    private void mainMenu() throws SQLException {

        while (loop) {

            System.out.println("\n===============用户管理================");
            System.out.println("\t\t 1 登录");
            System.out.println("\t\t 2 注册");
            System.out.println("\t\t 3 退出");
            System.out.print("请输入你的选择: ");
            key = Utility.readString(1);
            switch (key) {
                case "1":
                    System.out.print("输入用户名: ");
                    String name = Utility.readString(50);
                    System.out.print("输入密码: ");
                    String pwd = Utility.readString(50);
                    boolean res = userService.getUserByIdAndPwd(name, pwd);
                    if (res) { //名字和密码匹配成功
                        System.out.println("===============登录成功[" + name + "]================\n");
                        //显示二级菜单, 这里二级菜单是循环操作，所以做成while
                        while (loop) {
                            System.out.println("\n===============用户管理================");
                            System.out.println("\t\t 1 显示");
                            System.out.println("\t\t 2 添加");
                            System.out.println("\t\t 3 修改密码");
                            System.out.println("\t\t 4 修改信息");
                            System.out.println("\t\t 5 删除");
                            System.out.println("\t\t 6 导出用户");
                            System.out.println("\t\t 9 退出");
                            System.out.print("请输入你的选择: ");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    listUser();
                                    break;
                                case "2":
                                    if (addUser()) System.out.println("添加成功");
                                    else System.out.println("添加失败");
                                    break;
                                case "3":
                                    updatePwd(name);
                                    break;
                                case "4":
                                    updateInfo();
                                    break;
                                case "5":
                                    delete();
                                    break;
                                case "6":
                                    export();
                                    break;
                                case "9":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("输入有误，请重新输入");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("===============登录失败================");
                    }
                    break;
                case "2":
                    if (addUser()) System.out.println("注册成功");
                    break;
                case "3":
                    loop = false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }
        System.out.println("你退出了用户管理系统");
        
    }

    private void export() {
        String outfile = "src\\new_user.txt";  //导出的用户信息
        File f = new File(outfile);
        FileOutputStream fos1 = null;
        OutputStreamWriter dos1 = null;
        try {
            fos1 = new FileOutputStream(f);
            dos1 = new OutputStreamWriter(fos1);
            dos1.write("uid,uname,upwd,umajor,utype\n");
           List<User> lists=  userService.getList();
            for (User list : lists) {
                dos1.write(list.getUid()+","+list.getUname()+","+list.getUpwd()+","+list.getUmajor()+","+list.getUtype()+"\n");
                dos1.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos1.close();
                fos1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void delete() {
        System.out.print("输入id: ");
        String uid = Utility.readString(50);
        System.out.print("确定删除？（Y/N）");
        String flag = Utility.readString(50);
        if (flag.equals("y") || flag.equals("Y")) {
            boolean res = userService.delete(uid);
            if (res) {
                System.out.println("删除成功");
            } else {
                System.out.println("删除失败");
            }
        }

    }

    private void updateInfo() {
        System.out.print("输入id: ");
        String uid = Utility.readString(50);
        System.out.print("输入用户名: ");
        String uname = Utility.readString(50);
        System.out.print("输入密码: ");
        String upwd = Utility.readString(50);
        if (!checkPwd(upwd)){
            System.out.println("密码强度不够，应包含大小写英文字母和数字，长度4~10位");
            System.out.println("修改失败");
            return ;
        }
        System.out.print("输入专业: ");
        String umajor = Utility.readString(50);
        System.out.print("输入类别: ");
        String utype = Utility.readString(50);
        boolean res = userService.updateInfo(uid, uname, upwd, umajor, utype);
        if (res) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
    }

    private void updatePwd(String name) {
        System.out.print("输入密码: ");
        String upwd = Utility.readString(50);
        if (!checkPwd(upwd)){
            System.out.println("密码强度不够，应包含大小写英文字母和数字，长度4~10位");
            System.out.println("修改失败");
            return ;
        }
        boolean res = userService.updatePwd(name, upwd);
        if (res) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
    }

    private boolean addUser() {
        System.out.print("输入id: ");
        String uid = Utility.readString(50);
        System.out.print("输入用户名: ");
        String uname = Utility.readString(50);
        System.out.print("输入密码: ");
        String upwd = Utility.readString(50);
        if (!checkPwd(upwd)){
            System.out.println("密码强度不够，应包含大小写英文字母和数字，长度4~10位");
            return false;
        }
        System.out.print("输入专业: ");
        String umajor = Utility.readString(50);
        System.out.print("输入类别: ");
        String utype = Utility.readString(50);
        return userService.add(uid, uname, upwd, umajor, utype);
    }

    private void listUser() {
        userService.listUser();
    }
}


