package test.com.firstchest.orewatch;

import static org.mockito.Mockito.*;

import org.bukkit.event.block.BlockBreakEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.firstchest.orewatch.BlockBreakHandler;
import com.firstchest.orewatch.Main;


@RunWith( PowerMockRunner.class )
@PrepareForTest( Main.class )
public class MainTest
{
	/**
	 * Test that the BlockBreak event handler calls the handler class.
	 */
	@Test
	public void BlockBreakEventHookedUp()
	{	
		BlockBreakHandler mockHandler = mock( BlockBreakHandler.class );
		BlockBreakEvent mockEvent = PowerMockito.mock( BlockBreakEvent.class );

		// mut
		Main main = new Main( mockHandler );
		main.BlockBreak( mockEvent );
		
		// verify the handler class was called
		verify( mockHandler ).BlockBreak( mockEvent );
	}
}
