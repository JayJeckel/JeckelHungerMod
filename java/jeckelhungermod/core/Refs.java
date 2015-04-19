package jeckelhungermod.core;

import jeckelhungermod.common.UpdateChecker;
import jeckelhungermod.common.configs.ConfigHandler;
import jeckelhungermod.content.HungerEventHandler;
import jeckelhungermod.content.HungerUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Refs
{
	public static final String ModId = "jeckelhungermod";
	public static final String ModName = "JeckelHungerMod";

	public static final String ConfigFactoryTypeName = Refs.ModId + ".common.configs.ConfigHandler$Factory";
	public static final String ProxyServerTypeName = Refs.ModId + ".proxy.CommonProxy";
	public static final String ProxyClientTypeName = Refs.ModId + ".proxy.ClientProxy";

	public static Object getMod() { return _mod; }
	private static Object _mod;

	public static Logger getLogger() { return _logger; }
	private static Logger _logger;

	public static ModMetadata getMetadata() { return _metadata; }
	private static ModMetadata _metadata;

	public static ConfigHandler getConfigHandler() { return _configHandler; }
	private static ConfigHandler _configHandler;

	public static ConfigValues getConfigValues() { return _configValues; }
	private static ConfigValues _configValues;

	public static UpdateChecker getUpdateChecker() { return _updateChecker; }
	private static UpdateChecker _updateChecker;

	public static void pre(final Object modInstance, final FMLPreInitializationEvent event)
	{
		_mod = modInstance;
		_logger = LogManager.getLogger(Refs.ModName);
		_metadata = event.getModMetadata();

		_configValues = new ConfigValues();
		_configHandler = new ConfigHandler(Refs.ModId, Refs.ModName, Refs.getConfigValues());
		_updateChecker = new UpdateChecker(Refs.ModName, Refs.getMetadata().version, Refs.getLogger());

		_configHandler.initialize(event);
		_updateChecker.initialize(event);
	}

	public static void initialize(FMLInitializationEvent event)
	{
		HungerUtil.initialize(Refs.getLogger());
		MinecraftForge.EVENT_BUS.register(new HungerEventHandler());
	}

	public static void post(FMLPostInitializationEvent event)
	{
	}

	public static boolean isSinglePlayer() { return MinecraftServer.getServer().isSinglePlayer(); }

	public static boolean isMultiPlayer() { return MinecraftServer.getServer().isDedicatedServer(); }
}
