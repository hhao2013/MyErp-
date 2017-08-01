package cn.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public void checkUser(){
		/*Emp loginUser = null;
		loginUser = empBiz.findByUsernameAndPsw(username, pwd);*/
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			ajaxReturn(true,"登录成功");
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
		//Emp user = (Emp) ServletActionContext.getContext().getSession().get("user");
		Subject subject = SecurityUtils.getSubject();
		Emp user = (Emp) subject.getPrincipal();
		if(null!=user){
			ajaxReturn(true, user.getName());
		}else{
			ajaxReturn(false, "您还没登陆，请登陆");
		}
	}
	public void loginOut(){
		//ServletActionContext.getContext().getSession().remove("user");
		SecurityUtils.getSubject().logout();
	}
}
