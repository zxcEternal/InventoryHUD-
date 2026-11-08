package eternal.inventoryhud.client
 
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
 
class HudMenu(private val parent: Screen?, private val config: Configurable) : Screen(Component.translatable("screen.inventoryhud-.config")) {
 
    override fun init() {
        val buttonWidth = 220
        val left = (width - buttonWidth) / 2
        val top = height / 2 - 80
 
        addRenderableWidget(toggleButton(left, top, "button.inventoryhud-.armor", config.isArmorEnabled) { config.isArmorEnabled = it })
        addRenderableWidget(toggleButton(left, top + 25, "button.inventoryhud-.cooldown", config.isCooldownEnabled) { config.isCooldownEnabled = it })
        addRenderableWidget(toggleButton(left, top + 50, "button.inventoryhud-.potions", config.isPotionEnabled) { config.isPotionEnabled = it })
        addRenderableWidget(toggleButton(left, top + 75, "button.inventoryhud-.totem", config.isTotemEnabled) { config.isTotemEnabled = it })
 
        addRenderableWidget(Button.builder(Component.translatable("gui.done")) { onClose() }.bounds(left, top + 115, buttonWidth, 20).build())
    }
 
    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick)
        graphics.centeredText(font, Component.literal("InventoryHUD-"), width / 2, height / 2 - 120, -0x1)
    }
 
    private fun toggleButton(x: Int, y: Int, translationKey: String, enabled: Boolean, setter: (Boolean) -> Unit): Button {
        var state = enabled
 
        return Button.builder(label(translationKey, enabled)) { button ->
            state = !state
            setter(state)
            button.message = label(translationKey, state)
        }.bounds(x, y, 220, 20).build()
    }
 
    private fun label(key: String, enabled: Boolean): Component {
        return Component.translatable(key, if (enabled) Component.translatable("options.on") else Component.translatable("options.off"))
    }

    override fun onClose() {
        if (parent != null) {
            minecraft.setScreenAndShow(parent)
        } else {
            super.onClose()
        }
    }
}
