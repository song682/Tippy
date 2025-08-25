package decok.dfcdvadstf.tips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
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
        int xPos = Tippy.posX;
        if (xPos < 0) {
            xPos = screen.width + xPos;
        }

        // 计算实际Y位置
        // Calculate the actually Y axis that rendering on the screen
        int yPos = Tippy.posY;
        if (yPos < 0) {
            yPos = screen.height + yPos;
        }
        // 获取本地化文本
        // Get localized text
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
                xPos,
                yPos,
                Tippy.titleColor
        );

        // 处理提示内容中的换行符（包括本地化文件中的字面\n）
        // Split tip content by newline characters (including literal \n from lang files)
        String[] tipLines;
        if (tip.contains("\\n")) {
            // 处理本地化文件中的字面\n
            tipLines = tip.split("\\\\n");
        } else {
            // 处理代码中添加的换行符
            tipLines = tip.split("\n");
        }

        int lineHeight = mc.fontRenderer.FONT_HEIGHT + 2; // 行高，包括间距

        // 绘制提示内容
        // Rendering tips content
        mc.fontRenderer.drawStringWithShadow(
                tip,
                xPos,
                yPos + 10,
                Tippy.contentColor
        );

        // 禁用混合模式
        // Disable blending mode
         GL11.glDisable(GL11.GL_BLEND);

    }
}