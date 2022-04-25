package com.moviedb_api.ratings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {
    public FilterConfiguration(ObjectMapper objectMapper) {

        System.out.println("FilterConfiguration");
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
        //simpleFilterProvider.addFilter("ratingFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
        //simpleFilterProvider.addFilter("movieFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "title", "background", "poster", "year", "runtime, rated"));
        objectMapper.setFilterProvider(simpleFilterProvider);
        objectMapper.addMixIn(Object.class, Rating.class);
    }
}
