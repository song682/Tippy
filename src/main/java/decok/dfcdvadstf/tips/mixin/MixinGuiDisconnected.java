package decok.dfcdvadstf.tips.mixin;

import decok.dfcdvadstf.tips.TipRenderer;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDisconnected.class)
public class MixinGuiDisconnected extends GuiScreen {
    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        TipRenderer.renderTip(this);
    }
}