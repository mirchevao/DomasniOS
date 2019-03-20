import java.util.Random;

public class Switcher {
	private static final Random RANDOM = new Random();

	
	public static void forceSwitch(int range) {
		try {
			Thread.sleep(RANDOM.nextInt(range));
		} catch (InterruptedException e) {
		}
	}
}
