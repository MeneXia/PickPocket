package com.bukkit.FlingeR.PickPocket;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

	public class PickPocketEntityListener extends EntityListener {
	    private final PickPocket plugin;
	    public PickPocketEntityListener(PickPocket instance) {
	        plugin = instance;
	    }
	    
		public void onEntityDamage (EntityDamageEvent event) {
			if (event.isCancelled())
	    		return;
			else {
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent entEvent = (EntityDamageByEntityEvent)event;
				if ( (entEvent.getDamager() instanceof Player) && (entEvent.getEntity() instanceof Player) ) {
					Player attacker = (Player)entEvent.getDamager();
					Player victim = (Player)entEvent.getEntity();
					int tool=Integer.valueOf(PickPocket.pconfig.get("PickPocketTool").trim()).intValue();
					if (!(PickPocket.hasPermissions(victim, "PickPocket.protected"))&&(PickPocket.hasPermissions(attacker, "PickPocket.use"))&&(attacker.getItemInHand().getTypeId()==tool)) {
						event.setCancelled(true);
						PlayerInventory inv = victim.getInventory();
						Random rand = new Random();
						int x = rand.nextInt(inv.getSize());
						int y = x;
						while(x<inv.getSize()&&!(PickPocket.canBeStolen(inv.getItem(x).getType()))){
							x++;
						}
						if(inv.getItem(x).getAmount()==0){
							x=0;
							while(x<y&&!(PickPocket.canBeStolen(inv.getItem(x).getType()))){
								x++;
							}	
						}
						String Attacker = null;
						String Victim = null;
						if(inv.getItem(x).getAmount()==0){
							if(PickPocket.pconfig.get("LooseToolOnFail").toUpperCase().equals("YES")){
								attacker.setItemInHand(null);
							}
								int damage=Integer.valueOf(PickPocket.pconfig.get("DamageOnFail").trim()).intValue();
								attacker.damage(damage);
							Attacker= PickPocket.pconfig.get("FailToSteal").replace("%t", attacker.getName());
							Attacker= Attacker.replace("%v", victim.getName());
							
							Victim= PickPocket.pconfig.get("TryToStealYou").replace("%t", attacker.getName());
							Victim= Victim.replace("%v", victim.getName());
							
							attacker.sendMessage(Attacker);
							victim.sendMessage(Victim);
						}
						else if(PickPocket.canBeStolen(inv.getItem(x).getType())&&(Math.random()<PickPocket.getProb(inv.getItem(x).getType()))){
							String Mtype = String.valueOf(inv.getItem(x).getType());;
							attacker.getInventory().addItem(inv.getItem(x));
							inv.clear(x);
							
							
							Attacker= PickPocket.pconfig.get("ItemStolen").replace("%t", attacker.getName());
							Attacker= Attacker.replace("%v", victim.getName());
							Attacker= Attacker.replace("%i", Mtype);
							
							Victim= PickPocket.pconfig.get("HasStolenYou").replace("%t", attacker.getName());
							Victim= Victim.replace("%v", victim.getName());
							Victim= Victim.replace("%i", Mtype);
							
							attacker.sendMessage(Attacker);
							victim.sendMessage(Victim);
						}		
						else{
							if(PickPocket.pconfig.get("LooseToolOnFail").toUpperCase().equals("YES")){
								attacker.setItemInHand(null);
							}
							
							int damage=Integer.valueOf(PickPocket.pconfig.get("DamageOnFail").trim()).intValue();
							attacker.damage(damage);
							Attacker= PickPocket.pconfig.get("FailToSteal").replace("%t", attacker.getName());
							Attacker= Attacker.replace("%v", victim.getName());
							
							Victim= PickPocket.pconfig.get("TryToStealYou").replace("%t", attacker.getName());
							Victim= Victim.replace("%v", victim.getName());
							
							attacker.sendMessage(Attacker);
							victim.sendMessage(Victim);
						}
					}
					}
				}
			}
			}
		}