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

package org.browsit.milkgui.util;

import org.bukkit.attribute.AttributeModifier;

import java.lang.reflect.Constructor;
import java.util.UUID;

public final class AttributeUtil {

    private AttributeUtil() {}

    public static AttributeModifier create(final String name, final double amount,
                                           final AttributeModifier.Operation operation) {
        for (final Constructor<?> constructor : AttributeModifier.class.getConstructors()) {
            try {
                final Class<?>[] params = constructor.getParameterTypes();

                // (UUID, String, double, Operation, EquipmentSlotGroup) [newer Paper]
                if (params.length == 5
                        && params[0] == UUID.class
                        && params[1] == String.class
                        && params[2] == double.class
                        && params[3] == AttributeModifier.Operation.class) {

                    final Object slotGroup = getDefaultSlotGroup(params[4]);
                    return (AttributeModifier) constructor.newInstance(
                            UUID.randomUUID(), name, amount, operation, slotGroup
                    );
                }

                // (UUID, String, double, Operation) [1.20.6+]
                if (params.length == 4 && params[0] == UUID.class && params[1] == String.class
                        && params[2] == double.class && params[3] == AttributeModifier.Operation.class) {

                    return (AttributeModifier) constructor.newInstance(
                            UUID.randomUUID(), name, amount, operation
                    );
                }

                // (String, double, Operation) [old]
                if (params.length == 3 && params[0] == String.class && params[1] == double.class
                        && params[2] == AttributeModifier.Operation.class) {

                    return (AttributeModifier) constructor.newInstance(
                            name, amount, operation
                    );
                }

            } catch (final Throwable ignored) {}
        }

        throw new IllegalStateException("No compatible AttributeModifier constructor found");
    }

    private static Object getDefaultSlotGroup(Class<?> slotGroupClass) {
        try {
            // Try common constant names
            for (String constant : new String[]{"HAND", "MAINHAND", "ANY"}) {
                try {
                    return Enum.valueOf((Class<Enum>) slotGroupClass.asSubclass(Enum.class), constant);
                } catch (IllegalArgumentException ignored) {}
            }

            // Fallback: first enum constant
            final Object[] constants = slotGroupClass.getEnumConstants();
            if (constants != null && constants.length > 0) {
                return constants[0];
            }

        } catch (final Throwable ignored) {}

        return null;
    }
}