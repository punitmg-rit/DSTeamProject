import edu.rit.util.Random;
import edu.rit.numeric.AggregateXYSeries;
import edu.rit.numeric.ListSeries;
import edu.rit.numeric.plot.Plot;

public class Simulate {
	private static Random prng;
	private static ListSeries noOfHops;
	private static ListSeries dimensions;

	public static void main(String args[]) {
		int dLower = 0;
		int dUpper = 0;
		int iterations = 0;
		int seed;
		int srcCyclic;
		int srcCubical;
		int destCyclic;
		int destCubical;
		int hops = 0;

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
		noOfHops = new ListSeries();
		dimensions = new ListSeries();
		for (int i = dLower; i <= dUpper; i++) {
			int denominator=0;
			for (int j = 0; j < iterations; j++) {

				srcCyclic = (prng.nextInt(seed) % i);
				destCyclic = (prng.nextInt(seed) % i);
				srcCubical = (int) (prng.nextInt(seed) % (Math.pow(2, i)));
				destCubical = (int) (prng.nextInt(seed) % (Math.pow(2, i)));
				// System.out.println("Dimension: "+i+". Source: ("+srcCyclic+","+srcCubical+"). Destination: ("+destCyclic+","+destCubical+").");
				Cycloid s = new Cycloid();
				int tempHops = s.noOfHops(i, srcCyclic, srcCubical, destCyclic,
						destCubical);
				// System.out.println("Dimension: "+i+". Source: ("+srcCyclic+","+srcCubical+"). Destination: ("+destCyclic+","+destCubical+"). Hops: "+tempHops);
				if (tempHops > 0){
					hops += tempHops;
					denominator++;
				}

			}
			hops = hops / denominator;
			noOfHops.add(hops);
			dimensions.add(i);
			System.out.println("Dimension: " + i + ". Avg hops: " + hops);
		}

		new Plot().xAxisTitle("Number of hops").yAxisTitle("Dimensions")
				.xySeries(new AggregateXYSeries(noOfHops, dimensions));
	}
}
