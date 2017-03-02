package me.mrCookieSlime.Slimefun;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.Android.AndroidType;
import me.mrCookieSlime.Slimefun.Android.ProgrammableAndroid;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoDisenchanter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoEnchanter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.firesoftitan.AncientAltarCrafter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.firesoftitan.AutomatedAncientAltarCrafter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.firesoftitan.AutomatedVanillaCraftingChamber;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.Soul;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by Daniel on 1/10/2017.
 */
public class TitanHooks {
    public static Config backupChecker = new Config("Slimefun-backups.yml");
    public static Map<String, String> backupCheck = new HashMap<String, String>();

    public static Map<String, ItemStack> recipesV = new HashMap<String, ItemStack>();

    public static ItemStack ANCIENT_ALTAR_CRAFTER_BLOCK = getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWZkMDE2NzY4OTcxNWRmMWFhNTA1NWE2M2VhNmI4YmE2NTZlMmI0YjgxZmNjYWI1M2MzZTIxMDhkODBiODFjIn19fQ==");


    public static ItemStack AUTOMATED_VANILLA_CRAFTING_CHAMBER = new CustomItem(new MaterialData(Material.WORKBENCH), "&eAutomated Vanilla Crafting Chamber", "", "&6Advanced Machine", "&8\u21E8 &e\u26A1 &710 J/Item");
    public static ItemStack ANCIENT_ALTAR_CRAFTER = new CustomItem(ANCIENT_ALTAR_CRAFTER_BLOCK, "&6Ancient Altar Crafter", "", "&6Advanced Machine", "&8\u21E8 &e\u26A1 &750 J/Item");
    public static ItemStack AUTOMATED_ANCIENT_ALTAR_CRAFTER = new CustomItem(new MaterialData(Material.WORKBENCH), "&6Automated Ancient Altar Crafter", "", "&6Advanced Machine", "&8\u21E8 &e\u26A1 &750 J/Item");
    public static ItemStack THERMAL_GENERATOR;
    public static List<AContainer> allMachines = new ArrayList<AContainer>();
    private static LockedCategory ELECTRICITY = null;

    static {
        try {
            THERMAL_GENERATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "&cThermal Generator", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &78192 J Buffer", "&8\u21E8 &e\u26A1 &7500 J/s", "&bRequires:","&63x3 of lava below", "&63x3 of Air above", "&4Could Exploded!");
            ELECTRICITY = new LockedCategory(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU4NDQzMmFmNmYzODIxNjcxMjAyNThkMWVlZThjODdjNmU3NWQ5ZTQ3OWU3YjBkNGM3YjZhZDQ4Y2ZlZWYifX19"), "&bEnergy and Electricity", "", "&a> Click to open"), 5, Categories.MACHINES_1);
        } catch (Exception e) {

        }
    }

    ;



    public TitanHooks()
    {
        for(String s: backupChecker.getKeys())
        {
            backupCheck.put(s, (String)backupChecker.getValue(s));
        }
        clearBackupfromFile();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunStartup.instance, new Runnable() {
            @Override
            public void run() {
                System.out.println("Slimefun is checking backups, maybe lag...");
                //Bukkit.getServer().broadcastMessage(ChatColor.GRAY  + "Slimefun is checking backups, maybe lag...");
                List<String> deleteme = new ArrayList<String>();
                for(String s: backupCheck.keySet())
                {
                    String[] e2 = s.split(",");
                    World world = Bukkit.getWorld(e2[0]);
                    double x = Double.parseDouble(e2[1]);
                    double y = Double.parseDouble(e2[2]);
                    double z = Double.parseDouble(e2[3]);

                    Location place = new Location(world, x, y, z);
                    if (place.getChunk().isLoaded())
                    {
                        if (place.getBlock().getType() == Material.AIR)
                        {
                            deleteme.add(s);
                            continue;
                        }
                    }
                    if (!BlockStorage.hasBlockInfo(place)) {
                        BlockStorage.setBlockInfo(place,(String)backupCheck.get(s),true);

                        System.out.println("SF --------------------> Fixed: " + s);
                        if (!BlockStorage.hasBlockInfo(place))
                        {
                            System.out.println("SF --------------------> ERROR Fixing: " + s);
                            System.out.println("SF ----------------->>> " + (String)backupCheck.get(s));
                            backupCheck.remove(s);
                            break;
                        }
                    }
                }
                for(int i = 0; i <deleteme.size();i++)
                {
                    String[] e2 = deleteme.get(i).split(",");
                    World world = Bukkit.getWorld(e2[0]);
                    double x = Double.parseDouble(e2[1]);
                    double y = Double.parseDouble(e2[2]);
                    double z = Double.parseDouble(e2[3]);

                    Location place = new Location(world, x, y, z);


                    if (BlockStorage.hasBlockInfo(place))
                    {
                        BlockStorage.clearBlockInfo(place);
                        System.out.println("SF --------------------> Delete: " + deleteme.get(i));
                    }
                    else
                    {
                        backupCheck.remove(deleteme.get(i));
                        System.out.println("BC --------------------> Delete: " + deleteme.get(i));
                    }
                }
                //Bukkit.getServer().broadcastMessage(ChatColor.GRAY  + "Slimefun backup check done!");
            }
        }, 300, 1000);
    }
    public void safeDeleteBlocks() {
        Map<Location, Boolean> remove = new HashMap<Location, Boolean>(SlimefunStartup.ticker.delete);

        for (Map.Entry<Location, Boolean> entry: remove.entrySet()) {
            if (entry.getKey().getChunk().isLoaded()) {
                if (entry.getKey().getBlock().getType() != Material.SKULL) {
                    BlockStorage._integrated_removeBlockInfo(entry.getKey(), entry.getValue());
                    SlimefunStartup.instance.myTitanHooks.deleteBackup(entry.getKey());
                }
            }
            SlimefunStartup.ticker.delete.remove(entry.getKey());
        }
    }
    public void clearBackupfromFile() {
        for(String s: backupCheck.keySet())
        {
            backupChecker.setValue(s, null);
        }
    }

