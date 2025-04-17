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

package org.browsit.milkgui.item;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.browsit.milkgui.MilkGUI;
import org.browsit.milkgui.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item implements ConfigurationSerializable {

    private final ItemStack item;
    private ItemMeta meta;
    
    @SuppressWarnings("deprecation")
    public Item(final ItemBuilder builder) {
        item = new ItemStack(builder.material);
        item.setAmount(builder.amount);
        item.setDurability(builder.durability);
        if (item.getItemMeta() == null) {
            meta = Bukkit.getItemFactory().getItemMeta(builder.material);
        } else {
            meta = item.getItemMeta();
        }
        if (meta != null) {
            if (builder.displayName != null) {
                meta.setDisplayName(builder.displayName);
            }
            if (builder.lore != null && !builder.lore.isEmpty() && !builder.lore.get(0).equals("")) {
                meta.setLore(builder.lore);
            }
            if (builder.enchantments != null) {
                for (final ItemEnchantment ie : builder.enchantments) {
                    meta.addEnchant(ie.getEnchantment(), ie.getLevel(), ie.isUnsafe());
                }
            }
            this.addItemFlags(ItemFlag.values());
            if (builder.flags != null) {
                this.removeItemFlags(builder.flags);
            }
        }
        item.setItemMeta(meta);
    }
    
    public Item(final Material type) {
        item = new ItemStack(type);
        meta = item.getItemMeta();
        this.addItemFlags(ItemFlag.values());
    }
    
    public Item(final String type) {
        item = new ItemStack(getMaterialFromName(type));
        meta = item.getItemMeta();
        this.addItemFlags(ItemFlag.values());
    }
    
    public Item(final ItemStack itemStack) {
        item = itemStack;
        meta = item.getItemMeta();
        this.addItemFlags(ItemFlag.values());
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public Item(final Map<String, Object> data) {
        final String type = data.get("material").toString();
        final Material material = getMaterialFromName(type);
        final int amount = data.containsKey("amount") ? (int) data.get("amount") : 1;
        final short durability = data.containsKey("durability") ? Short.valueOf(data.get("durability").toString()) : 0;
        item = new ItemStack(material, amount, durability);

        if (item.getItemMeta() == null) {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        } else {
            meta = item.getItemMeta();
        }
        if (data.get("displayName") != null) {
            meta.setDisplayName(ColorUtil.fixColor(data.get("displayName").toString()));
        }
        if (data.get("lore") != null && data.get("lore") instanceof List) {
            final List<String> loreList = (List<String>) data.get("lore");
            if (!loreList.isEmpty() && !loreList.get(0).equals("")) {
                meta.setLore(ColorUtil.fixColor(loreList));
            }
        }
        if (data.get("enchants") != null && data.get("lore") instanceof List) {
            final List<String> enchantsList = (List<String>) data.get("enchants");
            final Map<Enchantment, Integer> enchants = new HashMap<>();

            for (final String enchant : enchantsList) {
                final String[] part = enchant.split(":");
                if (part.length < 1)
                    continue;

                final Enchantment ench = Enchantment.getByName(part[0]);
                if (ench == null)
                    continue;

                int level;
                try {
                    level = Integer.parseInt(part[1]);
                }
                catch (final NumberFormatException ex) {
                    continue;
                }
                enchants.put(ench, level);
            }
            
            for (final Entry<Enchantment, Integer> e : enchants.entrySet()) {
                meta.addEnchant(e.getKey(), e.getValue(), true);
            }
        }

        this.addItemFlags(ItemFlag.values());
        if (data.get("itemflags") != null) {
            this.removeItemFlags((ItemFlag[]) data.get("itemflags"));
        }
        item.setItemMeta(meta);
    }
    
    public Item setAmount(final int amount) {
        item.setAmount(amount);
        return this;
    }
    
    @SuppressWarnings("deprecation")
    public Item setDurability(final short durability) {
        item.setDurability(durability);
        return this;
    }

    public Item setDisplayName(final String displayName) {
        if (meta != null) {
            meta.setDisplayName(ColorUtil.fixColor(displayName));
            item.setItemMeta(meta);
        }
        return this;
    }

    public Item setLore(final List<String> lore) {
        if (meta != null && lore != null && !lore.isEmpty() && !lore.get(0).equals("")) {
            meta.setLore(ColorUtil.fixColor(lore));
            item.setItemMeta(meta);
        }
        return this;
    }

    public Item setLore(final String... lore) {
        if (meta != null && lore != null && !Arrays.asList(lore).isEmpty() && !lore[0].equals("")) {
            meta.setLore(Arrays.asList(ColorUtil.fixColor(lore)));
            item.setItemMeta(meta);
        }
        return this;
    }
    
    public Item removeLore() {
        if (meta != null) {
            meta.setLore(null);
            item.setItemMeta(meta);
        }
        return this;
    }
    
    public Item addEnchant(final Enchantment ench, final int level, final boolean ignoreLevelRequirement) {
        if (meta != null) {
            meta.addEnchant(ench, level, ignoreLevelRequirement);
            item.setItemMeta(meta);
        }
        return this;
    }
    
    public Item removeEnchant(final Enchantment ench) {
        if (meta != null) {
            meta.removeEnchant(ench);
            item.setItemMeta(meta);
        }
        return this;
    }

    @SuppressWarnings({"deprecation", "removal"})
    public Item addItemFlags(final ItemFlag...itemFlags) {
        if (meta != null) {
            // Hide flags by default then remove as specified
            if (Material.getMaterial("ARMADILLO_SCUTE") != null) {
                try {
                    // This is necessary to use flags as of 1.20.6
                    meta.addAttributeModifier(Attribute.valueOf("GENERIC_ATTACK_DAMAGE"),
                            new AttributeModifier("foo", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
                } catch (final Exception ignored) {
                    // This is necessary to use flags as of 1.21.2/3
                    meta.addAttributeModifier(Attribute.ATTACK_DAMAGE,
                            new AttributeModifier("foo", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
                }
            }
            meta.addItemFlags(itemFlags);
            item.setItemMeta(meta);
        }
        return this;
    }
    
    public Item removeItemFlags(final ItemFlag...itemFlags) {
        if (meta != null) {
            // Hide flags by default then remove as specified
            if (Material.getMaterial("ARMADILLO_SCUTE") != null) {
                try {
                    // This is necessary to use flags as of 1.20.6
                    final Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(Attribute.valueOf("GENERIC_ATTACK_DAMAGE"));
                    if (modifiers != null) {
                        for (final AttributeModifier modifier : modifiers) {
                            meta.removeAttributeModifier(Attribute.valueOf("GENERIC_ATTACK_DAMAGE"), modifier);
                        }
                    }
                } catch (final Exception ignored) {
                    // This is necessary to use flags as of 1.21.2/3
                    final Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(Attribute.ATTACK_DAMAGE);
                    if (modifiers != null) {
                        for (final AttributeModifier modifier : modifiers) {
                            meta.removeAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);
                        }
                    }
                }
            }
            meta.removeItemFlags(itemFlags);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack getItemStack() {
        return item;
    }

    public ItemMeta getItemMeta() {
        return meta;
    }
    
    private static Material getMaterialFromName(final String name) {
        Material material = Material.matchMaterial(name);
        if (material == null) {
            try {
                material = Material.matchMaterial(name, true);
            } catch (final Exception e) {
                Bukkit.getLogger().severe("[MilkGUI] Received unknown material name: " + name);
                return Material.ROTTEN_FLESH;
            }
        }
        return material;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public Map<String, Object> serialize() {
        final ItemStack item = getItemStack();
        final ItemMeta meta = getItemMeta();

        final Map<String, Object> data = new LinkedHashMap<>();
        data.put("material", item.getType().toString());
        data.put("amount", item.getAmount());
        data.put("durability", item.getDurability());
        data.put("displayName", meta.getDisplayName() == null ? null
                : ColorUtil.fixColor(meta.getDisplayName()));
        data.put("lore", meta.getLore());
        data.put("enchants", meta.getEnchants()
                .keySet()
                .stream()
                .map(enchant -> enchant.getName() + ":" + meta.getEnchantLevel(enchant))
                .collect(Collectors.toList()));
        return data;
    }
    
    public static class ItemBuilder {
        private final Material material;
        private int amount = 1;
        private short durability;
        private String displayName;
        private List<String> lore;
        private List<ItemEnchantment> enchantments;
        private ItemFlag[] flags;
 
        public ItemBuilder(final Material material) {
            this.material = material;
        }
        public ItemBuilder(final String material) {
            this.material = getMaterialFromName(material);
        }
        public ItemBuilder amount(final int amount) {
            if (amount > 0 && amount < 65) {
                this.amount = amount;
            }
            return this;
        }
        public ItemBuilder durability(final int durability) {
            this.durability = (short)durability;
            return this;
        }
        public ItemBuilder durability(final short durability) {
            this.durability = durability;
            return this;
        }
        public ItemBuilder displayName(final String displayName) {
            this.displayName = displayName;
            return this;
        }
        public ItemBuilder lore(final List<String> lore) {
            if (!lore.isEmpty() && !lore.toString().equals("[]")) {
                this.lore = ColorUtil.fixColor(lore);
            }
            return this;
        }
        public ItemBuilder lore(final String...lore) {
            if (!Arrays.asList(lore).isEmpty() && !lore.toString().equals("[]")) {
                this.lore = Arrays.asList(ColorUtil.fixColor(lore));
            }
            return this;
        }
        public ItemBuilder enchants(final List<ItemEnchantment> enchantments) {
            this.enchantments = enchantments;
            return this;
        }
        public ItemBuilder removeFlags(final List<ItemFlag> flags) {
            this.flags = (ItemFlag[]) flags.toArray();
            return this;
        }
        public ItemBuilder removeFlags(final ItemFlag...flags) {
            this.flags = flags;
            return this;
        }
        public Item build() {
            return new Item(this);
        }
    }
}
