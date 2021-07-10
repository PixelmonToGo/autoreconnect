package com.clasher113.autoreconnect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class OnJoinServerEvent
{
  public static String host;
  Minecraft mc = Minecraft.getMinecraft();
  
  @SubscribeEvent
  public void onJoinServerMessage(EntityJoinWorldEvent event) {
    if (!MainClass.isEnabled && 
      event.getEntity() instanceof EntityPlayer && 
      FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
      EntityPlayer player = (EntityPlayer)event.getEntity();
      if (!Minecraft.getMinecraft().isSingleplayer()) {
        MainClass.tryNumber = 0;
        MainClass.connectingTimer = 300;
        MainClass.onGuiConnecting = false;
        host = (Minecraft.getMinecraft().getCurrentServerData()).serverIP;
        //player.sendMessage((ITextComponent)new TextComponentString(I18n.format("chat.string.enabled", new Object[0]) + host));
        MainClass.isEnabled = true;
      } else {
        //player.sendMessage((ITextComponent)new TextComponentString(I18n.format("chat.string.notEnabled", new Object[0])));
        MainClass.isEnabled = true;
      } 
    } 
  }
}
