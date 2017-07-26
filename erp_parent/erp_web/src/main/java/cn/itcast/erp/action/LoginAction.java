package cn.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;

public class LoginAction {
	private static final Logger log = LoggerFactory.getLogger(LoginAction.class);
	private String username;
	private String pwd;
	private IEmpBiz empBiz;
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
	}
	public void checkUser(){
		Emp loginUser = null;
		try {
			loginUser = empBiz.findByUsernameAndPsw(username, pwd);
			if(null == loginUser){
				ajaxReturn(false,"用户名或密码错误");
			}else{
				ServletActionContext.getContext().getSession().put("user", loginUser);
				ajaxReturn(true,"登录成功");
			}
		} catch (Exception e) {
			log.error("登录失败",e);
			ajaxReturn(false,"登录失败");
		}
	}
	public void ajaxReturn(boolean success, String message){
		//返回前端的JSON数据
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("success",success);
		rtn.put("message",message);
		write(JSON.toJSONString(rtn));
	}
	
	/**
	 * 输出字符串到前端
	 * @param jsonString
	 */
	public void write(String jsonString){
		try {
			//响应对象
			HttpServletResponse response = ServletActionContext.getResponse();
			//设置编码
			response.setContentType("text/html;charset=utf-8"); 
			//输出给页面
			response.getWriter().write(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showName(){
		Emp user = (Emp) ServletActionContext.getContext().getSession().get("user");
		if(null!=user){
			ajaxReturn(true, user.getName());
		}else{
			ajaxReturn(false, "您还没登陆，请登陆");
		}
	}
	public void loginOut(){
		ServletActionContext.getContext().getSession().remove("user");
	}
}
