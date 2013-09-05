package com.firstchest.orewatch;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin implements Listener 
{
	private BlockBreakHandler _blockBreakHandler;
	
	
	/**
	 * Default constructor that initializes external dependencies
	 */
	public Main()
	{
		_blockBreakHandler = new BlockBreakHandler();
	}
	
	
	/**
	 * Constructor for testing that implements poor man's dependency injection.
	 * @param blockBreakHandler
	 */
	public Main( BlockBreakHandler blockBreakHandler )
	{
		_blockBreakHandler = blockBreakHandler;
	}
	
	
	@EventHandler( priority = EventPriority.MONITOR )
	public void BlockBreak( BlockBreakEvent event )
	{
		_blockBreakHandler.BlockBreak( event );
	}
}
