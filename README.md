# InventoryHUD-

---

# English

A client-side Fabric mod for Minecraft: Java Edition, written in Kotlin. It adds extra HUD elements to the screen: armor, item cooldown timers, active potion effects, and a Totem of Undying counter. Each element can be toggled individually.

## Features

| Element | What it shows |
|---------|---------------|
| **Armor** | Helmet, chestplate, leggings, and boots in a separate panel next to the hotbar, on the side of your main hand |
| **Cooldown** | The item cooldown timer drawn over the item icon in its slot. Under one second it shows tenths; above that, whole seconds |
| **Potions** | Active effects in the top-right corner: icon, level, and remaining time in `mm:ss` format (`**:**` for infinite effects) |
| **Totem** | The number of Totems of Undying in your inventory and off hand, shown in a separate slot next to the hotbar |

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

## Configuration

After a fresh install, **all elements are disabled**. To enable the ones you want:

1. Press **H** in game (default key; it can be rebound in the Controls settings).
2. In the menu that opens, toggle the **Armor**, **Cooldown**, **Potions**, and **Totem** buttons.
3. Click **Done**. Settings are saved immediately on every toggle.

Settings are stored in `config/inventoryhud-/default.json`:

```json
{
  "elements": {
    "armor": false,
    "cooldown": false,
    "potions": false,
    "totem": false
  }
}
```

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

---

# Русский

Клиентский Fabric-мод для Minecraft: Java Edition, написанный на Kotlin. Добавляет на экран дополнительные элементы HUD: броню, таймеры перезарядки предметов, активные эффекты зелий и счётчик тотемов бессмертия. Каждый элемент включается и выключается отдельно.

## Возможности

| Элемент | Что показывает |
|---------|----------------|
| **Броня** | Шлем, нагрудник, поножи и ботинки в отдельной панели рядом с хотбаром, со стороны основной руки |
| **Перезарядка** | Таймер перезарядки предмета поверх его иконки в слоте. Меньше секунды: с десятыми долями, дальше: в целых секундах |
| **Зелья** | Активные эффекты в правом верхнем углу: иконка, уровень и оставшееся время в формате `мм:сс` (для бесконечных эффектов `**:**`) |
| **Тотем** | Количество тотемов бессмертия в инвентаре и второй руке в отдельном слоте рядом с хотбаром |

## Требования

| Компонент | Версия |
|-----------|--------|
| Minecraft: Java Edition | 26.3 |
| [Fabric Loader](https://fabricmc.net/use/installer/) | 0.20.1 или новее |
| [Fabric API](https://modrinth.com/mod/fabric-api) | 0.165.2+26.3 |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) | 1.14.2+kotlin.2.4.21 |
| Java | 25 |

## Установка

1. Установите [Fabric Loader](https://fabricmc.net/use/installer/) для Minecraft 26.3.
2. Поместите в папку `mods` следующие файлы:
   - Fabric API;
   - Fabric Language Kotlin;
   - `inventoryhud--1.0.jar` (со страницы [Releases](https://github.com/zxcEternal/InventoryHUD-/releases) или собранный самостоятельно).
3. Запустите игру через профиль Fabric.

## Настройка

После первой установки **все элементы выключены**. Чтобы включить нужные:

1. Нажмите клавишу **H** (по умолчанию) в игре. Её можно переназначить в настройках управления.
2. В открывшемся меню переключайте кнопки **Броня**, **Перезарядка**, **Зелья** и **Тотем**.
3. Нажмите **Готово**. Настройки сохраняются сразу при каждом переключении.

Настройки хранятся в файле `config/inventoryhud-/default.json`:

```json
{
  "elements": {
    "armor": false,
    "cooldown": false,
    "potions": false,
    "totem": false
  }
}
```

## Сборка из исходников

Нужен JDK 25. Проект собирается через Gradle (Kotlin DSL), Gradle Wrapper уже в репозитории.

```bash
git clone https://github.com/zxcEternal/InventoryHUD-.git
cd InventoryHUD-
./gradlew build
```

Готовый мод будет лежать в `build/libs/inventoryhud--1.0.jar`.

Для запуска клиента с модом в среде разработки:

```bash
./gradlew runClient
```

## Структура проекта

```
InventoryHUD-/
├── gradle/wrapper/      # Gradle Wrapper
├── src/main/            # Исходный код и ресурсы мода
├── build.gradle.kts     # Конфигурация сборки
├── gradle.properties    # Версии Minecraft, Fabric и мода
├── settings.gradle.kts
└── LICENSE.txt
```

## Лицензия

Copyright (c) 2026. Все права защищены. Подробности в файле [LICENSE.txt](LICENSE.txt).

## Автор

[zxcEternal](https://github.com/zxcEternal)
