package dev.emi.trinkets.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

/**
 * Drops trinkets on death if other items are dropping
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract ItemEntity dropItem(ItemStack stack, boolean b1, boolean b2);

	@Inject(at = @At("RETURN"), method = "dropInventory")
	protected void dropInventory(CallbackInfo info) {
		if (!this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
			Inventory inv = TrinketsApi.TRINKETS.get(this).getInventory();
			for (int i = 0; i < inv.getInvSize(); i++) {
				ItemStack stack = inv.getInvStack(i);
				if (!stack.isEmpty()) {
					this.dropItem(stack, true, false);
				}
			}
			inv.clear();
		}
	}
}