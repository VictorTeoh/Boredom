import java.lang.Math;
public class RunMe{
    double distHeal = 10.0;

    public static double hitstokill(Character attacker, Character opponent, long level){
	attacker.setlevel(level);
	double totalhits = 0;
	while(opponent.isalive()){
	    attacker.hit(opponent, level);
	    totalhits++;
	}
	opponent.heal();
	return totalhits;
    }
    public static void main(String[] args){
	long level = 0;
	Character g1 = new Character("G", 0L, 0.0, 550.0, 20.0, 3.0, 1.0, 0.0, 0.924, false, 0, 0, 7.0, 2.976, 0.0);
        Character g2 = new Character("G", level, 0.0, 550.0, 20.0, 3.0, 1.0, 0.0, 0.924, false, 0, 0, 7.0, 2.976, 0.0);
	hitstokill(g1, g2, 12);
    }

    
}
