import java.lang.Math;
public class RunMe{
    static double distHeal = 10.0;
    static int levellength = 76; // change later with a level array
    
    public static double hitstokill(Character attacker, Character opponent, int level){
	attacker.setlevel(level);
	double totalhits = 0;
	while(opponent.isalive()){
	    attacker.hit(opponent, level);
	    //System.out.println(opponent.getcurHp());
	    totalhits++;
	}
	opponent.heal();
	return totalhits;
    }

    public static double killtime(Character attacker, Character opponent, int level){
	return hitstokill(attacker, opponent, level) * attacker.getatkspd();
    }

    public static double plsec(Character attacker, Character opponent, int level){
	return 1/killtime(attacker, opponent, level) * opponent.getplgiven();
    }

	    
    public static String[] spararr(Character attacker, Character opponent){
	String[] retarr = new String[levellength];//change later with levelarr
	int i;
	for(i = 0; i < levellength; i++){
	    attacker.setlevel(i);
	    retarr[i] = "At level " + i + " " + attacker.getname() + " vs " + opponent.getname() + "\t" + plsec(attacker, opponent, i);

	}
	return retarr;
    }


    // wip
    public static String[] strarrcondenser(String[] arr){
	int i;
	for(i = 0; i < starrcondenser.length; i++){

	}
	return arr;
    }
    
    public static void printarr(String[] arr){
	String[]modarr = strarrcondenser(arr);
	for(String str: modarr){
	    System.out.println(str);
	}

    }
    
    public static void main(String[] args){
	int level = 0;
	Character g1 = new Character("G", 0L, 0.0, 550.0, 20.0, 3.0, 1.0, 0.924, false, 0, 1, 7.0, 2.976, 0.0);
	Character g2 = new Character("G4", 0L, 0.0, 1100.0, 20.0, 6.0, 6.0, 0.924, false, 0, 1, 7.0, 2.976, 0.0);
	Character g3 = new Character("G10", 0L, 0.0, 2750.0, 200.0, 19.0, 12.0, 1.24, false, 2, 0, 7.0, 2.976, 0.0);
	Character g4 = new Character("GS1", 0L, 0.0, 5500.0, 100.0, 30.0, 25.0, 0.924, false, 0, 1, 7.0, 2.976, 0.0);
	Character[] g = { g1, g2, g3, g4};
	
	/*
	Character g5 = new Character("GS2", 0L, 0.0, 2750.0, 200.0, 19.0, 12.0, 1.24, false, 2, 0, 7.0, 2.976, 0.0);
	Character g6 = new Character("G10", 0L, 0.0, 2750.0, 200.0, 19.0, 12.0, 1.24, false, 2, 0, 7.0, 2.976, 0.0);
	*/
        Character g1s = g1.myclone();
	g1s.setplgiven(100);
	Character g2s = g4.myclone();
	g2s.setplgiven(400);
	/* killtime and hits to kill working in most cases
        System.out.println(killtime(g1, g1s, 12, false));
	System.out.println(killtime(g4, g2s, 50, false));
	*/
	printarr(spararr(g4,g1s));
	printarr(spararr(g4,g2s));
	
    }

    
}
