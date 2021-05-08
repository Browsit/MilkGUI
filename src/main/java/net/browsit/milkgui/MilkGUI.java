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

package net.browsit.milkgui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.browsit.milkgui.gui.GUIExtender;

public enum MilkGUI implements Listener {
    
    INSTANCE;

    private Plugin instance;

    private final Map<Integer, GUIExtender> guiMap = new HashMap<>();

    public Map<Integer, GUIExtender> getGUIs() {
        return guiMap;
    }

    public GUIExtender getGUI(final int id) {
        return guiMap.get(id);
    }

    public void register(final JavaPlugin instance) {
        final PluginManager pm = Bukkit.getPluginManager();
        Plugin plugin = null;
        for (final Plugin bukkitPlugin : pm.getPlugins()) {
            if (bukkitPlugin.equals(instance)) {
                plugin = bukkitPlugin;
                break;
            }
        }
        this.instance = plugin;
    }

    public Plugin getInstance() {
        return instance;
    }
}
