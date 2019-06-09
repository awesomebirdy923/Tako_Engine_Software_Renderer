package renderEngine;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import math.*;

public class Display {

	private static final String TITLE = "Tako Engine";
	
	private JFrame frame;
	private JLabel display;
	
	private static final int BYTES_PER_PIXEL = 4;
	
	private BufferedImage imageBuffer;
	private byte[] backBuffer;
	
	public Display(int width, int height) {
		frame = new JFrame(TITLE);
		display = new JLabel();
		backBuffer = new byte[width*height*BYTES_PER_PIXEL];
		imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		display.setMinimumSize(new Dimension(width, height));
		display.setMaximumSize(new Dimension(width, height));
		display.setPreferredSize(new Dimension(width, height));
		frame.add(display);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	public void renderLine(Vector2 start, Vector2 end, Vector4 color1) {
		int startX = (int) (((start.x+1.0f)*0.5f) * frame.getWidth());
		int startY = (int) (((start.y+1.0f)*0.5f) * frame.getHeight());
		int endX = (int) (((end.x+1.0f)*0.5f) * frame.getWidth());
		int endY = (int) (((end.y+1.0f)*0.5f) * frame.getHeight());
		
		Vector2 point1 = new Vector2(startX, startY);
		Vector2 point2 = new Vector2(endX, endY);
		
		int rise = endY - startY;
		int run = endX - startX;
		
		if(run!=0) {
			float m = (float)rise / (float)run;
			float b = (float) (startY - (m * ((float)startX)));
			int ars = Math.abs(rise);
			int arn = Math.abs(run);
			if(ars > arn) {
				int addAmount = (rise > 0 ? 1 : -1);
				for (int i = 0; i < ars; i++) {
					int y = startY + (i*addAmount);
					int x = (int) (((float) y / m) - (b / m));
					renderPixel(x, y, color1);
				}
			}else {
				int addAmount = (rise > 0 ? 1 : -1);
				for (int i = 0; i < arn; i++) {
					int x = startX + (i*addAmount);
					int y = (int) (((float) x / m) - (b / m));
					renderPixel(x, y, color1);
				}
			}
		}else {
			int ars = Math.abs(rise);
			int addAmount = (rise > 0 ? 1 : -1);
			for (int i = 0; i < ars; i++) {
				int y = startY + (i*addAmount);
				int x = startX;
				renderPixel(x, y, color1);
			}
		}
	}
	
	public void renderPoint(Vector2 point, Vector4 color) {
		int x = (int) (((point.x+1f)*0.5f) * frame.getWidth());
		int y = (int) (((point.y+1f)*0.5f) * frame.getHeight());
		renderPixel(x,y,color);
	}
	
	public void renderPixel(int x, int y, Vector4 color) {
		int rows = frame.getWidth() * BYTES_PER_PIXEL;
		int pixelIndex =  (int) ((rows * y) + (x * BYTES_PER_PIXEL));
		if(pixelIndex>=backBuffer.length || pixelIndex < 0) {
			return;
		}
		backBuffer[pixelIndex] = (byte) color.w;
		backBuffer[pixelIndex+1] = (byte) color.z;
		backBuffer[pixelIndex+2] = (byte) color.y;
		backBuffer[pixelIndex+3] = (byte) color.x;
	}
	
	public void clearBackBuffer(float r, float b, float g, float a) {
		for (int i = 0; i < backBuffer.length; i+=4) {
			backBuffer[i] = (byte) (a*255);
			backBuffer[i+1] = (byte) (b*255);
			backBuffer[i+2] = (byte) (g*255);
			backBuffer[i+3] = (byte) (r*255);
		}
	}
	
	public void swapBuffers() {
		byte[] buffer = ((DataBufferByte)imageBuffer.getRaster().getDataBuffer()).getData();
		System.arraycopy(backBuffer, 0, buffer, 0, backBuffer.length);
		display.setIcon(new ImageIcon(imageBuffer));
		frame.pack();
	}
	
}
