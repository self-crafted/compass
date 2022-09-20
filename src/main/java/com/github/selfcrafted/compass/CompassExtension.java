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

import com.electronwill.toml.Toml;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntitySpawnEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CompassExtension extends Extension {

    private final MiniMessage minimsg = MiniMessage.miniMessage();
    private final File configFile = MinecraftServer.getExtensionManager().getExtensionDataRoot().resolve("compass.toml").toFile();
    private Set<Instance> instances = new HashSet<>();

    @Override
    public void initialize() {
        MinecraftServer.LOGGER.info("Compass start.");

        // Default values
        Material openerMaterial;
        Component openerName;
        Component openerLore;

        Component menuTitle;
        Map<ItemStack, String> serverButtons;
        List<ItemStack> buttonsList;
        String menuLayout;
        InventoryType menuSize;

        // Read config
        try {
            if (!configFile.exists()) {
                Map<String, Object> defaults = Map.of(
                        "opener.material", Material.COMPASS.namespace().asString(),
                        "opener.name", minimsg.serialize(Component.translatable("key.keyboard.menu")),
                        "opener.lore", minimsg.serialize(Component.text("Some Lore")
                                .append(Component.newline()).append(Component.text("second Line"))),

                        "menu.title", minimsg.serialize(Component.translatable("key.keyboard.menu")),
                        "menu.servers", List.of(
                                Map.of(
                                        "server", "lobby",
                                        "item.material", Material.ACACIA_SAPLING.namespace().asString(),
                                        "item.name", minimsg.serialize(Component.text("Lobby", NamedTextColor.GOLD)),
                                        "item.lore", minimsg.serialize(Component.text("This is the lobby.")
                                                .append(Component.newline())
                                                .append(Component.text("A nice place to wait for your friends...")))
                                )
                        ),
                        "menu.layout", "# # # # 1",
                        "menu.size", 6
                );
                Toml.write(defaults, configFile);
            }
            Map<String, Object> config = Toml.read(configFile);

            openerMaterial = Material.fromNamespaceId(config.get("opener.material").toString());
            if (openerMaterial == null) openerMaterial = Material.COMPASS;
            openerName = minimsg.deserialize(config.get("opener.name").toString());
            openerLore = minimsg.deserialize(config.get("opener.lore").toString());

            menuTitle = minimsg.deserialize(config.get("menu.title").toString());
            serverButtons = new HashMap<>();
            buttonsList = new ArrayList<>();
            for (Map<String, Object> server : (List<Map<String, Object>>) config.get("menu.servers")) {
                var target = server.get("server").toString();
                var material = Material.fromNamespaceId(server.get("item.material").toString());
                if (material == null) continue;
                var name = minimsg.deserialize(server.get("item.name").toString());
                var lore = minimsg.deserialize(server.get("item.lore").toString());
                var button = ItemStack.of(material).withDisplayName(name).withLore(List.of(lore));
                serverButtons.put(button, target);
                buttonsList.add(button);
            }
            menuLayout = config.get("menu.layout").toString();
            menuSize = switch (Integer.parseInt(config.get("menu.size").toString())) {
                case 1 -> InventoryType.CHEST_1_ROW;
                case 2 -> InventoryType.CHEST_2_ROW;
                case 3 -> InventoryType.CHEST_3_ROW;
                case 4 -> InventoryType.CHEST_4_ROW;
                case 5 -> InventoryType.CHEST_5_ROW;
                default -> InventoryType.CHEST_6_ROW;
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Prepare menu and menu opener
        final ItemStack compassItem = ItemStack.of(openerMaterial)
                .withDisplayName(openerName)
                .withLore(List.of(openerLore));
        final MenuInventory menuInventory = new MenuInventory(menuTitle, menuSize, serverButtons, buttonsList, menuLayout);

        EventNode<InstanceEvent> node = EventNode.event("compass-instances", EventFilter.INSTANCE,
                instanceEvent -> instances.contains(instanceEvent.getInstance()));

        node.addListener(EntitySpawnEvent.class, event -> {
            if (event.getEntity() instanceof Player player)
                player.getInventory().addItemStack(compassItem);
        });

        node.addListener(PlayerUseItemEvent.class, event -> {
            if (event.getItemStack() == compassItem)
                event.getPlayer().openInventory(menuInventory);
        });

        getEventNode().addChild(node);

        // Get either the instance with the zero UUID or one of the existing
        var manager = MinecraftServer.getInstanceManager();
        var instance = manager.getInstance(UUID.fromString("00000000-0000-4000-8000-000000000000"));
        if (instance == null) {
            var optionalInstance = manager.getInstances().stream().findFirst();
            if (optionalInstance.isPresent()) instance = optionalInstance.get();
        }
        if (instance != null) instances.add(instance);
    }

    @Override
    public void terminate() {

    }
}