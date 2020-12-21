package com.clasher113.autoreconnect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class OnJoinServerEvent {
	public static String host;
	Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void onJoinServerMessage(EntityJoinWorldEvent event){
		if(MainClass.isEnabled == false){
			if(event.entity instanceof EntityPlayer){
				if(FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT){
					EntityPlayer player = (EntityPlayer) event.entity;
						if (Minecraft.getMinecraft().isSingleplayer() == false){
							MainClass.tryNumber = 0;
							MainClass.connectingTimer = 300;
							MainClass.onGuiConnecting = false;
							host = Minecraft.getMinecraft().getCurrentServerData().serverIP;
							player.addChatMessage(new ChatComponentText(I18n.format("chat.string.enabled") + host));
							MainClass.isEnabled = true;
						}
						else{
							player.addChatMessage(new ChatComponentText(I18n.format("chat.string.notEnabled")));
							MainClass.isEnabled = true;
							}
				}
			}
		}	
	}	
}