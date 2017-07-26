package cn.itcast.erp.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IStoreoperBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
/**
 * 仓库操作记录业务逻辑类
 * @author Administrator
 *
 */
public class StoreoperBiz extends BaseBiz<Storeoper> implements IStoreoperBiz {

	private IStoreoperDao storeoperDao;
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;
	private IEmpDao empDao;
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}
	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}
	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}
	
	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
		super.setBaseDao(this.storeoperDao);
	}
	
	@Override
	public List<Storeoper> getListByPage(Storeoper t1, Storeoper t2, Object param, int firstResult, int maxResults) {
		List<Storeoper> listByPage = super.getListByPage(t1, t2, param, firstResult, maxResults);
		Map<Long,String> empNameMap = new HashMap<Long, String>();
		Map<Long,String> goodsNameMap = new HashMap<Long, String>();
		Map<Long,String> storeNameMap = new HashMap<Long, String>();
		for (Storeoper stor : listByPage) {
			stor.setEmpName(getName(stor.getEmpuuid(),empNameMap,empDao));
			stor.setGoodsName(getName(stor.getGoodsuuid(),goodsNameMap,goodsDao));
			stor.setStoreName(getName(stor.getStoreuuid(), storeNameMap,storeDao));
		}
		return listByPage;
	}
	/*private String getEmpName(Long uuid,Map<Long,String> empNameMap){
		if(null==uuid){
			return null;
		}
		String empName = empNameMap.get(uuid);
		if(null ==empName){
			empName = empDao.get(uuid).getName();
			empNameMap.put(uuid,empName);
		}
		return empName;
	}
	private String getGoodsName(Long uuid,Map<Long,String> goodsNameMap){
		if(null==uuid){
			return null;
		}
		String goodsName = goodsNameMap.get(uuid);
		if(null ==goodsName){
			goodsName = goodsDao.get(uuid).getName();
			goodsNameMap.put(uuid,goodsName);
		}
		return goodsName;
	}
	private String getStoreName(Long uuid,Map<Long,String> storeNameMap){
		if(null==uuid){
			return null;
		}
		String storeName = storeNameMap.get(uuid);
		if(null ==storeName){
			storeName = goodsDao.get(uuid).getName();
			storeNameMap.put(uuid,storeName);
		}
		return storeName;
	}
	*/
}
