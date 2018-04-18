package com.codigohacks.metroapp;

/**
 * Created by k_y on 08/04/18.
 */

public class Route_items {

    private String interchanges;
    private String route;

    public Route_items(String interchanges, String route) {

        this.interchanges = interchanges;
        this.route = route;

    }

    public String getInterchanges() {
        return interchanges;
    }

    public String getRoute() {
        return route;
    }
}
