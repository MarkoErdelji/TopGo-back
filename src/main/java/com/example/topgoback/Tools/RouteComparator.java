package com.example.topgoback.Tools;

import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Routes.Model.Route;
import org.springframework.stereotype.Component;

import java.util.Comparator;
@Component

public class RouteComparator implements Comparator<Route> {
    @Override
    public int compare(Route r1, Route r2) {
        return (int) (r2.getLenght() - r1.getLenght());
    }
}