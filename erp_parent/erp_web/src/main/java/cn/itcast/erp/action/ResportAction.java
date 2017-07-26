package cn.itcast.erp.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IResportBiz;

public class ResportAction {
	private Date starttime;
	private Date endtime;
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	private IResportBiz resportBiz;
	private int year;
	public void setYear(int year) {
		this.year = year;
	}
	public void setResportBiz(IResportBiz resportBiz) {
		this.resportBiz = resportBiz;
	}
	
	public void resport(){
		List<?> resport = resportBiz.ordersResport(starttime, endtime);
		WebUtils.write(JSON.toJSONString(resport));
	}
	public void trendResport(){
		List<Map<String, Object>> trendResport = resportBiz.trendResport(year);
		WebUtils.write(JSON.toJSONString(trendResport));
	}
}
