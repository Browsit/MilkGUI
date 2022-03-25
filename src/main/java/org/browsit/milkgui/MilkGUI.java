/*
 * This file is part of MilkGUI, licensed under the MIT License.
 *
 * Copyright (c) 2021 Browsit, LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.browsit.milkgui;

import java.util.HashMap;
import java.util.Map;

import org.browsit.milkgui.gui.GUIExtender;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public enum MilkGUI implements Listener {
    
    INSTANCE;

    private Plugin instance;
    private final String bukkitVersion = Bukkit.getServer().getBukkitVersion().split("-")[0];
    private final Map<Integer, GUIExtender> guiMap = new HashMap<>();
    
    public Plugin getInstance() {
        return instance;
    }
    
    public String getBukkitVersion() {
        return bukkitVersion;
    }

    public Map<Integer, GUIExtender> getGUIs() {
        return guiMap;
    }

    public GUIExtender getGUI(final int id) {
        return guiMap.get(id);
    }

    public void register(final JavaPlugin instance) {
        final PluginManager pm = Bukkit.getPluginManager();
        Plugin plugin = null;
        for (final Plugin bukkitPlugin : pm.getPlugins()) {
            if (bukkitPlugin.equals(instance)) {
                plugin = bukkitPlugin;
                break;
            }
        }
        this.instance = plugin;
    }
    
    public boolean isBelow113() {
        if (bukkitVersion.matches("^[0-9.]+$")) {
            switch(bukkitVersion) {
            case "1.12.2" :
            case "1.12.1" :
            case "1.12" :
            case "1.11.2" :
            case "1.11.1" :
            case "1.11" :
            case "1.10.2" :
            case "1.10.1" :
            case "1.10" :
            case "1.9.4" :
            case "1.9.3" :
            case "1.9.2" :
            case "1.9.1" :
            case "1.9" :
            case "1.8.9" :
            case "1.8.8" :
            case "1.8.7" :
            case "1.8.6" :
            case "1.8.5" :
            case "1.8.4" :
            case "1.8.3" :
            case "1.8.2" :
            case "1.8.1" :
            case "1.8" :
            case "1.7.10" :
            case "1.7.9" :
            case "1.7.2" :
                return true;
            default:
                // Bukkit version is 1.13+ or unsupported
                return false;
            }
        }
        Bukkit.getLogger().severe("MilkGUI received invalid Bukkit version " + bukkitVersion);
        return false;
    }
}
