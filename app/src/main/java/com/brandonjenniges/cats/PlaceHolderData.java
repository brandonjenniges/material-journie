package com.brandonjenniges.cats;

import java.util.ArrayList;

public class PlaceHolderData {

    public static String[] cats = {"Max", "Chloe", "Simba", "Charlie", "Milo", "Lucy", "Kitty", "Daisy"};

    public static ArrayList<Cat> placeList() {
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
