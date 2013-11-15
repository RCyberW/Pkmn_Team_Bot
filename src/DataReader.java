import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class DataReader {
	ArrayList<Move> movelist = new ArrayList<Move>();
	HashMap<String, Ability> abilitydict = new HashMap<String, Ability>();
	HashMap<String, Type> typedict = new HashMap<String, Type>();
	ArrayList<Type> typelist = new ArrayList<Type>();
	ArrayList<Creature> creaturelist = new ArrayList<Creature>();
	ArrayList<Creature> partyLineup = new ArrayList<Creature>();
	ArrayList<String> exceptions = new ArrayList<String>();

	public DataReader() {
		exceptions.add("Arceus");
		// exceptions.add("Blaziken");
		exceptions.add("Darkrai");
		exceptions.add("Deoxys");
		exceptions.add("Deoxys");
		exceptions.add("Dialga");
		// exceptions.add("Excadrill");
		exceptions.add("Genesect");
		exceptions.add("Giratina");
		exceptions.add("Groudon");
		exceptions.add("Ho-oh");
		exceptions.add("Kyogre");
		exceptions.add("Kyurem");
		exceptions.add("Landorus");
		exceptions.add("Lugia");
		exceptions.add("Manaphy");
		exceptions.add("Mewtwo");
		exceptions.add("Palkia");
		exceptions.add("Rayquaza");
		exceptions.add("Reshiram");
		exceptions.add("Shaymin");
		exceptions.add("Thundurus");
		exceptions.add("Tornadus");
		exceptions.add("Zekrom");
		exceptions.add("Xerneas");
		exceptions.add("Yveltal");
		exceptions.add("Zygarde");
	}

	public void readFile(String file) throws FileNotFoundException {
		int counter = 0;
		Scanner scan = new Scanner(new File(file));

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] elements = line.split("\t");
			if (file.equals("AbilityList") && counter > 0) {
				// is reading ability
				Ability newAbility = new Ability();
				newAbility.setName(elements[0]);
				newAbility.setDescription(elements[1]);

				abilitydict.put(newAbility.getName(), newAbility);
			} else if (file.equals("MoveList") && counter > 0) {
				// is reading move
				Move newMove = new Move();
				newMove.setName(elements[0]);
				newMove.setAttribute(elements[1]);
				newMove.setType(elements[2]);
				newMove.setDamage(elements[4]);
				newMove.setAccuracy(elements[5]);
				if (elements.length == 10)
					newMove.setDescrption(elements[9]);
				else
					newMove.setDescrption("Raw damage");
				if (newMove.getDamage() > 70 && newMove.getAccuracy() > 80) {
					String key = newMove.getAttribute().substring(0, 3)
							.toUpperCase();
					Type currType = typedict.get(key);
					if (currType != null)
						currType.addStrongMoves(newMove);
				}
				movelist.add(newMove);
			} else if (file.equals("TypeChart")) {
				// by default it is reading type chart
				Type currType = new Type();
				for (int i = 0; i < elements.length; i++) {
					try {
						double value = Double.parseDouble(elements[i]);
						Type against = typelist.get(i - 1);
						if (value == 0.5) {
							currType.addHalfDamageAgainst(against);
							against.addHalfDamageFrom(currType);
							// against.addDoubleDamageAgainst(currType);
						} else if (value == 2) {
							currType.addDoubleDamageAgainst(against);
							against.addDoubleDamageFrom(currType);
							// against.addHalfDamageAgainst(currType);
						} else if (value == 0) {
							currType.addNoDamageAgainst(against);
							against.addNoDamageFrom(currType);
						} else {
						}
					} catch (NumberFormatException e) {
						if (!elements[i].isEmpty()) {
							String key = elements[i].substring(0, 3);
							currType = typedict.get(key);
							if (currType == null) {
								currType = new Type();
								currType.setName(key);
								typedict.put(key, currType);
								typelist.add(currType);
							}
						}
					}
				}
			} else if (file.equals("CreatureList") && counter > 0) {
				Creature newCreature = new Creature();
				newCreature.setName(elements[1]);
				String[] types = elements[2].split("/");
				Type[] creatureType;
				boolean toAdd = false;
				types[0] = types[0].substring(0, 3).toUpperCase();
				if (types.length > 1) {
					types[1] = types[1].substring(0, 3).toUpperCase();

					int con = calculateWeaknessCount(types[0], types[1]);
					int pro = calculateStrengthCount(types[0], types[1]);
					int atkr = calculateDoubleCount(types[0], types[1]);

					if (pro >= con) {
						// good defensive type
						toAdd = true;
					}
					if (atkr < 4) {
						// if a duo type can be only good against 4 types
						// do not want
						// toAdd = false;
					}
					creatureType = new Type[] { typedict.get(types[0]),
							typedict.get(types[1]) };
				} else {
					Type t = typedict.get(types[0]);
					if (t.getHalfDamageFrom().size()
							+ t.getNoDamageFrom().size() >= t
							.getDoubleDamageFrom().size()) {
						// good defensive type
						toAdd = true;
					}
					if (t.getDoubleDamageAgainst().size() < 2) {
						// if it is only good against 1 type
						// do not want
						// toAdd = false;
					}
					creatureType = new Type[] { typedict.get(types[0]) };
				}
				newCreature.setTypes(creatureType);

				String[] abilities = elements[3].split(",");
				for (int i = 0; i < abilities.length; i++) {
					Ability currAbil = abilitydict.get(abilities[i].trim());
					newCreature.addAbility(currAbil);
				}

				Stats baseStats = new Stats(elements[4], elements[5],
						elements[6], elements[7], elements[8], elements[9]);
				if (baseStats.getTotal() < 500) {
					// total status value need to be at least 500 to consider
					// competitive
					toAdd = false;
				}
				newCreature.setBaseStats(baseStats);
				if (toAdd) {
					creaturelist.add(newCreature);
				}
			} else if (file.equals("Misc")) {
				if (abilitydict.get(elements[0]) == null) {
					exceptions.add(elements[0]);
				}
			}
			counter++;
		}
	}

	private int calculateDoubleCount(String t1, String t2) {
		Type type1 = typedict.get(t1);
		Type type2 = typedict.get(t2);

		ArrayList<Type> resultDoubleAgainst = new ArrayList<Type>();
		resultDoubleAgainst.addAll(type1.getDoubleDamageAgainst());
		if (type2 != null)
			resultDoubleAgainst.addAll(type2.getDoubleDamageAgainst());

		LinkedHashSet<Type> rda = new LinkedHashSet<Type>(resultDoubleAgainst);
		resultDoubleAgainst = new ArrayList<Type>(rda);

		return resultDoubleAgainst.size();
	}

	private int calculateWeaknessCount(String t1, String t2) {
		Type type1 = typedict.get(t1);
		Type type2 = typedict.get(t2);

		ArrayList<Type> resultGoodAgainst = new ArrayList<Type>();
		ArrayList<Type> resultBadAgainst = new ArrayList<Type>();

		resultBadAgainst.addAll(type1.getDoubleDamageFrom());
		resultGoodAgainst.addAll(type1.getHalfDamageFrom());
		resultGoodAgainst.addAll(type1.getNoDamageFrom());

		if (type2 != null) {
			resultBadAgainst.addAll(type2.getDoubleDamageFrom());
			resultGoodAgainst.addAll(type2.getHalfDamageFrom());
			resultGoodAgainst.addAll(type2.getNoDamageFrom());
		}

		LinkedHashSet<Type> rga = new LinkedHashSet<Type>(resultGoodAgainst);
		resultGoodAgainst = new ArrayList<Type>(rga);
		LinkedHashSet<Type> rba = new LinkedHashSet<Type>(resultBadAgainst);
		resultBadAgainst = new ArrayList<Type>(rba);

		ArrayList<Type> temp = new ArrayList<Type>();
		temp.addAll(resultBadAgainst);
		resultBadAgainst.removeAll(resultGoodAgainst);
		resultGoodAgainst.removeAll(temp);

		return resultBadAgainst.size();
	}

	private int calculateStrengthCount(String t1, String t2) {
		Type type1 = typedict.get(t1);
		Type type2 = typedict.get(t2);

		ArrayList<Type> resultGoodAgainst = new ArrayList<Type>();
		ArrayList<Type> resultBadAgainst = new ArrayList<Type>();

		resultBadAgainst.addAll(type1.getDoubleDamageFrom());
		resultGoodAgainst.addAll(type1.getHalfDamageFrom());
		resultGoodAgainst.addAll(type1.getNoDamageFrom());

		if (type2 != null) {
			resultBadAgainst.addAll(type2.getDoubleDamageFrom());
			resultGoodAgainst.addAll(type2.getHalfDamageFrom());
			resultGoodAgainst.addAll(type2.getNoDamageFrom());
		}

		LinkedHashSet<Type> rga = new LinkedHashSet<Type>(resultGoodAgainst);
		resultGoodAgainst = new ArrayList<Type>(rga);
		LinkedHashSet<Type> rba = new LinkedHashSet<Type>(resultBadAgainst);
		resultBadAgainst = new ArrayList<Type>(rba);

		ArrayList<Type> temp = new ArrayList<Type>();
		temp.addAll(resultBadAgainst);
		resultBadAgainst.removeAll(resultGoodAgainst);
		resultGoodAgainst.removeAll(temp);

		return resultGoodAgainst.size();
	}

	private void duoCombination() {
		for (int i = 0; i < typelist.size(); i++) {
			Type type1 = typelist.get(i);
			for (int j = i + 1; j < typelist.size(); j++) {
				Type type2 = typelist.get(j);
				if (i == j) {
					continue;
				} else {
					ArrayList<Type> resultGoodAgainst = new ArrayList<Type>();
					ArrayList<Type> resultBadAgainst = new ArrayList<Type>();

					resultBadAgainst.addAll(type1.getDoubleDamageFrom());
					resultGoodAgainst.addAll(type1.getHalfDamageFrom());
					resultGoodAgainst.addAll(type1.getNoDamageFrom());

					resultBadAgainst.addAll(type2.getDoubleDamageFrom());
					resultGoodAgainst.addAll(type2.getHalfDamageFrom());
					resultGoodAgainst.addAll(type2.getNoDamageFrom());

					LinkedHashSet<Type> rga = new LinkedHashSet<Type>(
							resultGoodAgainst);
					resultGoodAgainst = new ArrayList<Type>(rga);
					LinkedHashSet<Type> rba = new LinkedHashSet<Type>(
							resultBadAgainst);
					resultBadAgainst = new ArrayList<Type>(rba);

					ArrayList<Type> temp = new ArrayList<Type>();
					temp.addAll(resultBadAgainst);
					resultBadAgainst.removeAll(resultGoodAgainst);
					resultGoodAgainst.removeAll(temp);

					if (resultBadAgainst.size() < resultGoodAgainst.size()) {
						System.out.println("DUO TYPE " + type1.getName()
								+ " + " + type2.getName());
						System.out.println("Bad against " + resultBadAgainst);
						System.out.println("Good against " + resultGoodAgainst);
						System.out.println("======" + resultBadAgainst.size()
								+ ":" + resultGoodAgainst.size() + "======");
					}
				}
			}
		}
	}

	private void typeAnalysis() {
		for (int i = 0; i < typelist.size(); i++) {
			Type type = typelist.get(i);
			System.out.println(type.getName());
			// System.out.println("2.0 dmg atk "
			// + type.getDoubleDamageAgainst());
			// System.out
			// .println("0.5 dmg atk " + type.getHalfDamageAgainst());
			// System.out.println("0.0 dmg atk " +
			// type.getNoDamageAgainst());
			// System.out.println("2.0 dmg def " +
			// type.getDoubleDamageFrom());
			// System.out.println("0.5 dmg def " +
			// type.getHalfDamageFrom());
			// System.out.println("0.0 dmg def " + type.getNoDamageFrom());
			System.out.println("STRONGER MOVES: \n" + type.getStrongMoves());
			System.out.println();
		}
	}

	private void avgStatsCalculator(ArrayList<Creature> list) {
		double totalHP = 0, totalPhy = 0, totalMag = 0, totalPDef = 0, totalMDef = 0, totalSpd = 0;

		System.out.println("There are " + list.size() + " candidates");
		int x = 1;
		for (int i = 0; i < list.size(); i++) {
			Creature currCreature = list.get(i);
			if (exceptions.contains(currCreature.getName()))
				continue;
			totalHP += currCreature.getBaseStats().getHealth();
			totalPhy += currCreature.getBaseStats().getPhyDmg();
			totalMag += currCreature.getBaseStats().getMagDmg();
			totalPDef += currCreature.getBaseStats().getPhyDef();
			totalMDef += currCreature.getBaseStats().getMagDef();
			totalSpd += currCreature.getBaseStats().getSpd();
			x++;
		}
		System.out.println("finished reading " + x + " candidates");

		totalHP /= x;
		totalPhy /= x;
		totalMag /= x;
		totalPDef /= x;
		totalMDef /= x;
		totalSpd /= x;

		System.out.printf(
				"HP %f\nAtk %f\nDef %f\nSp.Atk %f\nSp.Def %f\nSpd %f\n",
				totalHP, totalPhy, totalPDef, totalMag, totalMDef, totalSpd);
	}

	private void candidateAnalysis(String type) throws MalformedURLException {
		int y = 1;
		int atkCount = 0, atkMargin = 1;
		int defCount = 0, defenseMargin = 300;
		Type searchType = typedict.get(type.substring(0, 3).toUpperCase());

		for (int i = 0; i < creaturelist.size(); i++) {
			Creature currCreature = creaturelist.get(i);
			if (exceptions.contains(currCreature.getName()))
				continue;

			boolean matchType = false;
			for (int t = 0; t < currCreature.getTypes().length; t++) {
				if (currCreature.getTypes()[t] == searchType) {
					matchType = true;
				}
			}
			if (!matchType && searchType != null)
				continue;

			int offensiveSpear = 0;
			int defensiveShield = 0;

			offensiveSpear += 100 <= currCreature.getBaseStats().getPhyDmg() ? 1
					: 0;
			offensiveSpear += 100 <= currCreature.getBaseStats().getMagDmg() ? 1
					: 0;

			defensiveShield += currCreature.getBaseStats().getPhyDef()
					+ currCreature.getBaseStats().getMagDef()
					+ currCreature.getBaseStats().getHealth();

			if (offensiveSpear < atkMargin && defensiveShield < defenseMargin
					|| 90 > currCreature.getBaseStats().getSpd())
				continue;

			String url = "http://bulbapedia.bulbagarden.net/wiki/"
					+ currCreature.getName();
			URL creatureUrl = new URL(url);

			String t1 = currCreature.getTypes()[0].getName(), t2 = "";

			if (currCreature.getTypes().length > 1) {
				t2 = currCreature.getTypes()[1].getName();
			}
			int doubleAgainst = calculateDoubleCount(t1, t2);
			int halfFrom = calculateStrengthCount(t1, t2);

			if (offensiveSpear >= atkMargin) {
				System.out
						.printf("%d. %s doubleAgainst %s halfFrom %s\n[attacker][%s]\n",
								y, currCreature, doubleAgainst, halfFrom,
								creatureUrl);
				for (Ability a : currCreature.getAbilities()) {
					if (a == null) {
						System.exit(0);
					}
					System.out.println(a);
				}
				System.out.println(currCreature.getBaseStats());

				boolean foundMoves = false;
				for (Type t : currCreature.getTypes()) {
					if (t == null) {
						// something went wrong
					}
					for (Move m : t.getStrongMoves()) {
						// only increment attacker count if it finds
						// compatible moves
						if (currCreature.getBaseStats().getPhyDmg() >= 100
								&& m.getType() == MoveType.PHYSICAL) {
							// list all physical move of the type
							foundMoves = true;
							System.out.println(m);
						}
						if (currCreature.getBaseStats().getMagDmg() >= 100
								&& m.getType() == MoveType.SPECIAL) {
							// list all special move of the type
							foundMoves = true;
							System.out.println(m);
						}
					}
				}

				System.out.println();

				if (foundMoves)
					atkCount++;
			}

			if (defensiveShield >= defenseMargin) {
				System.out
						.printf("%d. %s doubleAgainst %s halfFrom %s\n[defender][%s]\n",
								y, currCreature, doubleAgainst, halfFrom,
								creatureUrl);
				for (Ability a : currCreature.getAbilities()) {
					if (a == null) {
						System.exit(0);
					}
					System.out.println(a);
				}
				System.out.println(currCreature.getBaseStats());
				System.out.println();
				defCount++;
			}
			y++;
		}
		System.out.println("finished displaying " + atkCount
				+ " attackers out of " + y + " candidates and " + defCount
				+ " defenders out of " + y + " candidates");
	}

	boolean[] defeatedTypes = new boolean[typelist.size()];

	private void buildParty(String[] existingIdea) {
		if (defeatedTypes == null || defeatedTypes.length == 0)
			defeatedTypes = new boolean[typelist.size()];

		if (existingIdea == null || existingIdea.length == 0) {
			// no idea, fill party
		} else {
			// process the existing party
			for (String name : existingIdea) {
				for (int i = 0; i < creaturelist.size(); i++) {
					Creature partySelect = creaturelist.get(i);
					if (partySelect.getName().equalsIgnoreCase(name)) {
						partyLineup.add(partySelect);
						for (Type t : partySelect.getTypes()) {
							// get the type of the selected creature
							for (Type strongAgainst : t
									.getDoubleDamageAgainst()) {
								// mark off all the type this creature will
								// cover by STAB
								int typeIdx = findTypeIndex(strongAgainst);
								if (typeIdx > -1) {
									defeatedTypes[typeIdx] = true;
								}
							}
						}
					}
				}
			}
			buildParty(null);
		}
	}

	private int findTypeIndex(Type type) {
		for (int i = 0; i < typelist.size(); i++) {
			if (typelist.get(i) == type) {
				return i;
			}
		}

		return -1;
	}

	public static void main(String[] args) {
		DataReader dr = new DataReader();
		try {
			dr.readFile("TypeChart");
			dr.readFile("AbilityList");
			dr.readFile("MoveList");
			dr.readFile("CreatureList");
			dr.readFile("Misc");

			dr.candidateAnalysis("ABCD");
			dr.buildParty(null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
