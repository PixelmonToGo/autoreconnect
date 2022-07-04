package com.clasher113.autoreconnect;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGui extends GuiConfig {
	
    public ConfigGui(GuiScreen parent) {
        super(parent, new ConfigElement(MainClass.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                "autoreconnect", false, false, ConfigGui.getAbridgedConfigPath(MainClass.configFile.toString()));
    }
}