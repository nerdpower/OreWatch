package test.com.firstchest.orewatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.firstchest.orewatch.Main;


@RunWith( PowerMockRunner.class )
@PrepareForTest( BlockBreakEvent.class )
public class MainTest {

	
	/**
	 * Test that an untracked player is added to the log. The player name
	 * should be in all upper case.
	 */
	@Test
	public void testBlockBreak_AddPlayerToLog()
	{
		BlockBreakEvent mockEvent = PowerMockito.mock( BlockBreakEvent.class );
		Player mockPlayer = mock( Player.class );

		when( mockPlayer.getName() ).thenReturn( "fooName" );
		
		// MUT
		Main main = new Main();
		main.BlockBreak( mockEvent );
		
		// verify the player is contained in the hashmap log
		assertTrue( "Player name not found in hashmap.",
				main.playerOreLog.containsKey( "FOONAME" )); // REDTAG randomize the name
	}
}
