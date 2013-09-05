package test.com.firstchest.orewatch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.firstchest.orewatch.OreWatchPluginHandler;


@RunWith( PowerMockRunner.class )
@PrepareForTest( BlockBreakEvent.class )
public class OreWatchPluginHandlerTest
{
	private OreWatchPluginHandler _handler;
	private BlockBreakEvent _mockEvent;
	private Player _mockPlayer;
	private Block _mockBlock;
	private Location _mockLocation;
	private Server _mockServer;
	private PluginManager _mockPluginManager;
	private String _givenName;
	private String _hashName;

	
	@Before
	public void setUp() throws Exception
	{
		_handler = new OreWatchPluginHandler();
		
		_givenName = UUID.randomUUID().toString();
		_hashName = _givenName.toUpperCase();

		_mockEvent = PowerMockito.mock( BlockBreakEvent.class );
		_mockPlayer = mock( Player.class );
		_mockBlock = mock( Block.class );
		_mockLocation = mock( Location.class );
		_mockServer = mock( Server.class );
		_mockPluginManager = mock( PluginManager.class );
		
		when( _mockBlock.getLocation() ).thenReturn( _mockLocation );
		when( _mockPlayer.getName() ).thenReturn( _givenName );
		when( _mockEvent.getPlayer() ).thenReturn( _mockPlayer );
		when( _mockEvent.getBlock() ).thenReturn( _mockBlock );
		when( _mockServer.getPluginManager() ).thenReturn( _mockPluginManager );
	}

	
	/**
	 * Test that breaking stone blocks is tracked. Stone is the base to which
	 * all other breaking is compared so it must be tracked regardless of
	 * whether it is configured to be.
	 */
	@Test
	public void blockBreakEventHandler_StoneIsTrackedWithoutBeingConfigured()
	{
		when( _mockBlock.getTypeId() ).thenReturn( Material.STONE.getId() );
		
		blockBreakEventHandler_mut();
		
		// verify the stone block break is added to the hashmap
		int log[] = _handler.PlayerOreLog.get( _hashName );
		assertEquals( "Stone was not tracked.", 1, log[Material.STONE.getId()] );
	}

	
	/**
	 * Test that a configured block is tracked. Valid block IDs are anything
	 * between 0 and 255.
	 */
	@Test
	public void blockBreakEventHandler_ConfiguredBlockIsTracked()
	{
		// configure a block to be tracked (ID between 0 and 255)
		int blockId = new Random().nextInt( 256 );
		_handler.ConfiguredBlocks.put( blockId, new int[1] );

		// configure the block mock to simulate this block breaking 
		when( _mockBlock.getTypeId() ).thenReturn( blockId );
		
		blockBreakEventHandler_mut();
		
		// verify the configured block was incremented in the hashmap
		assertLog( blockId, 1 );
	}
	
	
	/**
	 * Test that a block not configured for tracking is not logged.
	 */
	@Test
	public void blockBreakEventHandler_NonConfiguredBlockIsNotTracked()
	{
		// simulate any random block breaking
		int blockId = new Random().nextInt( 256 );
		when( _mockBlock.getTypeId() ).thenReturn( blockId );

		blockBreakEventHandler_mut();
		
		// verify the block was not incremented in the hashmap
		assertLog( blockId, 0 );
	}
	

	/**
	 * Test that breaking a block above the height limit specified will not
	 * add that block to the log.
	 */
	@Test
	public void blockBreakEventHandler_IsNotTrackedAboveSpecifiedHeight()
	{
		// configure the height limit
		int height = new Random().nextInt( 64 ) + 64;
		int[] config = new int[1];
		config[0] = height;
		
		// simulate any random block breaking one above the specific height
		int blockId = new Random().nextInt( 256 );
		_handler.ConfiguredBlocks.put( blockId, config );
		when( _mockLocation.getBlockY() ).thenReturn( height + 1 );
		when( _mockBlock.getTypeId() ).thenReturn( blockId );
		
		blockBreakEventHandler_mut();
		
		// verify the block was not incremented in the hashmap
		assertLog( blockId, 0 );
	}
	

	/**
	 * Test that breaking a block at the height limit specified will add
	 * that block to the log.
	 */
	@Test
	public void blockBreakEventHandler_IsTrackedAtSpecifiedHeight()
	{
		// configure the height limit
		int height = new Random().nextInt( 128 ) + 1;
		int[] config = new int[1];
		config[0] = height;
		
		// simulate any random block breaking at the specific height
		int blockId = new Random().nextInt( 256 );
		_handler.ConfiguredBlocks.put( blockId,  config );
		when( _mockLocation.getBlockY() ).thenReturn( height );
		when( _mockBlock.getTypeId() ).thenReturn( blockId );
		
		blockBreakEventHandler_mut();
		
		// verify the block was incremented in the hashmap
		assertLog( blockId, 1 );
	}
	
	
	/**
	 * Test that existing logs are not lost when a new block is broken.
	 */
	@Test
	public void blockBreakEventHandler_ExistingLogNotLost()
	{
		// configure a block to be tracked
		int blockId = new Random().nextInt( 256 );
		_handler.ConfiguredBlocks.put( blockId, new int[1] );
		when( _mockBlock.getTypeId() ).thenReturn( blockId );

		// create an existing log for this block
		// REDTAG this knows a bit too much about the inner workings of the class under test
		int log[] = new int[256];
		log[blockId]++;
		_handler.PlayerOreLog.put( _hashName, log );
		
		blockBreakEventHandler_mut();
		
		// verify the configured block was incremented on top of the existing value
		assertLog( blockId, 2 );
	}
	
	
	/**
	 * Helper function to run the method under test (mut).
	 */
	private void blockBreakEventHandler_mut()
	{
		_handler.blockBreakEventHandler( _mockEvent );
	}
	
	
	/**
	 * Helper function to assert a specific value in the hashmap.
	 */
	private void assertLog( int blockId, int expected )
	{
		int log[] = _handler.PlayerOreLog.get( _hashName );
		assertEquals( "Block [" + blockId + "] value incorrect.", expected, log[blockId] );
	}
}
