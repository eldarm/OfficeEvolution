package OfficeEvolution;

import java.util.Random;

public class Generation {
	
	public final int SELECION_NONE = 0;
	public final int SELECTION_TARGET = 1;
	public final int SELECTION_MAJORITY = 2;
	
	RGB target = new RGB();
	RGB majorityOpinion = new RGB();
	
	public int lostThisGeneration = 0;
	public double deviationFromTarget = 0.0;
	public double deviationFromMajority = 0.0;
	
	public Generation(int width, RGB target) {
		this.target = target;
		drones = new RGB[width];
		for (int i = 0; i < drones.length; i++) {
			drones[i] = new RGB();
		}
	}

	/**
	 * Generate a new generation. Done in place, the same instance of Generation.
	 * 
	 * @param target      An ideal market required color.
	 * @param attrition   Percentage of unfit to fire.
	 * @param minDistance Min distance from the ideal to keep. If more, fire with
	 *                    attrition probability.
	 * @return The number of people replaced.
	 */
	public void nextGeneration(
			double attrition, 
			double minDistance, 
			double managementErrors) {
		// TODO: actual generation using parameters.
		Random rand = new Random();
		int rs = 0, gs = 0, bs = 0; // Sum of individual colors.
		int replaced = 0; // Number of people replaced.
		
		for (int i = 0; i < drones.length; i++) {
			if ((drones[i].Distance(target) > minDistance && rand.nextDouble() < attrition)
					|| rand.nextDouble() < managementErrors) {
				drones[i] = new RGB();
				replaced++;
			}
			rs += drones[i].R;
			gs += drones[i].G;
			bs += drones[i].B;
		}
		RGB average = new RGB(rs/drones.length, gs/drones.length, bs/drones.length);
		double devTarget = 0.0, devMajority = 0.0; 
		for (RGB p : drones) {
			devTarget += p.Distance(target);
			devMajority += p.Distance(average);
		}
		deviationFromTarget = devTarget / drones.length;
		deviationFromMajority = devMajority/drones.length;
		lostThisGeneration = replaced;
		//System.out.printf("Generated new layer: %d\n", replaced);		
	}

	public RGB[] getPoints() {
		return drones;
	}

	RGB[] drones;
}
