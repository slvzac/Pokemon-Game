// The move class represents an attack move in the game
public class Move {
	
	private String attackName; //The name of the attack move
	private int energyCost; //The amount of energy required to perform the attack
	private int damage; //The amount of damage the attack does to an opponent
	private String special; //Any special effects of the attack
	private String[]attack = new String[4]; //An array that stores the above four attributes as strings
	
    //Constructor
    public Move(String[] tempArray,int i) { //Each Attack contains 4 datas
    	int numAttacks = Integer.parseInt(tempArray[5]); //The number of attacks the pokemon can perform
    	attackName = tempArray[6+i]; //The anme of the attack
    	energyCost = Integer.parseInt(tempArray[7+i]); //The energy cost of the attack
    	damage = Integer.parseInt(tempArray[8+i]); //The damage of the attack
    	special = tempArray[9+i]; //The spcial effect of the attack
    	attack[0] = attackName; //Store the attack detials in the attack array
    	attack[1] = ""+energyCost;
    	attack[2] = ""+damage;
    	attack[3] = special;
    	
    }
    //Getters and Setters
    public String[]getAttack(){
    	return attack;
    }
    
    public String getAttackName(){
    	return attackName;
    }
    
    public int getAttackCost(){
    	return energyCost;
    }
  
}
