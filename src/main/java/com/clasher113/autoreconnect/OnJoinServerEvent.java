package com.clasher113.autoreconnect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class OnJoinServerEvent {
    public static String host;
    public static String nickname;
    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onJoinServerMessage(EntityJoinWorldEvent event) {
        if (!MainClass.isEnabled) {
            if (event.getEntity() instanceof EntityPlayer) {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                    EntityPlayer player = (EntityPlayer) event.getEntity();
                    if (nickname == null) {
                        nickname = player.getDisplayName().getFormattedText();
                        MainClass.isEnabled = true;
                    }
                    if (!Minecraft.getMinecraft().isSingleplayer()) {
                        if (!player.isDead && player.getDisplayName().getFormattedText().equals(nickname)) {
                            MainClass.tryNumber = 0;
                            MainClass.connectingTimer = 300;
                            MainClass.onGuiConnecting = false;
                            host = Minecraft.getMinecraft().getCurrentServerData().serverIP;
                            player.sendMessage(new TextComponentString(I18n.format("chat.string.enabled") + host));
                            MainClass.isEnabled = true;
                        }
                    } else {
                        if (!player.isDead && player.getDisplayName().getFormattedText().equals(nickname))
                            player.sendMessage(new TextComponentString(I18n.format("chat.string.notEnabled")));
                        MainClass.isEnabled = true;
                    }
                }
            }
        }
    }
}