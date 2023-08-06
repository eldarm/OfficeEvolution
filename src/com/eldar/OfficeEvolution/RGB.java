/**
 * RGB class represents a collor in RGB scheme.
 */
package OfficeEvolution;

import java.awt.Color;
import java.lang.Math;
import java.util.Random;

/**
 * RGB color.
 */
public class RGB {
	/**
	 * Generate a random RGB color.
	 */
	public RGB() {
		Random r = new Random();
		R = r.nextInt(256);
		G = r.nextInt(256);
		B = r.nextInt(256);
	}

	/**
	 * Generate a specific RGB color.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 */
	public RGB(int red, int green, int blue) {
		R = red;
		G = green;
		B = blue;
	}

	public double Distance(RGB other) {
		// Should be between 0.0 and 1.0.
		int rd = R - other.R;
		int gd = G - other.G;
		int bd = B - other.B;
		return Math.sqrt(rd * rd + gd * gd + bd * bd) / maxDistance;
	}

	public Color getColor() {
		// System.out.printf("%d %d %d", R, G, B);
		return new Color(R, G, B);
	}

	int R;
	int G;
	int B;
	private double maxDistance = Math.sqrt(255 * 255 * 3);

}
