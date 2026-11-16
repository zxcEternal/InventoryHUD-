package eternal.inventoryhud.client.config

import net.minecraft.client.Minecraft
import net.minecraft.client.Options

object Config {
    var armorBar = true
    var cooldownTimer = true
    var potionTimer = true
    var totemCounter = true

    fun process(options: Options.FieldAccess) {
        armorBar = options.process("inventoryhud.armor_bar", armorBar)
        cooldownTimer = options.process("inventoryhud.cooldown_timer", cooldownTimer)
        potionTimer = options.process("inventoryhud.potion_timer", potionTimer)
        totemCounter = options.process("inventoryhud.totem_counter", totemCounter)
    }

    fun save() {
        Minecraft.getInstance().options.save()
    }
}
