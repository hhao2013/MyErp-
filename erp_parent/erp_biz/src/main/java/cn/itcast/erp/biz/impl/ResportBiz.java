package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IResportBiz;
import cn.itcast.erp.dao.IResportDao;

public class ResportBiz implements IResportBiz {
	private IResportDao resportDao;

	public void setResportDao(IResportDao resportDao) {
		this.resportDao = resportDao;
	}

	@Override
	public List<?> ordersResport(Date starttime, Date endtime) {
		return resportDao.ordersResport(starttime, endtime);
	}

	@Override
	public List<Map<String, Object>> trendResport(int yest) {
		List<Map<String,Object>> trendResport = resportDao.trendResport(yest);
		Map<String,Map<String,Object>> mapResult = new HashMap<String,Map<String,Object>>();
		for (Map<String, Object> map : trendResport) {
			mapResult.put((String) map.get("month"), map);
		}
		List<Map<String,Object>> newResult  = new ArrayList<Map<String,Object>>();
		Map<String,Object> data = null;
		for(int i = 1;i<=12;i++){
			data = mapResult.get(i+"月");
			if(null==data){
				data = new HashMap<String, Object>();
				data.put("month",i+"月");
				data.put("y", 0);
			}
			newResult.add(data);
		}
		return newResult;
	}

}
