package test.com.firstchest.orewatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.firstchest.orewatch.Main;


@RunWith( PowerMockRunner.class )
@PrepareForTest( BlockBreakEvent.class )
public class MainTest
{
	private BlockBreakEvent _mockEvent;
	private Player _mockPlayer;
	private Block _mockBlock;
	private String _givenName;
	private String _hashName;
	
	
	@Before
	public void setUp() throws Exception
	{
		_givenName = UUID.randomUUID().toString();
		_hashName = _givenName.toUpperCase();

		_mockEvent = PowerMockito.mock( BlockBreakEvent.class );
		_mockPlayer = mock( Player.class );
		_mockBlock = mock( Block.class );
		
		when( _mockPlayer.getName() ).thenReturn( _givenName );
		when( _mockEvent.getPlayer() ).thenReturn( _mockPlayer );
		when( _mockEvent.getBlock() ).thenReturn( _mockBlock );
	}

	
	/**
	 * Test that an untracked player is added to the log. The player name
	 * should be in all upper case.
	 */
	@Test
	public void testBlockBreak_AddPlayerToLog()
	{
		String expected = _givenName.toUpperCase();

		// MUT
		Main main = new Main();
		main.BlockBreak( _mockEvent );
		
		// verify the player is contained in the hashmap log
		assertTrue( "Player name not found in hashmap.",
				main.playerOreLog.containsKey( expected ));
	}
	
	
	/**
	 * Test that breaking stone blocks is tracked.
	 */
	@Test
	public void testBlockBreak_StoneIsTracked()
	{
		when( _mockBlock.getTypeId() ).thenReturn( Material.STONE.getId() );
		
		// MUT
		Main main = new Main();
		main.BlockBreak( _mockEvent );
		
		// verify the stone block break is added to the hashmap
		int log[] = (int[]) main.playerOreLog.get( _hashName );
		int actual = log[ Main.STONE_POS ];
		assertEquals( "Stone not added to hashmap.", 1, actual );
	}
}
