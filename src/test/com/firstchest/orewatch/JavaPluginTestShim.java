package test.com.firstchest.orewatch;

import org.bukkit.Server;

import com.firstchest.orewatch.JavaPluginShimInterface;
import com.firstchest.orewatch.Main;

public class JavaPluginTestShim implements JavaPluginShimInterface {
	private Server _mockServer;
	
	public JavaPluginTestShim( Server mockServer ) {
		_mockServer = mockServer;
	}
	
	public Server getServer( Main plugin ) {
		return _mockServer;
	}
}