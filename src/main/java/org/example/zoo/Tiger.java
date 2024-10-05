package org.example.zoo;

import org.example.zoo.type.Predators;
import org.example.zoo.whatEat.Beef;
import org.example.zoo.whereLive.Terrestrial;

public class Tiger extends Predators implements Terrestrial {
    public Tiger() {
        animalName = "Тигр";
    }

    @Override
    public void walk() {
        System.out.println(animalName + " ходит");
    }

    public void eat(Beef meal) {
        super.eat(meal);
    }
}
