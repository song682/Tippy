package decok.dfcdvadstf.tips;

import com.google.gson.JsonParser;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CommandCheckJson extends CommandBase {
    /*
     * A  that add a Json check
     */
    @Override
    public String getCommandName() {
        return "checktipsjson";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/checktipsjson - Validates tips.json file";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        File tipsFile = new File("config/tippy/tips.json");
        if (!tipsFile.exists()) {
            sender.addChatMessage(new ChatComponentText("§cError: tips.json not found!"));
            return;
        }

        try {
            String jsonContent = FileUtils.readFileToString(tipsFile, StandardCharsets.UTF_8);
            new JsonParser().parse(jsonContent);
            sender.addChatMessage(new ChatComponentText("§aSuccess: tips.json is valid JSON!"));
        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText("§cError: " + e.getMessage()));
            sender.addChatMessage(new ChatComponentText("§ePlease check line 10 column 25 in tips.json"));
        }
    }
}