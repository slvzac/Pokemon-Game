public abstract class Item {
    protected String name; //The name of the item

    
    public Item(String name) {// The constructor for the Item class, which takes a name as a parameter.
        this.name = name;
    }

    public String getName() {// A getter method for the name of the item.
        return name;
    }

    public void use(PokemonData pokemon) {
        // This method will be overridden in subclasses
        // In this case, we can treat any specific item (like a HealingItem) as a general Item,
        // but when we call the use method, the specific use method for that type of item will be called.
    }
}