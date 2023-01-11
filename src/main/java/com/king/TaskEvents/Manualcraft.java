package com.king.TaskEvents;

import com.king.Zhu;
import com.king.command.QuestscrollsCommandExecutor;
import com.king.plugincore.MainBusiness;
import com.king.plugincore.ToolClass;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class Manualcraft implements Listener {

    @EventHandler
    public void craft(CraftItemEvent event){

        if(event.getInventory() == null){  //没人能看见这个物品栏
            return;
        }

        for (HumanEntity humanEntity : event.getInventory().getViewers()) { //获取能看到这个物品栏的玩家

            if(QuestscrollsCommandExecutor.isQuery){
                Zhu.SendManualObject((Player) humanEntity,"craft(合成)", ToolClass.nmsitem(event.getCurrentItem()));
            }
            new MainBusiness().Task("craft", ToolClass.nmsitem(event.getCurrentItem()), (Player) humanEntity,event.getRecipe().getResult().getAmount());
        }

    }

    }

