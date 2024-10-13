package org.example.zoo;

import org.example.zoo.type.Predators;
import org.example.zoo.whatEat.Fish;
import org.example.zoo.whereLive.Waterfowl;

public class Dolphin extends Predators implements Waterfowl {
    public Dolphin(){
        animalName = "Дельфин";
    }

    public void eat(Fish meal) {
        super.eat(meal);
    }

    @Override
    public void swim() {
        System.out.println(animalName + " плавает");
    }
}
