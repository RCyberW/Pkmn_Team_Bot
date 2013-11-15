import java.util.ArrayList;

public class Type {
	private String name;
	private ArrayList<Type> doubleDamageAgainst = new ArrayList<Type>();
	private ArrayList<Type> halfDamageAgainst = new ArrayList<Type>();
	private ArrayList<Type> noDamageAgainst = new ArrayList<Type>();

	private ArrayList<Type> halfDamageFrom = new ArrayList<Type>();
	private ArrayList<Type> doubleDamageFrom = new ArrayList<Type>();
	private ArrayList<Type> noDamageFrom = new ArrayList<Type>();

	private ArrayList<Move> strongMoves = new ArrayList<Move>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Type> getDoubleDamageAgainst() {
		return doubleDamageAgainst;
	}

	public void addDoubleDamageAgainst(Type type) {
		if (!doubleDamageAgainst.contains(type))
			this.doubleDamageAgainst.add(type);
	}

	public ArrayList<Type> getHalfDamageAgainst() {
		return halfDamageAgainst;
	}

	public void addHalfDamageAgainst(Type type) {
		if (!halfDamageAgainst.contains(type))
			this.halfDamageAgainst.add(type);
	}

	public ArrayList<Type> getNoDamageAgainst() {
		return noDamageAgainst;
	}

	public void addNoDamageAgainst(Type type) {
		if (!noDamageAgainst.contains(type))
			this.noDamageAgainst.add(type);
	}

	public ArrayList<Type> getHalfDamageFrom() {
		return halfDamageFrom;
	}

	public void addHalfDamageFrom(Type type) {
		if (!halfDamageFrom.contains(type))
			this.halfDamageFrom.add(type);
	}

	public ArrayList<Type> getDoubleDamageFrom() {
		return doubleDamageFrom;
	}

	public void addDoubleDamageFrom(Type type) {
		if (!doubleDamageFrom.contains(type))
			this.doubleDamageFrom.add(type);
	}

	public ArrayList<Type> getNoDamageFrom() {
		return noDamageFrom;
	}

	public void addNoDamageFrom(Type type) {
		if (!noDamageFrom.contains(type))
			this.noDamageFrom.add(type);
	}

	public String toString() {
		return name;
	}

	public ArrayList<Move> getStrongMoves() {
		return strongMoves;
	}

	public void addStrongMoves(Move strongMove) {
		if (!strongMoves.contains(strongMove))
			this.strongMoves.add(strongMove);
	}

}
