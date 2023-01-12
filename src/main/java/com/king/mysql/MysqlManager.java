package com.king.mysql;

import com.king.resource.ReadConfig;
import org.bukkit.Bukkit;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.logging.Level;

/**
 * @Author: RiceTofu
 * @Date: 2023-1-11
 * @Discription: 负责sql操作相关的类
 * */
public class MysqlManager {

    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    static {
        // 驱动加载
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING,"加载mysql驱动时出现了一个错误!!");
        }
        // 获取mysql连接
        String url = "jdbc:mysql://"+ReadConfig.Host+"/"+ReadConfig.Name+"?useSSL=false";
        String username = ReadConfig.Username;
        String password = ReadConfig.Password;

        Bukkit.getLogger().log(Level.INFO, username,password);

        try {
            connection = DriverManager.getConnection(url,username,password);
        }catch (Exception e){
            e.printStackTrace();
            Bukkit.getLogger().log(Level.WARNING,"获取SQL连接时出现异常，请检查sql配置!!");
        }

    }

    /**
     * 玩家数据保存
     * @param player_id 玩家id
     * @param times 完成次数
     * */
    public static boolean save(String player_id,Integer times){
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING,"获取sql执行对象时出现了错误");
            return false;
        }
        //先查询一次判断数据库中有无这个玩家的数据
        String sql = "SELECT * FROM playerdata where player_id = '" +player_id+"'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if(!resultSet.next()){
                //插入 一个用户
                sql = "INSERT INTO playerdata(player_id,finished_times,last_receive_time) values ('"+player_id+"',"+times+",'"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"')";
                statement.execute(sql);
            }else {
                //更新 一个用户
                sql = "UPDATE playerdata set finished_times = " +times +",last_receive_time = '"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"' where player_id = '"+player_id+"'";
                statement.execute(sql);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING,"执行sql查询语句出现错误");
            return false;
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.WARNING,"关闭statement对象时出现了错误!");
            }
        }
        return true;
    }

    /**
     * 判断指定玩家是否今日已经收到任务
     * @param player_id 玩家id
     * @return 是否收到过仍无
     * */
    public static boolean isReceiveToday(String player_id){

        String sql = "SELECT * FROM playerdata where player_id = '"+player_id+"' and last_receive_time = '"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"'";

        Statement statement = null;
        try {
            statement = connection.createStatement();
            if (statement.executeQuery(sql).next()) {
                return true;
            }else return false;
        }catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING,"执行sql查询语句出现错误");
            return true;
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.WARNING,"关闭statement对象时出现了错误!");
            }
        }
    }

    /**
     * 玩家收到任务事件调用
     * */
    public static void receive(String player_id){
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING,"获取sql执行对象时出现了错误");
        }
        //先查询一次判断数据库中有无这个玩家的数据
        String sql = "SELECT * FROM playerdata where player_id = '" +player_id+"'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if(!resultSet.next()){
                //插入 一个用户
                sql = "INSERT INTO playerdata(player_id,finished_times,last_receive_time) values ('"+player_id+"',"+0+",'"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"')";
                statement.execute(sql);
            }else {
                //更新 一个用户
                sql = "UPDATE playerdata set last_receive_time = '"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"' where player_id = '"+player_id+"'";
                statement.execute(sql);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING,"执行sql查询语句出现错误");
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.WARNING,"关闭statement对象时出现了错误!");
            }
        }

    }


    /**
     * 玩家数据读取
     * */
    public static List<Map> get(){
        String sql = "SELECT * FROM playerdata";

        List<Map> result  = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Map<String,Object> map = new HashMap<>();

                map.put("player_id",resultSet.getString(1));
                map.put("finished_times",resultSet.getInt(2));
                map.put("last_receive_time",resultSet.getString(3));

                result.add(map);
            }


        } catch (SQLException e) {

        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.WARNING,"关闭statement对象时出现了错误!");
            }
        }

        return result;
    }

}
