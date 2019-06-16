package runner;

import math.Matrix4;
import math.Vector2;
import math.Vector4;
import renderEngine.Display;

public class Runner {

	Display display;
	
	public Runner() {
		display = new Display(720, 720);
		display.clearBackBuffer(0, 0, 0, 1);
		display.renderFlatBottomTriangle(new Vector2(-1f, -1f), new Vector2(1f, -1f), new Vector2(0f, 0f), new Vector4(255f, 0, 0, 255f));		
		display.swapBuffers();
	}
	
	public static void main(String[] args) {
		new Runner();
	}
	
}
