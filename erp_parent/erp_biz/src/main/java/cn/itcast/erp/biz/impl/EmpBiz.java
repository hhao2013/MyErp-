package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.entity.Tree;
import redis.clients.jedis.Jedis;
/**
 * 员工业务逻辑类
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

	private IEmpDao empDao;
	private Jedis jedis;
	
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}
	
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		super.setBaseDao(this.empDao);
	}

	@Override
	public Emp findByUsernameAndPsw(String username, String pwd) {
		String md5Return = Md5Return(pwd, username);
		Emp emp = empDao.findByUsernameAndPsw(username, md5Return);
		return emp;
	}

	@Override
	public void updatePwd(String newPwd, Long uuid, String oldPwd) {
		Emp emp = empDao.get(uuid);
		String md5Return = Md5Return(oldPwd, emp.getUsername());
		if(!md5Return.equals(emp.getPwd())){
			throw new ErpException("旧密码输入错误");
		}
		String newMd5Hash = Md5Return(newPwd, emp.getUsername());
		empDao.updatePwd(newMd5Hash, uuid);
	}
	
	protected String Md5Return(String pwd, String username) {
		Md5Hash md5Hash = new Md5Hash(pwd, username, 2);
		return md5Hash.toString();
	}
	public static void main(String[] args) {
			Md5Hash md5Hash = new Md5Hash("admin", "admin", 2);
		System.out.println(md5Hash.toString());
	}

	@Override
	public void updatePwd_rest(String newPwd, Long uuid) {
		Emp emp = empDao.get(uuid);
		String md5Return = Md5Return(newPwd, emp.getUsername());
		empDao.updatePwd(md5Return, uuid);
	}

	@Override
	public void add(Emp t) {
		String username = t.getUsername();
		String md5Return = Md5Return(username, username);
		t.setPwd(md5Return);
		super.add(t);
	}
	/**
	 * 更新
	 */
	public void update(Emp t){
		jedis.del("emp_"+t.getUuid());
		super.update(t);
	}
}
