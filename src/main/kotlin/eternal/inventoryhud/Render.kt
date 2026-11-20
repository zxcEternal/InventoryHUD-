package eternal.inventoryhud

import eternal.inventoryhud.config.Config
import eternal.inventoryhud.mixin.CooldownInstanceAccessor
import eternal.inventoryhud.mixin.ItemCooldownsAccessor
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.max
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.Hud
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object ArmorBar : IMinecraft {
    private val HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar")
    private val ARMOR_SLOTS =
        arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)

    @JvmStatic
    fun extract(
        graphics: GuiGraphicsExtractor,
        deltaTracker: DeltaTracker,
        slotExtractor: SlotExtractor
    ) {
        val player = client.player ?: return

        val x = hotbarX(player.mainArm)
        val y = graphics.guiHeight() - 22

        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            HOTBAR_SPRITE,
            182,
            22,
            0,
            0,
            x,
            y,
            61,
            22
        )

        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            HOTBAR_SPRITE,
            182,
            22,
            161,
            0,
            x + 61,
            y,
            21,
            22
        )

        var seed = 11

        for (i in ARMOR_SLOTS.indices) {
            val itemX = x + i * 20 + 3
            val itemY = graphics.guiHeight() - 19
            val itemStack = player.getItemBySlot(ARMOR_SLOTS[i])

            slotExtractor.extractSlot(
                graphics,
                itemX,
                itemY,
                deltaTracker,
                player,
                itemStack,
                seed++
            )
        }
    }

    @JvmStatic
    fun attackIndicatorX(fallback: Int): Int {
        val player = client.player ?: return fallback
        val armorX = hotbarX(player.mainArm)

        return if (player.mainArm == HumanoidArm.RIGHT) {
            armorX + 91
        } else {
            armorX - 25
        }
    }

    private fun hotbarX(mainArm: HumanoidArm): Int {
        val screenCenter = client.window.guiScaledWidth / 2
        val totemWidth = if (Config.totemCounter) 29 else 0

        return if (mainArm == HumanoidArm.RIGHT) {
            screenCenter + 98 + totemWidth
        } else {
            screenCenter - 180 - totemWidth
        }
    }

    fun interface SlotExtractor {
        fun extractSlot(
            graphics: GuiGraphicsExtractor,
            x: Int,
            y: Int,
            deltaTracker: DeltaTracker,
            player: Player,
            itemStack: ItemStack,
            seed: Int
        )
    }
}

object CooldownTimer : IMinecraft {
    @JvmStatic
    fun extract(graphics: GuiGraphicsExtractor, itemStack: ItemStack, x: Int, y: Int) {
        val player = client.player ?: return

        val cooldowns =
            player.cooldowns.takeIf { it.isOnCooldown(itemStack) } as? ItemCooldownsAccessor
                ?: return

        val entry =
            cooldowns.cooldowns()[player.cooldowns.getCooldownGroup(itemStack)]
                as? CooldownInstanceAccessor ?: return

        val time = format((entry.endTime - cooldowns.tickCount()) / 20.0f)

        graphics.text(client.font, time, x + 17 - client.font.width(time), y, -0x1, true)
    }

    private fun format(seconds: Float): String {
        if (seconds < 1.0f) {
            return String.format(Locale.ROOT, "%.1f", seconds)
        }

        return ceil(seconds.toDouble()).toInt().toString()
    }
}

object PotionTimer : IMinecraft {
    private val EFFECT_BACKGROUND_SPRITE =
        Identifier.fromNamespaceAndPath("inventoryhud-", "textures/effect_background.png")

    @JvmStatic
    fun extract(graphics: GuiGraphicsExtractor) {
        if (client.gui.screen() != null && client.gui.screen()!!.showsActiveEffects()) return
        val player = client.player ?: return

        var good = 0
        var bad = 0

        val top = if (client.isDemo) 16 else 1

        for (effect in player.activeEffects.filter { it.showIcon() }.sortedDescending()) {
            val beneficial = effect.effect.value().isBeneficial
            val column = if (beneficial) good++ else bad++

            val x = graphics.guiWidth() - 37 - column * 38
            val y = top + (if (beneficial) 0 else 40)

            val level = (effect.amplifier + 1).toString()
            val duration = format(effect)

            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                EFFECT_BACKGROUND_SPRITE,
                x,
                y,
                0.0f,
                0.0f,
                36,
                38,
                36,
                38
            )

            graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                Hud.getMobEffectSprite(effect.effect),
                x + 9,
                y + 3,
                18,
                18
            )

            graphics.text(client.font, level, x + 28 - client.font.width(level), y + 14, -0x1, true)

            graphics.text(
                client.font,
                duration,
                x + (36 - client.font.width(duration)) / 2,
                y + 25,
                -0x1,
                true
            )
        }
    }

    private fun format(effect: MobEffectInstance): String {
        if (effect.isInfiniteDuration) {
            return "**:**"
        }

        val seconds = max(0, effect.duration / 20)

        return String.format(Locale.ROOT, "%02d:%02d", seconds / 60, seconds % 60)
    }
}

object TotemCounter : IMinecraft {
    private val HOTBAR_OFFHAND_LEFT_SPRITE =
        Identifier.withDefaultNamespace("hud/hotbar_offhand_left")
    private val HOTBAR_OFFHAND_RIGHT_SPRITE =
        Identifier.withDefaultNamespace("hud/hotbar_offhand_right")

    @JvmStatic
    fun extract(graphics: GuiGraphicsExtractor) {
        val player = client.player ?: return

        val screenCenter = graphics.guiWidth() / 2
        val y = graphics.guiHeight() - 23

        val itemX: Int
        val itemY = graphics.guiHeight() - 19

        if (player.mainArm === HumanoidArm.RIGHT) {
            graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                HOTBAR_OFFHAND_RIGHT_SPRITE,
                screenCenter + 91,
                y,
                29,
                24
            )

            itemX = screenCenter + 101
        } else {
            graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                HOTBAR_OFFHAND_LEFT_SPRITE,
                screenCenter - 120,
                y,
                29,
                24
            )

            itemX = screenCenter - 117
        }

        graphics.item(player, ItemStack(Items.TOTEM_OF_UNDYING), itemX, itemY, 10)

        graphics.itemDecorations(
            client.font,
            ItemStack(Items.TOTEM_OF_UNDYING),
            itemX,
            itemY,
            count().toString()
        )
    }

    private fun count(): Int {
        var count = 0

        for (itemStack in client.player!!.inventory.nonEquipmentItems) {
            if (itemStack.item === Items.TOTEM_OF_UNDYING) {
                count += itemStack.count
            }
        }

        val offhand = client.player!!.offhandItem
        if (offhand.item === Items.TOTEM_OF_UNDYING) {
            count += offhand.count
        }

        return count
    }
}
