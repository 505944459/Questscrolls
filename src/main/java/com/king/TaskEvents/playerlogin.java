package com.king.TaskEvents;

import com.king.mysql.MysqlManager;
import com.king.plugincore.giveTask;
import com.king.resource.ReadConfig;
import com.king.resource.ReadTime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class playerlogin implements Listener {

    @EventHandler
    public void denglu(PlayerJoinEvent event){

        // 数据库判断玩家有没有领取过任务卷轴
        if(MysqlManager.isReceiveToday(event.getPlayer().getName()))return;
        else {
            MysqlManager.receive(event.getPlayer().getName());
        }

        if(ReadConfig.task.equalsIgnoreCase("-")){
            return;
        }

        if(!ReadTime.time.equalsIgnoreCase(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")))){ //假如时间不对了 就重载
            ReadTime.reload();
        }

        if(ReadTime.cha(event.getPlayer().getName())){
            giveTask.give(event.getPlayer().getName(),ReadConfig.task,event.getPlayer());
        }


    }

}
