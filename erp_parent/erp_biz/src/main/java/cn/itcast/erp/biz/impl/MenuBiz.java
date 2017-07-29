package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
/**
 * 菜单业务逻辑类
 * @author Administrator
 *
 */
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

	private IMenuDao menuDao;
	private IRoleDao roleDao;
	private IEmpDao empDao;
	
	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
		super.setBaseDao(this.menuDao);
	}

	@Override
	public Menu getMenusByEmpid(Long empuuid) {
		List<Menu> menusByEmpid = empDao.getMenusByEmpid(empuuid);
		Menu menu = menuDao.get("0");
		Menu cloneMenus = cloneMenus(menu);
		Menu _m1= null;
		Menu _m2= null;
		for(Menu m1 : menu.getMenus()){
			_m1= cloneMenus(m1);
			for(Menu m2 : m1.getMenus()){
				if(menusByEmpid.contains(m2)){
					_m2 = cloneMenus(m2);
					_m1.getMenus().add(_m2);
				}
			}
			if(_m1.getMenus().size()>0){
				cloneMenus.getMenus().add(_m1);
			}
		}
		return cloneMenus;
	}
	private Menu cloneMenus(Menu src){
		Menu _new = new Menu();
		_new.setIcon(src.getIcon());
		_new.setMenuid(src.getMenuid());
		_new.setMenuname(src.getMenuname());
		_new.setUrl(src.getUrl());
		_new.setMenus(new ArrayList<Menu>());
		return _new;
	}

	@Override
	public void updateRoleMenus(Long uuid, String checkIds) {
		Emp emp = empDao.get(uuid);
		//清空菜单下角色权限
		emp.setRoles(new ArrayList<Role>());
		//获得数组中Id值
		String[] ids = checkIds.split(",");
		Menu menu = null;
		for (String id : ids) {
			Role role = roleDao.get(Long.valueOf(id));
			emp.getRoles().add(role);
		}
		
	}

	@Override
	public List<Tree> readRoleMenus(Long id) {
		Emp emp = empDao.get(id);
		List<Role> roles = emp.getRoles();
		List<Role> list = roleDao.getList(null,null,null);
		ArrayList<Tree> treelist = new ArrayList<Tree>();
		Tree t = null;
		for(Role r : list){
			t = new Tree();
			t.setId(r.getUuid()+"");
			t.setText(r.getName());
			if(roles.contains(r)){
				t.setChecked(true);
			}
			treelist.add(t);
		}
		return treelist;
	}
}
