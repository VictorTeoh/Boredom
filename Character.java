
public class Character {
    //firebat does 3 hits with 1.5 dmg mutiplier
    String name;
    double baseHp;
    double curHp;
    double baseatk;
    double upatk;
    double numatks;
    double atkspd;//in secs per hit
    double basearmr;
    double uparmr;
    int level;
    double healrate;
    int size;//0 small 1 medium 2 large
    int dmgtype;// 0 normal 1 concussive 2 explosive
    long pLock;
    double range;
    double movespeed;
    double gas;
    boolean hasSplash;
    long plgiven;
    
    Character() {
	name = "filler";
	baseHp = Math.pow(2.0, 16.0)-1;
	curHp = baseHp;
	baseatk = 0;
	upatk = 0;
	numatks = 1;
	atkspd = 1;
	basearmr = 0;
	uparmr = 1;
	level = 0;
	healrate = 0;
	size = 0;
	dmgtype = 0;
	pLock = 0;
	range = 0;
	movespeed = 0;
	gas = 0;
	hasSplash = false;
	plgiven = 0;

    }

    Character(String cname, long cpLock, double cgas, double cbaseHp,
	      double cbaseatk, double cupatk, double cbasearmr, double catkspd,
	      boolean chasSplash, int csize, int cdmgtype, double crange,
	      double cmovespeed, double chealrate){
	this();
	name = cname;
	pLock = cpLock;
	gas = cgas;
	baseHp = cbaseHp;
	baseatk = cbaseatk;
	upatk = cupatk;
	basearmr = cbasearmr;
	atkspd = catkspd;
	hasSplash = chasSplash;
	size = csize;
	dmgtype = cdmgtype;
	range = crange;
	movespeed = cmovespeed;
	healrate = chealrate;
	
	
    }

    String getname() {
	return name;
    }

    double getbaseHp(){
	return baseHp;
    }
    
    double getcurHp(){
	return curHp;
    }

    double getnumatks(){
	return numatks;
    }

    double getatkspd(){
	return atkspd;
    }

    double getbasearmr(){
	return basearmr;
    }

    double getuparmr(){
	return uparmr;
    }
    
    double getsize(){
	return size;
    }

    double getrange(){
	return range;
    }

    double getmovespeed(){
	return movespeed;
    }
    
    double getgas(){
	return gas;
    }

    int getlevel(){
	return level;
    }

    double getpLock(){
	return pLock;
    }

    double getplgiven(){
	return plgiven;
    }

    boolean gethasSplash(){
	return hasSplash;
    }

    void setcurHp(double newHp){
	curHp = newHp;
    }

    void setlevel(int newlevel){
	level = newlevel;
    }

    void setplgiven( long newplgiven){
	plgiven = newplgiven;
    }

    boolean isalive(){
	if (curHp <= 0){
	    return false;
	}
	return true;
    }

    /*
      void setPL(long PL){
      if (PL< pLock){
      System.out.println(name); 
      System.out.println("Warning Pl lower than lock"); 
} what functionality does this really have if its workign
    */

    void heal(){
	setcurHp(getbaseHp());	
    }

    double hit(Character opponent, double atks){
	double dmg;
	double dmgmutiplier = 3.64;
	if(level < 0){
	    level = -3;
	}
	if(dmgtype == 0){
	    dmgmutiplier = 1.0;
	}
	if(dmgtype == 1){
	    if (opponent.getsize() == 0){
		dmgmutiplier = 1.0;
	    }
	    if (opponent.getsize() == 1){
		dmgmutiplier = 0.5;
	    }
	    if (opponent.getsize() == 2){
		dmgmutiplier = 0.25;
	    }
	}
	if(dmgtype == 2){
	    if (opponent.getsize() == 2){
		dmgmutiplier = 1.0;
	    }
	    if (opponent.getsize() == 1){
		dmgmutiplier = 0.75;
	    }
	    if (opponent.getsize() == 0){
		dmgmutiplier = 0.5;
	    }
	}
	dmg = Math.floor((baseatk + upatk *(3 + level) * 2  - opponent.getbasearmr() - opponent.getlevel() * opponent.getuparmr()) * 2.0 * dmgmutiplier * numatks) / 2.0;// 2/1 atk and def ups per level!
	if(dmg < 0.5){
	    dmg = 0.5;
	}
	return dmg;
    }

    void radialhit (Character opponent, double indexofhit){
	if(Math.random() > indexofhit){
	    hit(opponent, 1);
	}
    }



    Character myclone(){
	Character clone0 = new Character(name, pLock, gas, baseHp, baseatk, upatk, basearmr, atkspd, hasSplash, size, dmgtype, range, movespeed, healrate);
	clone0.heal();
	return clone0;
    }

    public String toString(){
	return name;
    }
}
