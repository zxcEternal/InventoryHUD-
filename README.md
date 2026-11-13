# InventoryHUD-

A client-side Fabric mod for Minecraft: Java Edition, written in Kotlin. It adds extra HUD elements to the screen: armor, item cooldown timers, active potion effects, and a Totem of Undying counter. Each element can be toggled individually.

## Features

| Element | What it shows |
|---------|---------------|
| **Armor bar** | Helmet, chestplate, leggings, and boots in a separate panel next to the hotbar, on the side of your main hand |
| **Cooldown timer** | The item cooldown timer drawn over the item icon in its slot. Under one second it shows tenths; above that, whole seconds |
| **Potion timer** | Active effects in the top-right corner: icon, level, and remaining time in `mm:ss` format (`**:**` for infinite effects) |
| **Totem counter** | The number of Totems of Undying in your inventory and off hand, shown in a separate slot next to the hotbar |

## Requirements

| Component | Version |
|-----------|---------|
| Minecraft: Java Edition | 26.3 |
| [Fabric Loader](https://fabricmc.net/use/installer/) | 0.20.1 or newer |
| [Fabric API](https://modrinth.com/mod/fabric-api) | 0.165.2+26.3 |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) | 1.14.2+kotlin.2.4.21 |
| Java | 25 |

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 26.3.
2. Put the following files into your `mods` folder:
   - Fabric API;
   - Fabric Language Kotlin;
   - `inventoryhud--1.0.jar` (from the [Releases](https://github.com/zxcEternal/InventoryHUD-/releases) page, or built from source).
3. Launch the game using the Fabric profile.

## Building from source

Requires JDK 25. The project uses Gradle (Kotlin DSL); the Gradle Wrapper is included in the repository.

```bash
git clone https://github.com/zxcEternal/InventoryHUD-.git
cd InventoryHUD-
./gradlew build
```

The built mod will be at `build/libs/inventoryhud--1.0.jar`.

To run the client with the mod in a development environment:

```bash
./gradlew runClient
```

## Project structure

```
InventoryHUD-/
├── gradle/wrapper/      # Gradle Wrapper
├── src/main/            # Mod source code and resources
├── build.gradle.kts     # Build configuration
├── gradle.properties    # Minecraft, Fabric, and mod versions
├── settings.gradle.kts
└── LICENSE.txt
```

## License

Copyright (c) 2026. All rights reserved. See [LICENSE.txt](LICENSE.txt) for details.

## Author

[zxcEternal](https://github.com/zxcEternal)
