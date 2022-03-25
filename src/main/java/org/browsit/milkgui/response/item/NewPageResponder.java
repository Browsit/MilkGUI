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

package org.browsit.milkgui.response.item;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.browsit.milkgui.MilkGUI;
import org.browsit.milkgui.gui.type.PaginatedGUI;
import org.browsit.milkgui.item.ItemSection;
import org.browsit.milkgui.response.Response;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NewPageResponder implements Response {
    
    PaginatedGUI gui;
    int page;
    
    public NewPageResponder(final PaginatedGUI gui) {
        this.gui = gui;
        this.page = gui.getGuiSettings().getCurrentPage();
    }

    @Override
    public void onClick(final InventoryClickEvent event) {
        final Collection<ItemSection> sections = gui.getSections();
        final int currentPage = gui.getGuiSettings().getCurrentPage();
        final int maxGUI = gui.getGui().getRows().getSlots() - 2;
        final int pageIndex = (currentPage-1) * maxGUI;
        
        final List<Integer> content = IntStream.range(pageIndex, pageIndex + maxGUI).boxed().collect(Collectors.toList());
        final int maxPage = (int)Math.ceil((double)sections.size() / content.size());
        
        if (event.getSlot() == maxGUI && page > 1) { //TODO adjust for smaller inventories
            page = page - 1;
        } else if (event.getSlot() == maxGUI+1 && page < maxPage) {
            page = page + 1;
        } else {
            event.setCancelled(true);
            return;
        }
                
        if (page >= 1 && page <= maxPage) {
            new BukkitRunnable() {
            
                @Override
                public void run() {
                    gui.getGuiSettings().setCurrentPage(page);
                    gui.draw();
                }
            }.runTaskLater(MilkGUI.INSTANCE.getInstance(), 1);
        }
    }
}
