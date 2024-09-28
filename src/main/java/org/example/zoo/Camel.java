package org.example.zoo;

import org.example.zoo.type.Herbivores;
import org.example.zoo.whereLive.Terrestrial;

public class Camel extends Herbivores implements Terrestrial {
    public Camel(){
        animalName = "Верблюд";
    }

    @Override
    public void walk() {
        System.out.println(animalName + " ходит");
    }
}
