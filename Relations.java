
public enum Relations {
	ASSOCIATION("->"),
	AGGREGATION_1("<>-1>"),
	AGGREGATION("+->"),
	COMPOSITION("++-1>"),
	INHERIANCE("^-"),
	INTERFACE_INHERIANCE("^-.-"),
	DEPENDENCIES("uses-.->"),
	CARDINALITY("1-0..*"),
	SIMPLEASSOCIATION("-");
	
	private final String label;
	
	Relations(String label) {
		this.label = label;
	}
	
	public String toString() {
		return this.label;
	}
}
