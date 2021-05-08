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

package net.browsit.milkgui.item;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import net.browsit.milkgui.event.ElementResponse;

public class ItemSection implements ConfigurationSerializable, Comparable<ItemSection>{

    private int slot;
    private Item item;
    private String task;
    private ElementResponse elementResponse;

    public ItemSection(final int slot, final Item item) {
        this.slot = slot;
        this.item = item;
    }
    
    public ItemSection(final int slot, final Item item, final String task) {
        this.slot = slot;
        this.item = item;
        this.task = task;
    }
    
    public ItemSection(final int slot, final Item item, final String task, final ElementResponse elementResponse) {
        this.slot = slot;
        this.item = item;
        this.task = task;
        this.elementResponse = elementResponse;
    }

    @SuppressWarnings("unchecked")
    public ItemSection(final Map<String, Object> data) {
        this.slot = (int) data.get("slot");
        this.item = new Item((Map<String, Object>) data.get("item"));
        this.task = (String) data.get("task");
    }
    
    @SuppressWarnings("unchecked")
    public ItemSection(final Map<String, Object> data, final ElementResponse elementResponse) {
        this.slot = (int) data.get("slot");
        this.item = new Item((Map<String, Object>) data.get("item"));
        this.task = (String) data.get("task");
        this.elementResponse = elementResponse;
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
    
    public ElementResponse getElementResponse() {
        return elementResponse;
    }

    public void setElementResponse(final ElementResponse elementResponse) {
        this.elementResponse = elementResponse;
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
        if (item != null && !item.getItemMeta().getDisplayName().equals(o.getItem().getItemMeta().getDisplayName())) {
            return item.getItemMeta().getDisplayName().compareTo(o.getItem().getItemMeta().getDisplayName());
        }
        if (task != null && !task.equals(o.getTask())) {
            return task.compareTo(o.getTask());
        }
        if (elementResponse != null && !elementResponse.getName().equals(o.getElementResponse().getName())) {
            return elementResponse.getName().compareTo(o.getElementResponse().getName());
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
            && elementResponse.getName().equals(is.getElementResponse().getName())) {
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
        result = prime * result + elementResponse.hashCode();
        return result;
    }
}
