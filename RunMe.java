import java.lang.Math;
public class RunMe{

    //splash is still wrong
    static double indexofsplash = 0.33; //the chance of splash from your unit to attack a brand new unit
    static double levelforgas = 6;//not in use as of right now
    static boolean nopLock = false; //setpLock to 42 when creating characters to never have that character's form appear
    static double distHeal = 4.75; //don't set it to a nice value to make sure downtime doesnt break
    static long[] levels = { 0, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000, 12000, 15000, 20000, 25000, 30000, 35000, 40000, 45000, 50000, 55000, 60000, 65000, 70000, 80000, 90000, 100000, 115000, 130000, 150000,
			     175000, 200000, 225000, 250000, 275000, 300000, 325000, 350000, 400000, 450000, 500000, 550000, 600000, 650000, 700000, 750000, 800000, 900000, 1000000, 1100000, 1200000, 1300000, 1400000, 1500000,
			     1600000, 1700000, 1800000, 1900000, 2000000, 2200000, 2400000, 2600000, 2800000, 3000000, 3200000, 3400000, 3600000, 3800000, 4000000, 4200000, 4400000, 4600000, 4800000, 5000000};

    //possibly wrong on edge case where opponent exactly dies in x hits
    public static double hitstokill(Character attacker, Character opponent, int level){
	attacker.setlevel(level);
	double totalhits = 0;
	double dmg = attacker.hit(opponent, attacker.getnumatks());
	opponent.heal();
	totalhits = Math.ceil(opponent.getcurHp()/dmg);
	return totalhits;
    }

    
    public static double killtime(Character attacker, Character opponent, int level){
	double totalhits = hitstokill(attacker, opponent, level);
	return totalhits * attacker.getatkspd();
    }
    
    public static long getPL(int level){
	if(level > levels.length){
	    return 2^31;
	}
        return levels[level];
    }

