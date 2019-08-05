package runner;

import math.Vector2;
import math.Vector4;
import renderEngine.Display;

public class Runner {

	Display display;

	public Runner() {
		display = new Display(720, 720);
		display.clearBackBuffer(0, 0, 0, 1);
//		display.renderTriangle(new Vector2(0f, 1f), new Vector2(-1f, 0.5f), new Vector2(1f, 1f), new Vector4(255f, 0, 0, 255f));		
//		display.renderTriangle(new Vector2(-0.5f, -1f), new Vector2(0f, 0f), new Vector2(0.7f, -1f), new Vector4(255f, 0, 0, 255f));
//		display.renderLine(new Vector2(-1.06f, 1f), new Vector2(1f, -1f), new Vector4(255f, 255f, 255f, 255f), new Vector4(0f, 0f, 255f, 255f));
		display.renderTriangle(new Vector2(-1.0f,0f), new Vector2(-0.75f, -0.75f), new Vector2(0f, 0f),
				new Vector4(255f, 0, 0, 255f));
		display.swapBuffers();
	}

	public static void main(String[] args) {
		new Runner();
	}

}
