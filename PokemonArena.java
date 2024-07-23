import java.util.*;
import java.io.*;

public class PokemonArena {
	private static ArrayList<PokemonData>pokemans;
	private static ArrayList<PokemonData>playerPokemans;
	private static ArrayList<PokemonData>enemyPokemans;
	private static String onField = ""; //Selects the name of pokemon
 	private static int index = -1; //Index in playerPokemans of player's pokemon on field
 	private static Boolean bigSkip = false;
 	private static Boolean bigEnemySkip = false;
	private static int score = 0; //Scoreboard 
	private static HealingItem healingItem = new HealingItem("Super Potion", 100);
	private static int healingItemUses = 0;
 	
  	public static void main(String[]args) {
    	pokemans = new ArrayList<PokemonData>(); //The list of pokemons
    	playerPokemans = new ArrayList<PokemonData>(); //Player's pokemon
    	enemyPokemans = new ArrayList<PokemonData>(); //Enemy's pokemon
    	
    	welcomeMessage();
    	readFile();
    	pokemonSelection();
    	enemyCreator();
    	
    	Collections.shuffle(enemyPokemans); //This will randomize the enemy's pokemon
    	
    	int round = 1; 
    	
    	if (firstTurn() == 1){ //If the trainer goes first
    		System.out.println("Player Starts");
    		while (validGame()){ 
    			if (round == 1){
    				choosePokes(); //Trainer chooses their pokemon
    			}//end if
				if (validGame()) {
    			chooseEnemy();
    			playerMove(); //Trainer makes their move
    			if (validGame() && bigEnemySkip==false && enemyPokemans.size()>0){  //If the game is still valid and the enemy doesn't skip their turn and there are still enemy pokemon
    				enemyMove(); //The enemy makes their move
    				if (enemyPokemans.size()>0) { //Check if there are still enemy pokemons before calculating damage
					calculateDamage(); //Calculate the damage from the enemy's move
					}
    			}//end if
    			bigEnemySkip = false; //Rest the enemy's skip turn flag
    			addEnergy(); //Add energy to the pokemon
    			round++; //Increment the round counter
				}
    		}//end while
    	}//end if
	
    	else{ //If the enemy goes first
    		System.out.println("Enemy Starts");
    		while (validGame()){
    			if (round == 1){
    				choosePokes();
    			}//end if
    			chooseEnemy();
    			enemyMove();
    			if (enemyPokemans.size()>0) {
					calculateDamage();
				}
    			if (validGame() && bigSkip ==false){
    				playerMove();
					if (enemyPokemans.size()>0) {
    				calculateDamage();
					}
    			}//end if
    			bigSkip = false;
    			addEnergy();
    			round++;
    			
    		}//end while
    	}//end else
    	
   		checkWinner(); //validGame doesn't tell you the result, it only tells you if the game is over
    
   }//end main
   public static void welcomeMessage(){
   		
   		try{
   			Scanner inFile = new Scanner((new BufferedReader(new FileReader("C:\\\\Users\\\\User\\\\Desktop\\\\OOP Project Final v0.3\\\\Pokemon Game\\\\src\\\\ASCII.txt"))));
   			while (inFile.hasNextLine()){ //printing ASCII from text file
   				System.out.println(inFile.nextLine());
   			}//end while
   		}//end try
   		
   		catch(IOException ex){
   			;
   		}
   		System.out.print("\n");	 
   		System.out.print("Hello Trainer! Welcome to the Pokemon Ga-ole! \n"+
   			"Your mission is to bring your team of 4 Pokemon and face off against\n"+
   			"a MASSIVE lineup of enemy Pokemons. Will you become Best Trainer?\n"+
   			"Test Your Luck in Pokemon Ga-ole! Get the highest score and become Trainer Supreme! \n");
   		System.out.println("Press Enter to Continue");
   		Scanner kb = new Scanner (System.in);
   		String nothing = kb.nextLine();
   		printDivider();
   }
   
