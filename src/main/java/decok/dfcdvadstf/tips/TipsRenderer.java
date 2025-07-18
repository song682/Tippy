package decok.dfcdvadstf.tips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;


public class TipsRenderer {
    public static void renderTip(GuiScreen screen) {

        // 获取 Minecraft 实例 (允许我使用 FontRenderer)
        // Get Minecraft instance to allow me use FontRenderer
        Minecraft mc = Minecraft.getMinecraft();

        // 获取 FontRenderer (同时做空值检查)
        // Get the FontRenderer method and do Null Point Check
        if (mc.fontRenderer == null) return;


        // 计算实际X位置（支持负值：从屏幕右侧定位）
        // Calculate the actually X axis that rendering on the screen (Support negative number, means from right)
        int xPos = Tippy.posX;
        if (xPos < 0) {
            xPos = screen.width + xPos;
        }

        // 计算实际Y位置（支持负值：从屏幕下方定位）
        // Calculate the actually Y axis that rendering on the screen (Support negative number, means from bottom)
        int yPos = Tippy.posY;
        if (yPos < 0) {
            yPos = screen.height + yPos;
        }
        
        // 获取本地化文本
        // Get localized text
        // String title = I18n.format("tippy.title");
        // String tip = I18n.format(Tippy.getCurrentTipKey());
        String title = StatCollector.translateToLocal("tippy.title");
        String tip = StatCollector.translateToLocal(Tippy.getCurrentTipKey());

        // 启用混合模式确保透明度正确
        // Enable blending mode to make sure the transparency is correct
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // 绘制标题
        // Rendering title
        mc.fontRenderer.drawStringWithShadow(
                title,
                Tippy.posX,
                yPos,
                Tippy.titleColor
        );

        // 绘制提示内容
        // Rendering tips content
        mc.fontRenderer.drawStringWithShadow(
                tip,
                Tippy.posX,
                yPos + 10,
                Tippy.contentColor
        );

        // ??? I don't understand.
        // ？？？我不理解？！
         GL11.glDisable(GL11.GL_BLEND);
    }
}
