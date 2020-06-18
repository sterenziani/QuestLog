package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.RegionDao;
import ar.edu.itba.paw.interfaces.service.RegionService;
import ar.edu.itba.paw.model.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionDao regionDao;

    @Transactional
    @Override
    public List<Region> getAllRegions() {
        return regionDao.getAllRegions();
    }
}
