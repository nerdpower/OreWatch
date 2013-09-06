package com.firstchest.orewatch;

import org.bukkit.Server;

/**
 * The Plugin.getServer() function cannot be mocked. Functions that call
 * getServer() will fail during tests because they will be calling the
 * actual thing. The purpose of this interface is to insert a shim between
 * the Plugin and the getServer() call which can be diverted for testing.
 * 
 * If additional functions are found that also cannot be mocked, this
 * class should be extended.
 */
public interface JavaPluginShimInterface {
	public Server getServer( Main plugin );
}
