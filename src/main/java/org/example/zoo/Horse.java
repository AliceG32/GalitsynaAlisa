package org.example.zoo;

import org.example.zoo.type.Herbivores;
import org.example.zoo.whereLive.Terrestrial;

public class Horse extends Herbivores implements Terrestrial {
    public Horse() {
        animalName = "Лощадь";
    }

    @Override
    public void walk() {
        System.out.println(animalName + " ходит");
    }
}