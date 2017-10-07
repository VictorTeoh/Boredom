import java.lang.Math;
public class RunMe{

    static double levelforgas = 6;//not in use as of right now
    static boolean nopLock = false; //setpLock to 42 when creating characters to never have that character's form appear
    static double distHeal = 4.75; //don't set it to a nice value to make sure downtime doesnt break
    static int levellength = 74; // change later with a level array
    static long[] levels = { 0, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000, 12000, 15000, 20000, 25000, 30000, 35000, 40000, 45000, 50000, 55000, 60000, 65000, 70000, 80000, 90000, 100000, 115000, 130000, 150000,
			     175000, 200000, 225000, 250000, 275000, 300000, 325000, 350000, 400000, 450000, 500000, 550000, 600000, 650000, 700000, 750000, 800000, 900000, 1000000, 1100000, 1200000, 1300000, 1400000, 1500000,
			     1600000, 1700000, 1800000, 1900000, 2000000, 2200000, 2400000, 2600000, 2800000, 3000000, 3200000, 3400000, 3600000, 3800000, 4000000, 4200000, 4400000, 4600000, 4800000, 5000000};

    //possibly wrong on edge case where opponent exactly dies in x hits
    public static double hitstokill(Character attacker, Character opponent, int level){
	attacker.setlevel(level);
	double totalhits = 0;
	double dmg = attacker.hit(opponent, attacker.getnumatks());
	totalhits = Math.ceil(opponent.getcurHp()/dmg);
	opponent.heal();
	return totalhits;
    }

    public static double killtime(Character attacker, Character opponent, int level){
	return hitstokill(attacker, opponent, level) * attacker.getatkspd();
    }
    
    public static long getPL(int level){
	if(level > levels.length){
	    return 2^31;
	}
        return levels[level];
    }

    //    public static double netgas(){}//not in use

    //the asumptions made rounds up values for any spar where healing is needed
    public static double plsec(Character attacker, Character opponent, int level){
	double downtime;
	double ktime = killtime(attacker, opponent, level);
	double dietime = killtime(opponent, attacker, -1);
	double tabtime = (distHeal * 2) / attacker.getmovespeed();
	downtime = tabtime;
	if(doublelessthan( distHeal, attacker.getrange())){
	    if(doublelessthan(ktime, tabtime)){
		downtime = tabtime - ktime;
	    }
	    else{
		downtime = 0;
	    }
	}
	if(nopLock){
	    return (1.0 /(ktime + downtime / dietime)) * opponent.getplgiven();
	}
	if(getPL(attacker.getlevel()) < attacker.getpLock()){
	    return 0.0;
	}
        return (1.0 / (ktime + downtime / dietime)) * opponent.getplgiven(); 
	
    }

	    
    public static String[] spararr(Character attacker, Character opponent){
	String[] retarr = new String[levels.length];//change later with levelarr
	int i;
	for(i = 0; i < levels.length; i++){
	    attacker.setlevel(i);
	    retarr[i] = "At power level " + getPL(i) + " " + attacker.getname() + " vs " + opponent.getname() + "\t" + plsec(attacker, opponent, i);
	}
	return retarr;
    }

    public static boolean doubleequality(double a, double b){
	double result = a - b;
	if(-0.00000001 < result && result < 0.00000001){
	    return true;
	}
	return false;
    }

    public static boolean doublelessthan(double a, double b){
	double result = a - b;
	if(-0.00000001 > result){
	    return true;
	}
	return false;
    }
    
    public static String[] betterpl(String[] arr1, String[] arr2){
	int i;
	int stri0;
	int stri1;
	String h0;
	String h1;
	for(i = 0; i < arr1.length; i++){
	    h0 = arr1[i];
	    h1 = arr2[i];
	    stri0 = arr1[i].indexOf("\t");
	    stri1 = arr2[i].indexOf("\t");
	    if(doublelessthan(Double.parseDouble(h0.substring(stri0)),Double.parseDouble(h1.substring(stri1)))){
		arr1[i] = arr2[i];
	    }
	}
	return arr1;
    }
    

    public static String[] bestroute(Character[] forms, Character[] spar ){
	int i;
	String[] retspar = new String[levels.length];
	for(i = 0; i<levels.length; i++){
	    retspar[i] = "\t -1.0";
	}
	
	for( Character attacker : forms){
	    for( Character opponent : spar){
		betterpl(retspar, spararr(attacker, opponent));
	    }
	}
	return retspar;
	

    }
    
    public static String[] strarrcondenser(String[] arr){
	int i;
	int stri0;
	int stri1;
	String h0;
	String h1;
	int retind = 0;
	String[] retarr = new String[levellength];
	retarr[0] = arr[0];
	h0 = arr[0];
	stri0 = arr[0].indexOf("\t");
	h1 = arr[1];
	stri1 = arr[1].indexOf("\t");
	for(i = 1; i < arr.length; i++){
	    if(i != 1){
	    h0 = h1;
	    h1 = arr[i];
	    stri0 = stri1;
	    stri1 = arr[i].indexOf("\t");
	    }
	    if(doubleequality(Double.parseDouble(h0.substring(stri0)),Double.parseDouble(h1.substring(stri1)))){
	    }
	    else{
		retarr[retind] = arr[i];
		retind++;
	    }
	}
	return retarr;
    }
    
    public static void printarr(String[] arr){
	String[] retarr = strarrcondenser(arr);
	for(String str: retarr){
	    if(str != null){
		System.out.println(str);
	    }
	}
    }
    
    public static void main(String[] args){
	//armor per level 1 atk up per level 2
	Character g1 = new Character("G", 0L, 0.0, 550.0, 20.0, 3.0, 1.0, 0.924, false, 0, 1, 7.0, 2.976, 0.0);
	Character g2 = new Character("G4", 25000L, 0.0, 1100.0, 20.0, 6.0, 6.0, 0.924, false, 0, 1, 7.0, 2.976, 0.0);
	Character g3 = new Character("G10", 50000L, 0.0, 2750.0, 200.0, 19.0, 12.0, 1.24, false, 2, 0, 7.0, 4.963, 0.0);
	Character g4 = new Character("GS1", 100000L, 0.0, 5500.0, 100.0, 30.0, 25.0, 0.924, false, 0, 1, 7.0, 2.976, 0.0);
	Character g5 = new Character("GS2", 400000L, 0.0, 11000.0, 200.0, 60.0, 50.0, 0.62, false, 0, 0, 5.0, 2.976, 0.0);
	Character g6 = new Character("GS3", 800000L, 0.0, 22000.0, 350.0, 120.0, 100.0, 0.315, false, 0, 0, 7.0, 2.976, 0.0);
	Character g7 = new Character("GVG", 1000000L, 0.0, 45000.0, 1000.0, 424.0, 200.0, 0.924, false, 2, 0, 7.0, 2.976, 0.0);
	Character[] g = { g1, g2, g3, g4, g5, g6, g7};
        Character g1s = g1.myclone();
	g1s.setplgiven(105);
	Character g2s = g4.myclone();
	g2s.setplgiven(320);
	Character g3s = g5.myclone();
	g3s.setplgiven(600);
	Character g4s = g6.myclone();
	g4s.setplgiven(1300);
	Character g5s = g7.myclone();
	g5s.setplgiven(2125);
	Character[] gs = {g1s, g2s, g3s, g4s, g5s}; 
	
	






	printarr(bestroute(g, gs));
    }

    
}
