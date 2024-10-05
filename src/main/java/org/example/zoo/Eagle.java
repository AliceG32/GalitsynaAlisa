package org.example.zoo;

import org.example.zoo.type.Predators;
import org.example.zoo.whereLive.Flying;

public class Eagle extends Predators implements Flying {
    public Eagle(){
        animalName = "Орел";
    }

    @Override
    public void fly() {
        System.out.println(animalName + " летает");
    }
}
