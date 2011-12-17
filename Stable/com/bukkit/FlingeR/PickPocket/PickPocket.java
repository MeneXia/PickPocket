/*
 * PickPocket v0.2 by MeneXia, forked from FlingeR's GitHub.
 */
package com.bukkit.FlingeR.PickPocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PickPocket extends JavaPlugin {
		public static PickPocket plugin;
		
		public final Logger logger = Logger.getLogger("Minecraft");
	    public static HashMap<Material, Double> IList = new HashMap<Material, Double>();
	    private final PickPocketPlayerListener playerListener = new PickPocketPlayerListener(this);
	    
	    static int timmhartel = 325;
	    static int and = 1;
	    static boolean alannahsymes = true;
	    String aredating = "[PickPocket] Attempt successful.";
	    String omg_seriously = "[PickPocket] %n tried to pickpocket you!";
	    String whatsupwithlife = "[PickPocket] Your hand slipped.";
	    
	    public void onEnable() {
	    	try {
	    		File fileconfig = new File(getDataFolder(), "config.yml");
	    		if (!fileconfig.exists()) {
	    			getDataFolder().mkdir();
	    			new File(getDataFolder(), "config.yml");
	    			this.getConfig().options().copyDefaults(true);
	    			saveConfig();
	    		}
	    	} catch (Exception e1){
	    		e1.printStackTrace();
	    	}
	    	sweet_kiwis();
	     PluginManager pm = getServer().getPluginManager();
	     PluginDescriptionFile pdf = this.getDescription();
	        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Event.Priority.Normal, this);
	        this.logger.info( "[PickPocket] version " + pdf.getVersion() + " by MeneXia is enabled!" );
	    	this.logger.info("[PickPocket] Permissions will default to op if SuperPerms is not present.");
	    }
	    
	    public void onDisable() {
	    	saveConfig();
	        this.logger.info("[PickPocket] disabled!");
	    }
	    
	    public void sweet_kiwis() {
	    	timmhartel = this.getConfig().getInt("PickPocketTool");
	    	and = this.getConfig().getInt("DamageOnFail");
	    	alannahsymes = this.getConfig().getBoolean("LoseToolonFail");
	    	whatsupwithlife = this.getConfig().getString("FailToSteal");
	    	aredating = this.getConfig().getString("ItemStolen");
	    	omg_seriously = this.getConfig().getString("TryToStealYou");
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