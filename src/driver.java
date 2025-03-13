/**
 * 
 * @author mike802
 *
 * brand_aware
 * ??? - 2019
 * 
 */
import java.io.IOException;

import core.Properties;
import core.Screen;

public class driver {
	
	public static void main(String[] args) {		
		Properties properties;
		Screen screen;
		String currentDir = System.getProperty("user.dir");
		String userDir = System.getProperty("user.home");
		try {
			properties = new Properties(currentDir, userDir);
			screen = new Screen(properties);
			screen.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}