public class Node {

	private int dimension;
	private int cyclicIndex;
	private String cubicalIndex;
	private Node cubicalNeighbor;
	private Node cyclicNeighbor1;
	private Node cyclicNeighbor2;
	private Node insideLeafSet1;
	private Node insideLeafSet2;
	private Node outsideLeafSet1;
	private Node outsideLeafSet2;

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public int getCyclicIndex() {
		return cyclicIndex;
	}

	public void setCyclicIndex(int cyclicIndex) {
		this.cyclicIndex = cyclicIndex;
	}

	public String getCubicalIndex() {
		return cubicalIndex;
	}

	public void setCubicalIndex(String cubicalIndex) {
		this.cubicalIndex = cubicalIndex;
	}

	public Node getCubicalNeighbor() {
		return cubicalNeighbor;
	}

	public void setCubicalNeighbor(Node cubicalNeighbor) {
		this.cubicalNeighbor = cubicalNeighbor;
	}

	public Node getCyclicNeighbor1() {
		return cyclicNeighbor1;
	}

	public void setCyclicNeighbor1(Node cyclicNeighbor1) {
		this.cyclicNeighbor1 = cyclicNeighbor1;
	}

	public Node getCyclicNeighbor2() {
		return cyclicNeighbor2;
	}

	public void setCyclicNeighbor2(Node cyclicNeighbor2) {
		this.cyclicNeighbor2 = cyclicNeighbor2;
	}

	public Node getInsideLeafSet1() {
		return insideLeafSet1;
	}

	public void setInsideLeafSet1(Node insideLeafSet1) {
		this.insideLeafSet1 = insideLeafSet1;
	}

	public Node getInsideLeafSet2() {
		return insideLeafSet2;
	}

	public void setInsideLeafSet2(Node insideLeafSet2) {
		this.insideLeafSet2 = insideLeafSet2;
	}

	public Node getOutsideLeafSet1() {
		return outsideLeafSet1;
	}

	public void setOutsideLeafSet1(Node outsideLeafSet1) {
		this.outsideLeafSet1 = outsideLeafSet1;
	}

	public Node getOutsideLeafSet2() {
		return outsideLeafSet2;
	}

	public void setOutsideLeafSet2(Node outsideLeafSet2) {
		this.outsideLeafSet2 = outsideLeafSet2;
	}

	public Node(int dimension, int cycIndex, String cubIndex) {
		this.cyclicIndex = cycIndex;
		this.cubicalIndex = cubIndex;
		this.dimension = dimension;
		//System.out.println("Dimension: " + dimension + ". Cyclic Index: "
			//	+ cycIndex + ". Cubical Index: " + cubIndex);
		generateNeighbors();
	}

	public Node(int cycIndex, String cubIndex) {
		this.cyclicIndex = cycIndex;
		this.cubicalIndex = cubIndex;
	}

	public Node() {
	}

