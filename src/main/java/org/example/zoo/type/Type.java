package org.example.zoo.type;
import org.example.zoo.whatEat.Meal;

public abstract class Type {
    public String animalName;

    public void printType(String typeName){
        System.out.println(typeName);
    }

    public void eat(Meal meal){
        System.out.println(animalName + " ест " + meal.getMealName());
    }
}