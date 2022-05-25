package com.moviedb_api.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Elastic BeanStalk’s load balancer uses path “/” by default for health checks.
If you don’t have that path defined in your controller, your application will keep
failing health checks and will show Severe as status in dashboard.
You can either have “/” endpoint in your rest controller or edit the load balancer setting later to use different path instead
 */
@RestController
public class HealthController {

    @GetMapping("/")
    public String health() {
        return "Welcome to MovieDB API";
    }
}