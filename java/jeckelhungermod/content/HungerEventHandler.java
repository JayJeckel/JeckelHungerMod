package jeckelhungermod.content;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HungerEventHandler
{
	@SubscribeEvent public void onPlayerJoin(EntityJoinWorldEvent event)
	{
		if (HungerUtil.isDisabled()) { return; }
		if (!(event.entity instanceof EntityPlayer)) { return; }

	    final EntityPlayer player = (EntityPlayer)event.entity;
	    HungerUtil.setExhaustionLevel(player, HungerUtil.exhaustionStartValue);
	}
	
	@SubscribeEvent public void onPlayerUpdate(LivingUpdateEvent event)
    {
		if (event.entityLiving.worldObj.isRemote) { return; }
		if (HungerUtil.isDisabled()) { return; }
		if (!(event.entityLiving instanceof EntityPlayer)) { return; }
		final EntityPlayer player = (EntityPlayer)event.entityLiving;
		if (HungerUtil.isInactive(player)) { return; }

	    final double hungerRate = HungerUtil.hungerRate;
	    final double metabolicRate = HungerUtil.metabolicRate;
	    final double exhaustStartLevel = HungerUtil.exhaustionStartValue;

	    double exhaustion = HungerUtil.getExhaustionLevel(player);

        if (hungerRate < 0.0)
	    {
	        if (exhaustion > -1.0 && exhaustion < 0.0) { HungerUtil.setExhaustionLevel(player, 4.0); }
	        if (exhaustion > 0.0 && exhaustion < 4.0) { HungerUtil.setExhaustionLevel(player, exhaustStartLevel); }
        }

        if (hungerRate > 0.0 && exhaustion <= exhaustStartLevel) { HungerUtil.setExhaustionLevel(player, exhaustStartLevel); }

        exhaustion = HungerUtil.getExhaustionLevel(player);
        //if (JeckelHungerMod.getConfig().enablePeacefulHunger && event.entityLiving.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
        if (HungerUtil.isPeacefulHunger(event.entityLiving.worldObj))
        {
            if (exhaustion > 4.0F)
            {
            	exhaustion -= 4.0F;
                HungerUtil.setExhaustionLevel(player, exhaustion);

                if (player.getFoodStats().getSaturationLevel() > 0.0F)
                {
                	player.getFoodStats().setFoodSaturationLevel(Math.max(player.getFoodStats().getSaturationLevel() - 1.0F, 0.0F));
                }
                else
                {
                	player.getFoodStats().setFoodLevel(Math.max(player.getFoodStats().getFoodLevel() - 1, 0));
                }
            }
        }
        if (metabolicRate != 0.0)
        {
	        if (hungerRate < 0.0) { HungerUtil.setExhaustionLevel(player, exhaustion - metabolicRate); }
	        if (hungerRate > 0.0) { HungerUtil.setExhaustionLevel(player, exhaustion + metabolicRate); }
        }
    }
}
