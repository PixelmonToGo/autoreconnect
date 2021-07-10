package com.clasher113.autoreconnect;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;

public class FailedToConnectScreen
  extends GuiScreen {
  public void drawScreen(int par1, int par2, float par3) {
    drawDefaultBackground();
    drawCenteredString(this.fontRenderer, I18n.format("gui.string.failedToConnect", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
    if (MainClass.connectingTimer <= -80) {
      MainClass.syncConfig();
      MainClass.connectingTimer = 300;
      MainClass.onGuiConnecting = false;
      FMLClientHandler.instance().connectToServer((GuiScreen)new GuiMainMenu(), new ServerData("server", OnJoinServerEvent.host, false));
    } 
    super.drawScreen(par1, par2, par3);
  }
}
