import edu.rit.numeric.ListSeries;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.ListXYZSeries;
import edu.rit.numeric.Series;
import edu.rit.numeric.XYZSeries;
import edu.rit.numeric.plot.Plot;
import edu.rit.numeric.plot.Strokes;
import edu.rit.util.Random;

public class Simulate02 {
	private static Random prng;

	public static void main(String args[]) {
		int dLower = 0;
		int dUpper = 0;
		int iterations = 0;
		int seed;
		int srcCyclic;
		int srcCubical;
		int destCyclic;
		int destCubical;

		if (args.length != 4) {
			throw new IllegalArgumentException(
					"Usage: java Simulate <dLower> <dUpper> <iterations> <seed>");
		} else {
			dLower = Integer.parseInt(args[0]);
			dUpper = Integer.parseInt(args[1]);
			iterations = Integer.parseInt(args[2]);
			seed = Integer.parseInt(args[3]);
		}

		prng = Random.getInstance(seed);
		ListXYSeries hopsDimensionSeries = new ListXYSeries();
		ListXYZSeries xyzSeries = new ListXYZSeries();

		System.out.printf("H = a + b*D\n");
		System.out
				.println("Range\t\tAverage Hops\t a\t b\tstddev(a)\tstddev(b)\tchi^2\t\tp-value");
		System.out.println("\t\tMean\tStddev");
		for (int i = dLower; i <= dUpper; i++) {

			ListSeries hopsMeanSeries = new ListSeries();
			for (int j = 0; j < iterations; j++) {

				ListSeries hopsSeries = new ListSeries();
				for (int k = 0; k < iterations; k++) {

					srcCyclic = (prng.nextInt(seed) % i);
					destCyclic = (prng.nextInt(seed) % i);
					srcCubical = (int) (prng.nextInt(seed) % (Math.pow(2, i)));
					destCubical = (int) (prng.nextInt(seed) % (Math.pow(2, i)));
					// System.out.println("Dimension: "+i+". Source: ("+srcCyclic+","+srcCubical+"). Destination: ("+destCyclic+","+destCubical+").");
					Cycloid s = new Cycloid();
					int tempHops = s.noOfHops(i, srcCyclic, srcCubical,
							destCyclic, destCubical);
					// System.out.println("Dimension: "+i+". Source: ("+srcCyclic+","+srcCubical+"). Destination: ("+destCyclic+","+destCubical+"). Hops: "+tempHops);
					hopsSeries.add(tempHops);
				}
				Series.Stats stats = hopsSeries.stats();
				double hopsMean = stats.mean;
				// HDseries.add(i, H);
				hopsMeanSeries.add(hopsMean);

			}

			System.out.printf("%d", i-dLower);

			Series.Stats stats = hopsMeanSeries.stats();
			double hopsMeanOfMeans = stats.mean;
			double hopsStddev = stats.stddev;
			hopsDimensionSeries.add(i, hopsMeanOfMeans);

			System.out.printf("\t\t%.2f\t%.2f", hopsMeanOfMeans, hopsStddev);
			xyzSeries.add(i, stats.mean, stats.stddev);
			if (i - dLower > 1) {

				XYZSeries.Regression regression = xyzSeries.linearRegression();
				System.out.printf("\t%.2f\t%.2f\t%.2f\t\t%.2f\t\t%.6f\t%.6f",
						regression.a, regression.b,
						Math.sqrt(regression.var_a),
						Math.sqrt(regression.var_b), regression.chi2,
						regression.significance);
			}
			System.out.println();
		}

	}
}
