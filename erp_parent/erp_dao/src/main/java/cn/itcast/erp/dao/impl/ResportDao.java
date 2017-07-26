package cn.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IResportDao;

public class ResportDao extends HibernateDaoSupport implements IResportDao {

	@Override
	public List<?> ordersResport(Date starttime, Date endtime) {
		String hql = "select new Map(gt.name as name,sum(od.money) as y) from Goodstype gt,Goods g,Orders o,Orderdetail od where gt = g.goodstype and od.order = o and od.goodsuuid = g.uuid and o.type = '2'";
		List<Date> quaryparam  = new ArrayList<Date>();
		if(null!=starttime){
			quaryparam.add(starttime);
			hql +=" and o.createtime>=?";
		}
		if(null!=endtime){
			quaryparam.add(endtime);
			hql += " and o.createtime<=?";
		}
		hql += " group by gt.name";
		return getHibernateTemplate().find(hql,quaryparam.toArray());
	}

	@Override
	public List<Map<String, Object>> trendResport(int year) {
		String hql = "select new Map(month(o.createtime)||'月' as month,sum(od.money) as y) from Orderdetail od,Orders o where od.order = o and o.type=2 and year(o.createtime) =? group by month(o.createtime)";
		return (List<Map<String, Object>>) getHibernateTemplate().find(hql,year);
	}

}
