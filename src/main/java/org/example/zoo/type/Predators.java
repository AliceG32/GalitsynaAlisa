package org.example.zoo.type;

import org.example.zoo.whatEat.Meat;

public class Predators extends Type {
    public void printType() {
        super.printType("Хищники");
    }

    public void eat(Meat meal) {
        super.eat(meal);
    }
}
