package decok.dfcdvadstf.tips;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod(modid = Tippy.MODID, version = Tippy.VERSION, name = "Tippy")
public class Tippy {
    public static final String MODID = "tippy";
    public static final String VERSION = "1.0.0"; // 版本号更新
    public static Logger logger;

    // 配置选项
    public static int titleColor = 0xFFAA00; // 标题颜色 (橙色)
    public static int contentColor = 0xFFFFFF; // 内容颜色 (白色)
    public static int posX = 10; // X位置
    public static int posY = -30; // Y位置 (负数表示从底部计算)
    public static int switchInterval = 10; // 提示切换间隔(秒)
    public static List<String> tipKeys = new ArrayList<>();
    public static Random random = new Random();
    public static JsonObject configJson;

    // 提示状态管理
    public static long lastSwitchTime = 0; // 上次切换时间
    public static String currentTipKey = ""; // 当前显示的提示键

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Initializing Tippy Mod for Minecraft 1.7.10");

        // 加载配置
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        titleColor = config.getInt("titleColor", "colors", titleColor, 0, 0xFFFFFF, "Title text color (RGB hex)");
        contentColor = config.getInt("contentColor", "colors", contentColor, 0, 0xFFFFFF, "Content text color (RGB hex)");
        posX = config.getInt("posX", "position", posX, 0, 1000, "Horizontal position");
        posY = config.getInt("posY", "position", posY, -1000, 1000, "Vertical position (negative = from bottom)");
        switchInterval = config.getInt("switchInterval", "behavior", switchInterval, 1, 3600, "Tip switch interval in seconds");

        config.save();

        // 加载提示文件
        loadTipsConfig();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // 初始化提示
        updateTipIfNeeded(true);
    }

    private void loadTipsConfig() {
        File configDir = new File("config/tippy");
        File tipsFile = new File(configDir, "tips.json");

        try {
            // 创建配置目录
            if (!configDir.exists()) {
                configDir.mkdirs();
            }

            // 创建默认提示文件
            if (!tipsFile.exists()) {
                JsonObject defaultConfig = new JsonObject();
                JsonArray defaultTips = new JsonArray();

                // 添加默认提示键名
                defaultTips.add(new JsonPrimitive("tippy.tip1"));
                defaultTips.add(new JsonPrimitive("tippy.tip2"));
                defaultTips.add(new JsonPrimitive("tippy.tip3"));

                defaultConfig.add("tips", defaultTips);

                // 写入默认配置
                FileUtils.writeStringToFile(
                        tipsFile,
                        new Gson().toJson(defaultConfig),
                        StandardCharsets.UTF_8
                );
            }

            // 读取配置文件
            String jsonContent = FileUtils.readFileToString(tipsFile, StandardCharsets.UTF_8);
            JsonParser parser = new JsonParser();
            configJson = parser.parse(jsonContent).getAsJsonObject();

            // 加载提示键名
            tipKeys.clear();
            JsonArray tipsArray = configJson.getAsJsonArray("tips");
            for (int i = 0; i < tipsArray.size(); i++) {
                tipKeys.add(tipsArray.get(i).getAsString());
            }

            logger.info("Loaded " + tipKeys.size() + " tip keys from config file");
        } catch (IOException e) {
            logger.error("Failed to load tips config: " + e.getMessage());
            // 添加默认提示作为后备
            tipKeys.add("tippy.tip1");
            tipKeys.add("tippy.tip2");
        }
    }

    // 获取当前提示键
    public static String getCurrentTipKey() {
        return currentTipKey;
    }

    // 更新提示（如果需要）
    public static void updateTipIfNeeded(boolean forceUpdate) {
        long currentTime = System.currentTimeMillis();
        long intervalMillis = switchInterval * 1000L; // 转换为毫秒

        // 检查是否需要更新提示
        if (forceUpdate || currentTime - lastSwitchTime > intervalMillis) {
            // 获取新提示
            if (!tipKeys.isEmpty()) {
                currentTipKey = tipKeys.get(random.nextInt(tipKeys.size()));
            } else {
                currentTipKey = "tippy.no_tips";
            }

            // 更新切换时间
            lastSwitchTime = currentTime;
        }
    }
}