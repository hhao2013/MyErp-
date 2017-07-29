package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
/**
 * 角色业务逻辑类
 * @author Administrator
 *
 */
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {

	private IRoleDao roleDao;
	private IMenuDao menuDao;
	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
		super.setBaseDao(this.roleDao);
	}

	@Override
	public List<Tree> readRoleMenus(Long uuid) {
		ArrayList<Tree> treelist = new ArrayList<Tree>();
		List<Menu> menus = roleDao.get(uuid).getMenus();
		Menu menu = menuDao.get("0");
		Tree t1 = null;
		Tree t2 = null;
		for (Menu m1 : menu.getMenus()) {
			t1 = getTree(m1);
			for (Menu m2 : m1.getMenus()) {
				t2 = getTree(m2);
				if(menus.contains(m2)){
					t2.setChecked(true);
				}
				t1.getChildren().add(t2);
			}
			treelist.add(t1); 
		}
		return treelist;
	}

	private Tree getTree(Menu m) {
		Tree t = new Tree();
		t.setId(m.getMenuid());
		t.setText(m.getMenuname());
		return t;
	}

	@Override
	public void updateRoleMenus(Long uuid, String checkIds) {
		Role role = roleDao.get(uuid);
		//清空菜单下角色权限
		role.setMenus(new ArrayList<Menu>());
		//获得数组中Id值
		String[] ids = checkIds.split(",");
		Menu menu = null;
		for (String id : ids) {
			menu = menuDao.get(id);
			role.getMenus().add(menu);
		}
	}
	
}