	private void generateNeighbors() {

		// CUBICAL NEIGHBOR - START
		// a node (k, ad-1 ad-2 ... a0) (k != 0, d)
		// has a cubical neighbor (k - 1, ad-1 ad-2 ... ak' x ... x)
		// where x denotes an arbitrary bit value
		// ASSUMPTION: x will denote the highest value possible

		int tempCyclicIndex = 0;
		if (cyclicIndex == 0) {
			tempCyclicIndex = dimension - 1;
		} else {
			tempCyclicIndex = cyclicIndex - 1;
		}

		String tempCubicalIndex = "";
		for (int i = 0; i < cubicalIndex.length(); i++) {

			if (i == (cubicalIndex.length() - 1) - cyclicIndex) {
				if (cubicalIndex.charAt(i) == '0') {
					tempCubicalIndex = tempCubicalIndex + "1";
				} else {
					tempCubicalIndex = tempCubicalIndex + "0";
				}
			} else if (i > (cubicalIndex.length() - 1) - cyclicIndex) {
				tempCubicalIndex = tempCubicalIndex + "1";

			} else {
				tempCubicalIndex = tempCubicalIndex + cubicalIndex.charAt(i);
			}
		}

		cubicalNeighbor = new Node(tempCyclicIndex, tempCubicalIndex);
		//System.out.println("Cubical Neighbor " + cubicalNeighbor);

		// CUBICAL NEIGHBOR - END
		// CYCLIC NEIGHBORS - START

		tempCubicalIndex = "";
		String temp = "";
		for (int i = 0; i < cubicalIndex.length(); i++) {

			if (i >= (cubicalIndex.length() - 1) - (cyclicIndex - 1)) {
				temp = temp + cubicalIndex.charAt(i);

			} else {
				tempCubicalIndex = tempCubicalIndex + cubicalIndex.charAt(i);
			}
		}
		int x;
		if (cyclicIndex == 0)
			temp = cubicalIndex;
		//System.out.println("temp " + temp);
		x = Integer.valueOf(temp, 2);

		String temp1 = Integer.toBinaryString(x + 1);
		String temp2 = Integer.toBinaryString(x - 1);
		//System.out.println("temp1 " + temp1);
		//System.out.println("temp2 " + temp2);

		while (temp2.length() < temp.length()) {
			//System.out.println("loop check1. ("+cyclicIndex+","+cubicalIndex+")");
			temp2 = "0" + temp2;
		}

		if (temp2.length() > temp.length()) {
			temp2 = temp2.substring(0, temp.length());
		}

		if (temp1.length() > temp.length()) {
			temp1 = temp.substring(0, temp.length() - 1) + "0";
		}

		while (temp1.length() < temp.length()) {
			//System.out.println("loop check2. ("+cyclicIndex+","+cubicalIndex+")");
			temp1 = temp1 + "1";
		}

		if (cyclicIndex == 0) {
			cyclicNeighbor1 = new Node(tempCyclicIndex, temp1);
			cyclicNeighbor2 = new Node(tempCyclicIndex, temp2);
		} else {
			cyclicNeighbor1 = new Node(tempCyclicIndex, tempCubicalIndex
					+ temp1);
			cyclicNeighbor2 = new Node(tempCyclicIndex, tempCubicalIndex
					+ temp2);
		}

		//System.out.println("Cyclic Neighbor " + cyclicNeighbor1);
		//System.out.println("Cyclic Neighbor " + cyclicNeighbor2);
		// CYCLIC NEIGHBORS - END
		// INSIDE LEAF SET - START

		insideLeafSet1 = new Node((cyclicIndex + 1) % dimension, cubicalIndex);
		insideLeafSet2 = new Node(tempCyclicIndex, cubicalIndex);

		//System.out.println("Inside leaf set 1: " + insideLeafSet1);
		//System.out.println("Inside leaf set 2: " + insideLeafSet2);
		// INSIDE LEAF SET - END
		// OUTSIDE LEAF SET - START

		int cubicalIndexValue = Integer.valueOf(cubicalIndex, 2);
		temp1 = Integer.toBinaryString((int) ((cubicalIndexValue + 1) % Math
				.pow(2, dimension)));
		if (cubicalIndexValue == 0) {
			temp2 = Integer.toBinaryString(dimension - 1);
		} else {
			temp2 = Integer.toBinaryString(cubicalIndexValue - 1);
		}

		while (temp1.length() < dimension) {
			//System.out.println("loop check3. ("+cyclicIndex+","+cubicalIndex+")");
			temp1 = "0" + temp1;
		}
		while (temp2.length() < dimension) {
			//System.out.println("loop check4. ("+cyclicIndex+","+cubicalIndex+")");
			temp2 = "0" + temp2;
		}

		outsideLeafSet1 = new Node(dimension - 1, temp1);
		outsideLeafSet2 = new Node(dimension - 1, temp2);
		//System.out.println("Outside leaf set 1: " + outsideLeafSet1);
		//System.out.println("Outside leaf set 2: " + outsideLeafSet2);
		// OUTSIDE LEAF SET - END

	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Node) {

			Node n = (Node) obj;
			if (n.getCubicalIndex().equals(this.cubicalIndex)
					&& n.getCyclicIndex() == this.cyclicIndex) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

//	@Override
//	public String toString() {
//		
//		return "(" + cyclicIndex + ", " + cubicalIndex + ")";
//	}
	
	@Override
	public String toString() {
		return "Node [dimension=" + dimension + ", (" + cyclicIndex+ "," + cubicalIndex + " ) \n"+
				", cubicalNeighbor= ("+ cubicalNeighbor.getCyclicIndex() +","+cubicalNeighbor.getCubicalIndex()+" ) \n"  
				+ ", cyclicNeighbor1= (" + cyclicNeighbor1.getCyclicIndex()+","+cyclicNeighbor1.getCubicalIndex()+" )\n"
				+ ", cyclicNeighbor2= (" + cyclicNeighbor2.getCyclicIndex()+","+cyclicNeighbor2.getCubicalIndex()+" )\n" 
				+ ", insideLeafSet1= ("+ insideLeafSet1.getCyclicIndex() + ","+ insideLeafSet1.getCubicalIndex()+" )\n"
				+ ", insideLeafSet2= (" + insideLeafSet2.getCyclicIndex()+","+insideLeafSet2.getCubicalIndex()+" )\n"
				+ ", outsideLeafSet1= (" + outsideLeafSet1.getCyclicIndex()+","+outsideLeafSet1.getCubicalIndex()+" )\n" 
				+ ", outsideLeafSet2= (" + outsideLeafSet2.getCyclicIndex()+","+outsideLeafSet2.getCubicalIndex()+" )\n" 
				+ "]";
	}
	

	public static void main(String[] args) {
		Node n = new Node(4, 0, "1111");
		System.out.println("this " + n);
	}

}
