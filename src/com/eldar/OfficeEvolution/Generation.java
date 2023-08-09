package OfficeEvolution;

import java.util.Arrays;
import java.util.Random;

public class Generation {

	public static class ExperimentParams {
		double attrition;
		double managementErrors;
		double mutationRate;
		int selectionMode;
		int mutationSource;
		int changeTargetEvery;
		double layoffVolume;
		double recoveryInc;
		double marketPressure;
	}

	private static class Distance implements Comparable<Distance> {
		int index;
		double distance;

		@Override
		public int compareTo(Distance o) {
			// From largest to smallest:
			var r = o.distance - this.distance;
			return r < 0 ? -1 : r > 0 ? 1 : 0;
		}
	}

	// Must be in the same order as enum value underneath.
	public static final String[] SELECTION_LABELS = { "None", "Target",
			"Majority" };
	public static final int SELECION_NONE = 0;
	public static final int SELECTION_TARGET = 1;
	public static final int SELECTION_MAJORITY = 2;

	// Must be in the same order as enum value underneath.
	public final static String[] COMPANY_LABELS = { "Heaven", "Hell",
			"Low Selective", "Too selective", "Balanced selection",
			"Poor managers", "Fire at random", "Changing world", "With layoffs" };
	public final static int COMPANY_HEAVEN = 0;
	public final static int COMPANY_HELL = 1;
	public final static int COMPANY_LOW_SELECTIVE = 2;
	public final static int COMPANY_TOO_SELECTIVE = 3;
	public final static int COMPANY_BALANCED_SELECTION = 4;
	public final static int COMPANY_BALANCED_BAD_MANAGEMENT = 5;
	public final static int COMPANY_RANDOM_FIRE = 6;
	public final static int COMPANY_CHANGING_WORLD = 7;
	public final static int COMPANY_LAYOFFFS = 8;

	public RGB target = new RGB();
	public RGB average = target; // new RGB();

	private RGB[] drones;
	private Distance[] distances;
	private ExperimentParams prm;
	private int generationCount = 0;
	private int size, maxSize; // , sizeInc = 0;

	public int lostThisGeneration = 0;
	public double deviationFromTarget = 0.0;
	public double deviationFromMajority = 0.0;

	private Random rand = new Random();

	public Generation(int width, ExperimentParams p) {
		size = width;
		maxSize = width;
		target = new RGB();
		prm = p;
		drones = new RGB[width];
		distances = new Distance[width];
		for (int i = 0; i < width; i++) {
			drones[i] = new RGB();
			distances[i] = new Distance();
		}
	}

	public static ExperimentParams getPresetCompany(int company) {
		var r = new ExperimentParams();
		switch (company) {
		case COMPANY_HEAVEN:
			r.attrition = 0.0;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			r.changeTargetEvery = 0;
			r.layoffVolume = 0.0;
			r.recoveryInc = 0.0;
			r.marketPressure = 0.0;
			break;
		case COMPANY_HELL:
			r.attrition = 1.0;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			r.changeTargetEvery = 0;
			r.layoffVolume = 0.0;
			r.recoveryInc = 0.0;
			r.marketPressure = 0.0;
			break;
		case COMPANY_LOW_SELECTIVE:
			r.attrition = 0.003;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			r.changeTargetEvery = 0;
			r.layoffVolume = 0.0;
			r.recoveryInc = 0.0;
			r.marketPressure = 0.0;
			break;
		case COMPANY_TOO_SELECTIVE:
			r.attrition = 0.5;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			r.changeTargetEvery = 0;
			r.layoffVolume = 0.0;
			r.recoveryInc = 0.0;
			r.marketPressure = 0.0;
			break;
		case COMPANY_BALANCED_SELECTION:
			r.attrition = 0.1;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			r.changeTargetEvery = 0;
			r.layoffVolume = 0.0;
			r.recoveryInc = 0.0;
			r.marketPressure = 0.0;
			break;
		case COMPANY_BALANCED_BAD_MANAGEMENT:
			r.attrition = 0.1;
			r.managementErrors = 0.1;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			r.changeTargetEvery = 0;
			r.layoffVolume = 0.0;
			r.recoveryInc = 0.0;
			r.marketPressure = 0.0;
			break;
		case COMPANY_RANDOM_FIRE:
			r.attrition = 0.1;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECION_NONE;
			r.mutationSource = SELECTION_TARGET;
			r.changeTargetEvery = 0;
			r.layoffVolume = 0.0;
			r.recoveryInc = 0.0;
			r.marketPressure = 0.0;
			break;
		case COMPANY_CHANGING_WORLD:
			r.attrition = 0.1;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECION_NONE;
			r.mutationSource = SELECTION_TARGET;
			r.changeTargetEvery = 100;
			r.layoffVolume = 0.0;
			r.recoveryInc = 0.0;
			r.marketPressure = 0.0;
			break;
		case COMPANY_LAYOFFFS:
			r.attrition = 0.1;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECION_NONE;
			r.mutationSource = SELECTION_TARGET;
			r.changeTargetEvery = 100;
			r.layoffVolume = 0.5;
			r.recoveryInc = 0.01;
			r.marketPressure = 0.0;
			break;

		default: // Same as for COMPANY_HEAVEN.
			r.attrition = 0.0;
			r.managementErrors = 0.0;
			r.mutationRate = 0.0;
			r.selectionMode = SELECTION_TARGET;
			r.mutationSource = SELECION_NONE;
			break;
		}
		return r;
	}

