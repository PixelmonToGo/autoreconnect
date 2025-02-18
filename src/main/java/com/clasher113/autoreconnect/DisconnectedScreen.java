package com.clasher113.autoreconnect;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentBase;
import net.minecraftforge.fml.client.FMLClientHandler;

public class DisconnectedScreen extends GuiScreen{
	
	public String errorMessage;
	public TextComponentBase errorDetail;
	@SuppressWarnings("rawtypes")
	public List list;
	public final GuiScreen parent;

	public DisconnectedScreen(GuiDisconnected disconnected)throws RuntimeException {
		try {
			Field[] fields = GuiDisconnected.class.getDeclaredFields();
			for (Field f : fields)
				f.setAccessible(true);
			errorMessage = (String) fields[0].get(disconnected);
			errorDetail = (TextComponentBase) fields[1].get(disconnected);
			list = (List)  fields[2].get(disconnected);
			parent = (GuiScreen) fields[3].get(disconnected);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void keyTyped(char par1, int par2) { }
	@SuppressWarnings("unchecked")
	public void initGui() {
		MainClass.syncConfig();
		MainClass.tryNumber += 1;
		MainClass.isEnabled = false;
		this.buttonList.clear();
		//this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.toMenu")));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 62, I18n.format("gui.button.cancel0")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 41, I18n.format("gui.button.reconnect") + MainClass.reconnectDelay + "..."));
		this.list = this.fontRenderer.listFormattedStringToWidth(errorDetail.getFormattedText(), this.width - 50);
	}
	protected void actionPerformed(GuiButton button) {
		if (button.id == 1){
			MainClass.onGuiDisconnected = false;
			MainClass.syncConfig();
			FMLClientHandler.instance().connectToServer(new GuiMainMenu(), new ServerData("server", OnJoinServerEvent.host, false));
		}
		else if (button.id == 0) {
			MainClass.onGuiDisconnected = false;
			MainClass.syncConfig();
			MainClass.tryNumber = 0;
			OnJoinServerEvent.host = null;
			GuiMainMenu mainMenu = new GuiMainMenu();
			mc.displayGuiScreen(mainMenu);
		}
	}
	@SuppressWarnings("rawtypes")
	public void drawScreen(int par1, int par2, float par3) {
		if(MainClass.reconnectDelay == 0){
			MainClass.onGuiDisconnected = false;
			MainClass.syncConfig();
			FMLClientHandler.instance().connectToServer(new GuiMainMenu(), new ServerData("server", OnJoinServerEvent.host, false));
		}
		if(MainClass.reconnectDelayUpdater <= 0){
			MainClass.reconnectDelay -=1;
			MainClass.reconnectDelayUpdater = 20;
			this.buttonList.remove(1);
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 41, I18n.format("gui.button.reconnect") + " " + MainClass.reconnectDelay + "..."));
		}
		this.drawDefaultBackground();

		String reason = list.size() > 0 ? list.get(0).toString() : "";

		boolean showCustomMessage = this.errorMessage.contains("Connection Lost") && reason.contains("Unable to connect you to");

		this.drawCenteredString(this.fontRenderer, showCustomMessage ? I18n.format("reconnect.reason.title") : this.errorMessage, this.width / 2, this.height / 2 - 50, 11184810);
		this.drawCenteredString(this.fontRenderer, I18n.format("gui.string.attemp") + MainClass.tryNumber, this.width / 2, 10, 11184810);
		int k = this.height / 2 - 30;

		if(showCustomMessage) {
			this.drawCenteredString(this.fontRenderer, I18n.format("reconnect.reason.message"), this.width / 2, k, 16777215);
		} else {
			if (list != null) {
				for (Iterator iterator = list.iterator(); iterator.hasNext(); k += this.fontRenderer.FONT_HEIGHT) {
					String s = (String)iterator.next();
					this.drawCenteredString(this.fontRenderer, s, this.width / 2, k, 16777215);
				}
			}
		}
		super.drawScreen(par1, par2, par3);
	}
}