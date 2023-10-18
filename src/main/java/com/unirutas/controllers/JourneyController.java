package com.unirutas.controllers;

import com.unirutas.models.Coordinate;
import com.unirutas.models.Journey;
import com.unirutas.models.Section;
import com.unirutas.models.Stop;

import java.util.List;


// TODO: Implementar la lógica para añadirlo en la DB
public class JourneyController {
    private Journey journey;

    public void addStop(Stop stop) {
        journey.addStop(stop);
    }

    public void removeStop(Stop stop) {
        journey.removeStop(stop);
    }

    public void assignStops(List<Stop> stops) {
        journey.setStopsList(stops);
    }

    public void createSection(List<Coordinate> coordinates) {
        Section section = new Section(coordinates);
        journey.addSection(section);
    }

    public void removeSection(Section section) {
        journey.removeSection(section);
    }

    public void assignSections(List<Section> sections) {
        journey.setSectionsList(sections);
    }

    public void removeSections(List<Section> sections) {
        for (Section section : sections) {
            journey.removeSection(section);
        }
    }
}
