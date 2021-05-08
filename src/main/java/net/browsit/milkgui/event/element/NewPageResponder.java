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

package net.browsit.milkgui.event.element;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.browsit.milkgui.MilkGUI;
import net.browsit.milkgui.event.ElementResponse;
import net.browsit.milkgui.gui.type.PaginatedGUI;
import net.browsit.milkgui.item.ItemSection;

public class NewPageResponder implements ElementResponse {
    
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
