package ro.kmagic.resettableregions;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ResettableRegions extends JavaPlugin {

    private static ResettableRegions instance;

    private List<Block> brokenBlocks;
    private List<Material> brokenBlocksMaterials;

    private StateFlag RESETTABLE;


    @Override
    public void onEnable() {
        instance = this;
        brokenBlocks = new ArrayList<>();
        Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(), this);

        initTask();
    }

    @Override
    public void onLoad() {
        WorldGuard worldGuard = WorldGuard.getInstance();
        FlagRegistry flagRegistry = worldGuard.getFlagRegistry();

        flagRegistry.register(RESETTABLE = new StateFlag("resettable", false));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void initTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(int i = 0; i < brokenBlocks.size(); i++) {
                Block block = brokenBlocks.get(i);
                Material material = brokenBlocksMaterials.get(i);
                Location location = block.getLocation().clone();

                location.getBlock().setType(material);
            }

            brokenBlocks = new ArrayList<>();
            brokenBlocksMaterials = new ArrayList<>();
        }, 0, 1200);
    }

    public static ResettableRegions getInstance() {
        return instance;
    }

    public void addBlock(Block block, Material type) {
        brokenBlocks.add(block);
        brokenBlocksMaterials.add(type);
    }

    public StateFlag getFlag() {
        return RESETTABLE;
    }
}
