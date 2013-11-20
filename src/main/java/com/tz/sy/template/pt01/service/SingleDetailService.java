package com.tz.sy.template.pt01.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.basis.data.GridData;
import tz.extend.query.CommonDao;
import tz.extend.query.callback.AbstractRowStatusCallback;

import com.tz.sy.template.pt01.domain.Employee;

@Service
public class SingleDetailService {

	@Autowired
	@Qualifier("mainDB")
	private CommonDao commonDao;

	public Employee retrieveEmployee(String queryNum,String queryName) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("num", queryNum);
		paramMap.put("name", queryName);

		return commonDao.queryForObject("SingleDetail.retrieveEmployee", paramMap, Employee.class);
	}

	public Employee retrieveEmployee(Map<String,Object> paramMap) {
		return commonDao.queryForObject("SingleDetail.retrieveEmployee", paramMap, Employee.class);
	}

    public List<Map<String, Object>> retrieveEmployeeList(Map<String,Object> paramMap) {
        return commonDao.queryForMapList("SingleDetail.retrieveEmployeeList", paramMap);
    }

	public void saveEmployeeList(GridData<Employee> gridData) {
		gridData.forEachRow(new AbstractRowStatusCallback<Employee>() {

			@Override
			public void delete(Employee record, int rowNum) {
				commonDao.update("SingleDetail.deleteEmployee", record);
			}

			@Override
			public void insert(Employee record, int rowNum) {
				commonDao.update("SingleDetail.insertEmployee", record);
			}

			@Override
			public void update(Employee updatedRecord, Employee oldRecord, int rowNum) {
              commonDao.update("SingleDetail.updateEmployee", updatedRecord);
			}
		});
	}
	
    /**
     * 결재 연동 번호 업데이트
     */
    public void updateEappr( Map<String,Object> inputData ) {
        commonDao.update("SingleDetail.updateEappr", inputData);
    }
    
    /**
     * 결재 연동 번호 삭제
     */
    public void deleteEappr( Map<String,Object> inputData ) {
        commonDao.update("SingleDetail.deleteEappr", inputData);
    }
}
