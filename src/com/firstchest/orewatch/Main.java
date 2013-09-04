package com.firstchest.orewatch;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;


public final class Main
{
	public HashMap<String, int[]> playerOreLog = new HashMap<String, int[]>();


	@EventHandler( priority = EventPriority.MONITOR ) // REDTAG can this be tested?
	public void BlockBreak( BlockBreakEvent event )
	{
		playerOreLog.put( "FOONAME", new int[0] );
	}
}
