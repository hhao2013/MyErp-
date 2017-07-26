package cn.itcast.erp.action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.entity.Emp;

/**
 * 员工Action 
 * @author Administrator
 *
 */
public class EmpAction extends BaseAction<Emp> {
	private static final Logger log = LoggerFactory.getLogger(EmpAction.class);
	private String oldPwd;
	private String newPwd;
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	private IEmpBiz empBiz;

	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
		super.setBaseBiz(this.empBiz);
	}
	
	public void updatePwd(){
		Emp user = getLoginUser();
		if(null==user){
			log.debug("亲，您还没登录，请登录");
			ajaxReturn(false, "亲，您还没登录，请登录");
			return;
		}
		try {
			empBiz.updatePwd(newPwd, user.getUuid(), oldPwd);
			ajaxReturn(true,"修改密码成功");
		}catch (ErpException e) {
			log.debug(e.getMessage());
			ajaxReturn(false, e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "修改密码失败");
		}
		
	}
	public void updatePwd_rest(){
		try{
			empBiz.updatePwd_rest(newPwd,getId());
			ajaxReturn(true,"重置密码成功");
		}catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false,"重置密码失败");
		}
	}
}
