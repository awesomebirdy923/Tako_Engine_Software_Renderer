package renderEngine;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Vector;

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
	
	public void renderTriangle(Vector2 v1, Vector2 v2, Vector2 v3, Vector4 color) {
		v1 = new Vector2((v1.x + 1f)*0.5f*display.getWidth(), (v1.y + 1f)*0.5f*display.getHeight());
		v2 = new Vector2((v2.x + 1f)*0.5f*display.getWidth(), (v2.y + 1f)*0.5f*display.getHeight());
		v3 = new Vector2((v3.x + 1f)*0.5f*display.getWidth(), (v3.y + 1f)*0.5f*display.getHeight());
		Vector2 least;
		Vector2 secondGreatest;
		Vector2 greatest;
		if(v1.y < v2.y && v1.y < v3.y) {
			least = v1;
			if(v2.y < v3.y) {
				secondGreatest = v2;
				greatest = v3;
			}else {
				secondGreatest = v3;
				greatest = v2;
			}
		} else if(v2.y < v3.y) {
			least = v2;
			if(v1.y < v3.y) {
				secondGreatest = v1;
				greatest = v3;
			} else {
				secondGreatest = v3;
				greatest = v1;
			}
		} else {
			least = v3;
			if(v1.y < v2.y) {
				secondGreatest = v1;
				greatest = v2;
			} else {
				secondGreatest = v2;
				greatest = v1;
			}
		}
		if(least.y == secondGreatest.y) {
			if(secondGreatest.x < least.x) {
				renderFlatBottomTriangle(secondGreatest, least, greatest, color);
			} else {
				renderFlatBottomTriangle(least, secondGreatest, greatest, color);
			}
			return;
		} else if(greatest.y == secondGreatest.y) {
			if(secondGreatest.x < greatest.x) {
				renderFlatTopTriangle(secondGreatest, greatest, least, color);
			} else {
				renderFlatTopTriangle(greatest, secondGreatest, least, color);
			}
			return;
		}
		Vector2 fourthVertex;
		int rise = (int) (greatest.y-least.y);
		int run = (int) (greatest.x-least.x);
		float m = (float)rise / (float)run;
		float b = (float) ((greatest.y)-(m*greatest.x));
		float x = (float) ((secondGreatest.y/m) - (b/m));
		float y = m*x+b;
		fourthVertex = new Vector2(x,y);
		System.out.println(rise + " " + run);
		renderFlatTopTriangle(secondGreatest, fourthVertex, least, color);
		renderFlatBottomTriangle(secondGreatest, fourthVertex, greatest, color);
	}
	
	public void renderFlatTopTriangle(Vector2 t1, Vector2 t2, Vector2 b, Vector4 color) {
		Vector2 v1 = t1;
		Vector2 v2 = t2;
		Vector2 v3 = b;
		int rise = (int) (v1.y - v3.y);
		int run1 = (int) (v1.x - v3.x);
		int run2 = (int) (v2.x - v3.x);
		int[] startXs = new int[rise];
		int[] endXs = new int[rise];
		if(run1 != 0) {
			float m = (float)rise / (float)run1;
			float yIntercept = v3.y - (m * v3.x);
			for (int i = 0; i < rise; i++) {
				startXs[i] = (int) (((float)((v3.y+i)/m) - (yIntercept/m))); 
			}
		} else {
			for (int i = 0; i < rise; i++) {
				startXs[i] = (int) v3.x;
			}
		}
		if(run2 != 0) {
			float m = (float)rise / (float)run2;
			float yIntercept = v3.y - (m * v3.x);
			for (int i = 0; i < rise; i++) {
				endXs[i] = (int) (((float)((v3.y+i)/m) - (yIntercept/m))); 
			}
		} else {
			for (int i = 0; i < rise; i++) {
				endXs[i] = (int) v3.x;
			}
		}
		System.out.println(startXs[0]);
		System.out.println(endXs[0]);
		int ctr = 0;
		for (int y = (int) v3.y; y < v2.y; y++) {
			for (int x = startXs[ctr]; x < endXs[ctr]; x++) {
				renderPixel(x, y, color);
			}
			ctr++;
		}
	}
	
	public void renderFlatBottomTriangle(Vector2 b1, Vector2 b2, Vector2 t, Vector4 color) {
		Vector2 v1 = b1;
		Vector2 v2 = b2;
		Vector2 v3 = t;
		int rise = (int) (v3.y - v1.y);
		int run1 = (int) (v3.x - v1.x);
		int run2 = (int) (v3.x - v2.x);
		int[] startXs = new int[rise];
		int[] endXs = new int[rise];
		float[] reds = new float[rise];
		float[] greens = new float[rise];
		float[] blues = new float[rise];
		float[] alphas = new float[rise];
		if(run1 != 0) {
			float m = (float)rise / (float)run1;
			float yIntercept = v3.y - (m * v3.x);
			for (int i = 0; i < rise; i++) {
				startXs[i] = (int) (((float)((v1.y+i)/m) - (yIntercept/m))); 
			}
		} else {
			for (int i = 0; i < rise; i++) {
				startXs[i] = (int) v3.x;
			}
		}
		if(run2 != 0) {
			float m = (float)rise / (float)run2;
			float yIntercept = v3.y - (m * v3.x);
			for (int i = 0; i < rise; i++) {
				endXs[i] = (int) (((float)((v1.y+i)/m) - (yIntercept/m))); 
			}
		} else {
			for (int i = 0; i < rise; i++) {
				endXs[i] = (int) v3.x;
			}
		}
		int ctr = 0;
		for (int y = (int) v2.y; y < (int) v3.y; y++) {
			for (int x = startXs[ctr]; x < endXs[ctr]; x++) {
				renderPixel(x, y, color);
			}
			ctr++;
		}
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
		y = display.getHeight() - y;
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
