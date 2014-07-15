package kernitus.plugin.Hotels;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import managers.WorldGuardManager;
import me.confuser.barapi.BarAPI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class HotelsListener implements Listener {
	public final HashMap<Player, ArrayList<Block>> hashmapPlayerName = new HashMap<Player, ArrayList<Block>>();

	public HotelsMain plugin;

	public HotelsListener(HotelsMain plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		if(e.getLine(0).contains("[Hotels]")||e.getLine(0).contains("[hotels]")) {
			String Line2 = e.getLine(1);
			String Line3 = e.getLine(2);
			String Line4 = e.getLine(3);
			if ((!(Line2.isEmpty()))&&(WorldGuardManager.getWorldGuard().getRegionManager(e.getPlayer().getWorld()).hasRegion("Hotel-"+Line2))&&(WorldGuardManager.getWorldGuard().getRegionManager(e.getPlayer().getWorld()).getRegion("Hotel-"+Line2).contains(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ()))) {
				if((Integer.valueOf(Line3).equals(Integer.valueOf(Line3)))&&(WorldGuardManager.getWorldGuard().getRegionManager(e.getPlayer().getWorld()).hasRegion("Hotel-"+Line2+"-"+Line3))) {
					if(Line4.contains(":")) {
						//Successful Sign
						e.setLine(0, "�1"+Line2); //Hotel Name
						e.setLine(1, "�2Room " + Line3); //Room Number

						String[] parts = Line4.split(":");
						String cost = parts[0]; //Cost
						String time = parts[1]; //Time
						
						e.setLine(2,cost+"$");  //Cost
						
						time = time.replace("d", "d ");
						time = time.replace("h", "h ");
						time = time.replace("m", "m ");
						
						e.setLine(3,"�f"+time);      //Time
						p.sendMessage(ChatColor.DARK_GREEN + "Hotel sign has been successfully created!");

						//TODO Add to config

					} else {
						p.sendMessage(ChatColor.DARK_RED + "Line 4 must contain the separator �3:");    				
						e.setLine(0, "�4[Hotels]");
					}
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Line 3 must be the number of a room!");        			
					e.setLine(0, "�4[Hotels]");
				}
			} else {
				p.sendMessage(ChatColor.DARK_RED + "Sign was not placed within hotel borders");        		
				e.setLine(0, "�4[Hotels]");
			}
		}
	}

	@EventHandler
	public void onSignUse(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
				Sign s = (Sign) e.getClickedBlock().getState();

				//TODO We need to get from a YAML File. So on creating one lets make a Hashmap or list and add into Config!
				if (s.getLine(0).equalsIgnoreCase("") && s.getLine(1).equalsIgnoreCase("")) {
					@SuppressWarnings("unused")
					Player p = e.getPlayer();

					//TODO get player name and add his name to config under the room?
				}	
			}
		}
	}	

	//When a player tries to drop an item/block
	@EventHandler
	public void avoidDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		File file = new File("plugins//Hotels//Inventories//"+"Inventory-"+playerUUID+".yml");

		if(file.exists())
			e.setCancelled(true);
	}

	//When a player tries to pickup an item/block
	@EventHandler
	public void avoidPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		File file = new File("plugins//Hotels//Inventories//"+"Inventory-"+playerUUID+".yml");

		if(file.exists())
			e.setCancelled(true);
	}

	//Upon login check if player was in HCM mode, if yes, display boss bar
	@EventHandler
	public void bossBarCheck(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(plugin.getConfig().getBoolean("HCM.bossBar")==true){
			UUID playerUUID = p.getUniqueId();
			File file = new File("plugins//Hotels//Inventories//"+"Inventory-"+playerUUID+".yml");
			if(file.exists()){
				BarAPI.setMessage(p, "�2Hotel Creation Mode");
			}
		}

	}
}