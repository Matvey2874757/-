package net.novaclient.ui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ToggleButtonWidget extends ButtonWidget {
    private boolean toggled;

    public ToggleButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, boolean toggled) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.toggled = toggled;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int bgColor = toggled ? 0xFF4CAF50 : 0xFF555555;
        int textColor = toggled ? 0xFFFFFFFF : 0xFFDDDDDD;
        
        // Draw button background
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);
        
        // Draw border
        context.drawHorizontalLine(this.getX(), this.getX() + this.width, this.getY(), 0xFF888888);
        context.drawHorizontalLine(this.getX(), this.getX() + this.width, this.getY() + this.height - 1, 0xFF333333);
        context.drawVerticalLine(this.getX(), this.getY(), this.getY() + this.height, 0xFF888888);
        context.drawVerticalLine(this.getX() + this.width - 1, this.getY(), this.getY() + this.height, 0xFF333333);
        
        // Draw text
        context.drawCenteredTextWithShadow(this.textRenderer, this.getMessage(), 
            this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, textColor);
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible && button == 0) {
            this.toggled = !this.toggled;
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }
}
