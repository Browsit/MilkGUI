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

import org.browsit.milkgui.response.Response;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConversationInputResponder implements Response {
    
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
