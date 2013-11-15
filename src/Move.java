
public class Move {
	private String name;
	private String attribute;
	private MoveType type;
	private int damage;
	private int accuracy;
	private String descrption;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MoveType getType() {
		return type;
	}
	public void setType(String type) {
		
		if (type.equals("Special")) {
			this.type = MoveType.SPECIAL;
		} else if (type.equals("Physical")) {
			this.type = MoveType.PHYSICAL;
		} else {
			this.type = MoveType.STATUS;
		}
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(String damage) {
		if (damage.equals("---")) {
			this.damage = 0;
		} else {
			this.damage = Integer.parseInt(damage);
		}
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getDescrption() {
		return descrption;
	}
	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		if (accuracy.equals("---")) {
			this.accuracy = Integer.MAX_VALUE;
		} else {
			this.accuracy = Integer.parseInt(accuracy);
		}
	}
	
	public String toString() {
		return name + " " + attribute + " " + type + " " + damage + " " + accuracy + " " + descrption;
	}
}
