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

package org.browsit.milkgui.gui.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.browsit.milkgui.response.item.NewPageResponder;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class PaginatedGUI extends GUIExtender implements ConfigurationSerializable {
    
    Collection<ItemSection> sections = new ConcurrentSkipListSet<ItemSection>();
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
        final List<ItemSection> sortedSections = new ArrayList<ItemSection>(sections);
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
        setItem(new ItemSection(maxGUI, getGuiSettings().getPageArrowPrevious(), "none", responder));
        setItem(new ItemSection(maxGUI + 1, getGuiSettings().getPageArrowNext(), "none", responder));
    }
}
