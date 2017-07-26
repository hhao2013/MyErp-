package cn.itcast.erp.action;
import cn.itcast.erp.biz.IStoreBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Store;

/**
 * 仓库Action 
 * @author Administrator
 *
 */
public class StoreAction extends BaseAction<Store> {

	private IStoreBiz storeBiz;

	public void setStoreBiz(IStoreBiz storeBiz) {
		this.storeBiz = storeBiz;
		super.setBaseBiz(this.storeBiz);
	}
	
	public void myList(){
		Emp loginUser = getLoginUser();
		if(null==loginUser){
			ajaxReturn(false, "您还没登录，请登录");
			return;
		}
		Store store =getT1();
		if(null!=loginUser){
			if(null==store){
				store = new Store();
				setT1(store);
			}
			store.setEmpuuid(loginUser.getUuid());
			super.list();
		}
	}
}
