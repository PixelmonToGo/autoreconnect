package com.clasher113.autoreconnect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "autoreconnect", name = "Auto Reconnect", version = "1.3.0", useMetadata = true, guiFactory = "com.clasher113.autoreconnect.ConfigGuiFactory")
public class MainClass
{
  private GuiScreen guiCache;
  public ITextComponent errorDetail;
  public static Configuration configFile;
  public static boolean onGuiDisconnected = false;
  public static boolean onGuiConnecting = false;
  public static boolean isEnabled = false;
  public static byte reconnectDelayUpdater = 20;
  public static int tryNumber = 0;
  public static int reconnectDelay = 5;
  public static int connectingTimer = 300;
  
  Minecraft mc = Minecraft.getMinecraft();
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new OnJoinServerEvent());
    FMLCommonHandler.instance().bus().register(this);
    configFile = new Configuration(event.getSuggestedConfigurationFile());
    syncConfig();
  }
  
  public static void syncConfig() {
    reconnectDelay = configFile.getInt(I18n.format("config.string.delay", new Object[0]), "general", reconnectDelay, 10, 2147483647, "An integer!");
    if (configFile.hasChanged())
      configFile.save(); 
  }
  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals("autoreconnect"))
      syncConfig(); 
  }
  
  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.START) {
      if (onGuiDisconnected == true && reconnectDelay != 0) {
        reconnectDelayUpdater = (byte)(reconnectDelayUpdater - 1);
      }
      if (onGuiConnecting == true) {
        connectingTimer--;
        if (connectingTimer <= 0) {
          FailedToConnectScreen failedToConnectScreen = new FailedToConnectScreen();
          onGuiDisconnected = true;
          this.mc.displayGuiScreen(failedToConnectScreen);
        } 
      } 
      if (this.mc.currentScreen != this.guiCache) {
        this.guiCache = this.mc.currentScreen;
        if (this.guiCache instanceof GuiDisconnected) {
          onGuiConnecting = false;
          if (OnJoinServerEvent.host != null) {
            DisconnectedScreen reconnectEvent = new DisconnectedScreen((GuiDisconnected)this.guiCache);
            onGuiDisconnected = true;
            this.mc.displayGuiScreen(reconnectEvent);
          } 
        } 
        if (this.guiCache instanceof net.minecraft.client.multiplayer.GuiConnecting && OnJoinServerEvent.host != null) {
          onGuiConnecting = true;
        }
        if (this.guiCache instanceof net.minecraft.client.gui.GuiMultiplayer)
          isEnabled = false; 
      } 
    } 
  }
}
