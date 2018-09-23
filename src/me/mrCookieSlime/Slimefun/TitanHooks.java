package me.mrCookieSlime.Slimefun;


import me.badbones69.crazyenchantments.api.CEnchantments;
import me.badbones69.crazyenchantments.api.CrazyEnchantments;
import me.badbones69.crazyenchantments.api.EnchantmentType;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoDisenchanter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoEnchanter;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.Soul;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

import static me.mrCookieSlime.Slimefun.api.energy.ChargableBlock.getCharge;
import static me.mrCookieSlime.Slimefun.api.energy.ChargableBlock.getMaxCharge;

/**
 * Created by Daniel on 1/10/2017.
 */
public class TitanHooks {
    public static Map<Block, Long> blockTicks = new HashMap<Block, Long>();

    public static List<AContainer> allMachines = new ArrayList<AContainer>();





    public TitanHooks()
    {

    }
    public static void FurnaceBurnFix(Block b, int speed)
    {
        try {
            if (b.getLocation().getChunk().isLoaded()) {
                Furnace furnace = (Furnace)b.getState();

                if (furnace.getCookTime() > 0) {
                    furnace.setCookTime((short) (furnace.getCookTime() + speed * 10));
                    furnace.update();

                }
                //((Furnace) b.getState()).update(true, false);
            }
        } catch(NullPointerException x) {
        }

    }
    public static void updateTexture(final Location l) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {

            @Override
            public void run() {
                try {
                    Block b = l.getBlock();
                    int charge = getCharge(b), capacity = getMaxCharge(b);
                    if (b.getState() instanceof Skull) {
                        if (charge < (int) (capacity * 0.25D)) CustomSkull.setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ==");
                        else if (charge < (int) (capacity * 0.5D)) CustomSkull.setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA1MzIzMzk0YTdkOTFiZmIzM2RmMDZkOTJiNjNjYjQxNGVmODBmMDU0ZDA0NzM0ZWEwMTVhMjNjNTM5In19fQ==");
                        else if (charge < (int) (capacity * 0.75D)) CustomSkull.setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU4NDQzMmFmNmYzODIxNjcxMjAyNThkMWVlZThjODdjNmU3NWQ5ZTQ3OWU3YjBkNGM3YjZhZDQ4Y2ZlZWYifX19");
                        else CustomSkull.setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2EyNTY5NDE1YzE0ZTMxYzk4ZWM5OTNhMmY5OWU2ZDY0ODQ2ZGIzNjdhMTNiMTk5OTY1YWQ5OWM0MzhjODZjIn19fQ==");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public boolean checkforError(final Block b)
    {
        try
        {
            if (b.getChunk().isLoaded())
            {

            }
            return false;
        }
        catch (Exception e)
        {
            return  true;
        }
    }

    public static boolean checkForID(String Info)
    {
        if (Info.equalsIgnoreCase("id"))
        {
            return true;
        }
        return false;
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("save")) {
                    if (sender.hasPermission("slimefun.cheat.items")) {
                        SlimefunStartup.instance.Saver.run();
                        sender.sendMessage("Slimefun saving...");

                        return true;

                    }
                }
            }
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
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
        BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));
        AC.pushItems(b, AContainer.processing.get(b).getOutput());

        AC.progress.remove(b);
        AC.processing.remove(b);
    }
    public int fromRoman(String i)
    {
        return i.equals("I")?1:i.equals("II")?2:i.equals("III")?3:i.equals("IV")?4:i.equals("V")?5:i.equals("VI")?6:i.equals("VII")?7:i.equals("VIII")?8:i.equals("IX")?9:i.equals("X")?10:10;
    }
    public String toRoman(int i)
    {
        return i == 0?"I":(i == 1?"I":(i == 2?"II":(i == 3?"III":(i == 4?"IV":(i == 5?"V":(i == 6?"VI":(i == 7?"VII":(i == 8?"VIII":(i == 9?"IX":(i == 10?"X":String.valueOf(i)))))))))));
    }
    public int getTimePassed(Block b)
    {
        if (!blockTicks.containsKey(b))
        {
            blockTicks.put(b, System.currentTimeMillis());
            return 1;
        }
        Long first = blockTicks.get(b);
        Long passed = System.currentTimeMillis() - first;
        passed = passed / 500; // 1/2 seconds
        int out = Integer.parseInt(passed + "");
        blockTicks.put(b, System.currentTimeMillis());
        if (out < 1)
        {
            out = 1;
        }
        return out;
    }
    public void clearTimePassed(Block b)
    {
        if (blockTicks.containsKey(b))
        {
            blockTicks.remove(b);
        }

    }
    public void AutoEnchanter_tick(Block b, AutoEnchanter AE) {
        if (AE.isProcessing(b)) {
            int timeleft = AE.progress.get(b);
            int timePassed = getTimePassed(b);
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
                    if (getCharge(b) < AE.getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -AE.getEnergyConsumption());
                    AE.progress.put(b, timeleft - timePassed);
                }
                else AE.progress.put(b, timeleft - timePassed);
            }
            else {
                endProccess(b, AE);
            }
        }
        else {
            clearTimePassed(b);
            String OldPower = "";
            String Oldname = "";
            MachineRecipe r = null;
            slots:
            for (int slot: AE.getInputSlots()) {
                ItemStack target = BlockStorage.getInventory(b).getItemInSlot(slot == AE.getInputSlots()[0] ? AE.getInputSlots()[1]: AE.getInputSlots()[0]);
                // Check if enchantable
                SlimefunItem sfTarget = SlimefunItem.getByItem(target);
                if(sfTarget != null && !sfTarget.isEnchantable()) return;

                ItemStack item = BlockStorage.getInventory(b).getItemInSlot(slot);

                // Enchant

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
                            boolean make = true;
                            boolean CE = false;
                            String Power = "I";
                            if (lorlore.size() > 3) {
                                make = false;
                                CE = true;

                                EnchantmentType type =  EnchantmentType.getFromName(ChatColor.stripColor(lorlore.get(2)).replace(" Only", "").toLowerCase());
                                if (type == null)
                                {
                                    type =  EnchantmentType.getFromName(ChatColor.stripColor(lorlore.get(2)).replace("s Only", "").toLowerCase());
                                }
                                if (type == null)
                                {
                                    if (ChatColor.stripColor(lorlore.get(2)).contains("All Armor, Weapons, Tools"))
                                    {
                                        type = EnchantmentType.ALL;
                                    }
                                }
                                if (type != null) {
                                    List<Material> typeMats = (List<Material>) type.getItems().clone();
                                    if (typeMats.contains(target.getType())) {
                                        make = true;
                                        if (target.getItemMeta() != null) {
                                            List<String> targetLore = target.getItemMeta().getLore();
                                            if (targetLore != null) {
                                                for (int i = 0; i < targetLore.size(); i++) {
                                                    String[] getRo = targetLore.get(i).split(" ");
                                                    if (getRo.length > 1) {

                                                        if (getRo[0].equals(ChatColor.AQUA + "Titan." + lorlore.get(0))) {
                                                            OldPower = getRo[1];
                                                            int NumberR = fromRoman(getRo[1]);
                                                            if (NumberR < 10) {
                                                                NumberR++;
                                                            }
                                                            Power = toRoman(NumberR);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            if(make) {
                                if (lorlore.size() > 0) {
                                    for (int i = 0; i < lorlore.size(); i++) {
                                        if (lorlore.get(i).equals(""))
                                        {
                                            break;
                                        }
                                        if (CE)
                                        {
                                            //me.badbones69.crazyenchantments.api.CrazyEnchantments.getInstance().getMaxPower()
                                            CEnchantments cEncant = CEnchantments.valueOf(lorlore.get(i).toUpperCase());
                                            int maxPower = CrazyEnchantments.getInstance().getMaxPower(cEncant);
                                            //System.out.print(cEncant.getCustomName() + "," + maxPower + "," + Power);
                                            if (fromRoman(Power) > maxPower)
                                            {
                                                Power = toRoman(maxPower);
                                            }
                                            //System.out.print(cEncant.getCustomName() + "," + maxPower + "," + Power);
                                            Oldname = lorlore.get(i);
                                            lore.add(ChatColor.AQUA + "Titan." + lorlore.get(i) + " " + Power);
                                        }
                                        else {
                                            lore.add(ChatColor.AQUA + "Titan." + lorlore.get(i));
                                        }
                                    }
                                    amount++;
                                }
                            }
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
                                        if (!newLore.get(i).equals(ChatColor.AQUA +"Titan." + Oldname + " " + OldPower)) {
                                            lore.add(newLore.get(i));
                                        }
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
    }
    public boolean axeCheck(ItemStack itemStack)
    {
        if (itemStack.getType().toString().contains("_AXE")) return  true;
        return false;
    }
    public void AutoDisenchanter_tick(Block b, AutoDisenchanter AD) {
        if (AD.isProcessing(b)) {
            int timeleft = AD.progress.get(b);
            int timePassed = getTimePassed(b);
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
                    if (getCharge(b) < AD.getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -AD.getEnergyConsumption());
                    AD.progress.put(b, timeleft - timePassed);
                }
                else AD.progress.put(b, timeleft - timePassed);

            }
            else {
                endProccess(b, AD);
            }
        }
        else {
            clearTimePassed(b);
            MachineRecipe r = null;
            Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
            Set<ItemEnchantment> enchantments2 = new HashSet<ItemEnchantment>();
            slots:
            for (int slot: AD.getInputSlots()) {
                ItemStack target = BlockStorage.getInventory(b).getItemInSlot(slot == AD.getInputSlots()[0] ? AD.getInputSlots()[1]: AD.getInputSlots()[0]);
                ItemStack item = BlockStorage.getInventory(b).getItemInSlot(slot);

                // Check if disenchantable
                SlimefunItem sfItem = SlimefunItem.getByItem(item);
                if (sfItem != null && !sfItem.isDisenchantable()) return;

                // Disenchant

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
    public void registerCargo()
    {

    }
    public void registerNewRecipes()
    {
    }
    public void converSpawnerType(InventoryClickEvent e) {
        ItemStack CI = e.getCursor();
        ItemStack II = e.getCurrentItem();
        if (II != null && CI != null) {

            if (CI.getType().toString().contains("_EGG")) {
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
    public static void fixSpawnerPlace(Block block, EntityType type)
    {
        CreatureSpawner cs = ((CreatureSpawner) block.getState());
        cs.setSpawnedType(type);
        cs.update(true, false);
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
                            if (sfi.getItem().getType() == Material.PLAYER_HEAD) {
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
    public static boolean isItemSimiliar(ItemStack item, ItemStack SFitem, boolean lore, SlimefunManager.DataType data) {
        if (item == null) return SFitem == null;
        if (SFitem == null) return false;

        if (item.getType() == SFitem.getType() && item.getAmount() >= SFitem.getAmount()) {
            //ToDo: Removed data_safe - is that correct?
            if (data.equals(SlimefunManager.DataType.ALWAYS)/* || (data.equals(DataType.IF_COLORED) && data_safe.contains(item.getType()))*/) {
/*				if (data_safe.contains(item.getType())) {
					if (item.getData().getData() != SFitem.getData().getData()) {
						if (!(SFitem.getDurability() == item.getData().getData() && SFitem.getData().getData() == item.getDurability())) return false;
					}
				}
				else*/ if (data.equals(SlimefunManager.DataType.ALWAYS) && item.getDurability() != SFitem.getDurability()) {
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

    public void SetupAnd(boolean b) {
        if (b)
        {

        }
    }
}
