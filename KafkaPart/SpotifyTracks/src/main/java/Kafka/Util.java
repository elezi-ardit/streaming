package Kafka;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Util {

	public static Properties loadConfig(String configFile) throws IOException {
		configFile = configFile.replaceFirst("^~", System.getProperty("user.home"));
		if (!Files.exists(Paths.get(configFile))) {
			throw new IOException(configFile + " not found.");
		}
		final Properties cfg = new Properties();
		try (InputStream inputStream = new FileInputStream(configFile)) {
			System.out.println(" I will load " + inputStream);
			cfg.load(inputStream);
		}
		System.out.println(cfg);
		return cfg;
	}

}
