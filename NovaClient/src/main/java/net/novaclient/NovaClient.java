package net.novaclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.novaclient.config.ConfigManager;
import net.novaclient.module.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class NovaClient implements ClientModInitializer {
	public static final String MOD_ID = "novaclient";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing NovaClient");

		ConfigManager.init();
		ModuleManager.init();
		
		LOGGER.info("NovaClient initialized successfully!");
	}
}
