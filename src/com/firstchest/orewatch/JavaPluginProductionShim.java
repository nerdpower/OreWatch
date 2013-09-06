package com.firstchest.orewatch;

import org.bukkit.Server;

public class JavaPluginProductionShim implements JavaPluginShimInterface {
	public Server getServer( Main plugin )
	{
		return plugin.getServer(); // untested, see note in ignored test
	}
}