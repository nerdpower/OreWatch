package test.com.firstchest.orewatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.UUID;

import org.bukkit.event.block.BlockBreakEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.firstchest.orewatch.Main;
import com.firstchest.orewatch.OreWatchPluginHandler;


@RunWith( PowerMockRunner.class )
public class MainTest
{
	/**
	 * Test that BlockBreakEvent calls the handler class.
	 */
	@Test
	public void BlockBreakEventHookedUp()
	{	
		OreWatchPluginHandler mockHandler = mock( OreWatchPluginHandler.class );
		BlockBreakEvent mockEvent = PowerMockito.mock( BlockBreakEvent.class );

		// mut
		Main main = new Main( mockHandler );
		main.blockBreakEvent( mockEvent );
		
		// verify the handler class was called
		verify( mockHandler ).blockBreakEventHandler( mockEvent );
	}


	/**
	 * Test that onEnable event calls the desired functionality.
	 */
	@Test
	public void onEnableEventHookedUp()
	{	
		OreWatchPluginHandler mockHandler = mock( OreWatchPluginHandler.class );

		// mut
		Main plugin = new Main( mockHandler );
		plugin.onEnable();
		
		// verify the calls
		verify( mockHandler ).initConfig( plugin );
		verify( mockHandler ).loadConfig( plugin );
		verify( mockHandler ).loadPlayerLog( plugin );
		verify( mockHandler ).registerEvents( plugin );
	}
	
	
	/**
	 * Test that a new File object is created with the given pathname
	 * and filename.
	 */
	@Test
	public void getFile_Success()
	{
		// setup expected path and file names
		String expectedPathname = "path-" + UUID.randomUUID().toString();
		File expectedFolder = new File( expectedPathname );
		String expectedFilename = "file-" + UUID.randomUUID().toString();
		
		// mut
		Main plugin = new Main();
		File actual = plugin.getFile( expectedFolder, expectedFilename );
		
		assertEquals( expectedPathname, actual.getParent() );
		assertEquals( expectedFilename, actual.getName() );
	}
}
