package com.clasher113.autoreconnect;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGuiFactory
  implements IModGuiFactory
{
  public void initialize(Minecraft minecraftInstance) {}
  
  public Class<? extends GuiScreen> mainConfigGuiClass() {
    return (Class)ConfigGui.class;
  }

  
  public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return null;
  }
  
  public static class ConfigGui
    extends GuiConfig {
    public ConfigGui(GuiScreen parent) {
      super(parent, (new ConfigElement(MainClass.configFile.getCategory("general"))).getChildElements(), "autoreconnect", false, false, 
          getAbridgedConfigPath(MainClass.configFile.toString()));
    }
  }

  
  public boolean hasConfigGui() {
    return true;
  }

  
  public GuiScreen createConfigGui(GuiScreen parentScreen) {
    return (GuiScreen)new ConfigGui(parentScreen);
  }
}
