package eternal.inventoryhud.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class HudMenu extends Screen {
    private final Screen parent;
    private final Configurable config;

    public HudMenu(Screen parent, Configurable config) {
        super(Component.translatable("screen.inventoryhud-.config"));
        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
        int buttonWidth = 220;
        int left = (width - buttonWidth) / 2;
        int top = height / 2 - 80;

        addRenderableWidget(toggleButton(left, top, "button.inventoryhud-.armor", config.isArmorEnabled(), config::setArmorEnabled));
        addRenderableWidget(toggleButton(left, top + 25, "button.inventoryhud-.cooldown", config.isCooldownEnabled(), config::setCooldownEnabled));
        addRenderableWidget(toggleButton(left, top + 50, "button.inventoryhud-.potions", config.isPotionEnabled(), config::setPotionEnabled));
        addRenderableWidget(toggleButton(left, top + 75, "button.inventoryhud-.totem", config.isTotemEnabled(), config::setTotemEnabled));

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose()).bounds(left, top + 115, buttonWidth, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(font, Component.literal("InventoryHUD-"), width / 2, 20, 0xFFFFFFFF);
    }

    private Button toggleButton(int x, int y, String translationKey, boolean enabled, Consumer<Boolean> setter) {
        boolean[] state = {enabled};

        return Button.builder(label(translationKey, enabled), button -> {
            boolean next = !state[0];
            state[0] = next;
            setter.accept(next);
            button.setMessage(label(translationKey, next));
        }).bounds(x, y, 220, 20).build();
    }

    private static Component label(String key, boolean enabled) {
        return Component.translatable(key, enabled ? Component.translatable("options.on") : Component.translatable("options.off"));
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }
}
