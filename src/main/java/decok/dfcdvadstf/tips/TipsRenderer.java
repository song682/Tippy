package decok.dfcdvadstf.tips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TipsRenderer {
    // 转义序列模式
    private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\([nrt\\\\\"'])");

    public static void renderTip(GuiScreen screen) {
        // 获取 Minecraft 实例 (允许我使用 FontRenderer)
        // Get Minecraft instance to allow me to use FontRenderer
        Minecraft mc = FMLClientHandler.instance().getClient();

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

        // 处理转义序列
        tip = processEscapeSequences(tip);

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

        // 处理提示内容中的换行符
        // Split tip content by newline characters
        String[] tipLines = tip.split("\n");
        int lineHeight = mc.fontRenderer.FONT_HEIGHT + 2; // 行高，包括间距

        // 绘制提示内容（支持多行）
        // Rendering tips content (support multiple lines)
        for (int i = 0; i < tipLines.length; i++) {
            mc.fontRenderer.drawStringWithShadow(
                    tipLines[i],
                    xPos,
                    yPos + 10 + (i * lineHeight),
                    Tippy.contentColor
            );
        }

        // 禁用混合模式
        // Disable blending mode
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * 处理转义序列
     * 将字符串中的转义序列转换为相应的字符
     *
     * @param input 输入字符串
     * @return 处理后的字符串
     */

    private static String processEscapeSequences(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 创建匹配器
        // Create matcher
        Matcher matcher = ESCAPE_PATTERN.matcher(input);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String escapeChar = matcher.group(1);
            String replacement;

            switch (escapeChar) {
                case "n":
                    replacement = "\n"; // 换行符 line break character
                    break;
                case "t":
                    replacement = "\t"; // 制表符 tab
                    break;
                case "r" :
                    replacement = "\r"; // 回车符 return
                    break;
                case "\\":
                    replacement = "\\"; // 反斜杠本身 backslash
                    break;
                case "\"":
                    replacement = "\""; // 双引号 double quotation marks
                    break;
                case "'":
                    replacement = "'"; // 单引号 single quotation marks
                    break;
                default:
                    // 未知的转义序列，保留原样 （之前是如此，现在不是）,不应该到达这里，因为正则表达式已经限制了匹配的字符
                    // Unknown escape sequence, keep the original (Used to be correct, right now it isn't, since regex limited the character that we'll match)
                    replacement = matcher.group(0);
                    break;
            }

            // 使用 Matcher.quoteReplacement 来避免替换字符串中的特殊字符（如$）被解释
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }

}
