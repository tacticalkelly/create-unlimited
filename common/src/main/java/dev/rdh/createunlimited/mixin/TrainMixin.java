package dev.rdh.createunlimited.mixin;


import com.simibubi.create.content.trains.entity.Train;

import dev.rdh.createunlimited.config.CUConfigs;

import manifold.rt.api.NoBootstrap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@NoBootstrap
@Mixin(value = Train.class, remap = false)
public abstract class TrainMixin {
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = 4))
	private double modifyMaxStress(double original) {
		double a = CUConfigs.server().maxAllowedStress.get();
		return (a == -1) ? Double.MAX_VALUE : a;
	}
}
