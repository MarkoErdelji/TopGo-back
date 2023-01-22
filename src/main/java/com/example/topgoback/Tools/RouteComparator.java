package com.example.topgoback.Tools;

import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Routes.Model.Route;

import java.util.Comparator;

public class RouteComparator implements Comparator<Route> {
    @Override
    public int compare(Route r1, Route r2) {
        return (int) (r1.getLenght() - r2.getLenght());
    }
}