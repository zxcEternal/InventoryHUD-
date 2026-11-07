package eternal.inventoryhud.client.render;

import eternal.inventoryhud.client.IMinecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TotemRenderer implements IMinecraft {

    private static final Identifier HOTBAR_OFFHAND_LEFT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_left");
    private static final Identifier HOTBAR_OFFHAND_RIGHT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right");

    public void extract(GuiGraphicsExtractor graphics) {
        if (client.player == null) return;

        int count = getCount();
        if (count == 0) return;

        int screenCenter = graphics.guiWidth() / 2;
        int y = graphics.guiHeight() - 23;
        int itemY = graphics.guiHeight() - 19;
        int itemX;

        if (client.player.getMainArm() == HumanoidArm.RIGHT) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_SPRITE, screenCenter + 91, y, 29, 24);
            itemX = screenCenter + 101;
        } else {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, screenCenter - 91 - 29, y, 29, 24);
            itemX = screenCenter - 117;
        }

        ItemStack itemStack = new ItemStack(Items.TOTEM_OF_UNDYING);
        graphics.item(client.player, itemStack, itemX, itemY, 10);
        graphics.itemDecorations(client.font, itemStack, itemX, itemY, Integer.toString(count));
    }

    public static boolean hasTotems() {
        return client.player != null && getCount() > 0;
    }

    private static int getCount() {
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
}
