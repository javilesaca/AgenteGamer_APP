package com.miapp.agentegamer.domain.model;

public class GameInfo {

    private final String name;
    private final double rating;

    public GameInfo(String name, double rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() { return name; }
    public double getRating() { return rating; }
}
