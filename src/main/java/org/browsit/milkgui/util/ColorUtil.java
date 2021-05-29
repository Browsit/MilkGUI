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

package org.browsit.milkgui.util;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

public class ColorUtil {

    public static String fixColor(final String str) {
        if (str == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static List<String> fixColor(final List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        return stringList.stream().map(ColorUtil::fixColor)
                .collect(Collectors.toList());
    }

    public static String[] fixColor(final String[] strings) {
        if (strings == null) {
            return null;
        }
        final String[] stringArray = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            stringArray[i] = fixColor(strings[i]);
        }
        return stringArray;
    }
}
