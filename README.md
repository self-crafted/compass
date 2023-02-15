# compass

[![standard-readme compliant](https://img.shields.io/badge/standard--readme-OK-green.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)
[![GitHub](https://img.shields.io/github/license/self-crafted/compass?style=flat-square&color=b2204c)](https://github.com/self-crafted/compass/blob/master/LICENSE)
[![GitHub Repo stars](https://img.shields.io/github/stars/self-crafted/compass?style=flat-square)](https://github.com/self-crafted/compass/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/self-crafted/compass?style=flat-square)](https://github.com/self-crafted/compass/network/members)
[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/self-crafted/compass?style=flat-square)](https://github.com/self-crafted/compass/releases/latest)
[![GitHub all releases](https://img.shields.io/github/downloads/self-crafted/compass/total?style=flat-square)](https://github.com/self-crafted/compass/releases)

A server menu for Minestom

## Table of Contents

- [Install](#install)
- [Usage](#usage)
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)

## Install
You can download a binary from the [releases](https://github.com/self-crafted/compass/releases/latest).
If you want to compile it yourself, make sure you have a JDK 17 installed and execute following commands:
```shell
git clone https://github.com/self-crafted/compass.git
cd compass
./gradlew build
```
The binary will be located at `./build/libs/compass-<VERSION>.jar`.
Copy it into the `./extensions/` folder of your server.

## Usage
If you start your server, the extension generates an example configuration file (`compass.toml`).
```toml
"opener.material" = "minecraft:compass"
"opener.name" = "<lang:key.keyboard.menu>"
"menu.layout" = "# # # # 1"
"menu.size" = 6
"opener.lore" = "Some Lore<br>second Line"
"menu.title" = "<lang:key.keyboard.menu>"

[["menu.servers"]]
"item.name" = "<gold>Lobby"
"item.lore" = "This is the lobby.<br>A nice place to wait for your friends..."
"item.material" = "minecraft:acacia_sapling"
server = "lobby"
```

## Maintainers

[@offby0point5](https://github.com/offby0point5)

## Contributing

PRs accepted.

Small note: If editing the README, please conform to the [standard-readme](https://github.com/RichardLitt/standard-readme) specification.

## License

[Apache License Version 2.0](LICENSE) © 2022 compass contributors
