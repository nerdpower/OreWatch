package test.com.firstchest.orewatch;

import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.firstchest.orewatch.JavaPluginProductionShim;
import com.firstchest.orewatch.Main;

@RunWith( PowerMockRunner.class )
@PrepareForTest( JavaPlugin.class )
public class JavaPluginProductionShimTest {

	/**
	 * It is not possible to mock and verify the JavaPlugin.getServer() function because
	 * PluginBase (org.bukkit.plugin) overrides equals() and hashCode() and declares them
	 * final, which is not to the liking of PowerMock. See the following:
	 * 
	 * https://code.google.com/p/powermock/issues/detail?id=88
	 * http://stackoverflow.com/questions/17151464/powermock-stackoveflowerror-on-arraylistmultimap
	 * https://groups.google.com/forum/#!msg/powermock/DpVlG4ueOyw/fMaJX4wIGpsJ
	 */
	@Test
	@Ignore( "PowerMock fails for getServer(). See description." )
	public void test() {
		Main mockMain = PowerMockito.mock( Main.class );

		JavaPluginProductionShim handler = new JavaPluginProductionShim();
		handler.getServer( mockMain );
		
		Mockito.verify( mockMain ).getServer();
	}

}
