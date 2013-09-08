package com.firstchest.orewatch;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

public class OreWatchPluginHandler
{
	// REDTAG would be nice to throw an exception if any function is called without first setting plugin
	public HashMap<String, int[]> PlayerOreLog = new HashMap<String, int[]>();
	public HashMap<Integer, int[]> ConfiguredBlocks = new HashMap<Integer, int[]>();
	
	
	public void blockBreakEventHandler( BlockBreakEvent event )
	{
		String player = event.getPlayer().getName().toUpperCase();
		
		int[] frequency;
		if ( PlayerOreLog.containsKey( player ))
			frequency = PlayerOreLog.get( player );
		else
			frequency = new int[256];
		
		Block block = event.getBlock();
		int blockId = block.getTypeId();

		if ( blockId == Material.STONE.getId() )
		{
			frequency[blockId]++;
		}
		else if ( ConfiguredBlocks.containsKey( blockId ))
		{
			int[] config = ConfiguredBlocks.get( blockId );
			
			// config[0] is height limit
			if ( config[0] < 1 )
				frequency[blockId]++;
			else if ( block.getLocation().getBlockY() <= config[0] )
				frequency[blockId]++;
		}

		PlayerOreLog.put( player, frequency );
	}
	
	
	public void initConfig( Main plugin )
	{
		plugin.getLoggerShim().info( "No config file exists. Creating a new config file." );
		plugin.saveDefaultConfigShim();
	}
	
	
	public void loadConfig( Main plugin )
	{
		
	}
	
	
	public void loadPlayerLog( Main plugin )
	{
		
	}
	
	
	public void registerEvents( Main plugin )
	{
		plugin.getServerShim().getPluginManager().registerEvents( plugin, plugin );
	}
}
