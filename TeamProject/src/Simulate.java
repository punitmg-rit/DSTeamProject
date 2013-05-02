import edu.rit.numeric.AggregateXYSeries;
import edu.rit.numeric.ListSeries;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.ListXYZSeries;
import edu.rit.numeric.Series;
import edu.rit.numeric.XYZSeries;
import edu.rit.numeric.plot.Plot;
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
		ListXYSeries HDseries = new ListXYSeries();
		ListXYZSeries xyzSeries = new ListXYZSeries();
		for (int i = dLower; i <= dUpper; i++) {
			System.out.printf("%d", i);
			ListSeries HMeanSeries = new ListSeries();
			for (int j = 0; j < iterations; j++) {

				ListSeries Hseries = new ListSeries();
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
					Hseries.add(tempHops);
				}
				Series.Stats stats = Hseries.stats();
				double H = stats.mean;
				// HDseries.add(i, H);
				System.out.printf("\t%.2f", H);
				HMeanSeries.add(H);
				

			}

			System.out.println();
			Series.Stats stats = HMeanSeries.stats();
			double HMean = stats.mean;
			double HStddev = stats.stddev;
			System.out.printf("\tmean = %.2f, stddev = %.2f%n", HMean, HStddev);
			HDseries.add(i, HMean);
			xyzSeries.add(i, stats.mean, stats.stddev);
			System.out.println();
		}

		XYZSeries.Regression regr = xyzSeries.linearRegression();
		System.out.printf("H = a + b*D\n");
		System.out.printf("a = %.2f%n", regr.a);
		System.out.printf("b = %.2f%n", regr.b);
		System.out.printf("stddev(a) = %.2f%n", Math.sqrt(regr.var_a));
		System.out.printf("stddev(b) = %.2f%n", Math.sqrt(regr.var_b));

		System.out.printf("chi^2 = %.6f%n", regr.chi2);
		System.out.printf("p-value = %.6f%n", regr.significance);

		new Plot().yAxisTitle("Average Number of hops").xAxisTitle("Dimension")
				.xySeries(HDseries).seriesStroke(null).getFrame()
				.setVisible(true);
	}
}
