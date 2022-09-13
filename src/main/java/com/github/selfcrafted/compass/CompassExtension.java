package com.github.selfcrafted.compass;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;

public class CompassExtension extends Extension {
    @Override
    public void initialize() {
        MinecraftServer.LOGGER.info("Compass start.");

    }

    @Override
    public void terminate() {

    }
}