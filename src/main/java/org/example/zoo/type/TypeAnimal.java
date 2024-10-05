package org.example.zoo.type;
import org.example.zoo.whatEat.Meal;

public abstract class TypeAnimal {
    public String animalName;

    public abstract String getTypeName();

    public void printType(){
        System.out.println(getTypeName());
    }

    public void eat(Meal meal){
        System.out.println(animalName + " ест " + meal.getMealName());
    }
}