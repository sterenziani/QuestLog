package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.RegionDao;
import ar.edu.itba.paw.interfaces.service.RegionService;
import ar.edu.itba.paw.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionDao regionDao;

    @Override
    public List<Region> getAllRegions() {
        return regionDao.getAllRegions();
    }
}
