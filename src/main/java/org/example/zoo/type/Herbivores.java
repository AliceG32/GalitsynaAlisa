package org.example.zoo.type;

import org.example.zoo.whatEat.Grass;

public class Herbivores extends TypeAnimal {
    public String getTypeName() {
        return "Травоядные";
    }

    public void eat(Grass meal) {
        super.eat(meal);
    }
}
