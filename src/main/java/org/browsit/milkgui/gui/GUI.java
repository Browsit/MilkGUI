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

package org.browsit.milkgui.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.browsit.milkgui.item.Item;
import org.browsit.milkgui.item.ItemSection;
import org.browsit.milkgui.util.ColorUtil;
import org.browsit.milkgui.util.Rows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GUI implements ConfigurationSerializable {

    private final UUID uniqueId = UUID.randomUUID();
    private Inventory inventory;
    private InventoryHolder holder;
    private Rows rows;
    private String title;
    private Map<Integer, String> tasks = new HashMap<Integer, String>();

    public GUI(final InventoryHolder holder, final String title) {
        this.holder = holder;
        this.rows = Rows.SIX;
        this.title = title;

        this.inventory = createInventory(holder, Rows.SIX, title);
    }

    public GUI(final InventoryHolder holder, final String title, final Rows rows) {
        this.holder = holder;
        this.rows = rows;
        this.title = title;

        this.inventory = createInventory(holder, rows, title);
    }

    public GUI(final String title, final Rows rows) {
        this.rows = rows;
        this.title = title;
        this.holder = new GUIHolder(this, Bukkit.createInventory(null, rows.getSlots()));

        this.inventory = createInventory(holder, rows, title);
    }

    public GUI(final String title) {
        this.rows = Rows.SIX;
        this.title = title;
        this.holder = new GUIHolder(this, Bukkit.createInventory(null, Rows.SIX.getSlots()));

        this.inventory = createInventory(holder, Rows.SIX, title);
    }
    
    @SuppressWarnings("unchecked")
    public GUI(final Map<String, Object> data) {
        this.rows = Rows.fromInt((int)data.get("rows"));
        this.holder = (InventoryHolder) data.get("holder");
        this.title = (String) data.get("title");
        this.inventory = createInventory(holder, rows, title);

        for (final Object o : ((ArrayList<?>) data.get("inventory"))) {
            setItem(new ItemSection((Map<String, Object>) o));
        }
    }

    public void openInventory(final Player player) {
        player.openInventory(this.inventory);
    }

    @SuppressWarnings("deprecation")
    private int getPosition(final Material material, final int data) {
        try {
            for (int i = 0; i < this.inventory.getContents().length; i++) {
                final ItemStack itemStack = this.inventory.getItem(i);

                if (itemStack.getType().equals(material)
                        && itemStack.getDurability() == data)
                    return i;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    @SuppressWarnings("deprecation")
    public int addItem(final Item itemBuilder) {
        try {
            this.inventory.addItem(itemBuilder.getItemStack());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return getPosition(itemBuilder.getItemStack().getType(), itemBuilder.getItemStack().getDurability());
    }
    
    public Item getItemFor(final int slot) {
        try {
            return new Item(this.inventory.getItem(slot));
        } catch (final Exception e) {
            return null;
        }
    }

    public void setItem(final int slot, final Item item) {
        try {
            this.inventory.setItem(slot, item.getItemStack());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setItem(final ItemSection section) {
        try {
            this.inventory.setItem(section.getSlot(), section.getItem().getItemStack());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        tasks.put(section.getSlot(), section.getTask());
    }

    public void removeItem(final int slot) {
        this.inventory.setItem(slot, new ItemStack(Material.AIR));
        tasks.remove(slot);
    }

    public static Inventory createInventory(final InventoryHolder holder, final Rows rows, final String title) {
        return Bukkit.createInventory(holder, rows.getSlots(), ColorUtil.fixColor(title));
    }

    public void setInventory(final Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return uniqueId.toString();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Inventory getInventory() {
        return inventory;
    }
    
    public InventoryHolder getHolder() {
        return holder;
    }
    
    public void setHolder(final InventoryHolder holder) {
        this.holder = holder;
    }

    public Rows getRows() {
        return rows;
    }
    
    public void setRows(final Rows rows) {
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }
    
    /**
     * Creates a duplicate inventory with new title.
     * @param title the new title
     */
    public void setTitle(final String title) {
        this.title = title;
        final ItemStack[] contents = inventory.getContents();
        this.inventory = createInventory(holder, rows, title);
        inventory.setContents(contents);
    }
    
    public Map<Integer, String> getTasks() {
        return tasks;
    }
    
    public void setTasks(final Map<Integer, String> tasks) {
        this.tasks = tasks;
    }
    
    public String getTaskFor(final int slot) {
        return tasks.get(slot);
    }

    public boolean hasTask(final int slot) {
        return tasks.containsKey(slot);
    }

    public void addTask(final int slot, final String task) {
        this.tasks.put(slot, task);
    }

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> data = new LinkedHashMap<>();
        data.put("title", getTitle());
        data.put("rows", Rows.toInt(rows));

        final List<ItemSection> itemSections = new ArrayList<>();
        for (int i = 0; i < getInventory().getContents().length; i++) {
            final ItemStack itemStack = getInventory().getContents()[i];
            if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
                continue;
            }
            itemSections.add(new ItemSection(i, new Item(itemStack), tasks.get(i)));
        }
        final List<Map<String, Object>> itemSectionsSerialized = new ArrayList<>();
        for (final ItemSection section : itemSections) {
            itemSectionsSerialized.add(section.serialize());
        }

        data.put("inventory", itemSectionsSerialized);
        return data;
    }
}
