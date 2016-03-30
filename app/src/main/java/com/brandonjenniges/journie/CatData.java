package com.brandonjenniges.journie;

import java.util.ArrayList;

public class CatData {

    public static String[] cats = {"Focused", "Tricky", "Gaming", "Helping", "Hopeful", "Lazy", "Lonely", "Friendly",  "Pretty", "Shocking", "Scary", "Stretchy", "Thirsty", "Bored", "Tired", "Hungry", "Cartoon", "Working"};

    public static ArrayList<Cat> catList() {
        ArrayList<Cat> list = new ArrayList<>();
        for (String c : cats) {
            Cat cat = new Cat();
            cat.setName(c);
            cat.setImageName(c.replaceAll("\\s+", "").toLowerCase());
            list.add(cat);
        }
        return (list);
    }
}
