import java.util.ArrayList;


public class Creature {
	
	private String name;
	private ArrayList<Ability> abilities = new ArrayList<Ability>();
	private Type[] types = new Type[2];
	private Stats baseStats;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Ability> getAbilities() {
		return abilities;
	}
	public void addAbility(Ability ability) {
		this.abilities.add(ability);
	}
	public Type[] getTypes() {
		return types;
	}
	public void setTypes(Type[] types) {
		this.types = types;
	}
	public Stats getBaseStats() {
		return baseStats;
	}
	public void setBaseStats(Stats baseStats) {
		this.baseStats = baseStats;
	}

	public String toString() {
		if (types.length > 1) 
			return name + ":::" + types[0] + "/" + types[1];
		else
			return name + ":::" + types[0];
	}
}
