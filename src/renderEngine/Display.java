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
	
	private Vector2[] getBarycentric(Vector2 v1, Vector2 v2, Vector3 v3) {
		Vector2[] vertices = new Vector2[6];
		Vector2 v4 = new Vector2((v1.x/2)+(v2.x/2),(v1.y/2)+(v2.y/2));
		Vector2 v5 = new Vector2((v2.x/2)+(v3.x/2),(v2.y/2)+(v3.y/2));
		Vector2 v6 = new Vector2((v3.x/2)+(v1.x/2),(v3.y/2)+(v1.y/2));
		Vector2 v7 = new Vector2((v4.x/2)+(v6.x/2),(v4.y/2)+(v6.y/2));
		Vector2 v8 = new Vector2((v4.x/2)+(v5.x/2),(v4.y/2)+(v5.y/2));
		Vector2 v9 = new Vector2((v5.x/2)+(v6.x/2),(v5.y/2)+(v6.y/2));
		vertices[0] = v4;
		vertices[1] = v5;
		vertices[2] = v6;
		vertices[3] = v7;
		vertices[4] = v8;
		vertices[5] = v9;
		return vertices;
	}
	
	//TODO:(Robby) The method needs to "like" any order of the given vertices. 
	
	public void renderTriangle(Vector2 v1, Vector2 v2, Vector2 v3, Vector4 color) {
		v1 = new Vector2((v1.x + 1f)*0.5f*display.getWidth(), (v1.y + 1f)*0.5f*display.getHeight());
		v2 = new Vector2((v2.x + 1f)*0.5f*display.getWidth(), (v2.y + 1f)*0.5f*display.getHeight());
		v3 = new Vector2((v3.x + 1f)*0.5f*display.getWidth(), (v3.y + 1f)*0.5f*display.getHeight());
		if(v1.y == v2.y && v3.y > v1.y && v3.y > v2.y) {
			if(v1.x < v2.x) {
				renderFlatBottomTriangle(v1, v2, v3, color);
			}else {
				renderFlatBottomTriangle(v2, v1, v3, color);
			}
		}else if(v1.y == v2.y && v3.y < v1.y && v3.y < v2.y){
			if(v1.x < v2.x) {
				renderFlatTopTriangle(v1, v2, v3, color);
			}else {
				renderFlatTopTriangle(v2, v1, v3, color);
			}
		}else if(v1.y == v3.y && v2.y > v1.y && v2.y > v3.y){
			if(v1.x < v3.x) {
				renderFlatBottomTriangle(v1, v3, v2, color);
			}else {
				renderFlatBottomTriangle(v3, v1, v2, color);
			}
		}else if(v1.y == v3.y && v2.y < v1.y && v2.y < v3.y){
			if(v1.x < v3.x) {
				renderFlatTopTriangle(v1, v3, v2, color);
			}else {
				renderFlatTopTriangle(v3, v1, v2, color);
			}
		}else if(v2.y == v3.y && v1.y > v2.y && v1.y > v3.y){
			if(v2.y < v3.y) {
				renderFlatBottomTriangle(v2, v3, v1, color);
			}else {
				renderFlatBottomTriangle(v3, v2, v1, color);
			}
		}else if(v2.y == v3.y && v1.y < v2.y && v1.y < v3.y){
			if(v2.y < v3.y) {
				renderFlatTopTriangle(v2, v3, v1, color);
			}else {
				renderFlatTopTriangle(v3, v2, v1, color);
				System.out.println("ejiwrw");
			}
		}else {
			Vector2 topVertex = null;
			Vector2 middleVertex = null;
			Vector2 bottomVertex = null;
			Vector2 fourthVertex = null;
			System.out.println(topVertex);
			System.out.println(middleVertex);
			System.out.println(bottomVertex);
			System.out.println(fourthVertex);
			if(v3.y > v1.y && v3.y > v2.y) {
				topVertex = v3;
				if(v1.y > v2.y) {
					middleVertex = v1;
					bottomVertex = v2;
				}else {
					middleVertex = v2;
					bottomVertex = v1;
				}
			}else if(v1.y > v3.y && v1.y > v2.y) {
				topVertex = v1;
				if(v2.y > v3.y) {
					middleVertex = v2;
					bottomVertex = v3;
				}else {
					middleVertex = v3;
					bottomVertex = v2;
				}
			}else{
				topVertex = v2;
				if(v1.y > v3.y) {
					middleVertex = v1;
					bottomVertex = v3;
				}else {
					middleVertex = v3;
					bottomVertex = v1;
				}
			}
			int rise = (int) (topVertex.y-bottomVertex.y);
			int run = (int) (topVertex.x-bottomVertex.x);
			if(run != 0) {
			float m = (float)rise / (float)run;
			float b = topVertex.y-(m*topVertex.x);
			int x = (int) ((middleVertex.y/m) - (b/m));
			fourthVertex = new Vector2(x,middleVertex.y);
			}else {
			fourthVertex = new Vector2(topVertex.x, middleVertex.y);
			}
			if(fourthVertex.x < middleVertex.x) {
				renderFlatBottomTriangle(fourthVertex, middleVertex, topVertex, color);
				renderFlatTopTriangle(fourthVertex, middleVertex, bottomVertex, color);				
			}else {
				renderFlatBottomTriangle(middleVertex, fourthVertex, topVertex, color);
				renderFlatTopTriangle(middleVertex, fourthVertex, bottomVertex, color);		
			}
		}
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
			if(ctr<startXs.length || ctr<endXs.length) {
				for (int x = startXs[ctr]; x < endXs[ctr]; x++) {
					renderPixel(x, y, color);
				}
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
			if(ctr<startXs.length || ctr<endXs.length) {
				for (int x = startXs[ctr]; x < endXs[ctr]; x++) {
					renderPixel(x, y, color);
				}
			}
			ctr++;
		}
	}
	
	public void renderLine(Vector2 start, Vector2 end, Vector4 color1) {
		int startX = (int) (((start.x+1.0f)*0.5f) * frame.getWidth());
		int startY = (int) (((start.y+1.0f)*0.5f) * frame.getHeight());
		int endX = (int) (((end.x+1.0f)*0.5f) * frame.getWidth());
		int endY = (int) (((end.y+1.0f)*0.5f) * frame.getHeight());
		
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
	
	private Vector4 calculateColor(Vector2 v1, Vector2 p, Vector2 v3, Vector4 color1, Vector4 color2) {
		float lineLength = Vector2.lengthOfTwoVectors(v1, v3);
		float pixelLength = Vector2.lengthOfTwoVectors(v1, p);
		if(lineLength != 0) {
			float ratio1 = 1.0f - (pixelLength / lineLength);
			float ratio2 = 1.0f - ratio1;
			return new Vector4((color1.x * ratio1) + (color2.x * ratio2), 
					(color1.y * ratio1) + (color2.y * ratio2),
					(color1.z * ratio1) + (color2.z * ratio2),
					(color1.w * ratio1) + (color2.w * ratio2));
		}else {
			return new Vector4((color1.x + color2.x)/2,
					(color1.y + color2.y)/2,
					(color1.z + color2.z)/2,
					(color1.w + color2.w)/2);
		}
	}
	
	public void renderLine(Vector2 start, Vector2 end, Vector4 color1, Vector4 color2) {
		int startX = (int) (((start.x+1.0f)*0.5f) * frame.getWidth());
		int startY = (int) (((start.y+1.0f)*0.5f) * frame.getHeight());
		int endX = (int) (((end.x+1.0f)*0.5f) * frame.getWidth());
		int endY = (int) (((end.y+1.0f)*0.5f) * frame.getHeight());
				
		int rise = endY - startY;
		int run = endX - startX;
	
		if(run!=0) {
			float m = (float)rise / (float)run;
			float b = (float) (startY - (m * (float)startX));
			int ars = Math.abs(rise);
			int arn = Math.abs(run);
			if(ars > arn) {
				int addAmount = (rise > 0 ? 1 : -1);
				for (int i = 0; i < ars; i++) {
					int y = startY + (i*addAmount);
					int x = (int) (((float) y / m) - (b / m));
					renderPixel(x, y, calculateColor(new Vector2(startX,startY), new Vector2(x,y), new Vector2(endX,endY), color1, color2));
				}
			}else {
				int addAmount = (rise > 0 ? 1 : -1);
				for (int i = 0; i < arn; i++) {
					int x = startX + (i*addAmount);
					int y = (int) (((float) x / m) - (b / m));
					renderPixel(x, y, calculateColor(new Vector2(startX,startY), new Vector2(x,y), new Vector2(endX,endY), color1, color2));
				}
			}
		}else {
			int ars = Math.abs(rise);
			int addAmount = (rise > 0 ? 1 : -1);
			for (int i = 0; i < ars; i++) {
				int y = startY + (i*addAmount);
				int x = startX;
				renderPixel(x, y, calculateColor(new Vector2(startX,startY), new Vector2(x,y), new Vector2(endX,endY), color1, color2));
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
