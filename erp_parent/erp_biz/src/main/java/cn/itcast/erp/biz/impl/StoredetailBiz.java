package cn.itcast.erp.biz.impl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.utils.MailUtils;
/**
 * 仓库库存业务逻辑类
 * @author Administrator
 *
 */
public class StoredetailBiz extends BaseBiz<Storedetail> implements IStoredetailBiz {

	private IStoredetailDao storedetailDao;
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;
	private MailUtils mailUtils;
	private String to;
	private String text;
	private String subject;
	
	public void setMailUtils(MailUtils mailUtils) {
		this.mailUtils = mailUtils;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}
	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}
	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
		super.setBaseDao(this.storedetailDao);
	}
	
	
	@Override
	public List<Storedetail> getListByPage(Storedetail t1, Storedetail t2, Object param, int firstResult,
			int maxResults) {
		List<Storedetail> listByPage = super.getListByPage(t1, t2, param, firstResult, maxResults);
		Map<Long,String> goodsNameMap = new HashMap<Long, String>();
		Map<Long,String> storeNameMap = new HashMap<Long, String>();
		for (Storedetail storedetail : listByPage) {
			storedetail.setGoodsName(getName(storedetail.getGoodsuuid(),goodsNameMap,goodsDao));
			storedetail.setStoreName(getName(storedetail.getStoreuuid(),storeNameMap,storeDao));
		}
		return listByPage ;
	}
/*	private String getGoodsName(Long uuid,Map<Long,String> goodsNameMap){
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
			storeName = storeDao.get(uuid).getName();
			storeNameMap.put(uuid,storeName);
		}
		return storeName;
	}*/
	@Override
	public List<Storealert> getStorealert() {
		return storedetailDao.getStorealert();
	}
	@Override
	public void sendStorealertMail() throws MessagingException {
		List<Storealert> storealert = storedetailDao.getStorealert();
		int cnt = storealert==null?0:storealert.size();
		if(cnt>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			mailUtils.sendMail(to, subject.replace("[time]",sdf.format(new Date())), text.replace("[count]",String.valueOf(cnt)));
		}else{
			throw new ErpException("当前没有库存预警");
		}
	}
}
