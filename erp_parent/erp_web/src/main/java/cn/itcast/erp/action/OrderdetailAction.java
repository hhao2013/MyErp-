package cn.itcast.erp.action;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;

/**
 * 订单明细Action 
 * @author Administrator
 *
 */
public class OrderdetailAction extends BaseAction<Orderdetail> {

	private IOrderdetailBiz orderdetailBiz;
	private Long storeuuid;

	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}

	public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
		this.orderdetailBiz = orderdetailBiz;
		super.setBaseBiz(this.orderdetailBiz);
	}

	public  void doInStore(){
		Emp loginUser = getLoginUser();
		if(null==loginUser){
			ajaxReturn(false, "您还没登录，请登录");
			return;
		}
		try {
			Long uuid = getId();
			orderdetailBiz.doInStore(uuid , storeuuid, loginUser.getUuid());
			ajaxReturn(true, "入库成功");
		} catch (ErpException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		}catch(UnauthorizedException e){ 
			ajaxReturn(false, "没有权限");
		}catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "入库失败");
		}
	}
	
	public  void doOutStore(){
		Emp loginUser = getLoginUser();
		if(null==loginUser){
			ajaxReturn(false, "您还没登录，请登录");
			return;
		}
		try {
			Long uuid = getId();
			orderdetailBiz.doOutStore(uuid , storeuuid, loginUser.getUuid());
			ajaxReturn(true, "出库成功");
		} catch (ErpException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		}catch(UnauthorizedException e){ 
			ajaxReturn(false, "没有权限");
		}catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "出库失败");
		}
	}
}
