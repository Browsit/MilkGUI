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

import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.browsit.milkgui.event.ElementResponse;

public class ConversationInputResponder implements ElementResponse {
    
    Conversable conversable;
    String input;
    boolean closeInventory = false;
    
    public ConversationInputResponder(final Conversable conversable, final String input) {
        this.conversable = conversable;
        this.input = input;
    }
    
    public ConversationInputResponder(final Conversable conversable, final String input, final boolean closeInventory) {
        this.conversable = conversable;
        this.input = input;
        this.closeInventory = closeInventory;
    }

    @Override
    public void onClick(final InventoryClickEvent event) {
        conversable.acceptConversationInput(input);
        if (closeInventory && conversable instanceof Player) {
            ((Player)conversable).closeInventory();
        }
    }
}
