package cn.itcast.erp.action;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;

/**
 * 订单Action 
 * @author Administrator
 *
 */
public class OrdersAction extends BaseAction<Orders> {
	private Logger log = LoggerFactory.getLogger(OrdersAction.class);
	private IOrdersBiz ordersBiz;
	private String json;
	public void setJson(String json) {
		this.json = json;
	}

	public void setOrdersBiz(IOrdersBiz ordersBiz) {
		this.ordersBiz = ordersBiz;
		super.setBaseBiz(this.ordersBiz);
	}

	public void add() {
		Emp loginUser = getLoginUser();
		log.info("loginUser:"+(null==loginUser?null:loginUser.getUuid()));
		if(null==loginUser){
			ajaxReturn(false, "您还没登录，请登录");
			return;
		}
		try {
			Orders order =getT();
			log.info("supplieruuid:"+(null==order.getSupplieruuid()?null:order.getSupplieruuid()));
			log.debug("orderdetails:"+json);
			List<Orderdetail> orderdetails = JSON.parseArray(json, Orderdetail.class);
			order.setCreater(loginUser.getUuid());
			order.setOrderdetails(orderdetails);
			ordersBiz.add(order);
			ajaxReturn(true, "添加成功");
		} catch (Exception e) {
			ajaxReturn(false, "添加失败");
			log.debug("添加失败",e);
		}
		
		
	}
	
	public void doCheck(){
		Emp loginUser = getLoginUser();
		log.info("loginUser:"+(null==loginUser?null:loginUser.getUuid()));
		if(null==loginUser){
			ajaxReturn(false, "您还没登录，请登录");
			return;
		}
		try {
			ordersBiz.doCheck(getId(), loginUser.getUuid());
			ajaxReturn(true, "审核成功");
		}catch(ErpException e){
			log.debug("审核失败");
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			log.debug("审核失败");
			ajaxReturn(true, "审核失败");
		}
		
	}
	public void doStart(){
		Emp loginUser = getLoginUser();
		log.info("loginUser:"+(null==loginUser?null:loginUser.getUuid()));
		if(null==loginUser){
			ajaxReturn(false, "您还没登录，请登录");
			return;
		}
		try {
			ordersBiz.doStart(getId(), loginUser.getUuid());
			ajaxReturn(true, "确认成功");
		}catch(ErpException e){
			log.debug("确认失败");
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			log.debug("确认失败");
			ajaxReturn(true, "确认失败");
		}
	}
	public void doMyOrderList(){
		if(null==getT1()){
			setT1(new Orders());
		}
		Emp loginUser = getLoginUser();
		log.info("loginUser:"+(null==loginUser?null:loginUser.getUuid()));
		if(null!=loginUser){
			Orders t1 = getT1();
			t1.setCreater(loginUser.getUuid());
			super.listByPage();
		}
	}
}
