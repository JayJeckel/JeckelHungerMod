package jeckelhungermod.core;

import jeckelhungermod.common.configs.ConfigHandlerValues;
import net.minecraftforge.common.config.Configuration;

public class ConfigValues extends ConfigHandlerValues
{
	private static final long serialVersionUID = -3063149289875479294L;

	public ConfigValues()
	{
		this.add(this._updateChecking);
		this.add(this._peacefulHunger);
		this.add(this._hungerRate);
		this.add(this._metabolicRate);
	}

	public boolean isUpdateCheckingEnabled() { return this._updateChecking.getValue(); }
	protected final ConfigValueBoolean _updateChecking = new ConfigValueBoolean("EnableUpdateChecking", Configuration.CATEGORY_GENERAL,
			"Control automatic update checking.\n.Setting this option to false will disable version checking.",
			true);

	public boolean isPeacefulHungerEnabled() { return this._peacefulHunger.getValue(); }
	protected final ConfigValueBoolean _peacefulHunger = new ConfigValueBoolean("EnablePeacefulHunger", Configuration.CATEGORY_GENERAL,
			"Hunger is not normally enabled if the difficulty is set to peaceful in vanilla Minecraft, but this option can change that behavior.\nSetting the option to true will enable hunger when difficulty is peaceful and false will use vanilla Minecraft behavior.",
			false);

	public float getDefaultHungerRate() { return this._hungerRate.getValue(); }
	protected final ConfigValueFloat _hungerRate = new ConfigValueFloat("DefaultHungerRate", Configuration.CATEGORY_GENERAL,
			"Hunger Rate affects how quickly taking actions, such as sprinting, mining, etc, will cause a player to lose shanks and lower their hunger bar."
				+ "\n" + "Setting this value to a positive number, for example 1.5 or 4.0, will cause shanks to disappear at an increased rate. Conversely, using a negative value, for example -0.5 or -2.0, will decrease the rate at which actions lower the hunger bar. Using a value of 1.0, actions will consume the same amount of shanks as vanilla Minecraft.",
			1.0f, -1000.0f, 1000.0f);

	public float getDefaultMetabolicRate() { return this._metabolicRate.getValue(); }
	protected final ConfigValueFloat _metabolicRate = new ConfigValueFloat("DefaultMetabolicRate", Configuration.CATEGORY_GENERAL,
			"Metabolic Rate controls the steady drain of hunger over time, even when remaining completely still and not performing any actions."
				+ "\n" + "The value represents the number of Minecraft day/night cycles, each 20 real world minutes, that a freshly spawned player with full food and saturation points will take to start starving if the player takes no actions and does not move at all."
				+ "\n" + "Decimal numbers are acceptable, for example 3.5, but negative numbers are not valid. Using a value of 0.0 will disable this feature.",
				0.0f, 0.0f, 1000.0f);

	@Override public void update(final Configuration config)
	{
		super.update(config);

		Refs.getUpdateChecker().enable(this.isUpdateCheckingEnabled());
	}
}
