package com.utils;

import com.service.UserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Reader {

    public static void reader(){
        UserService userService = new UserService();
        String inputFile ="src\\user.txt";

        //读取文件
        try (FileReader reader = new FileReader(inputFile);
             BufferedReader br = new BufferedReader(reader) //建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            br.readLine();
            //读
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                String[] split = line.split(",");
                userService.add(split[0],split[1],split[2],split[3],split[4]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
