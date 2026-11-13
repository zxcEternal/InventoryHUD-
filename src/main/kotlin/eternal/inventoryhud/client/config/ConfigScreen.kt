package eternal.inventoryhud.client.config

import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Checkbox
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class ConfigScreen(private val parent: Screen) : Screen(Component.translatable("inventoryhud-.config.title")) {

    override fun init() {
        val center = width / 2
        val left = center - 100
        val top = height / 2 - 80

        addRenderableWidget(
            StringWidget(title, font).apply {
                setPosition(center - width / 2, top - 24)
            }
        )

        addRenderableWidget(
            Checkbox.builder(Component.translatable("inventoryhud-.config.armor_bar"), font)
                .pos(left, top)
                .selected(Config.armorBar)
                .onValueChange { _, value ->
                    Config.armorBar = value
                    Config.save()
                }
                .build()
        )

        addRenderableWidget(
            Checkbox.builder(Component.translatable("inventoryhud-.config.cooldown_timer"), font)
                .pos(left, top + 24)
                .selected(Config.cooldownTimer)
                .onValueChange { _, value ->
                    Config.cooldownTimer = value
                    Config.save()
                }
                .build()
        )

        addRenderableWidget(
            Checkbox.builder(Component.translatable("inventoryhud-.config.potion_timer"), font)
                .pos(left, top + 48)
                .selected(Config.potionTimer)
                .onValueChange { _, value ->
                    Config.potionTimer = value
                    Config.save()
                }
                .build()
        )

        addRenderableWidget(
            Checkbox.builder(Component.translatable("inventoryhud-.config.totem_counter"), font)
                .pos(left, top + 72)
                .selected(Config.totemCounter)
                .onValueChange { _, value ->
                    Config.totemCounter = value
                    Config.save()
                }
                .build()
        )

        addRenderableWidget(
            Button.builder(Component.translatable("gui.done")) { onClose() }
                .pos(center - 100, top + 108)
                .width(200)
                .build()
        )
    }

    override fun onClose() {
        Config.save()

        minecraft.setScreenAndShow(parent)
    }
}
