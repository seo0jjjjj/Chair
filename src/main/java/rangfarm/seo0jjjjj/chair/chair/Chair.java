package rangfarm.seo0jjjjj.chair.chair;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Chair extends JavaPlugin {
    private final Vector ADD_CENTER = new Vector(0.5, -1.13125, 0.5);
    private File pluginFolder;
    private File configFile;
    public List<Location> locations = new ArrayList<Location>();

    @Override
    public void onEnable() {
        initConfig();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getCommand("의자").setExecutor(new Kommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initConfig(){
        pluginFolder = getDataFolder();
        configFile = new File(pluginFolder, "config.yml");
        createConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadConfig();
    }
    private void createConfig() {
        if (!pluginFolder.exists()) {
            try {
                pluginFolder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void loadConfig() {
        if (getConfig().getList("의자좌표.locations") != null) {
            for (Object location : getConfig().getList("의자좌표.locations"))
                locations.add((Location) location);
        }
    }

    public ArmorStand dropSeat(Location location) {
        Location fixedlocation = location.clone();
        fixedlocation.add(ADD_CENTER); // (plugin.sittingheight - 0.5)
        ArmorStand drop = location.getWorld().spawn(fixedlocation, ArmorStand.class);
        try {
            drop.setVisible(false);
            drop.teleport(fixedlocation);
            drop.setCustomName("의자");
            drop.setCustomNameVisible(false);
            drop.setInvulnerable(true); // 아머스탠드 무적
            drop.setGravity(false);

        } catch (NullPointerException np) {

            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD+ "[의자 지정 플러그인]"+ChatColor.RED+"오류: 아머스탠드를 설치할 수 없습니다.");
        }
        return drop;
    }
    public ArmorStand getNearestArmorStand(Location blockLocation) {
        ArmorStand nearest = null;
        Location fl = blockLocation.clone().add(ADD_CENTER);
        double distance;
        for (Entity e : blockLocation.getWorld().getNearbyEntities(fl, 0.1, 0.1, 0.1)) {
            if (e instanceof ArmorStand) {
                //의자인지 확인
                if(e.getCustomName().equals("의자")) {
                    nearest = (ArmorStand) e;
                }
            }
        }
        return nearest;
    }
    public boolean setChair(Block block) {
        Location blockLocation = block.getLocation();
        // 이미 지정된 좌표 일 경우
            if (locations.contains(blockLocation)) {
            locations.remove(blockLocation);
            saveConfig();
            return false;
        }
        // 새로운 좌표 일 경우
        else {
            locations.add(blockLocation);
            getConfig().set("의자좌표.locations", locations);
            saveConfig();
            return true;
        }
    }

}
