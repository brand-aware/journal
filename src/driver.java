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
		if(args.length < 2) {
			System.out.println("java driver <current dir> <user dir>");
			System.exit(0);
		}
		System.out.println(args[0] + "\n" + args[1]);
		
		Properties properties;
		Screen screen;
		try {
			properties = new Properties(args[0], args[1]);
			screen = new Screen(properties);
			screen.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}