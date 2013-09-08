package com.firstchest.orewatch;

import java.util.logging.Logger;

import org.bukkit.Server;
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
	}
	
	
	/**
	 * Event fired by bukkit when the plugin is enabled.
	 */
	public void onEnable()
	{
		_handler.initConfig( this );
		_handler.loadConfig( this );
		_handler.loadPlayerLog( this );
		_handler.registerEvents( this );
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
	
	
	/**
	 * It is not possible to mock and verify JavaPlugin functions because
	 * PluginBase (org.bukkit.plugin) overrides equals() and hashCode() and declares them
	 * final, which is not to the liking of PowerMock. See the following:
	 * 
	 * https://code.google.com/p/powermock/issues/detail?id=88
	 * http://stackoverflow.com/questions/17151464/powermock-stackoveflowerror-on-arraylistmultimap
	 * https://groups.google.com/forum/#!msg/powermock/DpVlG4ueOyw/fMaJX4wIGpsJ
	 * 
	 * Any JavaPlugin function that is needed above is shimmed here and the shim
	 * is used instead so that a mock can be injected for testing. 
	 */
	public Server getServerShim() { return this.getServer(); }
	public Logger getLoggerShim() { return this.getLogger(); }
	public void saveDefaultConfigShim() { saveDefaultConfig(); }
}
