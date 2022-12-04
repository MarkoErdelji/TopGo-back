package com.example.topgoback.Routes.Service;

import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Routes.Repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {
    @Autowired
    RouteRepository routeRepository ;

    public void addOne(Route route)
    {
        routeRepository.save(route);
    }

}
