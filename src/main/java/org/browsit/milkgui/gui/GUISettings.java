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
import java.util.List;

import org.browsit.milkgui.item.Item;
import org.browsit.milkgui.response.EnteredItemResponse;
import org.browsit.milkgui.response.NotEnterableItemResponse;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GUISettings {

    private boolean dragItems = false;
    private List<ItemStack> enterableItems = new ArrayList<>();
    private int currentPage = 1;
    private Item prevPage = new Item(Material.ARROW);
    private Item nextPage = new Item(Material.ARROW);
    private boolean flankedArrows = false;
    private NotEnterableItemResponse notEnterableItemResponse;
    private EnteredItemResponse enteredItemResponse;

    public void addEnterableItem(final Material material) {
        this.enterableItems.add(new ItemStack(material));
    }

    @SuppressWarnings("deprecation")
    public void addEnterableItem(final Material material, final short data) {
        this.enterableItems.add(new ItemStack(material, 1, data));
    }

    @SuppressWarnings("deprecation")
    public void addEnterableItem(final Material material, final int data) {
        this.enterableItems.add(new ItemStack(material, 1, (short) data));
    }
    
    public boolean canDragItems() {
        return dragItems;
    }

    public void setDragItems(final boolean dragItems) {
        this.dragItems = dragItems;
    }

    public boolean hasEnterableItems() {
        return !enterableItems.isEmpty();
    }
    
    public List<ItemStack> getEnterableItems() {
        return enterableItems;
    }
    
    public void setEnterableItems(final List<ItemStack> enterableItems) {
        this.enterableItems = enterableItems;
    }

    public NotEnterableItemResponse getNotEnterableItemResponse() {
        return notEnterableItemResponse;
    }

    public void setNotEnterableItemResponse(final NotEnterableItemResponse notEnterableItemResponse) {
        this.notEnterableItemResponse = notEnterableItemResponse;
    }

    public EnteredItemResponse getEnteredItemResponse() {
        return enteredItemResponse;
    }

    public void setEnteredItemResponse(final EnteredItemResponse enteredItemResponse) {
        this.enteredItemResponse = enteredItemResponse;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }
    
    public Item getPageArrowPrevious() {
        return prevPage;
    }
    
    public void setPageArrowPrevious(final Item prevPage) {
        this.prevPage = prevPage;
    }
    
    public Item getPageArrowNext() {
        return nextPage;
    }
    
    public void setPageArrowNext(final Item nextPage) {
        this.nextPage = nextPage;
    }

    public boolean hasFlankedArrows() {
        return flankedArrows;
    }

    public void setFlankedArrows(final boolean flankedArrows) {
        this.flankedArrows = flankedArrows;
    }
}
