package com.firstchest.orewatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin implements Listener
{
	private static int STONE;
	private static int COAL;
	private static int IRON;
	private static int REDSTONE1;
	private static int REDSTONE2;
	private static int LAPIS;
	private static int GOLD;
	private static int DIAMOND;
	private static int EMERALD;

	private static final int STONE_POS = 0;
	private static final int COAL_POS = 1;
	private static final int IRON_POS = 2;
	private static final int REDSTONE_POS = 3;
	private static final int LAPIS_POS = 4;
	private static final int GOLD_POS = 5;
	private static final int DIAMOND_POS = 6;
	private static final int EMERALD_POS = 7;
	private static final int POS_SIZE = 8;

	private float coalLimit;
	private float ironLimit;
	private float redstoneLimit;
	private float lapisLimit;
	private float goldLimit;
	private float diamondLimit;
	private float emeraldLimit;

	private Logger log;
	private HashMap<String, int[]> playerOreLog;

	public Main ()
	{
		STONE = Material.STONE.getId();
		COAL = Material.COAL_ORE.getId();
		IRON = Material.IRON_ORE.getId();
		REDSTONE1 = Material.REDSTONE_ORE.getId();
		REDSTONE2 = Material.GLOWING_REDSTONE_ORE.getId();
		LAPIS = Material.LAPIS_ORE.getId();
		GOLD = Material.GOLD_ORE.getId();
		DIAMOND = Material.DIAMOND_ORE.getId();
		EMERALD = Material.EMERALD_ORE.getId();
	}


	public void onEnable()
	{
		log = getLogger();

		File configFile = new File( getDataFolder(), "config.yml" );
		if ( !configFile.exists() )
		{
			log.info( "No config file exists. Creating a new config file." );
			saveDefaultConfig();
		}

		FileConfiguration config = getConfig();
		coalLimit = (float) config.getDouble( "coal" );
		ironLimit = (float) config.getDouble( "iron" );
		redstoneLimit = (float) config.getDouble( "redstone" );
		lapisLimit = (float) config.getDouble( "lapis" );
		goldLimit = (float) config.getDouble( "gold" );
		diamondLimit = (float) config.getDouble( "diamond" );
		emeraldLimit = (float) config.getDouble( "emerald" );

		playerOreLog = LoadPlayerOreLog();

		getServer().getPluginManager().registerEvents( this,  this );
	}

	public void onDisable()
	{
		SavePlayerOreLog();
	}


	@EventHandler( priority = EventPriority.MONITOR )
	public void BlockBreak( BlockBreakEvent event )
	{
		// REDTAG TODO - ignore worlds
		// REDTAG TODO - ignore players based on permission

		Block block = event.getBlock();
		int blockId = block.getTypeId();

		if ( blockId == STONE || blockId == COAL || blockId == IRON || blockId == REDSTONE1 ||
				blockId == REDSTONE2 || blockId == LAPIS || blockId == GOLD ||
				blockId == DIAMOND || blockId == EMERALD )
		{
			int y = block.getLocation().getBlockY();
			String player = event.getPlayer().getName().toUpperCase();

			if ( ! playerOreLog.containsKey( player ))
				playerOreLog.put( player, new int[POS_SIZE] );

			int frequency[] = (int[]) playerOreLog.get( player );

			if ( blockId == STONE ) frequency[ STONE_POS ]++;
			else if ( blockId == COAL ) frequency[ COAL_POS ]++;
			else if ( blockId == IRON && y <= 64 ) frequency[ IRON_POS ]++;
			else if (( blockId == REDSTONE1 || blockId == REDSTONE2 ) && y <= 16 ) frequency[ REDSTONE_POS ]++;
			else if ( blockId == LAPIS && y <= 35 ) frequency[ LAPIS_POS ]++;
			else if ( blockId == GOLD && y <= 35 ) frequency[ GOLD_POS ]++;
			else if ( blockId == DIAMOND && y <= 16 ) frequency[ DIAMOND_POS ]++;
			else if ( blockId == EMERALD && y <= 32 ) frequency[ EMERALD_POS ]++;

			playerOreLog.put( player, frequency );
		}
	}


	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args)
	{
		if ( cmd.getName().equalsIgnoreCase( "orewatch" ))
		{
			String cmdname;
			if ( args.length < 1 )
				return false; // REDTAG send syntax error here
			cmdname = args[0];

			if ( cmdname.equalsIgnoreCase( "show" ))
			{
				String player;
				if ( args.length < 2 )
				{
					sender.sendMessage( ChatColor.DARK_RED + "Syntax: /orewatch show [player]" );
					return true;
				}

				player = args[1];

				// REDTAG TODO add permissions

				if ( ! playerOreLog.containsKey( player.toUpperCase() ))
				{
					sender.sendMessage( ChatColor.DARK_RED + "Player \"" + player + "\" not found. " );
					return true;
				}

				int frequency[] = (int[]) playerOreLog.get( player.toUpperCase() );
				sender.sendMessage( ChatColor.WHITE + "Ore log for " + ChatColor.GRAY + player );
				sender.sendMessage( ChatColor.GRAY + "Stone: " + frequency[ STONE_POS ] );

				float coalPercentage = GetPercentage( frequency[ COAL_POS ], frequency[ STONE_POS ] );
				float ironPercentage = GetPercentage( frequency[ IRON_POS ], frequency[ STONE_POS ] );
				float redstonePercentage = GetPercentage( frequency[ REDSTONE_POS ], frequency[ STONE_POS ] );
				float lapisPercentage = GetPercentage( frequency[ LAPIS_POS ], frequency[ STONE_POS ] );
				float goldPercentage = GetPercentage( frequency[ GOLD_POS ], frequency[ STONE_POS ] );
				float diamondPercentage = GetPercentage( frequency[ DIAMOND_POS ], frequency[ STONE_POS ] );
				float emeraldPercentage = GetPercentage( frequency[ EMERALD_POS ], frequency[ STONE_POS ] );

				sender.sendMessage( GetColor( COAL, coalPercentage ) + "Coal: " + coalPercentage + "% [" + frequency[ COAL_POS ] + "]" );
				sender.sendMessage( GetColor( IRON, ironPercentage ) + "Iron: " + ironPercentage + "% [" + frequency[ IRON_POS ] + "]" );
				sender.sendMessage( GetColor( REDSTONE1, redstonePercentage ) + "Redstone: " + redstonePercentage + "% [" + frequency[ REDSTONE_POS ] + "]" );
				sender.sendMessage( GetColor( LAPIS, lapisPercentage ) + "Lapis: " + lapisPercentage + "% [" + frequency[ LAPIS_POS ] + "]" );
				sender.sendMessage( GetColor( GOLD, goldPercentage ) + "Gold: " + goldPercentage + "% [" + frequency[ GOLD_POS ] + "]" );
				sender.sendMessage( GetColor( DIAMOND, diamondPercentage ) + "Diamond: " + diamondPercentage + "% [" + frequency[ DIAMOND_POS ] + "]" );
				sender.sendMessage( GetColor( EMERALD, emeraldPercentage ) + "Emerald: " + emeraldPercentage + "% [" + frequency[ EMERALD_POS ] + "]" );
			}

			return true;
		}

		return false;
	}


	private ChatColor GetColor( int blockId, float percentage )
	{
		float limit = 100;

		if ( blockId == COAL ) limit = coalLimit;
		else if ( blockId == IRON ) limit = ironLimit;
		else if ( blockId == REDSTONE1 ) limit = redstoneLimit;
		else if ( blockId == LAPIS ) limit = lapisLimit;
		else if ( blockId == GOLD ) limit = goldLimit;
		else if ( blockId == DIAMOND ) limit = diamondLimit;
		else if ( blockId == EMERALD ) limit = emeraldLimit;

		if ( percentage < limit )
			return ChatColor.GREEN;
		else
			return ChatColor.DARK_RED;
	}


	private float GetPercentage( int numerator, int denominator )
	{
		if ( denominator == 0 )
			denominator = 1;

		float percentage = (float) numerator / (float) denominator;
		percentage = Math.round( percentage * 10000F ) / 100F;
		return percentage;
	}


	@SuppressWarnings("unchecked")
	private HashMap<String, int[]> LoadPlayerOreLog()
	{
		HashMap<String, int[]> playerLog = null;
		try
		{
			ObjectInputStream stream = new ObjectInputStream( new FileInputStream( "plugins/OreWatch/playerorelog.bin" ));
			Object result = stream.readObject();
			playerLog = (HashMap<String, int[]>) result;
			stream.close();
			log.info( "Loaded player ore log." );
		}
		catch ( Exception e )
		{
			log.info( "No existing player ore log found. Starting from scratch." );
			playerLog = new HashMap<String, int[]>();
		}

		return playerLog;
	}


	private void SavePlayerOreLog()
	{
		try
		{
			ObjectOutputStream stream = new ObjectOutputStream( new FileOutputStream( "plugins/OreWatch/playerorelog.bin" ));
			stream.writeObject( playerOreLog );
			stream.flush();
			stream.close();
		}
		catch( Exception e )
		{
			log.severe( "Could not save player ore log. Please report the following error message:" );
			e.printStackTrace();
		}
	}
}
