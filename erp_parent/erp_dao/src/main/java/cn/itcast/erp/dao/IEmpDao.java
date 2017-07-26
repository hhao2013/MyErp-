package cn.itcast.erp.dao;

import cn.itcast.erp.entity.Emp;
/**
 * 员工数据访问接口
 * @author Administrator
 *
 */
public interface IEmpDao extends IBaseDao<Emp>{
	Emp findByUsernameAndPsw(String username,String pwd);
	void updatePwd(String newPwd,Long uuid);
}
