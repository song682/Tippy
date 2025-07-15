package decok.dfcdvadstf.tips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class TipRenderer {
    public static void renderTip(GuiScreen screen) {
        // 获取 Minecraft 实例
        Minecraft mc = Minecraft.getMinecraft();

        // 获取 FontRenderer
        if (mc.fontRenderer == null) return;

        // 计算实际Y位置
        int yPos = Tippy.posY;
        if (yPos < 0) {
            yPos = screen.height + yPos;
        }

        // 获取本地化文本
        String title = StatCollector.translateToLocal("tippy.title");
        String tip = StatCollector.translateToLocal(Tippy.getRandomTipKey());

        // 启用混合模式确保透明度正确
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // 绘制标题
        mc.fontRenderer.drawStringWithShadow(
                title,
                Tippy.posX,
                yPos,
                Tippy.titleColor
        );

        // 绘制提示内容
        mc.fontRenderer.drawStringWithShadow(
                tip,
                Tippy.posX,
                yPos + 10,
                Tippy.contentColor
        );

        GL11.glDisable(GL11.GL_BLEND);
    }
}