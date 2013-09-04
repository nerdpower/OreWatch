package test.com.firstchest.orewatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
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
	private Main _main;
	private BlockBreakEvent _mockEvent;
	private Player _mockPlayer;
	private Block _mockBlock;
	private Location _mockLocation;
	private String _givenName;
	private String _hashName;

	
	@Before
	public void setUp() throws Exception
	{
		_main = new Main();
		
		_givenName = UUID.randomUUID().toString();
		_hashName = _givenName.toUpperCase();

		_mockEvent = PowerMockito.mock( BlockBreakEvent.class );
		_mockPlayer = mock( Player.class );
		_mockBlock = mock( Block.class );
		_mockLocation = mock( Location.class );
		
		when( _mockBlock.getLocation() ).thenReturn( _mockLocation );
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
		_main.BlockBreak( _mockEvent );
		
		// verify the player is contained in the hashmap log
		assertTrue( "Player name not found in hashmap.",
				_main.playerOreLog.containsKey( expected ));
	}
	
	
	/**
	 * Test that breaking stone blocks is tracked.
	 */
	@Test
	public void testBlockBreak_StoneIsTracked()
	{
		when( _mockBlock.getTypeId() ).thenReturn( Material.STONE.getId() );
		
		// MUT
		_main.BlockBreak( _mockEvent );
		
		// verify the stone block break is added to the hashmap
		int log[] = _main.playerOreLog.get( _hashName );
		assertLog( log, 1, 0, 0 );
	}
	
	
	/**
	 * Test that breaking coal blocks is tracked.
	 */
	@Test
	public void testBlockBreak_CoalIsTracked()
	{
		when( _mockBlock.getTypeId() ).thenReturn( Material.COAL_ORE.getId() );
		
		// MUT
		_main.BlockBreak( _mockEvent );
		
		// verify the coal block break is added to the hashmap
		int log[] = _main.playerOreLog.get( _hashName );
		assertLog( log, 0, 1, 0 );
	}
	
	
	/**
	 * Test that breaking iron blocks is tracked when it occurs at a
	 * height of 64, which is where it occurs naturally.
	 */
	@Test
	public void testBlockBreak_IronIsTrackedAt64()
	{
		when( _mockBlock.getTypeId() ).thenReturn( Material.IRON_ORE.getId() );
		when( _mockLocation.getBlockY() ).thenReturn( 64 );

		// MUT
		_main.BlockBreak( _mockEvent );
		
		// verify the iron block break is added to the hashmap
		int log[] = _main.playerOreLog.get( _hashName );
		assertLog( log, 0, 0, 1 );
	}
	

	/**
	 * Test that breaking iron blocks is tracked when it occurs below a
	 * height of 64, which is where it occurs naturally.
	 */
	@Test
	public void testBlockBreak_IronIsTrackedBelow64()
	{
		when( _mockBlock.getTypeId() ).thenReturn( Material.IRON_ORE.getId() );
		when( _mockLocation.getBlockY() ).thenReturn( new Random().nextInt( 64 ));

		// MUT
		_main.BlockBreak( _mockEvent );
		
		// verify the iron block break is added to the hashmap
		int log[] = _main.playerOreLog.get( _hashName );
		assertLog( log, 0, 0, 1 );
	}

	
	/**
	 * Test that breaking iron blocks is NOT tracked when it occurs above a
	 * height of 64. 
	 */
	@Test
	public void testBlockBreak_IronIsNotTrackedAbove64()
	{
		when( _mockBlock.getTypeId() ).thenReturn( Material.IRON_ORE.getId() );
		when( _mockLocation.getBlockY() ).thenReturn( new Random().nextInt( 65 ) + 65 );
		
		// MUT
		_main.BlockBreak( _mockEvent );
		
		// verify the iron block break was not added to the hashmap
		int log[] = _main.playerOreLog.get( _hashName );
		assertLog( log, 0, 0, 0 );
	}
	
	
	/**
	 * Helper method to assert log values. 
	 */
	private void assertLog( int[] log, int stone, int coal, int iron )
	{
		assertEquals( "Incorrect stone value.", stone, log[Main.STONE_POS] );
		assertEquals( "Incorrect coal value.", coal, log[Main.COAL_POS] );
		assertEquals( "Incorrect iron value.", iron, log[Main.IRON_POS] );
	}
}
