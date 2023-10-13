package dev.rdh.createunlimited.fabric.mixin;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

import com.mojang.brigadier.arguments.ArgumentType;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.rdh.createunlimited.CreateUnlimited;

import dev.rdh.createunlimited.*;

import manifold.rt.api.NoBootstrap;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.commands.CommandSourceStack;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;

import net.minecraft.resources.ResourceLocation;

#if PRE_CURRENT_MC_1_19_2
import net.minecraftforge.api.ModLoadingContext;
#elif POST_CURRENT_MC_1_20_1
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
#endif
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@SuppressWarnings("OverwriteAuthorRequired")
@NoBootstrap
@Mixin(Utils.class)
public class UtilsFabricImpl {

	@Overwrite
	public static Path getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Overwrite
	public static void registerCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, selection) -> dispatcher.register(command));
	}

	@Overwrite
	public static void registerConfig(ModConfig.Type type, IConfigSpec<?> spec) {
		#if PRE_CURRENT_MC_1_19_2
		ModLoadingContext.registerConfig(CreateUnlimited.ID, type, spec);
		#elif POST_CURRENT_MC_1_20_1
		ForgeConfigRegistry.INSTANCE.register(CreateUnlimited.ID, type, spec);
		#endif
	}

	@Overwrite
	public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>>
	void registerArgument(Class<A> clazz, I info, ResourceLocation id) {
		ArgumentTypeRegistry.registerArgumentType(id, clazz, info);
	}

	@Overwrite
	public static String getVersion() {
		return FabricLoader.getInstance()
						   .getModContainer(CreateUnlimited.ID)
						   .orElseThrow()
						   .getMetadata()
						   .getVersion()
						   .getFriendlyString();
	}

	@Overwrite
	public static boolean isDevEnv() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Overwrite
	public static Attribute getReachAttribute() {
		return ReachEntityAttributes.REACH;
	}

	@Overwrite
	public static String platformName() {
		return FabricLoader.getInstance().isModLoaded("quilt_loader") ? "Quilt" : "Fabric";
	}
}