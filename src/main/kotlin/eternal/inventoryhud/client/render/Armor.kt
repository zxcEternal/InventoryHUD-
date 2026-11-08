package eternal.inventoryhud.client.render

import eternal.inventoryhud.client.IMinecraft
import eternal.inventoryhud.client.MainClient
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object Armor : IMinecraft {

    private val HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar")
    private val ARMOR_SLOTS = arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)

    @JvmStatic
    fun extract(graphics: GuiGraphicsExtractor, deltaTracker: DeltaTracker, slotExtractor: SlotExtractor) {
        val player = client.player ?: return

        val x = hotbarX(player.mainArm)
        val y = graphics.guiHeight() - 22

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 0, 0, x, y, 61, 22)
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 161, 0, x + 61, y, 21, 22)

        var seed = 11
        for (i in ARMOR_SLOTS.indices) {
            val itemX = x + i * 20 + 3
            val itemY = graphics.guiHeight() - 19
            val itemStack = player.getItemBySlot(ARMOR_SLOTS[i])
            slotExtractor.extractSlot(graphics, itemX, itemY, deltaTracker, player, itemStack, seed++)
        }
    }

    @JvmStatic
    fun attackIndicatorX(fallback: Int): Int {
        val player = client.player ?: return fallback
        val armorX = hotbarX(player.mainArm)
        return if (player.mainArm == HumanoidArm.RIGHT) armorX + 91 else armorX - 25
    }

    private fun hotbarX(mainArm: HumanoidArm): Int {
        val screenCenter = client.window.guiScaledWidth / 2
        val totemWidth = if (MainClient.config.isTotemEnabled) 29 else 0
        return if (mainArm == HumanoidArm.RIGHT) screenCenter + 98 + totemWidth else screenCenter - 180 - totemWidth
    }

    fun interface SlotExtractor {
        fun extractSlot(graphics: GuiGraphicsExtractor, x: Int, y: Int, deltaTracker: DeltaTracker, player: Player, itemStack: ItemStack, seed: Int)
    }
}
