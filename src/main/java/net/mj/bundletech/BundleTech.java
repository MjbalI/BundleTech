package net.mj.bundletech;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.DispenserBlock;
import net.minecraft.item.Items;
import net.mj.bundletech.bundledump.DispenseBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleTech implements ModInitializer {
	public static final String MOD_ID = "bundletech";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		DispenserBlock.registerBehavior(Items.BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.WHITE_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.ORANGE_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.MAGENTA_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.LIGHT_BLUE_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.YELLOW_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.LIME_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.PINK_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.GRAY_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.LIGHT_GRAY_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.CYAN_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.PURPLE_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.BLUE_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.BROWN_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.GREEN_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.RED_BUNDLE, new DispenseBundle());
		DispenserBlock.registerBehavior(Items.BLACK_BUNDLE, new DispenseBundle());
	}
}