   public static void printDivider(){
   		System.out.println("================================================"); 
   }
   public static void readFile(){
		String fileName = "C:\\Users\\User\\Desktop\\OOP Project Final v0.3\\Pokemon Game\\src\\pokemon.txt"; //This will read the "pokemon.txt" file
    	Scanner kb = new Scanner(System.in);
    	
    	try{
    		Scanner inFile = new Scanner(new File(fileName));
    		int n = inFile.nextInt();
    		inFile.nextLine(); //nextLine
    		for (int i = 0; i<n ; i++){
    			PokemonData temp = new PokemonData(inFile.nextLine(), i+1);
    			pokemans.add(temp); 
    		}//end for
    		
    	}//end try
    	catch (IOException ex){
    		System.out.println("Dude, did you misplace pokemon.txt?"); //Error will show when txt file are not placed correctly
    		
    	}//end catch
   }
   
   public static void pokemonSelection(){ //Allow the player to select their pokemon from the available list
   		Scanner kb = new Scanner(System.in);
   		printPokemonInfo();
   		while (true){
   			printDivider();
    		int choice = 0;
    		if(playerPokemans.size() < 4){ //Check if the player has selected less than 4 pokemons
    			System.out.println("Enter Pokemon #"+ (playerPokemans.size()+1)); //Prompt the player to select the next pokmeon
    			choice = kb.nextInt(); 
    		}//end if
    		else{ 
    			break; // If the player has already selected 4 pokemons, end the selection process

    		}//end else
    		if (choice < pokemans.size() && choice >= 0 && !(playerPokemans.contains(pokemans.get(choice)))){ //Check if the player's choice is a valid index in the pokemans list and the chosen pokemon is not already selected
    			playerPokemans.add(pokemans.get(choice)); // Add the chosen pokemon to the player's list
    			System.out.println("You selected "+pokemans.get(choice).getName()+
    				"| Type: "+pokemans.get(choice).getType().toUpperCase());
    		}//end if
    		else{
    			System.out.println("Invalid Input, Try Again");
    		}//end else
    	}//end while
   }//end pokemonSelection
   
   public static void enemyCreator(){ //Number of Enemies to face
	int maxEnemies = 3; // Maximum number of enemies
	int enemyCount = 0;

   		for (int i = 0; i < pokemans.size(); i++){ //Checks if that pokemon is not in the list. If it's not, add that pokemon to the enemy
    		if (!(playerPokemans.contains(pokemans.get(i)))){
    			enemyPokemans.add(pokemans.get(i));
				enemyCount++;

				if (enemyCount >= maxEnemies) {
					break; 
				}
    		}//end if
    		
    	}//end for
    	printDivider();
   }//end enemyCreator
   
   public static void printPlayerInfo(){
   		for (int i = 0; i < playerPokemans.size() ; i++){ //Loop through each pokemon in the player's list 
   			PokemonData temp = playerPokemans.get(i); //Get the current pokemon
   			System.out.println(i+". "+temp.getName()+"|DiskCode: "+temp.getDiskCode()+"| Type: "+temp.getType().toUpperCase()+
   				"| Health: "+ temp.getHealth()+"| Energy: "+temp.getEnergy());
   		}//end for 
   }//end printPlayerInfo
   
   public static void printPokemonInfo(){ //Used to print all available pokemon for selection
   		for (int i = 0; i < pokemans.size() ; i++){ //Loop through each pokemon in the 'pokemons' list 
   			PokemonData temp = pokemans.get(i);
   			System.out.println(i+". "+temp.getName()+ " |DiskCode: "+temp.getDiskCode()+"| Type: "+temp.getType().toUpperCase()+
   				"| Health: "+ temp.getHealth()+"| Energy: "+temp.getEnergy());
   		}//end for
   }
   
   public static int firstTurn(){ //Randomize who starts first
   		Random rand = new Random();
   		int n = rand.nextInt(2)+1;
   		return n; //returns either 1 or 2
   }//end firstTurn
   
   public static Boolean validGame(){
   		if (playerPokemans.size() == 0){ //Check if all trainer's pokemon have fainted
   			return false;
   		}//end if
   		if (enemyPokemans.size() == 0){ //Check if all enemy's pokemon have fainted
   			return false;
   		}//end if
   		return true; //If neither the player's nor the enemy's pokemon have all fainted, the game is still valid
   }//end validGame
   
