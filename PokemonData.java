/* Pokemon Data 
 * Pokemon class, define the data for pokemon
*/

import java.util.*;
import java.io.*;

public class PokemonData {
    public static final String [] ELEMENT_TYPES = {"Earth","Fire","Grass","Water","Fighting","Electric"};

    private String DiskCode;
    private String name;
    public char Grade;
    private Boolean stun = false;
    private Boolean disable = false;
    private int Hp;
    private int maxHp;
    private int energy = 50;
    public String type;
    private String resistance;
    private String weakness;
    private int numAttacks;
    public ArrayList<Move>moves;
    
    //Constructor
    public PokemonData(String line, int DiskCode){
    this.DiskCode = String.format("%04d", DiskCode);
    String[]statsArray = line.split(","); // This will split the line based on commas
    name = statsArray[0]; //Handing the appropriate values to the appropriate variables
    Hp = Integer.parseInt(statsArray[1]);
    maxHp = Hp;
    type = statsArray[2];
    resistance = statsArray [3];
    weakness =  statsArray[4];
    numAttacks = Integer.parseInt(statsArray[5]);

    moves = new ArrayList<Move>(); //Start an ArrayList of Moves
        for ( int i = 0; i < numAttacks; i++) {
        Move temp = new Move(statsArray, i*4); //Adding the move to the arraylist
        moves.add(temp);
        }
    }

    //Getters and Setters
    public String getDiskCode(){
        return DiskCode;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public char getGrade() {
        return Grade;
    }

    public int getHealth() {
        return Hp;
    }

    public int getMaxHealth() {
        return maxHp;
    }

    public void setHealth(int x) {
        Hp = x;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int x){
        energy = x;
    }

    public String getResistance() {
        return resistance;
    }

    public String getWeakness() {
        return weakness;
    }

    public String[] getMove(int index) {
        return (moves.get(index)).getAttack();
    }

    public String getMoveName(int index) {
        return(moves.get(index)).getAttackName();
    }

    public int getMoveCost(int index){
    	return (moves.get(index)).getAttackCost();
    }//end getMoveCost
    
    public int getNumAttacks(){
    	return numAttacks;
    }//end getNumAttacks
    
    public void setStun(Boolean x){
    	stun = x;
    }//end setStun
    
    public void setDisable(){
    	disable = true;
    }//end setDisable
    
    public Boolean getStun(){
    	return stun;
    }//end getStun
    
    public Boolean getDisable(){
    	return disable;
    }
}




