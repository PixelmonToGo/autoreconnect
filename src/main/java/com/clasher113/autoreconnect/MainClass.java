package com.clasher113.autoreconnect;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "autoreconnect",
	name = "Auto Reconnect",
	version ="1.1.0",
	useMetadata = true,
	guiFactory = "com.clasher113.autoreconnect.ConfigGuiFactory",
	clientSideOnly = true
)

public class MainClass {
		private GuiScreen guiCache;
		public TextComponentBase errorDetail;
		public static Configuration configFile;
		public static boolean onGuiDisconnected = false;
		public static boolean onGuiConnecting = false;
		public static boolean isEnabled = false;
		public static byte reconnectDelayUpdater = 20;
		public static int tryNumber = 0;
		public static int reconnectDelay = 5;
		public static int connectingTimer = 300;

		Minecraft mc = Minecraft.getMinecraft();
		
		@Mod.EventHandler
	    public void preInit(FMLPreInitializationEvent event) {
			 MinecraftForge.EVENT_BUS.register(new OnJoinServerEvent());
			 FMLCommonHandler.instance().bus().register(this);
			 configFile = new Configuration(event.getSuggestedConfigurationFile());
		     syncConfig();
		}
		public static void syncConfig() {
			reconnectDelay = configFile.getInt(I18n.format("config.string.delay"), Configuration.CATEGORY_GENERAL, reconnectDelay, 5, Integer.MAX_VALUE, "An Integer!");
		    if(configFile.hasChanged())
			    	  configFile.save();
		}
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
			 if(event.getModID().equals("autoreconnect")){
		       syncConfig();
			 }
		}
		@SubscribeEvent
		public void onClientTick(TickEvent.ClientTickEvent event){
				if(event.phase == TickEvent.Phase.START){
					if(onGuiDisconnected && reconnectDelay != 0){
						reconnectDelayUpdater -= 1;
					}
					if (onGuiConnecting){
						connectingTimer -= 1;
						if (connectingTimer <= 0){
							FailedToConnectScreen failedToConnectScreen = new FailedToConnectScreen();
							onGuiDisconnected = true;
							mc.displayGuiScreen(failedToConnectScreen);
						}
					}
					if (mc.currentScreen != guiCache) {
						guiCache = mc.currentScreen;
						if (guiCache instanceof GuiDisconnected){ 
							onGuiConnecting = false;
							if (OnJoinServerEvent.host != null){
								DisconnectedScreen reconnectEvent = new DisconnectedScreen((GuiDisconnected)guiCache);
								onGuiDisconnected = true;
								mc.displayGuiScreen(reconnectEvent);
							}
						}
						if (guiCache instanceof GuiConnecting && OnJoinServerEvent.host != null){
							onGuiConnecting = true;
						}
						if (guiCache instanceof GuiMainMenu)
							isEnabled = false;
				}
			}		
	}
}