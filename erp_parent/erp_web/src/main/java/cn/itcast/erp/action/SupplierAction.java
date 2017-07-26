package cn.itcast.erp.action;
import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Supplier;

/**
 * 供应商Action 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {
	
	private ISupplierBiz supplierBiz;
	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		super.setBaseBiz(this.supplierBiz);
	}
	@Override
	public void list() {
		String q = getQ();
		Emp loginUser = getLoginUser();
		if(null!=loginUser){
			if(null==getT1()){
				setT1(new Supplier());
			}
			getT1().setName(q);;
			super.list();
		}
	}

}
