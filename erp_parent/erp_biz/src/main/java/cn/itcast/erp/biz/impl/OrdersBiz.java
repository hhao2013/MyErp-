package cn.itcast.erp.biz.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑类
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

	private IOrdersDao ordersDao;
	private IEmpDao empDao;
	private ISupplierDao supplierDao;
	
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		super.setBaseDao(this.ordersDao);
	}

	@Override
	public void add(Orders order) {
		order.setState(Orders.STATE_CREATE);
		/*order.setType(Orders.TYPE_IN);*/
		order.setCreatetime(new Date());
		double total = 0;
		for (Orderdetail detail : order.getOrderdetails()) {
			total += detail.getMoney();
			detail.setState(Orderdetail.STATE_NOT_IN);
			detail.setOrder(order);
		}
		order.setTotalmoney(total);
		super.add(order);
	}
	@Override
	public List<Orders> getListByPage(Orders t1, Orders t2, Object param, int firstResult, int maxResults) {
		List<Orders> list = super.getListByPage(t1, t2, param, firstResult, maxResults);
		Map<Long,String> empMap = new HashMap<Long,String>();
		Map<Long,String> supplierNameMap = new HashMap<Long, String>();
		for (Orders orders : list) {
			orders.setCreaterName(getName(orders.getCreater(), empMap,empDao));
			orders.setCheckerName(getName(orders.getChecker(), empMap,empDao));
			orders.setStarterName(getName(orders.getStarter(), empMap,empDao));
			orders.setEnderName(getName(orders.getEnder(), empMap,empDao));
			orders.setSupplierName(getName(orders.getSupplieruuid(), supplierNameMap,supplierDao));
		}
		return list;
	}
	
	/*private String getEmpName(Long uuid,Map<Long,String> empMap){
		if(null == uuid){
			return null;
		}
		String empName = empMap.get(uuid);
		if(null == empName){
			empName = empDao.get(uuid).getName();
			empMap.put(uuid,empName);
		}
		return empName;
	}*/
	
/*	private String getSupplierName(Long uuid,Map<Long,String> supplierNameMap){
		if(null==uuid){
			return null;
		}
		String supplierName = supplierNameMap.get(uuid);
		if(null ==supplierName){
			supplierName = supplierDao.get(uuid).getName();
			supplierNameMap.put(uuid,supplierName);
		}
		return supplierName;
	}*/

	@Override
	public void doCheck(Long uuid, Long empuuid) {
		Orders orders = ordersDao.get(uuid);
		if(!Orders.STATE_CREATE.equals(orders.getState())){
			throw new ErpException("亲，您的订单已经审核过了");
		}
		orders.setChecker(empuuid);
		orders.setChecktime(new Date());
		orders.setState(orders.STATE_CHECK);
	}
	@Override
	public void doStart(Long uuid, Long empuuid) {
		Orders orders = ordersDao.get(uuid);
		if(!Orders.STATE_CHECK.equals(orders.getState())){
			throw new ErpException("亲，您的订单已经确认过了");
		}
		orders.setStarter(empuuid);
		orders.setStarttime(new Date());
		orders.setState(orders.STATE_START);
	}
}
