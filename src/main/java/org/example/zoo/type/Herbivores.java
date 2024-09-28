package org.example.zoo.type;

import org.example.zoo.whatEat.Grass;

public class Herbivores extends Type{
    public void printType() {
        super.printType("Травоядные");
    }

    public void eat(Grass meal) {
        super.eat(meal);
    }
}
