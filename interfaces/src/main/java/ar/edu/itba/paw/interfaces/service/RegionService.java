package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.entity.Region;

import java.util.List;

public interface RegionService {
    /**
     * Get a list of all regions
     * @return	A list of all regions
     */

    List<Region> getAllRegions();
}
