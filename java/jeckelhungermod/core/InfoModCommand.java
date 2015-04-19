package jeckelhungermod.core;

import jeckelhungermod.common.ModCommand;
import jeckelhungermod.common.UpdateChecker;
import jeckelhungermod.content.HungerUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.ModMetadata;

public class InfoModCommand extends ModCommand
{
	public InfoModCommand(final ModMetadata modMetadata, final UpdateChecker checker, final String usageText)
	{
		super(modMetadata.modId, usageText);
		this.modMetadata = modMetadata;
		this.checker = checker;
	}

	public InfoModCommand(final ModMetadata modMetadata, final UpdateChecker checker, int permLevel, final String usageText)
	{
		super(modMetadata.modId, permLevel, usageText);
		this.modMetadata = modMetadata;
		this.checker = checker;
	}

	public final ModMetadata modMetadata;

	public final UpdateChecker checker;

	@Override public void doCommand(ICommandSender sender)
	{
		String text;

		addMessage(sender, EnumChatFormatting.BOLD + "+++++ Mod Info +++++");

		addMessage(sender, "Mod Name: " + this.modMetadata.name);

		addMessage(sender, "Mod Id: " + this.modMetadata.modId);

		addMessage(sender, "Version: " + this.modMetadata.version);

		text = "Update Checker: ";
		if (!this.checker.isEnabled()) { text += "Disabled"; }
		else
		{
			text += "Enabled (";
			if (!this.checker.isChecked()) { text += "Unchecked"; }
			else
			{
				if (this.checker.isUpdatable()) { text += "Updatable"; }
				else { text += "Current"; }
				text += " [" + this.checker.getVersionRemote() + "]";
			}
			text += ")";
		}
		addMessage(sender, text);

		addMessage(sender);

		text = StatCollector.translateToLocalFormatted("command.display.difficulty.text", StatCollector.translateToLocal(sender.getEntityWorld().difficultySetting.getDifficultyResourceKey()));
		addMessage(sender, text);

		final boolean peaceful = HungerUtil.isPeacefulHunger();
		if (!peaceful) { text = "command.display.peacefulhunger.false.text"; }
		else
		{
			final boolean active = HungerUtil.isPeacefulHunger(sender.getEntityWorld());
			text = "command.display.peacefulhunger.true." + active + ".text";
		}
		text = StatCollector.translateToLocal(text);
		addMessage(sender, text);

		final boolean enabled = HungerUtil.isEnabled();
		if (!(sender instanceof EntityPlayer)) { text = "command.display.state." + enabled + ".text"; }
		else
		{
			final EntityPlayer player = (EntityPlayer) sender;
			if (!enabled) { text = "command.display.state.false.text"; }
			else
			{
				final boolean active = HungerUtil.isActive(player);
				text = "command.display.state.true." + active + ".text";
			}
		}
		text = StatCollector.translateToLocal(text);
		addMessage(sender, text);

		text = StatCollector.translateToLocalFormatted("command.display.hungerrate.text", Refs.getConfigValues().getDefaultHungerRate());
		addMessage(sender, text);

		text = StatCollector.translateToLocalFormatted("command.display.metabolicrate.text", Refs.getConfigValues().getDefaultMetabolicRate());
		addMessage(sender, text);

		if (sender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) sender;

			/*addMessage(sender);

			text = "Player ID: " + player.getEntityId();
			addMessage(sender, text);
			text = "Unique ID: " + player.getUniqueID();
			addMessage(sender, text);
			text = "Persistent ID: " + player.getPersistentID();
			addMessage(sender, text);*/

			addMessage(sender);

			text = String.format(StatCollector.translateToLocal("command.display.food.text"), player.getFoodStats().getFoodLevel());
			addMessage(sender, text);

			text = String.format(StatCollector.translateToLocal("command.display.saturation.text"), player.getFoodStats().getSaturationLevel());
			addMessage(sender, text);

			text = String.format(StatCollector.translateToLocal("command.display.exhaustion.text"), HungerUtil.getExhaustionLevel(player));
			addMessage(sender, text);
		}

		addMessage(sender, "--------------------");
	}

	private static void addMessage(ICommandSender sender, String text)
	{
		sender.addChatMessage(new ChatComponentText(text));
	}

	private static void addMessage(ICommandSender sender)
	{
		sender.addChatMessage(new ChatComponentText(""));
	}
}
