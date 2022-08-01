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

package org.browsit.milkgui.gui.type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.browsit.milkgui.MilkGUI;
import org.browsit.milkgui.gui.GUI;
import org.browsit.milkgui.gui.GUIExtender;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

public class TextGUI extends GUIExtender implements ConfigurationSerializable {
    
    private final MilkGUI plugin = MilkGUI.INSTANCE;
    private Builder builder;
    private Player player;
    private String response;
    
    public TextGUI(final GUI gui) {
        super(gui);
    }
    
    @SuppressWarnings("unchecked")
    public TextGUI(final Map<String, Object> data) {
        super(new GUI((Map<String, Object>)data.get("gui")));
        
        if (data.containsKey("dragItems")) {
            getGuiSettings().setDragItems((Boolean) data.get("dragItems"));
        }

        if (data.containsKey("enterableItems")) {
            final List<String> enterableMaterials = (List<String>) data.get("enterableItems");
            for (final String mat : enterableMaterials) {
                getGuiSettings().addEnterableItem(Material.matchMaterial(mat));
            }
        }
    }
    
    @Override
    public void onOpen(final InventoryOpenEvent event) {
    }

    @Override
    public void onClose(final InventoryCloseEvent event) {
    }

    public void register() {
        ConfigurationSerialization.registerClass(this.getClass());
    }

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> data = new LinkedHashMap<>();
        data.put("dragItems", getGuiSettings().canDragItems());
        final List<String> enterableItems = new ArrayList<>();
        for (final ItemStack itemStack : getGuiSettings().getEnterableItems())
            enterableItems.add(itemStack.getType().name());
        data.put("enterableItems", enterableItems);
        data.put("gui", getGui().serialize());
        return data;
    }
    
    public void build(final BukkitRunnable runnable, final boolean preventClose) {
        final GUI gui = getGui();
        builder = new AnvilGUI.Builder()
        .onClose(p -> {                   // called when the inventory is closing
        })
        .onComplete((p, response) -> {    // called when the inventory output slot is clicked
            this.player = p;
            this.response = response;
            runnable.runTaskLater(plugin.getInstance(), 2);
            return AnvilGUI.Response.close();
        })
        .text(isBelow114() ? gui.getTitle() : " ")     // sets the text the GUI should start with
        .itemLeft(gui.getInventory().getItem(0))        // use a custom item for the first slot
        //.itemRight(gui.getInventory().getItem(1))     // use a custom item for the last slot
        .title(isBelow114() ? "" : gui.getTitle())     // set the title of the GUI (only works in 1.14+)
        .plugin(plugin.getInstance());          // set the plugin instance
        
        if (preventClose) {
            builder.preventClose();      // prevents the inventory from being closed
        }
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public String getOutput() {
        return response;
    }
    
    @Override
    public void openInventory(final Player player) {
        if (builder != null && player != null) {
            player.closeInventory();
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    builder.open(player);
                }
            }.runTaskLater(plugin.getInstance(), 2);
        }
    }
    
    private boolean isBelow114() {
        return plugin.getBukkitVersion().startsWith("1.13") || plugin.isBelow113();
    }
}
