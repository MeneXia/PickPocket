package com.bukkit.FlingeR.PickPocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class PickPocket extends JavaPlugin {
	    private static Server Server = null;
	    public static HashMap<String, String> pconfig = new HashMap<String, String>();
	    public static HashMap<Material, Double> IList = new HashMap<Material, Double>();
	    public static PermissionHandler Permissions;
	    private final PickPocketEntityListener entityListener = new PickPocketEntityListener(this);
	    
	    public void onEnable() {
	    	
	    File yml = new File(getDataFolder()+"/config.yml");
	    
	    if (!yml.exists()) {
	        new File(getDataFolder().toString()).mkdir();
	    try {
	    yml.createNewFile();
	    }
	    catch (IOException ex) {
	    System.out.println("[PickPocket] cannot create file "+yml.getPath());
	    }
	       }  
	    
		pconfig.put("PickPocketTool", getConfiguration().getString("PickPocketTool", "325"));
		pconfig.put("DamageOnFail", getConfiguration().getString("DamageOnFail", "1"));
		pconfig.put("LooseToolOnFail", getConfiguration().getString("LooseToolOnFail", "NO"));
		pconfig.put("FailToSteal", getConfiguration().getString("FailToSteal", "[PickPocket] You have Fail!!"));
		pconfig.put("ItemStolen", getConfiguration().getString("ItemStolen", "[PickPocket] You have stolen an item!!"));
		pconfig.put("TryToStealYou", getConfiguration().getString("TryToStealYou", "[PickPocket] %n has tried to steal you!!"));
		pconfig.put("HasStolenYou", getConfiguration().getString("HasStolenYou", "[PickPocket] %n has stolen you!!"));
	     getConfiguration();
	     Server = getServer();
	     PluginManager pm = getServer().getPluginManager();
	        if (!(new File(getDataFolder(), "config.yml")).exists()) {
	         defaultConfig();
	        }  
	        loadConfig();
	        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
	        setupPermissions();
	        try {
				readProperties();
			} catch (Exception e) {
				System.out.println("[PickPocket] Exception while reading items.properties.");
			}
	        System.out.println("[PickPocket] v" + getDescription().getVersion() + " by FlingeR sucesfuly enabled.");
	    }
	    
	    public void onDisable() {
	        System.out.println("[PickPocket] Sucesfuly disabled");

	    }
	    
	    private void loadConfig() {
	    }

	    private void defaultConfig() {
	    }
	    
	    private void setupPermissions() {
	     Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");

	        if (Permissions == null) {
	         if (permissions != null) {
	             Permissions = ((Permissions)permissions).getHandler();
	            } else {
	             System.out.println("Permission system not detected, defaulting to OP");
	            }
	        }
	    }

	    public static boolean hasPermissions(Player p, String s) {
	        if (Permissions != null) {
	            return Permissions.has(p, s);
	        } else {
	            return p.isOp();
	        }
	    }
	    
	    public static Server getBukkitServer() {
	        return Server;
	    }

		public static void readProperties() throws Exception {
			String fileName = "plugins/PickPocket/items.properties";
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				if ((line.trim().length() == 0) || 
						(line.charAt(0) == '#')) {
					continue;
				}
				int keyPosition = line. indexOf('=');
				Material item = Material.getMaterial(line.substring(0, keyPosition).trim());
				double value = Double.parseDouble(line.substring(keyPosition+1, line.length()).trim());
				IList.put(item, value);
			}
	        System.out.println("[PickPocket] Configuration sucesfuly loaded");			
		}
		
		public static boolean canBeStolen(Material type){
			return(IList.containsKey(type));
		}

		public static double getProb(Material type) {
			return(IList.get(type));
		} 
	}