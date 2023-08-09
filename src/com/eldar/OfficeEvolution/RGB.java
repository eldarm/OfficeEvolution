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
	Random r = new Random();

	/**
	 * Generate a random RGB color.
	 */
	public RGB() {
		R = r.nextInt(256);
		G = r.nextInt(256);
		B = r.nextInt(256);
	}

	private int mutateInt(int v, double mutationRate) {
		// System.out.printf("\n%d %f\n", v, mutationRate);
		v = mutationRate == 0.0 ? v
				: v + r.nextInt((int) (mutationRate * 256))
						- (int) (mutationRate * 128);
		return v < 0 ? 0 : v > 255 ? 255 : v;
	}

	/**
	 * Mutate the RGB color.
	 */
	public RGB(RGB c, double mutationRate) {
		R = mutateInt(c.R, mutationRate);
		G = mutateInt(c.G, mutationRate);
		B = mutateInt(c.B, mutationRate);
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

	private static int weight(int l, int r, double w) {
		int rs = (int) Math.sqrt(l * l * (1 - w) + r * r * w);
		return rs > 127 ? 127 : rs < 0 ? 0 : rs;
	}

	int R;
	int G;
	int B;
	private double maxDistance = Math.sqrt(255 * 255 * 3);

}
