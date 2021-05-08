/*******************************************************************************************************
 * Copyright (c) 2021 Browsit, LLC. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************************************/

package net.browsit.milkgui.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.browsit.milkgui.event.EnteredItemResponse;
import net.browsit.milkgui.event.NotEnterableItemResponse;
import net.browsit.milkgui.item.Item;

public class GUISettings {

    private boolean dragItems = false;
    private List<ItemStack> enterableItems = new ArrayList<>();
    private int currentPage = 1;
    private Item prevPage = new Item(Material.ARROW);
    private Item nextPage = new Item(Material.ARROW);
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
}
