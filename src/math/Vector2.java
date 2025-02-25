package math;

public class Vector2 {
	
	public float x;
	public float y;

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public static float lengthOfTwoVectors(Vector2 v1, Vector2 v2) {
		return (float)(Math.sqrt(Math.pow(v1.x - v2.x, 2))+Math.sqrt(Math.pow(v1.y - v2.y, 2)));
	}
	
	public static Vector2 add(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2 sub(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}

	public static Vector2 mult(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x * v2.x, v1.y * v2.y);
	}

	public static Vector2 div(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x / v2.x, v1.y / v2.y);
	}

	public void add(Vector2 v) {
		x += v.x;
		y += v.y;
	}

	public void sub(Vector2 v) {
		x -= v.x;
		y -= v.y;
	}

	public void mult(Vector2 v) {
		x *= v.x;
		y *= v.y;
	}

	public void div(Vector2 v) {
		x /= v.x;
		y /= v.y;
	}
}