   public static void choosePokes(){
   		//Trainer selecting which pokemon to use for this. 
   		System.out.println("You have "+playerPokemans.size()+" Pokemon left");
   		printPlayerInfo();
   		System.out.println("Which Pokemon would you like to use?");
   		
   		while (true){
   			Scanner kb = new Scanner(System.in); //Scanner object to read player's choice
   			int n = kb.nextInt();
   			if (n>=0 && n < playerPokemans.size()){ //Check if the player's choice is a valid indext in the playerPokemons list
   				onField = playerPokemans.get(n).getName();
   				index = n;
   				System.out.println(onField+", I choose you!");
   				break; //Break out of the loop as the player has made a valid selection
   			}//end if
   			else{
   				System.out.println("Invalid Input");
   			}//end else
   			
   			printDivider(); //Print a divider for better readability
   		}//end while
   		
   }//end choosePokes
   
   public static void chooseEnemy(){
   		System.out.println("You are battling "+enemyPokemans.get(0).getName()+
   			"| Type: "+enemyPokemans.get(0).getType().toUpperCase()); 
   		printDivider();
   }//end chooseEnemy
   
   public static void playerMove(){
   		Scanner kb = new Scanner(System.in);
   		System.out.println(onField+" is currently on the field. What will Trainer do?");
   		System.out.println("1: Attack");
   		System.out.println("2: Retreat/Switch");
   		System.out.println("3: Pass");
		System.out.println("4: Catch Pokemon");
		System.out.println("5: Use Item");
   		int choice = 0;
   		while (true){
   			choice = kb.nextInt();
   			if (choice == 1 && playerPokemans.get(index).getStun()==true){ //Check if the trainer chose to attack but their pokemon is stunned
   				System.out.println("Invalid, this pokemon is stunned");
   			}//end if
   			else if (choice > 0 && choice <=5){ //Check if the player's choice is a valid option (1,2 or 3)
   				break; //If it is, break out of the loop
   			}//end else if 
   			else{ 
   				System.out.println("Invalid Input.");
   			}//end else
   		}//end while
   		printDivider();
   		if (choice == 1){ //Handle the player's choice 
   			choseAttack(); //If the player chose to attack
   		}//end if
   		else if (choice == 2){
   			choseSwitch(); //If the player chose to retreat/switch 
   		}//end else if
   		else if (choice == 3){
   			chosePass(); //If the player chose to pass
   		}//end else if
		else if (choice == 4){
			catchPokemon(); //If the player chose to catch the pokemon
		}
		else if (choice == 5) {
			useHealingItem();
		}
   		
   }//end playerMoves
   
   public static void choseAttack(){
   		Scanner kb = new Scanner (System.in);
   		for (int i = 0; i < playerPokemans.size(); i++){
   			if(playerPokemans.get(i).getName() == onField){
   				index = i; //Store the index of the pokemon on the field
   			}//end if
   		}//end for
   		System.out.println("Trainer wants to Attack! Your options are:");
   		
   		for (int i = 0; i < playerPokemans.get(index).getNumAttacks(); i++){ //Loop through the attacks of pokemon on the field
   			String []temp = playerPokemans.get(index).getMove(i);
   			String special;
   			if (temp[3].equals(" ")){ //Check if attack has a special effect
   				special = ("No special");
   			}//end if
   			else{
   				special = temp[3];
   			}//end else
   			System.out.println(i+": "+temp[0]+"| Special: "+special); //Print attack info
   		}//end for
   		System.out.println(playerPokemans.get(index).getName()+" has "+playerPokemans.get(index).getEnergy()+" energy left."); //Inform the trainer about energy left for the pokemon on the field
   		while (true){
   			int attackChoice = kb.nextInt();
   			if (attackChoice < 0 || attackChoice > playerPokemans.get(index).getNumAttacks()){ //Check if the player's choice is a valid index in the attack list
   				System.out.println("Invalid Input");
   			}
   			else if (playerPokemans.get(index).getEnergy() < playerPokemans.get(index).getMoveCost(attackChoice)){ //Check if the pokemon on the field has enough energy to perform the chosen attack
   				System.out.println("Not enough energy.");
   				printDivider();
   				playerMove(); //Ask the player to make another move
   			}//end if
   			else{
   				System.out.println(playerPokemans.get(index).getName()+" used " //If the trainer's choice is valid and the pokemon has enough energy, perform the attack
   					+playerPokemans.get(index).getMoveName(attackChoice)+"!");
   				int temp = playerPokemans.get(index).getEnergy(); //Subtract the cost of the attack from the pokemon's energy
   				playerPokemans.get(index).setEnergy(temp - playerPokemans.get(index).getMoveCost(attackChoice));
   				useAttack(playerPokemans.get(index).getMove(attackChoice),"enemy"); //Use the chosen attack against the enemy
   				break; //Break out of the loop as the player has made a valid selection
   			}//end else
   		}//end while
   		printDivider();	
   }//end choseAttack
   
