package com.firstchest.orewatch;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;


public final class Main
{
	public static final int STONE_POS = 0;
	
	public HashMap<String, int[]> playerOreLog = new HashMap<String, int[]>();


	@EventHandler( priority = EventPriority.MONITOR ) // REDTAG can this be tested?
	public void BlockBreak( BlockBreakEvent event )
	{
		String player = event.getPlayer().getName().toUpperCase();
		
		int frequency[] = new int[1];
		frequency[ STONE_POS ]++;
		
		playerOreLog.put( player, frequency );
	}
}
