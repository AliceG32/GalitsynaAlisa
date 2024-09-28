package org.example.zoo;


import org.example.zoo.whatEat.Grass;

public class Main {
    public static void main(String[] args) {
        Horse horse = new Horse();
        horse.walk();

        Grass grass = new Grass();
        horse.eat(grass);

        Eagle eagle = new Eagle();
        eagle.fly();
    }
}
