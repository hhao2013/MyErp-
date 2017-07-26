package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IResportDao {
	List<?> ordersResport(Date starttime,Date endtime);
	List<Map<String,Object>> trendResport(int yest);
}
