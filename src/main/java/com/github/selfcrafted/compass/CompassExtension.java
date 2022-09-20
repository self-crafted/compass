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