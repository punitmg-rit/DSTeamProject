import edu.rit.numeric.ListSeries;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.ListXYZSeries;
import edu.rit.numeric.Series;
import edu.rit.numeric.XYZSeries;
import edu.rit.numeric.plot.Plot;
import edu.rit.numeric.plot.Strokes;
import edu.rit.util.Random;

public class Simulate {
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
		for (int i = dLower; i <= dUpper; i++) {
			System.out.printf("Dimension: %d", i);
			System.out.printf("\t Average Hops: ");
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
				System.out.printf("\t%.2f", hopsMean);
				hopsMeanSeries.add(hopsMean);

			}

			System.out.println();
			Series.Stats stats = hopsMeanSeries.stats();
			double hopsMeanOfMeans = stats.mean;
			double hopsStddev = stats.stddev;
			System.out.printf("\tmean = %.2f, stddev = %.2f%n",
					hopsMeanOfMeans, hopsStddev);
			hopsDimensionSeries.add(i, hopsMeanOfMeans);
			xyzSeries.add(i, stats.mean, stats.stddev);
			System.out.println();
		}

		XYZSeries.Regression regression = xyzSeries.linearRegression();
		System.out.printf("H = a + b*D\n");
		System.out.printf("a = %.2f%n", regression.a);
		System.out.printf("b = %.2f%n", regression.b);
		System.out.printf("stddev(a) = %.2f%n", Math.sqrt(regression.var_a));
		System.out.printf("stddev(b) = %.2f%n", Math.sqrt(regression.var_b));

		System.out.printf("chi^2 = %.6f%n", regression.chi2);
		System.out.printf("p-value = %.6f%n", regression.significance);

		new Plot()
				.yAxisTitle("Average Number of hops")
				.xAxisTitle("Dimension")
				.seriesStroke(null)
				.xySeries(hopsDimensionSeries)
				.seriesDots(null)
				.seriesStroke(Strokes.solid(1))
				.xySeries(dLower, regression.a + regression.b * dLower, dUpper,
						regression.a + regression.b * dUpper).getFrame()
				.setVisible(true);
	}
}
