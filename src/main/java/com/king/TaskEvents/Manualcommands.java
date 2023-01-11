package com.king.TaskEvents;

import com.king.Zhu;
import com.king.command.QuestscrollsCommandExecutor;
import com.king.plugincore.MainBusiness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Manualcommands implements Listener {

    @EventHandler
    public void c(PlayerCommandPreprocessEvent event){

        if(QuestscrollsCommandExecutor.isQuery){
            Zhu.SendManualObject(event.getPlayer(),"command(指令)", event.getMessage());
        }
        new MainBusiness().Task("command", event.getMessage(),event.getPlayer(),1);

    }

}
