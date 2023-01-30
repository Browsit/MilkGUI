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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.browsit.milkgui.gui.GUI;
import org.browsit.milkgui.gui.GUIExtender;
import org.browsit.milkgui.item.Item;
import org.browsit.milkgui.item.ItemSection;
import org.browsit.milkgui.response.Response;
import org.browsit.milkgui.response.item.NewPageResponder;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class PaginatedGUI extends GUIExtender implements ConfigurationSerializable {
    
    Collection<ItemSection> sections = new ConcurrentSkipListSet<>();
    NewPageResponder responder = new NewPageResponder(this);
    
    public PaginatedGUI(final GUI gui) {
        super(gui);
        
        nameArrows();
    }
    
    @SuppressWarnings("unchecked")
    public PaginatedGUI(final Map<String, Object> data) {
        super(new GUI((Map<String, Object>)data.get("gui")));
        
        nameArrows();
        
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
    
    public Collection<ItemSection> getSections() {
        return sections;
    }
    
    public void setSections(final Collection<ItemSection> sections) {
        this.sections = sections;
    }
    
    private void nameArrows() {
        final Item prevPage = getGuiSettings().getPageArrowPrevious();
        prevPage.setDisplayName("< Prev");
        getGuiSettings().setPageArrowPrevious(prevPage);
        
        final Item nextPage = getGuiSettings().getPageArrowNext();
        nextPage.setDisplayName("Next >");
        getGuiSettings().setPageArrowNext(nextPage);
    }
    
    public void draw() {
        final int currentPage = getGuiSettings().getCurrentPage();
        final int maxGUI = getGui().getRows().getSlots() - 2;
        final int pageIndex = (currentPage-1) * maxGUI;
        
        final List<Integer> content = IntStream.range(pageIndex, pageIndex + maxGUI).boxed().collect(Collectors.toList());
        final List<ItemSection> sortedSections = new ArrayList<>(sections);
        Collections.sort(sortedSections);
        
        // Clear items for new content
        for (int i = 0; i < maxGUI; i++) {
            this.removeItem(i);
        }
        this.clearElements();
        
        // Set new content
        int i = 0;
        for (final ItemSection sortedSection : sortedSections) {
            if (content.contains(sortedSection.getSlot())) {
                final ItemSection section = new ItemSection(i, sortedSection.getItem(), "none", sortedSection.getResponse());
                setItem(section);
                i++;
            }
        }
        
        // Set page arrows
        removeItem(maxGUI);
        removeItem(maxGUI + 1);
        setItem(new ItemSection(maxGUI, getGuiSettings().getPageArrowPrevious(), "none", responder));
        setItem(new ItemSection(maxGUI + 1, getGuiSettings().getPageArrowNext(), "none", responder));
    }

    @Override
    public void setItem(final ItemSection... itemSections) {
        for (final ItemSection section : itemSections) {
            final int slot = section.getSlot();
            final Item item = section.getItem();

            if (section.getResponse() != null) {
                addResponse(slot, section.getResponse());
            } else {
                addEmptyResponse(slot);
            }

            removeItem(slot);
            ItemSection newSection = new ItemSection(slot, item);
            sections.add(newSection);
            if (section.getTask() != null) {
                getGui().addTask(slot, section.getTask());
            }
            updateInventory();
        }
    }

    @Override
    public void setItem(final int slot, final Item item) {
        removeItem(slot);
        ItemSection newSection = new ItemSection(slot, item);
        sections.add(newSection);
        addEmptyResponse(slot);
        updateInventory();
    }

    @Override
    public void setItem(final ItemSection section) {
        int slot = section.getSlot();
        Item item = section.getItem();
        removeItem(slot);
        ItemSection newSection = new ItemSection(slot, item);
        sections.add(newSection);
        if (section.getTask() != null) {
            getGui().addTask(section.getSlot(), section.getTask());
        }
        if (section.getResponse() != null) {
            addResponse(section.getSlot(), section.getResponse());
        } else {
            addEmptyResponse(section.getSlot());
        }
        updateInventory();
    }

    @Override
    public int addItem(final Item item) {
        final int index = sections.size();
        sections.add(new ItemSection(index, item));
        addEmptyResponse(index);
        updateInventory();
        return index;
    }

    @Override
    public int addItem(final Item item, final Response response) {
        final int index = sections.size();
        sections.add(new ItemSection(index, item));
        addResponse(index, response);
        updateInventory();
        return index;
    }

    @Override
    public void removeItem(final int slot) {
        sections.removeIf(oldSection -> oldSection.getSlot() == slot);
    }
}