    public void SlimeFunShutDown()
    {
        saveBackuptoFile();

    }

    public void saveBackuptoFile() {
        for(String s: backupCheck.keySet())
        {
            backupChecker.setValue(s, (String)backupCheck.get(s));
        }

        backupChecker.save();
    }

    public static boolean checkBackup(Location l)
    {
        if (backupCheck.containsKey(l.getWorld().getName() + "," + l.getBlockX() + ","  + l.getBlockY() + "," + l.getBlockZ()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public static void deleteBackup(Location l)
    {
        boolean wasright =false;
        if (l.getChunk().isLoaded()) {
            if (l.getBlock().getType() == Material.AIR) {
                backupCheck.remove(l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
                wasright = true;
            }
        }
        if (!wasright)
        {
            System.out.println("SF --------------------> Wrong delete: " + l.toString());
            try{
                throw new Exception("Wrong Delete");
            }catch(Exception ex){
                ex.printStackTrace();
                System.out.println(ex.toString());
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void setBackup(Location l, String Json)
    {
        backupCheck.put(l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ(), Json);
    }
    public static void setBackup(Location l)
    {
        Config cfg =  BlockStorage.getBlockInfo(l);

        JSONObject json = new JSONObject();
        for (String key: cfg.getKeys()) {
            json.put(key, cfg.getString(key));
        }
        setBackup(l, json.toJSONString());
    }
    public static ItemStack getHead(String Texture)
    {
        try
        {
            return CustomSkull.getItem(Texture);
        }catch (Exception e)
        {
            return null;
        }
    }
    public ItemStack getTool(ItemStack item)
    {
        for (ItemStack mTool: new ItemStack[] {SlimefunItems.DURALUMIN_MULTI_TOOL, SlimefunItems.SOLDER_MULTI_TOOL, SlimefunItems.BILLON_MULTI_TOOL, SlimefunItems.STEEL_MULTI_TOOL, SlimefunItems.DAMASCUS_STEEL_MULTI_TOOL, SlimefunItems.REINFORCED_ALLOY_MULTI_TOOL, SlimefunItems.CARBONADO_MULTI_TOOL}) {
            int i;
            for (i = 0; i < item.getItemMeta().getLore().size(); i++)
            {
                if (!item.getItemMeta().getLore().get(i).startsWith(ChatColor.AQUA + "Titan"))
                {
                    break;
                }
            }
            if (mTool.getItemMeta().getLore().get(0).equalsIgnoreCase(item.getItemMeta().getLore().get(i))) {
                return mTool;
            }
        }
        return null;
    }
    public void titanEnchants(EntityDeathEvent e, Iterator<ItemStack> drops, ItemStack item) {
        if (item.getItemMeta() != null)
        {
            if (item.getItemMeta().getLore() != null)
            {
                List<String> lore = item.getItemMeta().getLore();
                for (int i = 0; i < lore.size(); i++) {
                    System.out.println(lore.get(i));
                    if (lore.get(i).equalsIgnoreCase(ChatColor.AQUA + "titan." + ChatColor.RED + "soulbound")) {
                        Soul.storeItem(e.getEntity().getUniqueId(), item);
                        drops.remove();
                        return;
                    }
                }
            }
        }
    }
    public void open(BlockMenu bm){
        bm.changes++;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        int processing = 0;
        for(int i = 0; i<this.allMachines.size();i++)
        {
            AContainer thisMachine =  this.allMachines.get(i);
            Set<Block> toEnd = thisMachine.processing.keySet();
            for(Block e:toEnd)
            {
                processing++;
            }


        }

        processing = processing / 4;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("save")) {
                if (sender.hasPermission("slimefun.cheat.items")) {
                    SlimefunStartup.instance.Saver.run();
                    sender.sendMessage("Slimefun saving... Processing:" + processing);

                    return true;

                }
            }
        }
        return false;
    }
    public int compare(BlockMenu menu, Integer slot1, Integer slot2) {
        int Amtslot1 =999;
        int Amtslot2 =999;
        if (menu.getItemInSlot(slot1) != null)
        {
            Amtslot1 = menu.getItemInSlot(slot1).getAmount();
        }
        if (menu.getItemInSlot(slot2) != null)
        {
            Amtslot2 = menu.getItemInSlot(slot2).getAmount();
        }
        return Amtslot1 - Amtslot2;
    }
    public void endProccess(Block b, AContainer AC)
    {
        BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 15), " "));
        AC.pushItems(b, AContainer.processing.get(b).getOutput());

        AC.progress.remove(b);
        AC.processing.remove(b);
    }
    public void AutoEnchanter_tick(Block b, AutoEnchanter AE) {
        if (AE.isProcessing(b)) {
            int timeleft = AE.progress.get(b);
            if (timeleft > 0) {

                ItemStack item = AE.getProgressBar().clone();
                item.setDurability(MachineHelper.getDurability(item, timeleft, AE.processing.get(b).getTicks()));
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(" ");
                List<String> lore = new ArrayList<String>();
                lore.add(MachineHelper.getProgress(timeleft, AE.processing.get(b).getTicks()));
                lore.add("");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);

                BlockStorage.getInventory(b).replaceExistingItem(22, item);

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < AE.getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -AE.getEnergyConsumption());
                    AE.progress.put(b, timeleft - 1);
                }
                else AE.progress.put(b, timeleft - 1);
            }
            else {
                endProccess(b, AE);
            }
        }
        else {
            MachineRecipe r = null;
            slots:
            for (int slot: AE.getInputSlots()) {
                ItemStack target = BlockStorage.getInventory(b).getItemInSlot(slot == AE.getInputSlots()[0] ? AE.getInputSlots()[1]: AE.getInputSlots()[0]);
                ItemStack item = BlockStorage.getInventory(b).getItemInSlot(slot);
                List<String> lore = null;
                if (item != null && item.getType() == Material.ENCHANTED_BOOK && target != null) {
                    Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
                    Set<ItemEnchantment> enchantments2 = new HashSet<ItemEnchantment>();
                    int amount = 0;
                    if (item.getItemMeta().hasDisplayName())
                    {
                        if ((item.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Titan's Enchanted Book")) || (item.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Titan Book")))
                        {
                            List<String> lorlore = item.getItemMeta().getLore();
                            lore = new ArrayList<String>();
                            for(int i = 0; i<lorlore.size(); i++)
                            {
                                lore.add(ChatColor.AQUA + "Titan." + lorlore.get(i));
                            }

                            amount++;
                        }
                    }
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    for (Map.Entry<Enchantment, Integer> e: meta.getStoredEnchants().entrySet()) {
                        if (e.getKey().canEnchantItem(target)) {
                            amount++;
                            enchantments.put(e.getKey(), e.getValue());
                        }
                    }
                    if (Slimefun.isEmeraldEnchantsInstalled()) {
                        for (ItemEnchantment enchantment: EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)) {
                            if (EmeraldEnchants.getInstance().getRegistry().isApplicable(target, enchantment.getEnchantment()) && EmeraldEnchants.getInstance().getRegistry().getEnchantmentLevel(target, enchantment.getEnchantment().getName()) < enchantment.getLevel()) {
                                amount++;
                                enchantments2.add(enchantment);
                            }
                        }
                    }

                    if (amount > 0) {
                        ItemStack newItem = target.clone();

                        for (Map.Entry<Enchantment, Integer> e: enchantments.entrySet()) {
                            newItem.addUnsafeEnchantment(e.getKey(), e.getValue());
                        }
                        for (ItemEnchantment e: enchantments2) {
                            EmeraldEnchants.getInstance().getRegistry().applyEnchantment(newItem, e.getEnchantment(), e.getLevel());
                        }
                        if (lore != null)
                        {
                            ItemMeta newMeta = newItem.getItemMeta();
                            if (newMeta != null)
                            {
                                if (newMeta.getLore() != null)
                                {
                                    List<String> newLore = newMeta.getLore();

                                    for(int i = 0; i<newLore.size(); i++)
                                    {
                                        lore.add(newLore.get(i));
                                    }
                                }

                                newMeta.setLore(lore);

                            }
                            newItem.setItemMeta(newMeta);
                        }
                        newItem.setAmount(1);
                        r = new MachineRecipe(75 * amount, new ItemStack[] {target, item}, new ItemStack[] {newItem, new ItemStack(Material.BOOK)});
                    }
                    break slots;
                }
            }

            if (r != null) {
                if (!AE.fits(b, r.getOutput())) return;
                for (int slot: AE.getInputSlots()) {
                    BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
                }
                AE.processing.put(b, r);
                AE.progress.put(b, r.getTicks());
            }
        }
    }
    public void titanClose() {
        List<Block> toRemove;

        for(int i = 0; i<this.allMachines.size();i++)
        {
            AContainer thisMachine =  this.allMachines.get(i);
            Set<Block> toEnd = thisMachine.processing.keySet();
            toRemove = new ArrayList<Block>();
            for(Block e:toEnd)
            {
                toRemove.add(e);
            }
            for(int j = 0; j < toRemove.size(); j++) {
                Block e = toRemove.get(j);
                this.endProccess(e, thisMachine);
                thisMachine.getBlockMenu(e).changes++;
            }
            toRemove.clear();

        }

        SlimefunStartup.instance.Saver.run();
    }
    public void AutoDisenchanter_tick(Block b, AutoDisenchanter AD) {
        if (AD.isProcessing(b)) {
            int timeleft = AD.progress.get(b);
            if (timeleft > 0) {
                ItemStack item = AD.getProgressBar().clone();
                item.setDurability(MachineHelper.getDurability(item, timeleft, AD.processing.get(b).getTicks()));
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(" ");
                List<String> lore = new ArrayList<String>();
                lore.add(MachineHelper.getProgress(timeleft, AD.processing.get(b).getTicks()));
                lore.add("");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);

                BlockStorage.getInventory(b).replaceExistingItem(22, item);

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < AD.getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -AD.getEnergyConsumption());
                    AD.progress.put(b, timeleft - 1);
                }
                else AD.progress.put(b, timeleft - 1);

            }
            else {
                endProccess(b, AD);
            }
        }
        else {
            MachineRecipe r = null;
            Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
            Set<ItemEnchantment> enchantments2 = new HashSet<ItemEnchantment>();
            slots:
            for (int slot: AD.getInputSlots()) {
                ItemStack target = BlockStorage.getInventory(b).getItemInSlot(slot == AD.getInputSlots()[0] ? AD.getInputSlots()[1]: AD.getInputSlots()[0]);
                ItemStack item = BlockStorage.getInventory(b).getItemInSlot(slot);
                if(SlimefunItem.getByItem(item) != null && !SlimefunItem.getByItem(item).isDisenchantable()) return;
                if (item != null && target != null && target.getType() == Material.BOOK) {
                    int amount = 0;

                    for (Map.Entry<Enchantment, Integer> e: item.getEnchantments().entrySet()) {
                        enchantments.put(e.getKey(), e.getValue());
                        amount++;
                    }
                    if (Slimefun.isEmeraldEnchantsInstalled()) {
                        for (ItemEnchantment enchantment: EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)) {
                            amount++;
                            enchantments2.add(enchantment);
                        }
                    }
                    if (amount > 0) {
                        ItemStack newItem = item.clone();
                        ItemStack book = target.clone();
                        book.setAmount(1);
                        book.setType(Material.ENCHANTED_BOOK);
                        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
                        for (Map.Entry<Enchantment,Integer> e: enchantments.entrySet()) {
                            newItem.removeEnchantment(e.getKey());
                            meta.addStoredEnchant(e.getKey(), e.getValue(), true);
                        }
                        book.setItemMeta(meta);

                        for (ItemEnchantment e: enchantments2) {
                            EmeraldEnchants.getInstance().getRegistry().applyEnchantment(book, e.getEnchantment(), e.getLevel());
                            EmeraldEnchants.getInstance().getRegistry().applyEnchantment(newItem, e.getEnchantment(), 0);
                        }
                        newItem.setAmount(1);
                        r = new MachineRecipe(100 * amount, new ItemStack[] {target, item}, new ItemStack[] {newItem, book});
                        break slots;
                    }
                }
            }

            if (r != null) {
                if (!AD.fits(b, r.getOutput())) return;
                for (int slot: AD.getInputSlots()) {
                    BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
                }
                AD.processing.put(b, r);
                AD.progress.put(b, r.getTicks());
            }
        }
    }
    public static boolean equalsLore(List<String> lore, List<String> lore2) {
        String string1 = "", string2 = "";
        for (String string: lore) {
            if (!string.startsWith(ChatColor.AQUA + "Titan")) {
                if (!string.startsWith("&e&e&7")) string1 = string1 + "-NEW LINE-" + string;
            }
        }
        for (String string: lore2) {
            if (!string.startsWith(ChatColor.AQUA + "Titan")) {
                if (!string.startsWith("&e&e&7")) string2 = string2 + "-NEW LINE-" + string;
            }
        }
        return string1.equals(string2);
    }
    public static void setupVanillaCraft()
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
            @Override
            public void run() {
                Iterator iterator2 = Bukkit.recipeIterator();
                while (iterator2.hasNext()) {
                    Recipe r = (Recipe) iterator2.next();
                    if (r instanceof ShapedRecipe)
                    {
                        ShapedRecipe SR = (ShapedRecipe)r;
                        String[] shapeS = SR.getShape();
                        Map<Character, ItemStack> MapCM = SR.getIngredientMap();
                        ItemStack[] Reci = {null, null, null, null, null, null, null, null, null};
                        String myName = "";
                        Character[] key = new Character[9];
                        int counter = 0;
                        int[] yH = {0,1,2,3,4,5,6,7,8};//{0,3,6,1,4,7,2,5,8};
                        String teShape = "";
                        for (int o = 0; o < shapeS.length;o++ )
                        {
                            shapeS[o] = shapeS[o] + "***********";
                            shapeS[o] = shapeS[o].substring(0, 3);

                            for (int p = 0; p < shapeS[o].length();p++ )
                            {
                                key[yH[counter]] = shapeS[o].charAt(p);
                                counter++;
                            }
                            teShape = teShape + shapeS[o]  + "<>";
                        }
                        for (int o = shapeS.length; o < 3;o++ )
                        {
                            String missed = "***";

                            for (int p = 0; p < missed.length();p++ )
                            {
                                key[yH[counter]] = missed.charAt(p);
                                counter++;
                            }
                            teShape = teShape + "XXX"  + "<>";
                        }
                        Short Dura = SR.getResult().getDurability();

                        boolean goodRec = false;
                        for (int o = 0; o < 9;o++ )
                        {

                            Reci[o] = MapCM.get(key[o]);

                            if (Reci[o] == null || Reci[o].getType() == Material.AIR)
                            {
                                myName = myName + "null" + ChatColor.GRAY;
                            }
                            else {
                                goodRec = true;
                                myName = myName + Reci[o].getType().toString() + ":" + Reci[o].getDurability() + ChatColor.GRAY;
                            }
                        }
                        if (goodRec) {
                            recipesV.put(myName, r.getResult());
                        }
                    }
                }
                System.out.println("[Slimefun4]: All vinilla recipes are loaded!");
            }
        }, 300);
    }
    public void registerCargo()
    {

    }
    public void registerNewRecipes()
    {
        new AutomatedVanillaCraftingChamber(TitanHooks.ELECTRICITY, AUTOMATED_VANILLA_CRAFTING_CHAMBER, "AUTOMATED_VANILLA_CRAFTING_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.WORKBENCH), null, SlimefunItems.CARGO_MOTOR, SlimefunItems.COPPER_INGOT, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public int getEnergyConsumption() {
                return 10;
            }
        }.registerChargeableBlock(true, 256);

        new AncientAltarCrafter(TitanHooks.ELECTRICITY, ANCIENT_ALTAR_CRAFTER, "ANCIENT_ALTAR_CRAFTER_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ANCIENT_PEDESTAL, null, SlimefunItems.CARGO_MOTOR, SlimefunItems.ANCIENT_ALTAR, SlimefunItems.CARGO_MOTOR, SlimefunItems.ANCIENT_PEDESTAL, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ANCIENT_PEDESTAL}) {

            @Override
            public int getEnergyConsumption() {
                return 50;
            }
        }.registerChargeableBlock(true, 256);

        new AutomatedAncientAltarCrafter(TitanHooks.ELECTRICITY, AUTOMATED_ANCIENT_ALTAR_CRAFTER, "AUTOMATED_ANCIENT_ALTAR_CRAFTER_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.WORKBENCH), null, SlimefunItems.CARGO_MOTOR, ANCIENT_ALTAR_CRAFTER, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public int getEnergyConsumption() {
                return 50;
            }
        }.registerChargeableBlock(true, 256);


        new SlimefunItem(TitanHooks.ELECTRICITY, TitanHooks.THERMAL_GENERATOR, "THERMAL_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HEATING_COIL, SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.HEATING_COIL})
                .register(true, new EnergyTicker() {

                    @Override
                    public double generateEnergy(Location l, SlimefunItem item, Config data) {
                        try {
                            if (l == null) {
                                return 0;
                            }
                            Location lavaCheck = l.clone().add(0, -1, 0);
                            Location AirCheck = l.clone().add(0, 1, 0);
                            boolean Run = true;
                            boolean explode = false;
                            if (l.getChunk().isLoaded()) {
                                try {
                                    for (int x = -1; x < 2; x++) {
                                        for (int z = -1; z < 2; z++) {
                                            if (lavaCheck.clone().add(x, 0, z).getBlock().getType() != Material.STATIONARY_LAVA) {
                                                Run = false;
                                            }
                                            if (AirCheck.clone().add(x, 0, z).getBlock().getType() != Material.AIR) {
                                                explode = true;
                                            }
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }
                            if (Run && !explode) {
                                double past = 256 * (1D - (l.getBlockY() / 100D));
                                return past;
                            } else {
                                if (explode && Run) {
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {

                                        @Override
                                        public void run() {
                                            AirCheck.getWorld().createExplosion(AirCheck.add(0, 7, 0).clone(), 6);
                                            BlockStorage.clearBlockInfo(l);

                                            for (int y = 0; y < 100; y++) {
                                                for (int x = -1; x < 2; x++) {
                                                    for (int z = -1; z < 2; z++) {
                                                        Location exp = l.clone().add(x, y, z);
                                                        if (BlockStorage.hasBlockInfo(exp)) {
                                                            BlockStorage.clearBlockInfo(exp);
                                                        }
                                                        AirCheck.getWorld().getBlockAt(exp).setType(Material.AIR);
                                                    }
                                                }
                                            }
                                        }
                                    }, 20);

                                }
                                return 0;
                            }
                        }
                        catch (Exception e)
                        {
                            return 0;
                        }
                    }
                        //8192
                    @Override
                    public boolean explode(Location l) {
                        return false;
                    }

                });
        ChargableBlock.registerChargableBlock("THERMAL_GENERATOR", 8192, false);



        Slimefun.registerResearch(new Research(79001, "Thermal Power Plant", 89), TitanHooks.THERMAL_GENERATOR);
        Slimefun.registerResearch(new Research(79002, "Ancient Altar Crafter", 75), TitanHooks.ANCIENT_ALTAR_CRAFTER);
        Slimefun.registerResearch(new Research(79003, "Vanilla Auto Crafter", 25), TitanHooks.AUTOMATED_VANILLA_CRAFTING_CHAMBER);
        Slimefun.registerResearch(new Research(79004, "Automated Ancient Altar Crafter", 25), TitanHooks.AUTOMATED_ANCIENT_ALTAR_CRAFTER);
    }

    public static void vanillaCraft(AutomatedVanillaCraftingChamber machine, Block b, BlockMenu menu) {
        String myName = "";
        boolean craft = false;
        HashMap<String, Short> MatList = new HashMap<String, Short>();
        for (int o = 0; o < 9;o++ )
        {
            ItemStack item = menu.getItemInSlot(machine.getInputSlots()[o]);
            if (item == null)
            {
                myName = myName + "null" + ChatColor.GRAY;
            }
            else {
                if (item.getType() == Material.AIR)
                {
                    myName = myName + "null" + ChatColor.GRAY;
                }
                else {
                    if (item.getAmount() < 2 && item.getMaxStackSize() > 1)
                    {
                        return;
                    }
                    craft = true;//
                    myName = myName + item.getType().toString() + ":" + item.getDurability() + ChatColor.GRAY;
                    MatList.put(item.getType().toString() , item.getDurability());
                }
            }
        }


        if (craft) {
            craftFinal(machine, b, menu, myName, MatList);
        }



    }
    private static void craftFinal(AutomatedVanillaCraftingChamber machine, Block b, BlockMenu menu, String myName, HashMap<String, Short> matList) {
        craftFinal(machine, b, menu, myName, matList, 0);
    }
    private static void craftFinal(AutomatedVanillaCraftingChamber machine, Block b, BlockMenu menu, String myName, HashMap<String, Short> matList, int times) {
        times++;
        if (!checkAndCraft(machine, b, menu, myName)) {
            for(String mat: matList.keySet())
            {
                String newList = myName.replace(mat + ":" + matList.get(mat), mat + ":32767" );
                if (!newList.equals(myName)) {
                    if (checkAndCraft(machine, b, menu, newList)) {
                        break;
                    } else {
                        if (times < 10) {
                            craftFinal(machine, b, menu, newList, matList, times);
                        }
                    }
                }
            }
        }
    }

    private static boolean checkAndCraft(AutomatedVanillaCraftingChamber machine, Block b, BlockMenu menu, String myName) {
        if (recipesV.containsKey(myName)) {
            ItemStack output = recipesV.get(myName);

            if (machine.fits(b, new ItemStack[]{output})) {
                machine.pushItems(b, new ItemStack[]{output});
                ChargableBlock.addCharge(b, -machine.getEnergyConsumption());
                for (int j = 0; j < 9; j++) {
                    if (menu.getItemInSlot(machine.getInputSlots()[j]) != null)
                        menu.replaceExistingItem(machine.getInputSlots()[j], InvUtils.decreaseItem(menu.getItemInSlot(machine.getInputSlots()[j]), 1));
                }

            }
            return  true;
        }
        return  false;
    }
    public void SetupAnd(boolean oktoRun)
    {
        if (!oktoRun)
        {
            return;
        }
        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID, "PROGRAMMABLE_ANDROID", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.COAL_GENERATOR, SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.CHEST), SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.NONE;
            }

            @Override
            public float getFuelEfficiency() {
                return 1;
            }

            @Override
            public int getTier() {
                return 1;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_MINER, "PROGRAMMABLE_ANDROID_MINER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_PICKAXE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.MINER;
            }

            @Override
            public float getFuelEfficiency() {
                return 1;
            }

            @Override
            public int getTier() {
                return 1;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, "PROGRAMMABLE_ANDROID_FARMER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.FARMER;
            }

            @Override
            public float getFuelEfficiency() {
                return 1;
            }

            @Override
            public int getTier() {
                return 1;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER, "PROGRAMMABLE_ANDROID_WOODCUTTER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.DIAMOND_AXE), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_AXE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.WOODCUTTER;
            }

            @Override
            public float getFuelEfficiency() {
                return 1;
            }

            @Override
            public int getTier() {
                return 1;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_FISHERMAN, "PROGRAMMABLE_ANDROID_FISHERMAN", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.FISHERMAN;
            }

            @Override
            public float getFuelEfficiency() {
                return 1;
            }

            @Override
            public int getTier() {
                return 1;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER, "PROGRAMMABLE_ANDROID_BUTCHER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.FIGHTER;
            }

            @Override
            public float getFuelEfficiency() {
                return 1;
            }

            @Override
            public int getTier() {
                return 1;
            }

        }
                .register(true);

        new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.ANDROID_INTERFACE_ITEMS, "ANDROID_INTERFACE_ITEMS", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.BASIC_CIRCUIT_BOARD, new MaterialData(Material.STAINED_GLASS, (byte) 11).toItemStack(1), SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET})
                .register(true);

        new SlimefunItem(Categories.ELECTRICITY, SlimefunItems.ANDROID_INTERFACE_FUEL, "ANDROID_INTERFACE_FUEL", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, new MaterialData(Material.STAINED_GLASS, (byte) 14).toItemStack(1), SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET, SlimefunItems.PLASTIC_SHEET})
                .register(true);


        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_2, "PROGRAMMABLE_ANDROID_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.COMBUSTION_REACTOR, SlimefunItems.PROGRAMMABLE_ANDROID, new ItemStack(Material.CHEST), SlimefunItems.PLASTIC_SHEET, SlimefunItems.POWER_CRYSTAL, SlimefunItems.PLASTIC_SHEET}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.NONE;
            }

            @Override
            public float getFuelEfficiency() {
                return 1.5F;
            }

            @Override
            public int getTier() {
                return 2;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_2_FISHERMAN, "PROGRAMMABLE_ANDROID_2_FISHERMAN", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.FISHERMAN;
            }

            @Override
            public float getFuelEfficiency() {
                return 1.5F;
            }

            @Override
            public int getTier() {
                return 2;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_2_BUTCHER, "PROGRAMMABLE_ANDROID_2_BUTCHER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.FIGHTER;
            }

            @Override
            public float getFuelEfficiency() {
                return 1.5F;
            }

            @Override
            public int getTier() {
                return 2;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_2_FARMER, "PROGRAMMABLE_ANDROID_2_FARMER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER, null, new ItemStack(Material.DIAMOND_HOE), SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.DIAMOND_HOE), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.ADVANCED_FARMER;
            }

            @Override
            public float getFuelEfficiency() {
                return 1.5F;
            }

            @Override
            public int getTier() {
                return 2;
            }

        }
                .register(true);


        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_3, "PROGRAMMABLE_ANDROID_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.PLASTIC_SHEET, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.NUCLEAR_REACTOR, SlimefunItems.PROGRAMMABLE_ANDROID_2, new ItemStack(Material.CHEST), SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BLISTERING_INGOT_3}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.NONE;
            }

            @Override
            public float getFuelEfficiency() {
                return 1F;
            }

            @Override
            public int getTier() {
                return 3;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_3_FISHERMAN, "PROGRAMMABLE_ANDROID_3_FISHERMAN", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null, new ItemStack(Material.FISHING_ROD), SlimefunItems.PROGRAMMABLE_ANDROID_3, new ItemStack(Material.FISHING_ROD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.FISHERMAN;
            }

            @Override
            public float getFuelEfficiency() {
                return 1F;
            }

            @Override
            public int getTier() {
                return 3;
            }

        }
                .register(true);

        new ProgrammableAndroid(Categories.ELECTRICITY, SlimefunItems.PROGRAMMABLE_ANDROID_3_BUTCHER, "PROGRAMMABLE_ANDROID_3_BUTCHER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.GPS_TRANSMITTER_3, null, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.PROGRAMMABLE_ANDROID_3, new ItemStack(Material.DIAMOND_SWORD), null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public AndroidType getAndroidType() {
                return AndroidType.FIGHTER;
            }

            @Override
            public float getFuelEfficiency() {
                return 1F;
            }

            @Override
            public int getTier() {
                return 3;
            }

        }
                .register(true);
        //Slimefun.registerResearch(new Research(179, "Programmable Androids", 50), SlimefunItems.PROGRAMMABLE_ANDROID, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER, SlimefunItems.PROGRAMMABLE_ANDROID_FISHERMAN, SlimefunItems.PROGRAMMABLE_ANDROID_MINER, SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER);
        //Slimefun.registerResearch(new Research(191, "Butcher Androids", 32), SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER);
        //Slimefun.registerResearch(new Research(194, "Advanced Androids", 60), SlimefunItems.PROGRAMMABLE_ANDROID_2);
        //Slimefun.registerResearch(new Research(195, "Advanced Androids - Butcher", 30), SlimefunItems.PROGRAMMABLE_ANDROID_2_BUTCHER);
        //Slimefun.registerResearch(new Research(196, "Advanced Androids - Fisherman", 30), SlimefunItems.PROGRAMMABLE_ANDROID_2_FISHERMAN);
        //Slimefun.registerResearch(new Research(222, "Empowered Androids", 60), SlimefunItems.PROGRAMMABLE_ANDROID_3);
        //Slimefun.registerResearch(new Research(223, "Empowered Androids - Butcher", 30), SlimefunItems.PROGRAMMABLE_ANDROID_3_BUTCHER);
        //Slimefun.registerResearch(new Research(224, "Empowered Androids - Fisherman", 30), SlimefunItems.PROGRAMMABLE_ANDROID_3_FISHERMAN);
        //Slimefun.registerResearch(new Research(233, "Advanced Androids - Farmer", 30), SlimefunItems.PROGRAMMABLE_ANDROID_2_FARMER);
    }
    public void converSpawnerType(InventoryClickEvent e) {
        ItemStack CI = e.getCursor();
        ItemStack II = e.getCurrentItem();
        if (II != null && CI != null) {

            if (CI.getType() == Material.MONSTER_EGG) {
                if (II.hasItemMeta()) {
                    if (II.getItemMeta().hasDisplayName()) {
                        if (II.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Powered Spawner " + ChatColor.GRAY + "(")) {
                            if (II.getAmount() <= CI.getAmount()) {
                                String Stype = CI.toString().split(", id=")[1].replace("}}", "");
                                Stype = WordUtils.capitalize(EntityType.fromName(Stype).name().replace("_", " ").toLowerCase());
                                ItemMeta IM = II.getItemMeta();
                                IM.setDisplayName(ChatColor.YELLOW + "Powered Spawner " + ChatColor.GRAY + "(" + Stype + ")");
                                if (CI.getAmount() < II.getAmount() + 1) {
                                    e.setCursor(null);
                                } else {
                                    CI.setAmount(CI.getAmount() - II.getAmount());
                                    e.setCursor(CI.clone());
                                }
                                II.setItemMeta(IM);
                                e.setCurrentItem(II.clone());
                                e.setCancelled(true);
                            }
                        } else {
                            e.getWhoClicked().sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + "Slimefun" + ChatColor.GOLD + "]: " + ChatColor.AQUA + "You need more spawn eggs for that many spawners!");
                        }
                    }
                }
                if (SlimefunManager.isItemSimiliar(II, SlimefunItems.REPAIRED_SPAWNER, false)) {
                    if (II.getAmount() <= CI.getAmount()) {
                        String Stype = CI.toString().split(", id=")[1].replace("}}", "");
                        ItemMeta IM = II.getItemMeta();
                        List<String> Lore = IM.getLore();
                        for (int i = 0; i < Lore.size(); i++) {
                            if (ChatColor.stripColor(Lore.get(i)).startsWith("Type: ")) {
                                Stype = WordUtils.capitalize(EntityType.fromName(Stype).name().replace("_", " ").toLowerCase());
                                Lore.set(i, ChatColor.GRAY + "Type: " + ChatColor.AQUA + Stype);
                            }
                        }
                        if (CI.getAmount() < II.getAmount() + 1) {
                            e.setCursor(null);
                        } else {
                            CI.setAmount(CI.getAmount() - II.getAmount());
                            e.setCursor(CI.clone());
                        }
                        IM.setLore(Lore);
                        II.setItemMeta(IM);
                        e.setCurrentItem(II.clone());
                        e.setCancelled(true);
                    } else {
                        e.getWhoClicked().sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + "Slimefun" + ChatColor.GOLD + "]: " + ChatColor.AQUA + "You need more spawn eggs for that many spawners!");
                    }
                }
            }
        }
    }
    public static int getFrequency(Location l) {
        try {
            return Integer.parseInt(BlockStorage.getBlockInfo(l).getString("frequency"));
        }
        catch (Exception e)
        {
            System.out.println("[SlimeFun-MyError] " + l.getWorld() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
            System.out.println(BlockStorage.getBlockInfo(l).getString("frequency"));
            return 1;
        }

    }
    public static SlimefunItem getByTexture(String Texture, List<SlimefunItem> items) {
        try {
            if (Texture == null) return null;
            if (Texture.equals("")) return null;
            for (SlimefunItem sfi : items) {
                if (sfi != null) {
                    if (sfi.getItem() != null) {
                        if (sfi.getItem().getType() != null) {
                            if (sfi.getItem().getType() == Material.SKULL_ITEM) {
                                String SFS = CustomSkull.getTexture(sfi.getItem());
                                if (SFS != null) {
                                    if (SFS.equals(Texture)) {
                                        return sfi;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }
    public static boolean isItemSimiliar(ItemStack item, ItemStack SFitem, boolean lore, SlimefunManager.DataType data, List<Material> data_safe) {
        if (item == null) return SFitem == null;
        if (SFitem == null) return false;

        if (item.getType() == SFitem.getType() && item.getAmount() >= SFitem.getAmount()) {
            if (data.equals(SlimefunManager.DataType.ALWAYS) || (data.equals(SlimefunManager.DataType.IF_COLORED) && data_safe.contains(item.getType()))) {
                if (data_safe.contains(item.getType())) {
                    if (item.getData().getData() != SFitem.getData().getData()) {
                        if (!(SFitem.getDurability() == item.getData().getData() && SFitem.getData().getData() == item.getDurability())) return false;
                    }
                }
                else if (data.equals(SlimefunManager.DataType.ALWAYS) && item.getDurability() != SFitem.getDurability()) {
                    return false;
                }
            }

            if (item.hasItemMeta() && SFitem.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName() && SFitem.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().equals(SFitem.getItemMeta().getDisplayName())) {
                        if (lore) {
                            if (item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore()) {
                                for (String string: item.getItemMeta().getLore()) {
                                    if (!string.startsWith(ChatColor.AQUA + "Titan")) {
                                        return false;
                                    }
                                }
                                return true;
                            }
                            if (item.getItemMeta().hasLore() && SFitem.getItemMeta().hasLore()) {
                                return SlimefunManager.equalsLore(item.getItemMeta().getLore(), SFitem.getItemMeta().getLore());
                            }
                            else return !item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore();
                        }
                        else return true;
                    }
                    else return false;
                }
                else if (!item.getItemMeta().hasDisplayName() && !SFitem.getItemMeta().hasDisplayName()) {
                    if (lore) {
                        if (item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore()) {
                            for (String string: item.getItemMeta().getLore()) {
                                if (!string.startsWith(ChatColor.AQUA + "Titan")) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        if (item.getItemMeta().hasLore() && SFitem.getItemMeta().hasLore()) {
                            return SlimefunManager.equalsLore(item.getItemMeta().getLore(), SFitem.getItemMeta().getLore());
                        }
                        else return !item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore();
                    }
                    else return true;
                }
                else return false;
            }
            else return !item.hasItemMeta() && !SFitem.hasItemMeta();
        }
        else return false;
    }
}