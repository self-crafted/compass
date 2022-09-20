/*
 * Copyright 2022 compass contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.selfcrafted.compass;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MenuInventory extends Inventory {
    public MenuInventory(@NotNull Component title, InventoryType type, Map<ItemStack, String> servers, List<ItemStack> buttonList, String layout) {
        super(type, title);

        addInventoryCondition((player, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);

            var item = inventoryConditionResult.getClickedItem();
            var server = servers.get(item);
            if (server != null)
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    DataOutputStream msg = new DataOutputStream(out);
                    msg.writeUTF("Connect");
                    msg.writeUTF(server);
                    player.sendPluginMessage("bungeecord:main", out.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });

        int slotIndex = -1;
        for (String slot : layout.split(" ")) {
            slotIndex++;
            if (slot.equals("#")) continue;
            if (slot.equals("---")) {
                slotIndex += 8;
                continue;
            }
            try {
                int index = Integer.parseInt(slot);
                var server = buttonList.get(index-1);
                setItemStack(slotIndex, server);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
