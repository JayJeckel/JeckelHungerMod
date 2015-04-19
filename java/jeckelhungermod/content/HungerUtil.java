package jeckelhungermod.content;

import java.lang.reflect.Field;

import jeckelhungermod.core.Refs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class HungerUtil
{
	private static Logger _logger = null;
	private static Field _fieldExhaustion = null;

	public static double hungerRate = 0.0;
	public static double metabolicRate = 0.0;
	public static double exhaustionStartValue = 0.0;

	/**
	 * Initialize required static variables.
	 * This method is responsible for setting the internally used logger and
	 * resolving the FoodStats.foodExhaustionLevel field.
	 * @param logger Logger to use for internal logging.
	 */
	public static void initialize(final Logger logger)
	{
		HungerUtil._logger = logger;

		Field field = null;

		HungerUtil._logger.log(Level.INFO, "Resolving FoodStats.foodExhaustionLevel field.");
		try
		{
			field = FoodStats.class.getDeclaredField("field_75126_c");
			HungerUtil._logger.log(Level.INFO, "Resolved as obfuscated environment.");
		}
		catch (NoSuchFieldException e) { field = null; }

		if (field == null)
		{
			try
			{
				field = FoodStats.class.getDeclaredField("foodExhaustionLevel");
				HungerUtil._logger.log(Level.INFO, "Resolved as deobfuscated environment.");
			}
			catch (NoSuchFieldException e) { field = null; }
		}

		if (field == null)
		{
			HungerUtil._logger.log(Level.ERROR, "Unresolved! Mod effects will be disabled!");
			return;
		}

		if (!field.isAccessible()) { field.setAccessible(true); }
		HungerUtil._fieldExhaustion = field;
	}

	/**
	 * Calculate and store the core internal values used in hunger processing.
	 * @param defaultHungerRate Initial hunger rate used in calculations.
	 * @param defaultMetabolicRate Initial metabolic rate used in calculations.
	 */
	public static void calculateValues(final double defaultHungerRate, final double defaultMetabolicRate)
	{
		HungerUtil._logger.log(Level.INFO, "Initializing hunger values.");
		HungerUtil.hungerRate = defaultHungerRate;

		final double hungerRate = (defaultHungerRate == 0.0 ? 1.0 : defaultHungerRate);
		final double metabolicRate = defaultMetabolicRate;

		if (hungerRate > 0.0) { HungerUtil.exhaustionStartValue = 4.0 / hungerRate * (hungerRate - 1.0); }
		if (hungerRate < 0.0) { HungerUtil.exhaustionStartValue = hungerRate * 4.0 - 1.0; }

		if (metabolicRate == 0.0) { HungerUtil.metabolicRate = 0.0; }
		else { HungerUtil.metabolicRate = 0.0041666 / metabolicRate / hungerRate; }
	}

	/**
	 * Check if the mod effects are enabled.
	 * @return True if mod effects are enabled.
	 */
	public static boolean isEnabled() { return !isDisabled(); }

	/**
	 * Check if the mod effects are disabled.
	 * @return True if mod effects are disabled.
	 */
	public static boolean isDisabled() { return HungerUtil._fieldExhaustion == null || (HungerUtil.hungerRate == 0.0 && HungerUtil.metabolicRate == 0.0); }

	/**
	 * Check if hunger effects are active for the given player.
	 * @param player Player to check.
	 * @return True if player is not in creative mod or otherwise unaffected by mod effects.
	 */
	public static boolean isActive(EntityPlayer player) { return !isInactive(player); }

	/**
	 * Check if hunger effects are inactive for the given player.
	 * @param player Player to check.
	 * @return True if player is in creative mod or otherwise affected by mod effects.
	 */
	public static boolean isInactive(EntityPlayer player)
	{
		return player.capabilities.isCreativeMode || player.getFoodStats().getFoodLevel() <= 0;
	}

	/**
	 * Check if the world's difficulty is set to Peaceful.
	 * @param world
	 * @return True if difficulty is peaceful and peaceful hunger is enabled.
	 */
	public static boolean isPeaceful(World world) { return world.difficultySetting == EnumDifficulty.PEACEFUL; }

	/**
	 * Check if peaceful hunger is enabled.
	 * @return True if peaceful hunger is enabled.
	 */
	public static boolean isPeacefulHunger() { return Refs.getConfigValues().isPeacefulHungerEnabled(); }

	/**
	 * Check if peaceful hunger is enabled and active.
	 * @param world
	 * @return True if difficulty is peaceful and peaceful hunger is enabled.
	 */
	public static boolean isPeacefulHunger(World world) { return isPeacefulHunger() && isPeaceful(world); }

	/**
	 * Get the given player's exhaustion level.
	 * @param player Player to get exhaustion level from.
	 * @return Player's exhaustion level.
	 */
	public static double getExhaustionLevel(final EntityPlayer player)
	{
		try { return (float) HungerUtil._fieldExhaustion.getFloat(player.getFoodStats()); }
		catch (IllegalArgumentException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
		return 0;
	}

	/**
	 * Set the given player's exhaustion level.
	 * @param player Player to set exhaustion level on.
	 * @param exhaustion Exhaustion level to set on the player.
	 */
	public static void setExhaustionLevel(final EntityPlayer player, final double exhaustion)
	{
		try { HungerUtil._fieldExhaustion.setFloat(player.getFoodStats(), (float)exhaustion); }
		catch (IllegalArgumentException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
	}
}
