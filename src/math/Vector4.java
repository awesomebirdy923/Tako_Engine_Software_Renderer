package math;

public class Vector4 {
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vector4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public static Vector4 add(Vector4 v1, Vector4 v2) {
		return new Vector4(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z, v1.w + v2.w);
	}
	
	public static Vector4 sub(Vector4 v1, Vector4 v2) {
		return new Vector4(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z, v1.w - v2.w);
	}
	
	public static Vector4 mult(Vector4 v1, Vector4 v2) {
		return new Vector4(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z, v1.w * v2.w);
	}
	
	public static Vector4 div(Vector4 v1, Vector4 v2) {
		return new Vector4(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z, v1.w / v2.w);
	}
	
	public void add(Vector4 v) {
		x+=v.x;
		y+=v.y;
		z+=v.z;
		w+=v.w;
	}
	public void sub(Vector4 v) {
		x-=v.x;
		y-=v.y;
		z-=v.z;
		w-=v.w;
	}
	public void mult(Vector4 v) {
		x*=v.x;
		y*=v.y;
		z*=v.z;
		w*=v.w;
	}
	public void div(Vector4 v) {
		x/=v.x;
		y/=v.y;
		z/=v.z;
		w/=v.w;
	}
}
