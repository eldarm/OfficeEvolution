package OfficeEvolution;

import java.util.Random;

public class Generation {

	public static class ExperimentParams {
		double attrition;
		double minDistance;
		double managementErrors;
		double mutationRate;
		int selectionMode;
		int mutationSource;
		int changeTargetEvery;
	}

	public static final String[] SELECTION_LABELS = { "None", "Target",
			"Majority" };
	public static final int SELECION_NONE = 0;
	public static final int SELECTION_TARGET = 1;
	public static final int SELECTION_MAJORITY = 2;

	public final static String[] COMPANY_LABELS = { "Heaven", "Hell",
			"Low Selective", "Too selective", "Balanced selection",
			"Poor managers" };
	public final static int COMPANY_HEAVEN = 0;
	public final static int COMPANY_HELL = 1;
	public final static int COMPANY_LOW_SELECTIVE = 2;
	public final static int COMPANY_TOO_SELECTIVE = 3;
	public final static int COMPANY_BALANCED_SELECTION = 4;
	public final static int COMPANY_BALANCED_BAD_MANAGEMENT = 5;

	RGB target = new RGB();
	//RGB majorityOpinion = new RGB();
	ExperimentParams prm;
	int generationCount = 0;

	public int lostThisGeneration = 0;
	public double deviationFromTarget = 0.0;
	public double deviationFromMajority = 0.0;

	public Generation(int width, ExperimentParams p) {
		target = new RGB();
		prm = p;
		drones = new RGB[width];
		for (int i = 0; i < drones.length; i++) {
			drones[i] = new RGB();
		}
	}

	public static ExperimentParams getPresetCompany(int company) {
		var r = new ExperimentParams();
		switch (company) {
		case COMPANY_HEAVEN:
			r.attrition = 0.0;
			r.minDistance = 1.0;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			break;
		case COMPANY_HELL:
			r.attrition = 1.0;
			r.minDistance = 0.0;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			break;
		case COMPANY_LOW_SELECTIVE:
			r.attrition = 0.1;
			r.minDistance = 0.5;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			break;
		case COMPANY_TOO_SELECTIVE:
			r.attrition = 0.1;
			r.minDistance = 0.1;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			break;
		case COMPANY_BALANCED_SELECTION:
			r.attrition = 0.1;
			r.minDistance = 0.3;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			break;
		case COMPANY_BALANCED_BAD_MANAGEMENT:
			r.attrition = 0.1;
			r.minDistance = 0.3;
			r.managementErrors = 0.1;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			break;

		default: // Same as for COMPANY_HEAVEN.
			r.attrition = 0.0;
			r.minDistance = 1.0;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			break;
		}
		return r;
	}

	/**
	 * Generate a new generation. Done in place, the same instance of
	 * Generation.
	 * 
	 * @param target      An ideal market required color.
	 * @param attrition   Percentage of unfit to fire.
	 * @param minDistance Min distance from the ideal to keep. If more, fire
	 *                    with attrition probability.
	 * @return The number of people replaced.
	 */
	public void nextGeneration() {
		generationCount++;
		if (prm.changeTargetEvery != 0 && generationCount % prm.changeTargetEvery == 0) {
			target = new RGB();
		}
		
		Random rand = new Random();
		int rs = 0, gs = 0, bs = 0; // Sum of individual colors.
		for (var t : drones) {
			rs += t.R;
			gs += t.G;
			bs += t.B;
		}

		RGB average = new RGB(rs / drones.length, gs / drones.length,
				bs / drones.length);

		int replaced = 0; // Number of people replaced.
		for (int i = 0; i < drones.length; i++) {
			var selectionCriteria = rand.nextDouble() < prm.attrition
					&& (prm.selectionMode == SELECTION_TARGET
							? drones[i].Distance(target) > prm.minDistance
							: prm.selectionMode == SELECTION_MAJORITY
									? drones[i]
											.Distance(average) > prm.minDistance
									: true);
			if (rand.nextDouble() < prm.managementErrors || selectionCriteria) {
				drones[i] = prm.mutationSource == SELECION_NONE ? new RGB()
						: prm.mutationSource == SELECTION_TARGET
								? new RGB(target, prm.mutationRate)
								: new RGB(average, prm.mutationRate); // SELECTION_MAJORITY
				replaced++;
			}
		}
		lostThisGeneration = replaced;

		double devTarget = 0.0, devMajority = 0.0;
		for (var t : drones) {
			devTarget += t.Distance(target);
			devMajority += t.Distance(average);
		}
		deviationFromTarget = devTarget / drones.length;
		deviationFromMajority = devMajority / drones.length;
		// System.out.printf("Generated new layer: %d\n", replaced);
	}

	public RGB[] getPoints() {
		return drones;
	}

	RGB[] drones;
}
