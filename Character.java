
public class Character {
    
    String name;
    double baseHp;
    double curHp;
    double baseatk;
    double upatk;
    double numatks;
    double atkspd;//in secs per hit
    double basearmr;
    double uparmr;
    long level;
    double healrate;
    int size;//0 small 1 medium 2 large
    int dmgtype;// 0 normal 1 concussive 2 explosive
    long pLock;
    double range;
    double movespeed;
    double gas;
    boolean hasSplash;
    
    Character() {
	name = "filler";
	baseHp = Math.pow(2.0, 16.0)-1;
	curHp = baseHp;
	baseatk = 0;
	upatk = 0;
	numatks = 1;
	atkspd = 1;
	basearmr = 0;
	uparmr = 0;
	level = 0;
	healrate = 0;
	size = 0;
	dmgtype = 0;
	pLock = 0;
	range = 0;
	movespeed = 0;
	gas = 0;
	hasSplash = false;

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

    double getatkspd(){
	return atkspd;
    }

    double getbasearmr(){
	return basearmr;
    }

    double getsize(){
	return size;
    }

    double getrange(){
	return range;
    }

    double getgas(){
	return gas;
    }

    double getlevel(){
	return level;
    }

    void setcurHp(double newHp){
	curHp = newHp;
    }

    void setlevel(long newlevel){
	level = newlevel;
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
}
    */

    void heal(){
	setcurHp(getbaseHp());	
    }

    double hit(Character opponent, double atks){
	double dmg;
	double dmgmutiplier = 3.64;
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
	dmg = Math.floor((baseatk + upatk*level - opponent.getbasearmr()) * 2.0
			 * dmgmutiplier) / 2.0;
	opponent.setcurHp(opponent.getcurHp()-dmg);
	return dmg;
    }

    void radialhit (Character opponent, double indexofhit){
	if(Math.random() > indexofhit){
	    hit(opponent, 1);
	}
    }



    Character clone(){
	Character clone = new Character(name, pLock, gas, baseHp, baseatk, upatk, basearmr, atkspd, size, dmgtype, range, movespeed, healrate);
	return clone;
    }
}
