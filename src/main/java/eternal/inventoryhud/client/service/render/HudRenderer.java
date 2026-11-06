package eternal.inventoryhud.client.service.render;

import eternal.inventoryhud.client.service.IMinecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HudRenderer implements IMinecraft {

    private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
    private static final Identifier HOTBAR_OFFHAND_LEFT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_left");
    private static final Identifier HOTBAR_OFFHAND_RIGHT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right");

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private final SlotExtractor slotExtractor;

    public HudRenderer(final SlotExtractor slotExtractor) {
        this.slotExtractor = slotExtractor;
    }

    public void extractRenderState(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker) {
        Player player = client.player;
        if (player != null) {
            int totemCount = getTotemCount();
            int armorX = getArmorHotbarX(graphics.guiWidth(), player.getMainArm(), totemCount > 0);

            this.extractTotem(graphics, player, totemCount);
            this.extractArmorHotbar(graphics, deltaTracker, player, armorX);
        }
    }

    private void extractTotem(final GuiGraphicsExtractor graphics, final Player player, final int count) {
        if (count > 0) {
            int screenCenter = graphics.guiWidth() / 2;
            int y = graphics.guiHeight() - 23;
            int itemY = graphics.guiHeight() - 19;
            int itemX;

            if (player.getMainArm() == HumanoidArm.RIGHT) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_SPRITE, screenCenter + 91, y, 29, 24);
                itemX = screenCenter + 101;
            } else {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, screenCenter - 91 - 29, y, 29, 24);
                itemX = screenCenter - 117;
            }

            ItemStack itemStack = new ItemStack(Items.TOTEM_OF_UNDYING);
            graphics.item(player, itemStack, itemX, itemY, 10);
            graphics.itemDecorations(client.font, itemStack, itemX, itemY, Integer.toString(count));
        }
    }

    private void extractArmorHotbar(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker, final Player player, final int x) {
        int y = graphics.guiHeight() - 22;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 0, 0, x, y, 61, 22);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 161, 0, x + 61, y, 21, 22);

        int seed = 11;

        for (int i = 0; i < ARMOR_SLOTS.length; ++i) {
            int itemX = x + i * 20 + 3;
            int itemY = graphics.guiHeight() - 19;
            ItemStack itemStack = player.getItemBySlot(ARMOR_SLOTS[i]);
            this.slotExtractor.extractSlot(graphics, itemX, itemY, deltaTracker, player, itemStack, seed++);
        }
    }

    public int getAttackIndicatorX(final int fallback) {
        if (client.player != null) {
            int screenWidth = client.getWindow().getGuiScaledWidth();
            int armorX = getArmorHotbarX(screenWidth, client.player.getMainArm(), getTotemCount() > 0);

            if (client.player.getMainArm() == HumanoidArm.RIGHT) {
                return armorX + 91;
            } else {
                return armorX - 25;
            }
        } else {
            return fallback;
        }
    }

    private static int getArmorHotbarX(final int screenWidth, final HumanoidArm mainArm, final boolean hasTotems) {
        int screenCenter = screenWidth / 2;
        int totemWidth = hasTotems ? 29 : 0;

        if (mainArm == HumanoidArm.RIGHT) {
            return screenCenter + 98 + totemWidth;
        } else {
            return screenCenter - 180 - totemWidth;
        }
    }

    private static int getTotemCount() {
        int count = 0;

        for (ItemStack itemStack : client.player.getInventory().getNonEquipmentItems()) {
            if (itemStack.is(Items.TOTEM_OF_UNDYING)) {
                count += itemStack.getCount();
            }
        }

        ItemStack offhand = client.player.getOffhandItem();
        if (offhand.is(Items.TOTEM_OF_UNDYING)) {
            count += offhand.getCount();
        }

        return count;
    }

    @FunctionalInterface
    public interface SlotExtractor {
        void extractSlot(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed);
    }
}
