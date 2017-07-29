package cn.itcast.erp.dao;

import java.util.List;

import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Tree;
/**
 * 员工数据访问接口
 * @author Administrator
 *
 */
public interface IEmpDao extends IBaseDao<Emp>{
	Emp findByUsernameAndPsw(String username,String pwd);
	void updatePwd(String newPwd,Long uuid);
	List<Menu> getMenusByEmpid(Long empuuid);
}
