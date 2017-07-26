package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Emp;
/**
 * 员工业务逻辑层接口
 * @author Administrator
 *
 */
public interface IEmpBiz extends IBaseBiz<Emp>{
	Emp findByUsernameAndPsw(String username,String pwd);
	void updatePwd(String newPwd,Long uuid,String oldPwd);
	void updatePwd_rest(String newPwd,Long uuid);
}

