package eternal.inventoryhud.client.render;

import eternal.inventoryhud.client.IMinecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArmorRenderer implements IMinecraft {

    private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private final SlotExtractor slotExtractor;

    public ArmorRenderer(SlotExtractor slotExtractor) {
        this.slotExtractor = slotExtractor;
    }

    public void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (client.player == null) return;

        int x = getHotbarX(graphics.guiWidth(), client.player.getMainArm(), TotemRenderer.hasTotems());
        int y = graphics.guiHeight() - 22;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 0, 0, x, y, 61, 22);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 161, 0, x + 61, y, 21, 22);

        int seed = 11;
        for (int i = 0; i < ARMOR_SLOTS.length; ++i) {
            int itemX = x + i * 20 + 3;
            int itemY = graphics.guiHeight() - 19;
            ItemStack itemStack = client.player.getItemBySlot(ARMOR_SLOTS[i]);
            slotExtractor.extractSlot(graphics, itemX, itemY, deltaTracker, client.player, itemStack, seed++);
        }
    }

    public int getAttackIndicatorX(int fallback) {
        if (client.player == null) return fallback;

        int screenWidth = client.getWindow().getGuiScaledWidth();
        int armorX = getHotbarX(screenWidth, client.player.getMainArm(), TotemRenderer.hasTotems());
        return client.player.getMainArm() == HumanoidArm.RIGHT ? armorX + 91 : armorX - 25;
    }

    private static int getHotbarX(int screenWidth, HumanoidArm mainArm, boolean hasTotems) {
        int screenCenter = screenWidth / 2;
        int totemWidth = hasTotems ? 29 : 0;
        return mainArm == HumanoidArm.RIGHT ? screenCenter + 98 + totemWidth : screenCenter - 180 - totemWidth;
    }

    @FunctionalInterface
    public interface SlotExtractor {
        void extractSlot(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed);
    }
}
