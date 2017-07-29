package cn.itcast.erp.action;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Tree;

/**
 * 菜单Action 
 * @author Administrator
 *
 */
public class MenuAction extends BaseAction<Menu> {
	private static final Logger log = LoggerFactory.getLogger(LoginAction.class);
	private IMenuBiz menuBiz;
	private String checkIds;
	public void setCheckIds(String checkIds) {
		this.checkIds = checkIds;
	}

	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
		super.setBaseBiz(this.menuBiz);
	}
	public void readRoleMenus(){
		List<Tree> readRoleMenus = menuBiz.readRoleMenus(getId());
		write(JSON.toJSONString(readRoleMenus));
	}
	public void getMenuTree(){
		Emp loginUser = getLoginUser();
		if(null==loginUser){
			log.debug("亲，您还没登录，请登录");
			ajaxReturn(false, "亲，您还没登录，请登录");
			return;
		}
		Menu menu = menuBiz.getMenusByEmpid(loginUser.getUuid());
		String jsonString = JSON.toJSONString(menu);
		log.debug(jsonString);
		write(jsonString);
	}
	public void updateRoleMenus(){
		try {
			menuBiz.updateRoleMenus(getId(), checkIds);
			ajaxReturn(true, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "更新失败");
		}
	}
}