   public static void useAttack(String[]move,String target){ //This method is used to handle an attack
   		Boolean wildCard = false;   // Initialize flags that indicate to the program that it need to skip the else at the bottom
	   	Boolean wildStorm = false;
	   	Boolean skip = false; 
	   	Boolean enemyWildCard = false; 
   		Boolean enemyWildStorm = false;
   		Boolean enemySkip = false;
   		if (target == "enemy" && playerPokemans.get(index).getStun() == false){ // If the target is the enemy and the player's Pokemon is not stunned

	   		String special = move[3]; //Ge the special effect of the move
	   		if (!(special.equals(" "))){ //if there is a special effect
	   			Random rand = new Random();
	   			int rando = rand.nextInt(2)+0; //Generate a random number either 1 or 0
	   			if (rando == 1 && special.equals("stun")){ //Stuns target
	   				System.out.println(playerPokemans.get(index).getName()+" landed a stun!");
	   				enemyPokemans.get(0).setStun(true);
	   			}//end if
	   			else if (rando == 1 && special.equals("wild card")){ //Attack fails
	   				System.out.println("Wild Card! Attack Failed");
	   				wildCard = true;
	   			}//end if
	   			else if (rando == 1 && special.equals("wild storm")){ //Wild storm can go on forever
	   				wildStorm = true;
	   				skip = true;
	   				rawPlayerAttack(move);
	   				while (wildStorm){ 
	   					Random randInt = new Random();
	   					int nextRand = randInt.nextInt(2)+0;
	   					if(nextRand == 1){
	   						System.out.println("Wild Storm!");
	   						rawPlayerAttack(move); //Sending raw attacks
	   					}//end if
	   					else{
	   						System.out.println("Wild Storm Over!");
	   						wildStorm = false;
	   					}//end else
	   				}//end while wildStorm
	   			}//end else if
	   			
	   			else if (rando == 1 && special.equals("disable")){
	   				System.out.println(playerPokemans.get(index).getName()+" landed a DISABLE!");
	   				enemyPokemans.get(0).setDisable();
	   			}//end else if 
	   			
	   			else if (rando == 1 && special.equals("recharge")){
	   				System.out.println(playerPokemans.get(index).getName()+ "Got an Energy Recharge!");
	   				int currEnergy = playerPokemans.get(index).getEnergy();
	   				if (currEnergy < 30){
	   					playerPokemans.get(index).setEnergy(currEnergy+20);
	   				}//end if
	   				else{
	   					playerPokemans.get(index).setEnergy(50);
	   				}//end else1
	   			}//end else if
	   		}//end specials
   		}//end if
   		
   		else if (target == "player" && enemyPokemans.get(0).getStun() == false){ 

   			String special = move[3];
	   		if (!(special.equals(" "))){ //if there is a special
	   			Random rand = new Random();
	   			int rando = rand.nextInt(2)+0; //random number either 1 or 0
	   			if (rando == 1 && special.equals("stun")){ //stuns target
	   				System.out.println(enemyPokemans.get(0).getName()+" landed a stun!");
	   				playerPokemans.get(index).setStun(true); //changes the variable
	   			}//end if
	   			else if (rando == 1 && special.equals("wild card")){ //50/50 chance that the attack fails
	   				System.out.println("Wild Card! Attack Failed");
	   				enemyWildCard = true;
	   			}//end if
	   			else if (rando == 1 && special.equals("wild storm")){
	   				enemyWildStorm = true;
	   				enemySkip = true; //passes over the else at the bottom
	   				rawEnemyAttack(move);
	   				while (enemyWildStorm){
	   					Random randInt = new Random();
	   					int nextRand = randInt.nextInt(2)+0;
	   					if(nextRand ==1){ //wild storm can go on forever
	   						System.out.println("Enemy Wild Storm!");
	   						rawEnemyAttack(move); //keeps attacking
	   					}//end if
	   					else{
	   						System.out.println("Enemy Wild Storm Over!");
	   						enemyWildStorm = false;
	   					}//end else
	   				}//end while wildStorm
	   			}//end else if
	   			
	   			else if (rando == 1 && special.equals("disable")){
	   				System.out.println(enemyPokemans.get(0).getName()+" landed a DISABLE!");
	   				playerPokemans.get(index).setDisable(); //disable is a flag in the Pokemon file
	   			}//end else if 
	   			
	   			else if (rando == 1 && special.equals("recharge")){
	   				int currEnergy = enemyPokemans.get(0).getEnergy();
	   				System.out.println(enemyPokemans.get(0).getName()+ "Got an Energy Recharge!");
	   				if (currEnergy < 30){ //ensuring that the energy doesn't pass the max energy
	   					enemyPokemans.get(0).setEnergy(currEnergy+20);
	   				}//end if
	   				else{
	   					enemyPokemans.get(0).setEnergy(50);
	   				}//end else
	   			}//end else if
	   		}//end specials
   		}
   		if (target == "enemy" && wildCard == false && skip == false){  

   			rawPlayerAttack(move); //just the raw attack, no specials
   		}//end enemy target
   		else if (target == "player" && enemyWildCard == false && enemySkip == false){
   			rawEnemyAttack(move); //just the raw attack, no specials
   		}// end Player target
   		wildCard = false; //Reset the flags
   		skip = false;
   		enemyWildCard = false;
   		enemySkip = false;
   		printDivider();
   }//end useAttack
   
