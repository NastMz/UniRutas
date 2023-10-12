package com.unirutas.models;

import java.util.List;

public class Journey {
    private Direction direction;
    private List<Stop> stopsList;
    private List<Section> sectionsList;

    public Journey(List<Stop> stopsList, List<Section> sectionsList) {
        this.stopsList = stopsList;
        this.sectionsList = sectionsList;
    }


    public List<Stop> getStopsList() {
        return stopsList;
    }

    public void setStopsList(List<Stop> stopsList) {
        this.stopsList = stopsList;
    }

    public List<Section> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(List<Section> sectionsList) {
        this.sectionsList = sectionsList;
    }


    public void addStop(Stop stop) {
        stopsList.add(stop);
    }

    public void addSection(Section section) {
        sectionsList.add(section);
    }

    public void removeStop(Stop stop) {
        stopsList.remove(stop);
    }

    public void removeSection(Section section) {
        sectionsList.remove(section);
    }
}
