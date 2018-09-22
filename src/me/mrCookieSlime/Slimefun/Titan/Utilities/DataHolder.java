package me.mrCookieSlime.Slimefun.Titan.Utilities;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class DataHolder {

    private  String id;
    private  Location location;
    private  double energy_charge, energy_capacity;
    private HashMap<String, Object> extradata;
    public DataHolder(String id, Location location)
    {
        this.id = id;
        this.location = location;
        extradata = new HashMap<String, Object>();
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setEnergy_capacity(double energy_capacity) {
        this.energy_capacity = energy_capacity;
    }

    public void setEnergy_charge(double energy_charge) {
        this.energy_charge = energy_charge;
    }

    public double getEnergy_capacity() {
        return energy_capacity;
    }

    public double getEnergy_charge() {
        return energy_charge;
    }
    public Object get(String key)
    {
        return extradata.get(key);
    }
    public void set(String key, Object data)
    {
        extradata.put(key, data);
    }
    public String getString(String key)
    {
        Object data = this.get(key);
        if (data instanceof String)
        {
            return (String) data;
        }
        return null;
    }
    public Boolean getBoolean(String key)
    {
        Object data = this.get(key);
        if (data instanceof Boolean)
        {
            return (Boolean) data;
        }
        return null;
    }
    public int getInteger(String key)
    {

        Object data = this.get(key);
        if (data instanceof Integer)
        {
            return (int) data;
        }
        return 0;
    }
    public Double getDouble(String key)
    {

        Object data = this.get(key);
        if (data instanceof Double)
        {
            return (Double) data;
        }
        return 0D;
    }
    public UUID getUUID(String key)
    {

        Object data = this.get(key);
        if (data instanceof UUID)
        {
            return (UUID) data;
        }
        return null;
    }
    public Location getLocation(String key)
    {

        Object data = this.get(key);
        if (data instanceof Location)
        {
            return (Location) data;
        }
        return null;
    }
    public ItemStack getItemStack(String key)
    {

        Object data = this.get(key);
        if (data instanceof ItemStack)
        {
            return (ItemStack) data;
        }
        return null;
    }
}