   public static void calculateDamage(){
   		if (playerPokemans.get(index).getHealth() <= 0){ //Print the message that the pokemon has fainted
   			System.out.println(playerPokemans.get(index).getName()+" has fainted! Who will replace them?");
   			playerPokemans.remove(index); //Remove the fainted pokemon from the trainer's list
   			for (int i = 0; i < playerPokemans.size(); i++){ //List the remaining pokemon for the trainer to choose from
   				System.out.println(i+". "+playerPokemans.get(i).getName());
   			}//end for
   			bigSkip = true; //Set the flag to skip the trainer's turn
   			if (validGame()){ //Check if the game stil valid.
   				while (true){ //Ask the trainer to choose a pokemon untill a valid choice is made
   					Scanner kb = new Scanner(System.in);
   					System.out.println("Enter your choice:");
   					int temp = kb.nextInt();
   					if (temp >= 0 && temp < playerPokemans.size()){ // Check if the trainer's choice is a valid index in the player's Pokemon list

   						index = temp; // Set the chosen Pokemon as the current Pokemon on the field
   						onField = playerPokemans.get(index).getName();
   						System.out.println("Trainer Selected "+onField+" |Type :" +playerPokemans.get(index).getType().toUpperCase());
   						break;
   					}//end if
   					else{
   						System.out.println("Invalid Input");
   					}//end else
   				
   				}//end while
   			}
   			
   			
   		}//end if
   		
   		else{  // If the trainer's Pokemon has not fainted, print its remaining health
   			System.out.println(playerPokemans.get(index).getName()+" has "+playerPokemans.get(index).getHealth()+" health left");
   		}//end else
   		
   		if (enemyPokemans.get(0).getHealth() <= 0){ // Check if the enemy's Pokemon has fainted

   			bigEnemySkip = true;  // Set the flag to skip the enemy's turn
   			System.out.println(enemyPokemans.get(0).getName()+" has fainted! Your Pokemon receive a health bonus!");
   			enemyPokemans.remove(0); //Removes enemy from list, next in line becomes element 0
   			if (validGame()){
   				System.out.println(enemyPokemans.get(0).getName()+ " is entering the field!");
   			}
   			for (int i = 0; i < playerPokemans.size(); i++){ 
   				int currHealth = playerPokemans.get(i).getHealth(); //Ensure that health doesn't exceed limit
   				playerPokemans.get(i).setEnergy(50); //playerPokemans all get full energy
   				if(currHealth > playerPokemans.get(i).getMaxHealth()-20){ 
   					playerPokemans.get(i).setHealth(playerPokemans.get(i).getMaxHealth()); //sets to maxhealth
   				}//end if
   				else{
   					playerPokemans.get(i).setHealth(currHealth+20); //Increase the pokemon's health by 20
   				}//end else
   			}//end for
			score++;
			System.out.println("Your score is now " + score); //Display the updated score
   		}//end if
   		else{
   			System.out.println(enemyPokemans.get(0).getName()+" has "+enemyPokemans.get(0).getHealth()+" health left");
   		}//end else
   		printDivider();
   }//checkHealth
   
