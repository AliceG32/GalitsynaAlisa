package org.example.zoo.type;

import org.example.zoo.whatEat.Meat;

public abstract class Predators extends TypeAnimal {


    public String getTypeName() {
        return "Хищники";
    }

    public void eat(Meat meal) {
        super.eat(meal);
    }
}
