package rangfarm.seo0jjjjj.chair.chair;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EventListener implements Listener {

    // 생성자
    private final Chair plugin;

    public EventListener(Chair plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    // 의자에 앉는 이벤트
    public void onPlayerSeat(PlayerInteractEvent event) {
        try {
            // 맨손으로 블록을 우클릭 하였을 떄,
            if (event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                Block block = event.getClickedBlock(); // 우클릭한 블록
                Location blockLocation = block.getLocation(); // 베리어의 위치를 담음
                ArmorStand armorStand; // 의자로 사용할 아머스탠드
                Player player = event.getPlayer(); // 이벤트 주체

                if (block.getType().equals(Material.BARRIER) && plugin.locations.contains(blockLocation)) {
                    // 클릭한 블록의 Barrel 일 경우 && 지정된 의자일 경우

                    // 앉는 과정
                    armorStand = plugin.getNearestArmorStand(blockLocation);

                    if (armorStand == null) {
                        armorStand = plugin.dropSeat(blockLocation); // 의자 생성.
                    } else if (!armorStand.getPassengers().isEmpty()) { // 누군가 타고 있을 경우
                        return;
                    }
                    armorStand.addPassenger(player);
                    // Cancel BlockPlaceEvent Result, if player is rightclicking with a block in his hand.
                    event.setUseInteractedBlock(Event.Result.DENY);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    // 의자에서 내리는 이벤트
    public void onPlayerDismount(EntityDismountEvent event) {
        try {
            if (event.getDismounted() instanceof ArmorStand) {
                ArmorStand armorStand = (ArmorStand) event.getDismounted();
                if (armorStand.getCustomName().equals("의자")) {
                    armorStand.remove();
                    Entity entity = event.getEntity();
                    Location entityLocation = entity.getLocation();
                    if (entity instanceof Player) {// 벽에 끼는 것 방지.
                        entity.teleport(entityLocation.add(0, 1.0, 0));

                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    // 의자를 부셨을 때,
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            Block block = event.getBlock();
            Location blockLocation = block.getLocation(); // 부서진 위치를 담음
            if (block.getType().equals(Material.BARRIER)) { // 베리어 일 경우
                if (plugin.locations.contains(blockLocation)) { // 부셔진게 의자 일 경우,
                    plugin.locations.remove(blockLocation);
                    event.getPlayer().sendMessage(ChatColor.GOLD + "[의자 지정 플러그인] " + ChatColor.WHITE + "의자가 삭제되었습니다.");
                    plugin.saveConfig();
                }
            } }catch (Exception e){
            e.printStackTrace();
        }
    }

}
