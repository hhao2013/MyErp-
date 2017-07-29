package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;

/**
 * 角色Action 
 * @author Administrator
 *
 */
public class RoleAction extends BaseAction<Role> {

	private IRoleBiz roleBiz;
	private String checkIds;

	public void setCheckIds(String checkIds) {
		this.checkIds = checkIds;
	}

	public void setRoleBiz(IRoleBiz roleBiz) {
		this.roleBiz = roleBiz;
		super.setBaseBiz(this.roleBiz);
	}
	
	public void readRoleMenus(){
		List<Tree> readRoleMenus = roleBiz.readRoleMenus(getId());
		write(JSON.toJSONString(readRoleMenus));
	}
	
	public void updateRoleMenus(){
		try {
			roleBiz.updateRoleMenus(getId(), checkIds);
			ajaxReturn(true, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "更新失败");
		}
	}
}
