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
		generateNeighbors();
	}

	public Node(int cycIndex, String cubIndex) {
		this.cyclicIndex = cycIndex;
		this.cubicalIndex = cubIndex;
	}

	private void generateNeighbors() {

		
		// CUBICAL NEIGHBOR
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
		
		String tempCubicalIndex="";
		for(int i=0;i<cubicalIndex.length();i++){
			
			if(i==(cubicalIndex.length()-1)-cyclicIndex){
				if(cubicalIndex.charAt(i) == '0'){
					tempCubicalIndex = tempCubicalIndex+"1";
				}else{
					tempCubicalIndex = tempCubicalIndex+"0";
				}
			}else{
				tempCubicalIndex = tempCubicalIndex+cubicalIndex.charAt(i);
			}
		}
		
		cubicalNeighbor = new Node(tempCyclicIndex, tempCubicalIndex);
		
		
		
		
		

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

}
