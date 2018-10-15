package me.mrCookieSlime.Slimefun.api.MySQL;

import com.firesoftitan.play.titansql.*;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLMain {


    private TitanSQL mySQLMain;
    private Table block_storage;
    private HashMap<String, List<HashMap<String, ResultData>>> load_storage;
    private HashMap<Location, Config> blockBackup = new HashMap<Location, Config>();
    private boolean enabled = false;
    public static MySQLMain instance;
    public MySQLMain()
    {
        instance = this;
        mySQLMain = TitanSQL.instance;
        setupTables();
        load_storage = new HashMap<String, List<HashMap<String, ResultData>>>();
        for (World world: Bukkit.getWorlds()) {
            block_storage.search("world", world.getName(), new CallbackResults() {
                @Override
                public void onResult(List<HashMap<String, ResultData>> results) {
                    load_storage.put(world.getName(), results);
                    System.out.println("[Slimefun] MySQL, world: " + world.getName() + " data received for " + results.size() + " blocks.");
                }
            });
        }
        this.enabled = true;

    }
    public void setupTables()
    {
        block_storage = new Table("slimefun_block_storage");
        //Creating table if there isn't one
        //id must be used for the primary key
        block_storage.addDataType("id", DataTypeEnum.CHARARRAY, true, true, true);
        block_storage.addDataType("slimefun_id", DataTypeEnum.CHARARRAY, true, false, false);
        block_storage.addDataType("world", DataTypeEnum.CHARARRAY, true, false, false);
        block_storage.addDataType("json", DataTypeEnum.STRING, true, false, false);
        block_storage.createTable();
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void disable()
    {
        enabled = false;
    }
    public Table getBlock_storage() {
        return block_storage;
    }
    public boolean isLoaded(String worldname)
    {
        return load_storage.containsKey(worldname);
    }
    public List<HashMap<String, ResultData>> getLoad_storage(String worldname)
    {
        return load_storage.get(worldname);
    }
    public void  deleteBackUp(Location l)
    {
        blockBackup.remove(l);
    }
    public Config getBlockBackUp(Location l)
    {
        return blockBackup.get(l);
    }
    public void setBlockBackUp(Location l, String rawJSON)
    {

        try {
            blockBackup.put(l, new BlockInfoConfig(parseJSON(rawJSON)));
        } catch(Exception x) {
            System.err.println(x.getClass().getName());
            System.err.println("[Slimefun] Failed to parse BlockInfo for Block @ " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
            System.err.println(rawJSON);
            System.err.println("[Slimefun] ");
            System.err.println("[Slimefun] IGNORE THIS ERROR UNLESS IT IS SPAMMING");
            System.err.println("[Slimefun] ");
            x.printStackTrace();
        }
    }
    private static Map<String, String> parseJSON(String json) {
        Map<String, String> map = new HashMap<String, String>();

        if (json != null && json.length() > 2) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(json);
                for (Object entry: obj.keySet()) {
                    String key = entry.toString();
                    String value = obj.get(entry).toString();
                    map.put(key, value);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
