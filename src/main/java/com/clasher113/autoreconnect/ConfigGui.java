package com.clasher113.autoreconnect;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class ConfigGui extends GuiConfig {
	
    public ConfigGui(GuiScreen parent) {
        super(parent, new ConfigElement(MainClass.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                "autoreconnect", false, false, ConfigGui.getAbridgedConfigPath(MainClass.configFile.toString()));
    }
}