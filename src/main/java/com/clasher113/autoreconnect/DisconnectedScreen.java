package com.clasher113.autoreconnect;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.FMLClientHandler;


public class DisconnectedScreen
  extends GuiScreen
{
  public String errorMessage;
  public ITextComponent errorDetail;
  public List list;
  public final GuiScreen parent;
  
  public DisconnectedScreen(GuiDisconnected disconnected) throws RuntimeException {
    try {
      Field[] fields = GuiDisconnected.class.getDeclaredFields();
      for (Field f : fields)
        f.setAccessible(true); 
      this.errorMessage = (String)fields[0].get(disconnected);
      this.errorDetail = (ITextComponent)fields[1].get(disconnected);
      this.list = (List)fields[2].get(disconnected);
      this.parent = (GuiScreen)fields[3].get(disconnected);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } 
  }
  
  protected void keyTyped(char par1, int par2) {}
  
  public void initGui() {
    MainClass.syncConfig();
    MainClass.tryNumber++;
    MainClass.isEnabled = false;
    this.buttonList.clear();
    this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 3 + (this.height / 5), I18n.format("gui.toMenu", new Object[0])));
    this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 3 + (this.height / 5) + 42, I18n.format("gui.button.cancel0", new Object[0])));
    this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 3 + (this.height / 5) + 21, I18n.format("gui.button.reconnect", new Object[0]) + MainClass.reconnectDelay + "..."));
    this.list = this.fontRenderer.listFormattedStringToWidth(this.errorDetail.getFormattedText(), this.width - 50);
  }
  protected void actionPerformed(GuiButton button) {
    if (button.id == 0) {
      MainClass.onGuiDisconnected = false;
      MainClass.syncConfig();
      MainClass.tryNumber = 0;
      OnJoinServerEvent.host = null;
      GuiMultiplayer multiplayer = new GuiMultiplayer(this.parent);
      this.mc.displayGuiScreen((GuiScreen)multiplayer);
    } 
    if (button.id == 1) {
      MainClass.onGuiDisconnected = false;
      MainClass.syncConfig();
      MainClass.tryNumber = 0;
      OnJoinServerEvent.host = null;
      GuiDisconnected disconnected = new GuiDisconnected(this.parent, this.errorMessage, this.errorDetail);
      this.mc.displayGuiScreen((GuiScreen)disconnected);
    } 
    if (button.id == 2) {
      MainClass.onGuiDisconnected = false;
      MainClass.syncConfig();
      FMLClientHandler.instance().connectToServer((GuiScreen)new GuiMainMenu(), new ServerData("server", OnJoinServerEvent.host, false));
    } 
  }
  
  public void drawScreen(int par1, int par2, float par3) {
    if (MainClass.reconnectDelay == 0) {
      MainClass.onGuiDisconnected = false;
      MainClass.syncConfig();
      FMLClientHandler.instance().connectToServer((GuiScreen)new GuiMainMenu(), new ServerData("server", OnJoinServerEvent.host, false));
    } 
    if (MainClass.reconnectDelayUpdater <= 0) {
      MainClass.reconnectDelay--;
      MainClass.reconnectDelayUpdater = 20;
      this.buttonList.remove(2);
      this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 3 + (this.height / 5) + 21, I18n.format("gui.button.reconnect", new Object[0]) + MainClass.reconnectDelay + "..."));
    } 
    drawDefaultBackground();
    drawCenteredString(this.fontRenderer, this.errorMessage, this.width / 2, this.height / 2 - 50, 11184810);
    drawCenteredString(this.fontRenderer, I18n.format("gui.string.attemp", new Object[0]) + MainClass.tryNumber, this.width / 2, 10, 11184810);
    int k = this.height / 2 - 30;
    if (this.list != null) {
      for (Iterator<String> iterator = this.list.iterator(); iterator.hasNext(); k += this.fontRenderer.FONT_HEIGHT) {
        String s = iterator.next();
        drawCenteredString(this.fontRenderer, s, this.width / 2, k, 16777215);
      } 
    }
    super.drawScreen(par1, par2, par3);
  }
}
