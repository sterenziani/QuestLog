package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.entity.Region;

import java.util.List;

public interface RegionDao {

    /**
     * Get a list of all regions
     * @return	A list of all regions
     */

    List<Region> getAllRegions();
}
