package cn.itcast.erp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IResportBiz {
	List<?> ordersResport(Date starttime,Date endtime);
	List<Map<String,Object>> trendResport(int yest);
}
