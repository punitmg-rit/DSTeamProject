
public class Cycloid {
	private Node sourceNode;
	private Node destinationNode;

	private static int hopCount;

	public int noOfHops(int dimension, int sourceCyclic, int sourceCubical, 
			int destCyclic, int destCubical){

		String bin = Integer.toBinaryString(sourceCubical);
		if(bin.length() < dimension){
			while(bin.length() < dimension){
				bin = "0"+bin;
			}
		}
		sourceNode = new Node(dimension,sourceCyclic, bin);

		bin = Integer.toBinaryString(destCubical);
		if(bin.length() < dimension){
			while(bin.length() < dimension){
				bin = "0"+bin;
			}
		}
		destinationNode = new Node(destCyclic, bin);

		hopCount = 0;

		while(!sourceNode.equals(destinationNode)){
			hopCount++;

			sourceNode = nextHop(sourceNode, destinationNode);
		}

		return hopCount;
	}

	public int getMsdb(Node source, Node dest){
		for(int i = 0 ; i< source.getDimension(); i++){
			if(source.getCubicalIndex().charAt(i) != dest.getCubicalIndex().charAt(i))
				return source.getDimension() - i -1;
		}
		return -1;
	}


	public Node nextHop(Node source, Node destination){
		Node nextNode = new Node();
		int msdb = 0;
		Boolean flag = false;
		String sourceCubical = source.getCubicalIndex();
		String destCubical = destination.getCubicalIndex();
		int k = source.getCyclicIndex();
		for(int i = 0 ; i< source.getDimension(); i++){
			if(sourceCubical.charAt(i) != destCubical.charAt(i)){
				msdb = source.getDimension() - i -1;
				break;
			}else if (i == source.getDimension()-1 ){
				flag = true;
			}
		}

		if(flag == true ){
			int src = source.getCyclicIndex();
			int dest = destination.getCyclicIndex();
			hopCount += traverse(src, dest, source.getDimension());
			return destination;

		}

		if (k < msdb){
			nextNode = ascending(k,msdb,source, destination );
		}
		if (k >= msdb){
			nextNode = descending(k,msdb,source, destination );
		}

		return nextNode;
	}

	public Node descending(int k, int msdb,Node src, Node dest){
		Node next = new Node();
		if (k == msdb){
			return new Node(src.getDimension(), src.getCubicalNeighbor().getCyclicIndex(),
					src.getCubicalNeighbor().getCubicalIndex());

		}
		else {

			int msdbCyclic1 = getMsdb(src.getCyclicNeighbor1(),dest);
			int msdbCyclic2 = getMsdb(src.getCyclicNeighbor2(), dest);
			int msdbInside1 = getMsdb(src.getInsideLeafSet1(), dest);
			int msdbInside2 = getMsdb(src.getOutsideLeafSet2(), dest);


			if (msdbCyclic1 <= msdbCyclic2 && msdbCyclic1 < msdbInside1 && msdbCyclic1 < msdbInside2){
				return new Node(src.getDimension(), src.getCyclicNeighbor1().getCyclicIndex(),
						src.getCyclicNeighbor1().getCubicalIndex());
			}

			else	if (msdbCyclic2 <= msdbCyclic1 && msdbCyclic2 < msdbInside1 && msdbCyclic2 < msdbInside2){
				return new Node(src.getDimension(), src.getCyclicNeighbor2().getCyclicIndex(),
						src.getCyclicNeighbor2().getCubicalIndex());
			}
			else if (msdbInside1 <= msdbCyclic1 && msdbInside1 <= msdbCyclic2 && msdbInside1 <= msdbInside2){
				return new Node(src.getDimension(), src.getInsideLeafSet1().getCyclicIndex(),
						src.getInsideLeafSet1().getCubicalIndex());
			}else{
				return new Node(src.getDimension(), src.getInsideLeafSet2().getCyclicIndex(),
						src.getInsideLeafSet2().getCubicalIndex());
			}
		}

	}


	public Node ascending(int k, int msdb,Node src, Node dest){
		Node next = new Node();
		while(k < msdb){
			int msdb1 = getMsdb(src.getOutsideLeafSet1(), dest);
			int msdb2 = getMsdb(src.getOutsideLeafSet2(), dest);

			if(msdb1 <= msdb2){
				next = new Node(src.getDimension(), src.getOutsideLeafSet1().getCyclicIndex(),
						src.getOutsideLeafSet1().getCubicalIndex());
				src = next;
				k = src.getOutsideLeafSet1().getCyclicIndex();

				msdb = msdb1;
			}else{
				next = new Node(src.getDimension(), src.getOutsideLeafSet2().getCyclicIndex(),
						src.getOutsideLeafSet2().getCubicalIndex());
				src = next;
				k = src.getOutsideLeafSet2().getCyclicIndex();
				msdb = msdb2;
			}
			hopCount++;
		}

		return next;
	}


	public int traverse(int src, int dest, int d){
		int fwdCounter = 0;
		int backCounter = 0;
		int srcRef = src;

		while(srcRef != dest){
			if(srcRef == (d-1)){
				srcRef = 0;
				fwdCounter++;
			}else{
				srcRef++;
				fwdCounter++;
			}
		}
		srcRef = src;
		while(srcRef != dest){
			if(srcRef == 0){
				srcRef = d-1;
				backCounter++;
			}else{
				srcRef--;
				backCounter++;
			}
		}
		//System.out.println("forward c "+ fwdCounter);
		//System.out.println("b c "+ backCounter);

		if(fwdCounter <= backCounter)
			return fwdCounter;
		else
			return backCounter;
	}

}
