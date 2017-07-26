package cn.itcast.erp.action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Menu;

/**
 * 菜单Action 
 * @author Administrator
 *
 */
public class MenuAction extends BaseAction<Menu> {
	private static final Logger log = LoggerFactory.getLogger(LoginAction.class);
	private IMenuBiz menuBiz;

	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
		super.setBaseBiz(this.menuBiz);
	}
	
	public void getMenuTree(){
		Menu menu = menuBiz.get("0");
		String jsonString = JSON.toJSONString(menu);
		log.debug(jsonString);
		write(jsonString);
	}
}