   public static void choseSwitch(){
   		Scanner kb = new Scanner(System.in);
   		System.out.println("Who would you like to switch in?");
   		printPlayerInfo(); //Display player info
   		int switched = 0; //Switch selection
   		while (true){
   			switched = kb.nextInt();
   			if (switched >= 0 && switched < playerPokemans.size()){ //safety net
   				break;
   			}//end if
   			else{
   				System.out.println("Invalid Input.");
   			}//end else
   		}//end while true
   		onField = playerPokemans.get(switched).getName();
   		index = switched; //Switching the index of the trainer pokemon that's onField
   		printDivider();
   }//end choseSwitch
   
   public static void chosePass(){
   		System.out.println("Trainer Passed!");
   		playerPokemans.get(index).setStun(false);
   		printDivider();
   }//end pass   	
   
   public static void enemyPass(){
   		System.out.println(enemyPokemans.get(0).getName()+" Passed!");
   		enemyPokemans.get(0).setStun(false);
   		printDivider();
   }//end enemyPass
   
   public static void enemyMove(){
   		int enemyEnergy = enemyPokemans.get(0).getEnergy(); //Current energy
   		int AttacksIndex = enemyPokemans.get(0).getNumAttacks(); //Number of attacks
   		Random rand = new Random();
   		int n = rand.nextInt(AttacksIndex)+0; //Random selection of moves
   		if (enemyPokemans.get(0).getMoveCost(n) > enemyEnergy){ //If the move is too expensive
   			if (enemyPokemans.get(0).getNumAttacks() > 1){ //If there is an alternative to the move
   				if (enemyPokemans.get(0).getMoveCost(0) <= enemyEnergy){ //If the enemy can use the alternative
   					System.out.println(enemyPokemans.get(0).getName()+" used "+enemyPokemans.get(0).getMoveName(0)+"!");
   					useAttack(enemyPokemans.get(0).getMove(0),"player"); 
   				}//end if
   				else if (enemyPokemans.get(0).getMoveCost(1) <= enemyEnergy){ //If the other attack can be used
   					System.out.println(enemyPokemans.get(0).getName()+" used "+enemyPokemans.get(0).getMoveName(1)+"!");
   					useAttack(enemyPokemans.get(0).getMove(1),"player");
   				}//end else if
   				else{ //Pass due to not enough energy
   					enemyPass();
   				}//end else
   			}//end if
   			
   			else{ //Pass due to no other alternatives
   				enemyPass();
   			}//end else
   		}//end if
   		else{ //See the initially selected attack from the random.
   			System.out.println(enemyPokemans.get(0).getName() + " used "+ enemyPokemans.get(0).getMoveName(n)+"!");
   			useAttack(enemyPokemans.get(0).getMove(n),"player"); 
   		}//end else		
   }//end enemyMove

   public static void catchPokemon() {
		Random rand = new Random();
		int catchChance = rand.nextInt(100); //Random number between 0 and 99
		if (enemyPokemans.get(0).getHealth() <= enemyPokemans.get(0).getMaxHealth()*0.25) {
			catchChance -=25; //Increase the chance of catching the pokemon by 25%
		}

		if (catchChance < 50) { //50% chance to catch pokemon (75% if its health is below 25%)
			playerPokemans.add(enemyPokemans.get(0)); //Add the enemy pokemon to the player's list
			enemyPokemans.remove(0); //Remove the caught pokemon from the enemy's list
			System.out.println("You caught the enemy Pokemon!");
			score += 5; //Increase the score by 5
		}//end if
		else {
			System.out.println("The Pokemon escaped!");
		} //end else
		printDivider();
   } //end catchPokemon

   	public static void useHealingItem() { 
		if (healingItemUses >=2){ //Check if the healing item has been used 2 or more times
		System.out.println("All items have been used"); 
		} //end if 
		else {
		System.out.println("You used " + healingItem.getName() + "!");
		healingItem.use(playerPokemans.get(index));
		healingItemUses++; //Increment the counter tracking the number times the healing item has been used
		} //end else
   	}
   
