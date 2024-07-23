public class HealingItem extends Item{
    private int healingPower; //Amount of health the item restores

    public HealingItem(String name, int healingPower) { //Constructor for the HealingItem Class
        super(name);
        this.healingPower = healingPower;
    }

    public String getName() { //Method to get the name of healing item
        return name;
    }

    @Override
    public void use(PokemonData pokemon) {
        int newHealth = pokemon.getHealth() + healingPower; //Calculate the new health of the Pokemon after using the item
        if (newHealth > pokemon.getMaxHealth()) { // If the new health exceeds the Pokemon's max health, set it to the max health
            newHealth = pokemon.getMaxHealth();
        }
        pokemon.setHealth(newHealth);// Set the Pokemon's health to the new health
        System.out.println(pokemon.getName() + " has been healed by " + healingPower + " points!"); // Print a message stating that the Pokemon has been healed
    }
}
