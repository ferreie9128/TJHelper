package util;

import java.io.IOException;
import java.util.Properties;

public class LoadConfig {
	private Properties prop = new Properties();
	public LoadConfig() {
		try {
			prop.load(ClassLoader.getSystemResourceAsStream("application.prop"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Properties getProp() {
		return prop;
	}
	public void setProp(Properties prop) {
		this.prop = prop;
	}
}