   public static void rawPlayerAttack(String[]move){
   		int damage = 0;
   		if (enemyPokemans.get(0).getResistance() == playerPokemans.get(index).getType()){ //Check resistance,damage is reduced
   				damage = Integer.parseInt(move[2])/2;
   				System.out.println("It's not very effective...");
   		}//end if 
   		else if (enemyPokemans.get(0).getWeakness() == playerPokemans.get(index).getType()){ //Check weakness, damage is increased
   			damage = Integer.parseInt(move[2])*2;
   			System.out.println("It's Super Effective!");
   		}//end else if
   		else{ //regular attack
   			damage  = Integer.parseInt(move[2]);
   			System.out.println("Hit!"); 
   		}
   		if (playerPokemans.get(index).getDisable() == true){ 
            //removing damage for disabled pokemon
   			if (damage > 10){
   				damage -= 10;
   			}
   			else{
   				damage = 0;
   			}
   		}
   		System.out.println("The attack did "+damage+" damage");
   		int currHp = enemyPokemans.get(0).getHealth(); //It will ingore number below zero,since the calculatedamage method will check
   		enemyPokemans.get(0).setHealth(currHp - damage); 
   }//end rawPlayerAttack
   
   public static void rawEnemyAttack(String[]move){
   		int damage = 0; //Always starts an attack at 0
   		if (playerPokemans.get(index).getResistance() == enemyPokemans.get(0).getType()){ //if resistance
   			damage = Integer.parseInt(move[2])/2;
   			System.out.println("It's not very effective...");
   		}//end if
   		else if (playerPokemans.get(index).getWeakness() == enemyPokemans.get(0).getType()){ //if weakness
   			damage = Integer.parseInt(move[2])*2;
   			System.out.println("It's Super Effective!");
   		}//end else if
   		else{ //Regular attack
   			damage = Integer.parseInt(move[2]);
   			System.out.println("Hit!"); 
   		}//end else
   		if (enemyPokemans.get(0).getDisable() == true){
   			if (damage > 10){ //Damage lowered for disabled pokemon
   				damage -= 10;
   			}//end if
   			else{
   				damage = 0;
   			}//end else
  			
   		}//end if
   		System.out.println("The attack did "+damage+" damage");
   		int currHp = playerPokemans.get(index).getHealth(); 
   		playerPokemans.get(index).setHealth(currHp-damage); //It will ignore negative number, the calculate damage method will handle
   }//end rawEnemyAttack
   
   public static void addEnergy(){
   		for (int i = 0; i < playerPokemans.size(); i++){
   			int temp = playerPokemans.get(i).getEnergy(); //To ensure the max amount isn't surpassed
   			playerPokemans.get(i).setStun(false); //Reset stuns at the end of round
   			if (temp > 40){
   				playerPokemans.get(i).setEnergy(50); //Add energy
   			}//end if
   			else{
   				playerPokemans.get(i).setEnergy(temp+10); //Add energy
   			}//end else
   		}//end for
   		
   		for (int i = 0; i < enemyPokemans.size(); i++){
   			int current = enemyPokemans.get(i).getEnergy(); //Ensure the max amount isn't surpassed
   			enemyPokemans.get(i).setStun(false); //Reset stuns at the end of the round
   			if (current > 40){
   				enemyPokemans.get(i).setEnergy(50); //Add energy
   			}//end if
   			else{
   				enemyPokemans.get(i).setEnergy(current + 10); //Add energy
   			}//end else
   		}//end for
   		
   		
   }//end addEnergy
   
   public static void checkWinner(){
   		if (enemyPokemans.size() == 0){ //All enemies have defeated
   			System.out.println("CONGRATULATIONS YOU WIN! We have a new Trainer Supreme!");
			System.out.println("Your final score is: " + score); // Display the final score

   		}//END IF
   		else{ //All player have defeated
   			System.out.println("Sadly, you've lost. Try again?");
			System.out.println("Your final score is: " + score); // Display the final score
   		}//end else

		try {
			ScoreManager.addScore(score);
        	ScoreManager.displayScores(); // Display the top scores
		}	catch (IOException e) {
			System.out.println("Error updating or displaying scores: " + e.getMessage());
   		}//end checkWinner
	}	
}//end PokemonArena