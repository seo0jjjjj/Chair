package rangfarm.seo0jjjjj.chair.chair;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

public class Kommand implements CommandExecutor {
    private Chair plugin;
    public Kommand(Chair plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Block block = player.getTargetBlockExact(3);
        Location blockLocation;

        // 플레이어 인지, 권한이 있는지 체크
        if (!(sender instanceof Player && sender.isOp())) return false;
        // "의자 지정"인지 체크
        if (!(args.length != 0 && args[0].equals("지정"))) return false;

        if (block == null) {
            player.sendMessage(ChatColor.GOLD + "[의자 지정 플러그인] " + ChatColor.WHITE + "타겟한 블록을 찾을 수 없습니다.");
            return false;
        }

        // 블록이 베리어 인지 체크
        if (block.getType().equals(Material.BARRIER)) {
            blockLocation = block.getLocation();
            if (plugin.setChair(block))
                player.sendMessage(ChatColor.GOLD + "[의자 지정 플러그인] " + ChatColor.WHITE + "의자가 지정되었습니다.");
            else
                player.sendMessage(ChatColor.GOLD + "[의자 지정 플러그인] " + ChatColor.WHITE + "의자가 삭제되었습니다.");
        } // 베리어가 아닌 의자 지정
        else
            player.sendMessage(ChatColor.GOLD + "[의자 지정 플러그인] " + ChatColor.WHITE + "베리어 블록만 의자로 지정할 수 있습니다.");
        return false;

    }




}
