package com.tz.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.basis.data.GridData;
import tz.extend.query.CommonDao;

import com.tz.sy.template.pt42.domain.Center;
import com.tz.sy.template.pt42.domain.Region;

@Service
public class CenterMgmtService {

    @Autowired
    @Qualifier("mainDB")
    private CommonDao commonDao;

    public List<Center> retrieveCenterCodeList(String centerCode){
        return commonDao.queryForList("CenterMgmt.retrieveCenterList", centerCode, Center.class);
    }

    public List<Region> retrieveRegionList(String centerCode){
        return commonDao.queryForList("CenterMgmt.retrieveRegionList", centerCode, Region.class);
    }

    public void saveCenterRegion(GridData<Center> centerList, GridData<Region> regionList){
        saveCenter(centerList);
        saveRegion(regionList);
    }

    private void saveCenter(GridData<Center> centerList){
        for(int i = 0; i < centerList.size(); ++i){
            Center center = centerList.get(i);
            String statementId = "";

            switch(centerList.getStatusOf(i)){
                case INSERT:
                    statementId = "CenterMgmt.insertCenter";
                    break;
                case UPDATE:
                    statementId = "CenterMgmt.updateCenter";
                    break;
                case DELETE:
                    statementId = "CenterMgmt.deleteCenter";
                    break;
            }

            commonDao.update(statementId, center);
        }
    }

    private void saveRegion(GridData<Region> regionList){
        for(int i = 0; i < regionList.size(); ++i){
            Region region = regionList.get(i);
            String statementId = "";

            switch(regionList.getStatusOf(i)){
                case INSERT:
                    statementId = "CenterMgmt.insertRegion";
                    break;
                case UPDATE:
                    statementId = "CenterMgmt.updateRegion";
                    break;
                case DELETE:
                    statementId = "CenterMgmt.deleteRegion";
                    break;
            }

            commonDao.update(statementId, region);
        }
    }

    private void startRegion(GridData<Region> regionList){
        for(int i = 0; i < regionList.size(); ++i){
            Region region = regionList.get(i);
            String statementId = "";

            switch(regionList.getStatusOf(i)){
                case INSERT:
                    statementId = "CenterMgmt.insertRegion";
                    break;
                case UPDATE:
                    statementId = "CenterMgmt.updateRegion";
                    break;
                case DELETE:
                    statementId = "CenterMgmt.deleteRegion";
                    break;
            }

            commonDao.update(statementId, region);
        }
    }
}
