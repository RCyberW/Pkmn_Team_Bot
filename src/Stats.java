public class Stats {
	private int health;
	private int magDef;
	private int phyDef;
	private int magDmg;
	private int phyDmg;
	private int spd;

	public Stats(String health, String phyDmg, String phyDef, String magDmg,
			String magDef, String spd) {
		setHealth(health);
		setPhyDmg(phyDmg);
		setMagDmg(magDmg);
		setPhyDef(phyDef);
		setMagDef(magDef);
		setSpd(spd);
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = Integer.parseInt(health);
		;
	}

	public int getMagDef() {
		return magDef;
	}

	public void setMagDef(String magDef) {
		this.magDef = Integer.parseInt(magDef);
		;
	}

	public int getPhyDef() {
		return phyDef;
	}

	public void setPhyDef(String phyDef) {
		this.phyDef = Integer.parseInt(phyDef);
		;
	}

	public int getMagDmg() {
		return magDmg;
	}

	public void setMagDmg(String magDmg) {
		this.magDmg = Integer.parseInt(magDmg);
		;
	}

	public int getPhyDmg() {
		return phyDmg;
	}

	public void setPhyDmg(String phyDmg) {
		this.phyDmg = Integer.parseInt(phyDmg);
		;
	}

	public int getSpd() {
		return spd;
	}

	public void setSpd(String spd) {
		this.spd = Integer.parseInt(spd);
		;
	}

	public int getTotal() {
		return getHealth() + getPhyDmg() + getMagDmg() + getPhyDef()
				+ getMagDef() + getSpd();
	}

	public String toString() {
		return "[HP:" + getHealth() + " PhyAtk:" + getPhyDmg() + " Sp.Atk:"
				+ getMagDmg() + " PhyDef:" + getPhyDef() + " Sp.Def:" + getMagDef() + " Speed:" + getSpd() + "]";
	}
}
