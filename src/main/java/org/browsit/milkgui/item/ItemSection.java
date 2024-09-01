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

package org.browsit.milkgui.item;

import java.util.LinkedHashMap;
import java.util.Map;

import org.browsit.milkgui.response.Response;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class ItemSection implements ConfigurationSerializable, Comparable<ItemSection>{

    private int slot;
    private Item item;
    private String task;
    private Response response;

    public ItemSection(final int slot, final Item item) {
        this.slot = slot;
        this.item = item;
    }
    
    public ItemSection(final int slot, final Item item, final String task) {
        this.slot = slot;
        this.item = item;
        this.task = task;
    }
    
    public ItemSection(final int slot, final Item item, final String task, final Response response) {
        this.slot = slot;
        this.item = item;
        this.task = task;
        this.response = response;
    }

    @SuppressWarnings("unchecked")
    public ItemSection(final Map<String, Object> data) {
        this.slot = (int) data.get("slot");
        this.item = new Item((Map<String, Object>) data.get("item"));
        this.task = (String) data.get("task");
    }
    
    @SuppressWarnings("unchecked")
    public ItemSection(final Map<String, Object> data, final Response response) {
        this.slot = (int) data.get("slot");
        this.item = new Item((Map<String, Object>) data.get("item"));
        this.task = (String) data.get("task");
        this.response = response;
    }

    public void register() {
        ConfigurationSerialization.registerClass(this.getClass());
    }

    public int getSlot() {
        return slot;
    }
    
    public void setSlot(final int slot) {
        this.slot = slot;
    }

    public Item getItem() {
        return item;
    }
    
    public void setItem(final Item item) {
        this.item = item;
    }
    
    public String getTask() {
        return task;
    }
    
    public void setTask(final String task) {
        this.task = task;
    }
    
    public Response getResponse() {
        return response;
    }

    public void setResponse(final Response response) {
        this.response = response;
    }
    
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> data = new LinkedHashMap<>();
        data.put("slot", slot);
        data.put("item", item.serialize());
        data.put("task", task);
        return data;
    }
    
    @Override
    public int compareTo(final ItemSection o) {
        if (o == null) {
            return -1;
        }
        if (item != null && item.getItemMeta() != null && o.getItem().getItemMeta() != null &&
                !item.getItemMeta().getDisplayName().equals(o.getItem().getItemMeta().getDisplayName())) {
            return item.getItemMeta().getDisplayName().compareTo(o.getItem().getItemMeta().getDisplayName());
        }
        if (task != null && !task.equals(o.getTask())) {
            return task.compareTo(o.getTask());
        }
        if (response != null && !response.getName().equals(o.getResponse().getName())) {
            return response.getName().compareTo(o.getResponse().getName());
        }
        return Integer.compare(slot, o.getSlot());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ItemSection)) {
            return false;
        }
        final ItemSection is = (ItemSection) o;
        if (slot == is.getSlot()
            && item.getItemStack().getType().equals(is.getItem().getItemStack().getType())
            && task.equals(is.getTask())
            && response.getName().equals(is.getResponse().getName())) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + slot;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        result = prime * result + task.hashCode();
        result = prime * result + response.hashCode();
        return result;
    }
}
