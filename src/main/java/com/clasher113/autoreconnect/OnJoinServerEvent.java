package com.clasher113.autoreconnect;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class OnJoinServerEvent {
	public static String host;
	public static String nickname;
	Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void onJoinServerMessage(EntityJoinWorldEvent event){
		if(	MainClass.isEnabled == false){
			if(event.entity instanceof EntityPlayer){
				if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT){
					EntityPlayer player = (EntityPlayer) event.entity;
					if(nickname == null){
						nickname = player.getDisplayName();
						MainClass.isEnabled = true;
						}
						if (Minecraft.getMinecraft().isSingleplayer() == false){
							if (player.isDead == false && player.getDisplayName() == nickname){
								MainClass.tryNumber = 0;
								MainClass.connectingTimer = 300;
								MainClass.onGuiConnecting = false;
								host = Minecraft.getMinecraft().func_147104_D().serverIP;
								player.addChatMessage(new ChatComponentText(I18n.format("chat.string.enabled") + host));
								MainClass.isEnabled = true;
							}
						}
						else{
							if(player.isDead == false && player.getDisplayName() ==nickname)
								player.addChatMessage(new ChatComponentText(I18n.format("chat.string.notEnabled")));
								MainClass.isEnabled = true;
								}
				}
			}
		}	
	}	
}