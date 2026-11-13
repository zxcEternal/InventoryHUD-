package eternal.inventoryhud.client.render

import eternal.inventoryhud.client.IMinecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object TotemCounter : IMinecraft {

    private val HOTBAR_OFFHAND_LEFT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_left")
    private val HOTBAR_OFFHAND_RIGHT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right")

    @JvmStatic
    fun extract(graphics: GuiGraphicsExtractor) {
        val player = client.player ?: return

        val screenCenter = graphics.guiWidth() / 2
        val y = graphics.guiHeight() - 23
        val itemY = graphics.guiHeight() - 19
        val itemX: Int
        if (player.mainArm === HumanoidArm.RIGHT) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_SPRITE, screenCenter + 91, y, 29, 24)
            itemX = screenCenter + 101
        } else {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, screenCenter - 120, y, 29, 24)
            itemX = screenCenter - 117
        }
        graphics.item(player, ItemStack(Items.TOTEM_OF_UNDYING), itemX, itemY, 10)
        graphics.itemDecorations(client.font, ItemStack(Items.TOTEM_OF_UNDYING), itemX, itemY, count().toString())
    }

    private fun count(): Int {
        var count = 0

        for (itemStack in client.player!!.inventory.nonEquipmentItems) {
            if (itemStack.`is`(Items.TOTEM_OF_UNDYING)) {
                count += itemStack.count
            }
        }
        val offhand = client.player!!.offhandItem
        if (offhand.`is`(Items.TOTEM_OF_UNDYING)) {
            count += offhand.count
        }
        return count
    }
}
