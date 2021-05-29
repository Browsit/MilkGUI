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

package org.browsit.milkgui.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.browsit.milkgui.MilkGUI;
import org.browsit.milkgui.item.Item;
import org.browsit.milkgui.item.ItemSection;
import org.browsit.milkgui.response.Response;
import org.browsit.milkgui.response.WindowResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class GUIExtender implements Listener, WindowResponse {

    private final MilkGUI plugin = MilkGUI.INSTANCE;
    private final int id;
    private GUI gui;
    private WindowResponse windowResponse;
    private Map<Integer, Response> responses = new HashMap<Integer, Response>();
    private GUISettings guiSettings = new GUISettings();

    public GUIExtender(final GUI gui) {
        this.gui = gui;
        this.id = plugin.getGUIs().size();
        plugin.getGUIs().put(id, this);

        Bukkit.getPluginManager().registerEvents(this, plugin.getInstance());
    }
    
    public int getId() {
        return id;
    }
    
    public GUI getGui() {
        return gui;
    }
    
    public void setGUI(final GUI gui) {
        this.gui = gui;
        updateInventory();
    }
    
    public WindowResponse getWindowResponse() {
        return windowResponse;
    }
    
    public void setWindowResponse(final WindowResponse windowResponse) {
        this.windowResponse = windowResponse;
    }
    
    public Map<Integer, Response> getResponses() {
        return responses;
    }
    
    public void setResponses(final Map<Integer, Response> responses) {
        this.responses = responses;
    }
    
    public Response getResponseFor(final int slot) {
        return responses.get(slot);
    }

    public boolean hasResponse(final int slot) {
        return responses.containsKey(slot);
    }

    public void addResponse(final int slot, final Response response) {
        this.responses.put(slot, response);
    }
    
    public void addEmptyResponse(final int slot) {
        this.responses.put(slot, null);
    }
    
    public void clearElements() {
        responses.clear();
    }
    
    public GUISettings getGuiSettings() {
        return guiSettings;
    }
    
    public void setGuiSettings(final GUISettings guiSettings) {
        this.guiSettings = guiSettings;
    }
    
    private Inventory getBukkitInventory() {
        return gui.getInventory();
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if (event.getView().getTopInventory().equals(getBukkitInventory())
                && !guiSettings.canDragItems()) {
            event.setCancelled(true);
            return;
        }
        if (guiSettings.canDragItems() &&
                canEnter(event.getCursor())) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getView() == null
                || event.getView().getTopInventory() == null
                || event.getView().getBottomInventory() == null
                || event.getClickedInventory() == null) {
            return;
        }
        if (guiSettings.hasEnterableItems()) {
            if (!event.isShiftClick()) {
                if (event.getView().getTopInventory().equals(getBukkitInventory())
                        && event.getClickedInventory().equals(getBukkitInventory())
                        && event.getCursor() != null) {
                    if (canEnter(event.getCursor())) {
                        if (guiSettings.getEnteredItemResponse() != null) {
                            guiSettings.getEnteredItemResponse().event(event);
                        }
                        event.setCancelled(false);
                    } else {
                        if (guiSettings.getNotEnterableItemResponse() != null) {
                            guiSettings.getNotEnterableItemResponse().event(event);
                        }
                        event.setCancelled(true);
                    }
                    return;
                }
            } else {
                if (event.getView().getTopInventory().equals(getBukkitInventory())
                        && !event.getClickedInventory().equals(getBukkitInventory())
                        && event.getCurrentItem() != null) {
                    if (canEnter(event.getCurrentItem())) {
                        if (guiSettings.getEnteredItemResponse() != null) {
                            guiSettings.getEnteredItemResponse().event(event);
                        }
                        event.setCancelled(false);
                    } else {
                        if (guiSettings.getNotEnterableItemResponse() != null) {
                            guiSettings.getNotEnterableItemResponse().event(event);
                        }
                        event.setCancelled(true);
                    }
                    return;
                }
            }
        }

        if (event.getView().getTopInventory().equals(getBukkitInventory())
                && !guiSettings.hasEnterableItems()) {
            if (event.isShiftClick() &&
                    !event.getClickedInventory().equals(getBukkitInventory())) {
                event.setCancelled(true);
                return;
            } else if (!event.isShiftClick() &&
                    event.getClickedInventory().equals(getBukkitInventory())
                    && (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))) {
                event.setCancelled(true);
                return;
            } else if (!event.isShiftClick() &&
                    event.getClickedInventory().equals(getBukkitInventory())) {
                checkElements(event);
                return;
            } else if (event.isShiftClick() &&
                    event.getClickedInventory().equals(getBukkitInventory())) {
                checkElements(event);
                return;
            }
            event.setCancelled(false);
            return;
        }
        checkElements(event);
    }

    @SuppressWarnings("deprecation")
    private boolean canEnter(final ItemStack itemStack) {
        if (guiSettings.hasEnterableItems()) {
            final List<ItemStack> materials = guiSettings.getEnterableItems();
            
            if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
                return true;
            }
            
            for (final ItemStack entry : materials) {
                final Material material = entry.getType();
                final short data = entry.getDurability();

                if (itemStack.getType().equals(material)
                        && itemStack.getDurability() == data) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onInventoryOpen(final InventoryOpenEvent e) {
        if (e.getView().getTopInventory().equals(getBukkitInventory())
                || e.getView().getType().equals(InventoryType.ANVIL)) {
            if (windowResponse != null) {
                windowResponse.onOpen(e);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (e.getView().getTopInventory().equals(getBukkitInventory())
                || e.getView().getType().equals(InventoryType.ANVIL)) {
            if (windowResponse != null) {
                windowResponse.onClose(e);
            }

            Bukkit.getScheduler().runTaskLater(plugin.getInstance(), () ->
                    ((Player) e.getPlayer()).updateInventory(), 5);
        }
    }

    public void setItem(final ItemSection... itemSections) {
        for (final ItemSection section : itemSections) {
            final int slot = section.getSlot();
            final Item item = section.getItem();

            if (section.getResponse() != null) {
                addResponse(slot, section.getResponse());
            } else {
                addEmptyResponse(slot);
            }

            gui.setItem(slot, item);
            if (section.getTask() != null) {
                gui.addTask(slot, section.getTask());
            }
            updateInventory();
        }
    }
    
    public void setItem(final int slot, final Item item) {
        gui.setItem(slot, item);
        addEmptyResponse(slot);
        updateInventory();
    }
    
    public void setItem(final ItemSection section) {
        gui.setItem(section.getSlot(), section.getItem());
        if (section.getTask() != null) {
            gui.addTask(section.getSlot(), section.getTask());
        }
        if (section.getResponse() != null) {
            addResponse(section.getSlot(), section.getResponse());
        } else {
            addEmptyResponse(section.getSlot());
        }
        updateInventory();
    }

    public int addItem(final Item item) {
        final int index = gui.addItem(item);
        addEmptyResponse(index);
        updateInventory();
        return index;
    }

    public int addItem(final Item item, final Response response) {
        final int index = gui.addItem(item);
        addResponse(index, response);
        updateInventory();
        return index;
    }

    public void removeItem(final int slot) {
        gui.removeItem(slot);
    }

    public void openInventory(final Player player) {
        Bukkit.getScheduler().runTaskLater(plugin.getInstance(), () ->
        player.openInventory(getBukkitInventory()), 1);
    }

    @SuppressWarnings("deprecation")
    public void updateInventory() {
        final List<Integer> slots = new ArrayList<>();
        int temp = 0;
        for (final ItemStack itemStack : getBukkitInventory().getContents()) {
            temp++;
            if (itemStack == null || itemStack.getType().equals(Material.AIR))
                continue;

            final int current = temp - 1;
            slots.add(current);
        }

        getBukkitInventory().getViewers().forEach(viewer -> ((Player)viewer).updateInventory());
    }

    private void checkElements(final InventoryClickEvent event) {
        for (final Entry<Integer, Response> entry : responses.entrySet()) {
            final int slot = entry.getKey();

            if (slot != event.getSlot())
                continue;
            if (!event.getClickedInventory().equals(getBukkitInventory()))
                continue;
            if (!event.getView().getTopInventory().equals(getBukkitInventory()))
                continue;

            if (entry.getValue() != null) {
                entry.getValue().onClick(event);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
