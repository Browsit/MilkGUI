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

package net.browsit.milkgui.response.item;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.browsit.milkgui.response.Response;

public class CommandResponder implements Response {
    
    CommandSender sender;
    String commandLine;
    boolean closeInventory = false;
    
    public CommandResponder(final CommandSender sender, final String commandLine) {
        this.sender = sender;
        this.commandLine = commandLine;
    }
    
    public CommandResponder(final CommandSender sender, final String commandLine, final boolean closeInventory) {
        this.sender = sender;
        this.commandLine = commandLine;
        this.closeInventory = closeInventory;
    }

    @Override
    public void onClick(final InventoryClickEvent event) {
        Bukkit.getServer().dispatchCommand(sender, commandLine);
        if (closeInventory && sender instanceof Player) {
            ((Player)sender).closeInventory();
        }
    }
}