    public static double netgas(Character C){
	return Math.floor(C.getlevel() / levelforgas - C.getgas()) + 1;
    }
    
    
    //the asumptions made rounds up values for any spar where healing is needed in cases where you outrange the enemy
    public static double plsec(Character attacker, Character opponent, int level){
	if(getPL(attacker.getlevel()) < attacker.getpLock()){
	    return 0.0;
	}
	if((attacker.getcanhitground() && opponent.getunitype() == 0) || (attacker.getcanhitair() && opponent.getunitype() == 1)){
	    double downtime;
	    double ktime = killtime(attacker, opponent, level);
	    double dietime = killtime(opponent, attacker, -1);
	    double tabtime = (distHeal * 2) / attacker.getmovespeed();
	    downtime = tabtime;
	    if(doublelessthan( distHeal, attacker.getrange()) || (!doublelessthan( attacker.getrange(), opponent.getrange()))){
		if(doublelessthan(ktime, tabtime)){
		    downtime = tabtime - ktime;
		}
		else{ downtime = 0;}
	    }
	    if(attacker.gethasSplash()){
		double Shits;
		double Sktime;
		double dmg = attacker.hit(opponent, attacker.getnumatks());
		attacker.hit(opponent, 1);
		if(!opponent.isalive()){
		    Shits = 0;
		    Sktime = Shits * attacker.getatkspd();
		    return(2.0 * opponent.getplgiven() * indexofsplash + (1 - indexofsplash) * (1.0 / (ktime + downtime / dietime)) * opponent.getplgiven());
		}
		Shits = Math.ceil((opponent.getcurHp()+ dmg)/ dmg);
		Sktime = Shits * attacker.getatkspd();
		return(1.0 / (Sktime + downtime / dietime)) * opponent.getplgiven() * indexofsplash + (1 - indexofsplash) * (1.0 / (ktime + downtime / dietime)) * opponent.getplgiven();
	    }
	    return (1.0 / (ktime + downtime / dietime)) * opponent.getplgiven();
	}
	return 0.0;
    }

	    
    public static String[] spararr(Character attacker, Character opponent){
	String[] retarr = new String[levels.length];//change later with levelarr
	int i;
	for(i = 0; i < levels.length; i++){
	    attacker.setlevel(i);
	    retarr[i] = "At power level " + getPL(i) + " " + attacker.getname() + " vs " + opponent.getname() + " net gas " + netgas(attacker)  + "\t" + plsec(attacker, opponent, i);
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
	String[] retarr = new String[levels.length];
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
	System.out.println("\n");
    }
    
    public static void main(String[] args){
	//c-s label
	//All Sagas RX 25 label 1  
	//armor per level 1 atk up per level 2
	/*
	Character g1 = new Character("G", 0L, 0.0, 550.0, 20.0, 3.0, 0.0, 0.924, 1.0, true, true, false, 0, 0, 1, 7.0, 2.976, 0.0);
	Character g2 = new Character("G4", 25000L, 5.0, 1100.0, 20.0, 6.0, 6.0, 0.924, 1.0, true, true, false, 0, 0, 1, 7.0, 2.976, 0.0);
	Character g3 = new Character("G10", 50000L, 6.0, 2750.0, 200.0, 19.0, 12.0, 1.26, 1.0, true, false, false, 1, 2, 0, 5.0, 4.963, 0.0);
	Character g4 = new Character("GS1", 100000L, 8.0, 5500.0, 100.0, 30.0, 25.0, 0.924, 1.0, true, true, false, 0, 0, 1, 7.0, 2.976, 0.0);
	Character g5 = new Character("GS2", 400000L, 10.0, 11000.0, 200.0, 60.0, 50.0, 0.62, 1.0, true, true, false, 0, 0, 0, 5.0, 2.976, 0.0);
	Character g6 = new Character("GS3", 800000L, 12.0, 22000.0, 350.0, 120.0, 100.0, 0.315, 1.0, true, true, false, 0, 0, 0, 5.0, 4.464, -10.0/9);
	Character g7 = new Character("SVG", 1000000L, 14.0, 45000.0, 1000.0, 424.0, 200.0, 0.924, 1.0, true, false, false, 0, 2, 0, 6.0, 3.497, 0.0);
	Character g8 = new Character("SVA", 1000000L, 14.0, 45000.0, 500.0, 214.0, 200.0, 0.924, 2.0, false, true, false, 0, 2, 2, 8.0, 3.497, 0.0);
	Character[] g = { g1, g2, g3, g4, g5, g6, g7};

	
	Character v1 = new Character("V", 0L, 0.0, 550.0, 20.0, 3.0, 0.0, 0.924, 1.0, true, false, false, 0, 0, 1, 0.0, 2.976, 0.0);
	Character v2 = new Character("SV", 50000L, 6.0, 2100.0, 200.0, 12.0, 4.0, 0.84, 1.0, true, true, true, 0, 2, 0, 0.0, 3.720, 0.0);
	Character v3 = new Character("VS1", 100000L, 8.0, 5500.0, 100.0, 30.0, 25.0, 1.26, 1.0, true, true, false, 0, 0, 1, 4.0, 3.906, 0.0);
	Character v4 = new Character("VS2", 400000L, 10.0, 11000.0, 200.0, 116.0, 50.0, 0.924, 1.0, true, true, false, 0, 0, 0, 6.0, 3.906, 0.0);
	Character v5 = g7.myclone();
	Character v6 = g8.myclone();
	Character[] v = { v1, v2, v3, v4, v5, v6};

	Character p1 = new Character("P", 0L, 0.0, 500.0, 20.0, 4.0, 0.0, 0.924, 1.0, true, false, false, 0, 0, 1, 0.0, 2.976, 0.0);
	Character p2 = new Character("NP", 70000L, 0.0, 2250.0, 50.0, 15.0, 25.0, 0.924, 1.0, true, true, false, 0, 0, 1, 7.0, 2.976, 1.0);
	Character p3 = new Character("SNP", 225000L, 0.0, 7000.0, 250.0, 86.0, 50.0, 1.89, 1.0, true, true, false, 1, 2, 2, 5.0, 3.720, 0.0);
	Character p4 = new Character("UPG", 450000L, 11.0, 21000.0, 1000.0, 193.0, 135.0, 1.26, 1.0, true, false, false, 1, 2, 0, 5.0, 4.963, 0.0);
	Character p5 = new Character("UPA", 450000L, 11.0, 21000.0, 1000.0, 143.0, 135.0, 0.924, 1.0, false, true, false, 1, 2, 2, 5.0, 4.963, 0.0);
	Character[] p = { p1, p2, p3, p4, p5};

	Character t1 =  new Character("T", 0L, 0.0, 425.0, 15.0, 1.0, 0.0, 0.924, 2.0, true, false, false, 0, 0, 0, 0.0, 2.976, 0.0);
	Character t2 =  new Character("TS1", 100000L, 8.0, 4250.0, 75.0, 15.0, 0.0, 0.924, 2.0, true, false, false, 0, 0, 0, 0.0, 4.464, 0.0);
	Character t3 =  new Character("GS1G", 750000L, 11.0, 17500.0, 500.0, 140.0, 100.0, 1.26, 1.0, true, false, false, 1, 2, 0, 4.0, 4.963, 0.0);
	Character t4 =  new Character("GS1A", 750000L, 11.0, 17500.0, 250.0, 52.0, 100.0, 0.924, 2.0, false, true, false, 1, 2, 2, 4.0, 4.963, 0.0);
	Character t5 =  new Character("GS3G", 950000L, 14.0, 37500.0, 1000.0, 280.0, 100.0, 1.26, 1.0, true, false, false, 1, 2, 0, 4.0, 4.963, 0.0);
	Character t6 =  new Character("GS3A", 950000L, 14.0, 37500.0, 500.0, 101.0, 100.0, 0.924, 2.0, false, true, false, 1, 2, 2, 4.0, 4.963, 0.0);
	Character[] t = { t1, t2, t3, t4, t5, t6};

	Character gh1 = new Character("GH", 0L, 0.0, 425.0, 20.0, 0.0, 0.0, 0.252, 1.0, true, false, false, 0, 0, 0, 0.0, 6.286, 1.0); // use crack ling stats
	Character gh2 = new Character("EGH", 0L, 11.0, 1700.0, 80.0, 9.0, 4.0, 0.924, 1.0, true, false, false, 0, 0, 0, 0.0, 2.976, 1.0);
	Character gh3 = new Character("GHS1", 100000L, 8.0, 4250.0, 50.0, 7.0, 25.0, 0.252, 1.0, true, false, false, 0, 0, 0, 0.0, 6.286, 1.0);
	Character gh4 = new Character("GHS2", 400000L, 10.0, 9500.0, 200.0, 80.0, 50.0, 0.63, 1.0, true, true, false, 0, 1, 2, 5.0, 4.144, 1.0);
	Character gh5 = new Character("MSGH", 900000L, 12.0, 37500.0, 1000.0, 180.0, 150.0, 0.63, 1.0, true, true, false, 0, 1, 2, 5.0, 4.144, 1.0);
	Character[] gh = { gh1, gh2, gh3, gh4, gh5};

	Character go1 =  new Character("GO", 0L, 0.0, 425.0, 15.0, 1.0, 0.0, 0.924, 1.5, true, false, true, 0, 0, 1, 2.0, 2.976, 0.0);
	Character go2 =  new Character("GOS1", 100000L, 8.0, 4250.0, 75.0, 10.0, 0.0, 0.924, 1.5, true, false, true, 0, 0, 1, 2.0, 2.976, 0.0);
	Character go3 =  t3.myclone();
	Character go4 =  t4.myclone();
	Character go5 =  t5.myclone();
	Character go6 =  t6.myclone();
	Character[] go = { go1, go2};
	
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
	
        Character v1s = v1.myclone();
	v1s.setplgiven(95);
	Character v2s = v3.myclone();
	v2s.setplgiven(340);
	Character v3s = v4.myclone();
	v3s.setplgiven(800);
	Character v4s = p4.myclone();
	v4s.setplgiven(2000);
	Character v5s = v5.myclone();
	v5s.setplgiven(2125);
	Character[] vs = {v1s, v2s, v3s, v4s, v5s};

	Character p1s = g1.myclone();
	p1s.setplgiven(100);
	Character p2s = p2.myclone();
	p2s.setplgiven(400);
	Character p3s = v3.myclone();
	p3s.setplgiven(540);
	Character p4s = v4.myclone();
	p4s.setplgiven(1060);
	Character p5s = p5.myclone();
	p5s.setplgiven(2035);
	Character[] ps = {p1s, p2s, p3s, p4s, p5s};

	Character t1s = t1.myclone();
	t1s.setplgiven(100);
	Character t2s = t2.myclone();
	t2s.setplgiven(340);
	Character t3s = g5.myclone();
	t3s.setplgiven(1330);
	Character t4s = t4.myclone();
	t4s.setplgiven(2600);
	Character t5s = t6.myclone();
	t5s.setplgiven(5550);
	Character[] ts = {t1s, t2s, t3s, t4s, t5s};

	Character gh1s = gh1.myclone();
	gh1s.setplgiven(100);
	Character gh2s = gh3.myclone();
	gh2s.setplgiven(400);
	Character gh3s = gh4.myclone();
	gh3s.setplgiven(800);
	Character gh4s = t3.myclone();
	gh4s.setplgiven(1300);
	Character gh5s = g7.myclone();
	gh5s.setplgiven(2775);
	Character[] ghs = {gh1s, gh2s, gh3s, gh4s, gh5s};

	Character go1s = go1.myclone();
	go1s.setplgiven(105);
	Character go2s = go2.myclone();
	go2s.setplgiven(320);
	Character go3s = g5.myclone();
	go3s.setplgiven(1000);
	Character go4s = t4.myclone();
	go4s.setplgiven(2600);
	Character go5s = t6.myclone();
	go5s.setplgiven(5550);
	Character[] gos = {go1s, go2s, go3s, go4s, go5s};
	
	printarr(bestroute(g, gs));
	printarr(bestroute(v, vs));
	printarr(bestroute(p, ps));
	printarr(bestroute(t, ts));
	printarr(bestroute(gh, ghs));
	printarr(bestroute(go, gos));
	*/
	//DBZ Evil Sagas RX 6 label 2
	Character r1 = new Character("R", 0L, 0.0, 500.0, 8.0, 2.0, 0.0, 0.462, 1.5, true, false, true, 0, 0, 1, 2.0, 4.464, -10.0/9);
	Character r2 = new Character("RS1", 50000L, 7.0, 5000.0, 100.0, 30.0, 25.0, 0.924, 1.0, true, true, false, 0, 0, 1, 7.0, 2.976, 0.0);
	Character r3 = new Character("RS2", 250000L, 9.0, 12500.0, 250.0, 70.0, 50.0, 0.924, 1.0, true, true, false, 0, 0, 1, 7.0, 2.976, 0.0);
	Character r4 = new Character("RS3", 700000L, 12.0, 25000.0, 285.0, 120.0, 100.0, 0.315, 1.0, true, true, false, 0, 0, 0, 5.0, 4.464, -10.0/9);
	Character r5 = new Character("BRIZ", 800000L, 13.0, 31000.0, 300.0, 220.0, 200.0, 0.62, 1.0, true, true, false, 0, 1, 2, 5.0, 4.144, 1.0);
	Character r6 = new Character("BRIZS", 1000000L, 14.0, 56000.0, 500.0, 398.0, 255.0, 0.62, 1.0, true, true, false, 0, 1, 2, 5.0, 4.144, 1.0);
	Character[] r = {r1, r2, r3, r4, r5, r6};

	Character r1s = r1.myclone();
	r1s.setplgiven(100);
	Character r2s = r2.myclone();
	r2s.setplgiven(400);
	Character r3s = r3.myclone();
	r3s.setplgiven(1000);
	Character r4s = r5.myclone();
	r4s.setplgiven(2000);
	Character r5s = r6.myclone();
	r5s.setplgiven(3500);
	Character[] rs = {r1s, r2s, r3s, r4s, r5s};

	printarr(bestroute(r, rs));
	/*	printarr(bestroute(v, vs));
	printarr(bestroute(p, ps));
	printarr(bestroute(t, ts));
	printarr(bestroute(gh, ghs));
	printarr(bestroute(go, gos)); */
    }

    
}
