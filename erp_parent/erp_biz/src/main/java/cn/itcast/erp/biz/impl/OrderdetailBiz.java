package cn.itcast.erp.biz.impl;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.redsun.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.entity.Supplier;
/**
 * 订单明细业务逻辑类
 * @author Administrator
 *
 */
public class OrderdetailBiz extends BaseBiz<Orderdetail> implements IOrderdetailBiz {

	private IOrderdetailDao orderdetailDao;
	private IStoredetailDao storedetailDao;
	private IStoreoperDao storeoperDao;
	private ISupplierDao supplierDao;
	private IWaybillWs waybillWs;
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}

	public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
		this.orderdetailDao = orderdetailDao;
		super.setBaseDao(this.orderdetailDao);
	}

	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
	}

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
	}

	@Override
	@RequiresPermissions("采购订单入库")
	public void doInStore(Long uuid, Long storeuuid, Long empuuid) {
		Orderdetail orderDetail = orderdetailDao.get(uuid);
		if(!Orderdetail.STATE_NOT_IN.equals(orderDetail.getState())){
			throw new ErpException("亲，该明细已经入库了");
		}
		orderDetail.setState(Orderdetail.STATE_IN);
		orderDetail.setEnder(empuuid);
		orderDetail.setStoreuuid(storeuuid);
		orderDetail.setEndtime(new Date());
		Storedetail storedetail = new Storedetail();
		storedetail.setStoreuuid(storeuuid);
		storedetail.setGoodsuuid(orderDetail.getGoodsuuid());
		List<Storedetail> list = storedetailDao.getList(storedetail, null, null);
		if(list.size()>0){
			storedetail = list.get(0);
			storedetail.setNum(storedetail.getNum()+orderDetail.getNum());
		}else{
			storedetail.setNum(orderDetail.getNum());
			storedetailDao.add(storedetail);
		}
		Storeoper log = new Storeoper();
		log.setEmpuuid(empuuid);
		log.setStoreuuid(storeuuid);
		log.setNum(orderDetail.getNum());
		log.setOpertime(orderDetail.getEndtime());
		log.setGoodsuuid(orderDetail.getGoodsuuid());
		log.setType(Storeoper.TYPE_IN);
		storeoperDao.add(log);
		
		Orders order = orderDetail.getOrder();
		Orderdetail query = new Orderdetail();
		query.setOrder(order);
		query.setState(Orderdetail.STATE_NOT_IN);
		long count = orderdetailDao.getCount(query, null,null);
		if(count==0){
			order.setState(Orders.STATE_END);
			order.setEnder(empuuid);
			order.setEndtime(orderDetail.getEndtime());
		}
	}
	@RequiresPermissions("销售订单出库")
	public void doOutStore(Long uuid, Long storeuuid, Long empuuid) {
		Orderdetail orderDetail = orderdetailDao.get(uuid);
		if(!Orderdetail.STATE_NOT_IN.equals(orderDetail.getState())){
			throw new ErpException("亲，该明细已经入库了");
		}
		orderDetail.setState(Orderdetail.STATE_IN);
		orderDetail.setEnder(empuuid);
		orderDetail.setStoreuuid(storeuuid);
		orderDetail.setEndtime(new Date());
		//查询库存，构造查询条件
		Storedetail storedetail = new Storedetail();
		storedetail.setStoreuuid(storeuuid);
		storedetail.setGoodsuuid(orderDetail.getGoodsuuid());
		List<Storedetail> list = storedetailDao.getList(storedetail, null, null);
		if(list!=null&&list.size()>0){
			storedetail = list.get(0);
			Long oldNum = storedetail.getNum();
			Long outNum = orderDetail.getNum();
			Long newStoreNum = oldNum-outNum;
			if(newStoreNum<0){
				throw new ErpException("库存不足");
			}else{
				orderDetail.setNum(newStoreNum);
			}
		}else{
			throw new ErpException("库存不足");
		}
		Storeoper log = new Storeoper();
		log.setEmpuuid(empuuid);
		log.setStoreuuid(storeuuid);
		log.setNum(orderDetail.getNum());
		log.setOpertime(orderDetail.getEndtime());
		log.setGoodsuuid(orderDetail.getGoodsuuid());
		log.setType(Storeoper.TYPE_OUT);
		storeoperDao.add(log);
		
		Orders order = orderDetail.getOrder();
		Orderdetail query = new Orderdetail();
		query.setOrder(order);
		query.setState(Orderdetail.STATE_NOT_OUT);
		long count = orderdetailDao.getCount(query, null,null);
		if(count==0){
			order.setState(Orders.STATE_IN);
			order.setEnder(empuuid);
			order.setEndtime(orderDetail.getEndtime());
			Supplier supplier = supplierDao.get(order.getSupplieruuid());
			Long addWaybill = waybillWs.addWaybill(1l,supplier.getAddress(),supplier.getName(),supplier.getTele(),"--");
			order.setWaybillsn(addWaybill);
		}
	}
	
}
