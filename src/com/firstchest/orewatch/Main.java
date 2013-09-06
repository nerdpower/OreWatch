package com.firstchest.orewatch;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener 
{
	private OreWatchPluginHandler _handler;
	
	
	/**
	 * Default constructor that initializes external dependencies
	 */
	public Main()
	{
		this( new OreWatchPluginHandler() );
	}
	
	
	/**
	 * Constructor for testing that implements poor man's dependency injection.
	 * @param handler
	 */
	public Main( OreWatchPluginHandler handler )
	{
		_handler = handler;
		_handler.plugin = this;
	}
	
	
	/**
	 * Event fired by bukkit when the plugin is enabled.
	 */
	public void onEnable()
	{
		_handler.onEnableHandler();
	}
	
	
	/**
	 * Event fired by bukkit when a block is broken.
	 * @param event
	 */
	@EventHandler( priority = EventPriority.MONITOR )
	public void blockBreakEvent( BlockBreakEvent event )
	{
		_handler.blockBreakEventHandler( event );
	}
}
