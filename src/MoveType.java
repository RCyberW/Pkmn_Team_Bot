
public enum MoveType {
	SPECIAL("Special"), PHYSICAL("Physical"), STATUS("Status");
	
	String type;
	MoveType(String name) {
		type = name;
	}
	
	public String toString() {
		return type;
	}
}