	private RGB generateNewHire() {
		return switch (prm.mutationSource) {
		case SELECION_NONE -> new RGB();
		case SELECTION_TARGET -> new RGB(target, prm.mutationRate);
		case SELECTION_MAJORITY -> new RGB(average, prm.mutationRate);
		default -> new RGB();
		};
	}

	/**
	 * Generate a new generation. Done in place, the same instance of
	 * Generation.
	 */
	public void nextGeneration() {
		generationCount++;
		if (prm.changeTargetEvery != 0
				&& generationCount % prm.changeTargetEvery == 0) {
			target = new RGB();
			if (prm.layoffVolume > 0) {
				if (prm.layoffVolume > 0.8) {
					// Don't let user overdo it.
					prm.layoffVolume = 0.8;
				}
				size = (int) (maxSize * (1 - prm.layoffVolume));
			}
		} else if (size < maxSize && prm.recoveryInc > 0) {
			int incr = (int) (size * prm.recoveryInc);
			if (incr + size > maxSize) {
				incr = maxSize - size;
			}
			int offset = incr / 2;
			incr = offset*2; // Guaranteed that size + incr < maxSize
			for (int i = size - 1; i >= 0; i--) {
				drones[i + offset] = drones[i];
			}
			for (int i = 0; i < offset; i++) {
				drones[i] = generateNewHire();
			}
			for (int i = size + offset; i < size + incr; i++) {
				drones[i] = generateNewHire();
			};
			size += incr;
		}

		int rs = 0, gs = 0, bs = 0; // Sum of individual colors.
		for (var t : drones) {
			rs += t.R;
			gs += t.G;
			bs += t.B;
		}

		average = new RGB(rs / drones.length, gs / drones.length,
				bs / drones.length);

		// Find those to replace.
		for (int i = 0; i < size; i++) {
			distances[i].index = i;
			// A little randomization to prevent in order removal.
			distances[i].distance = rand.nextDouble() / 1000.0
					+ switch (prm.selectionMode) {
					case SELECTION_TARGET -> drones[i].Distance(target);
					case SELECTION_MAJORITY -> drones[i].Distance(average);
					case SELECION_NONE -> rand.nextDouble();
					default -> throw new IllegalStateException(
							"Unexpected selection value: " + prm.selectionMode);
					};
		}
		Arrays.sort(distances);

		// Replace attrition share of the current number of people.
		lostThisGeneration = (int) (size * prm.attrition);
		for (int i = 0; i < lostThisGeneration; i++) {
			drones[distances[i].index] = generateNewHire();
		}

		// Computer average deviations.
		double devTarget = 0.0, devMajority = 0.0;
		for (int i = 0; i < size; i++) {
			devTarget += drones[i].Distance(target);
			devMajority += drones[i].Distance(average);
		}
		deviationFromTarget = devTarget / size;
		deviationFromMajority = devMajority / size;
	}

	public RGB[] getPoints() {
		return drones;
	}

	public int getSize() {
		return size;
	}

}
