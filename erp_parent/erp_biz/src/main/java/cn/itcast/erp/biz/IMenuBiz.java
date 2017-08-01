package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Tree;
/**
 * 菜单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IMenuBiz extends IBaseBiz<Menu>{
	List<Tree> readRoleMenus(Long id);
	Menu getMenusByEmpid(Long empuuid);
	void updateRoleMenus(Long uuid,String checkIds);
	List<Menu> getsMenusByEmpid(Long empuuid);
}

