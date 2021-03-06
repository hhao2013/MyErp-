package cn.itcast.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Tree;
/**
 * 员工数据访问类
 * @author Administrator
 *
 */
public class EmpDao extends BaseDao<Emp> implements IEmpDao {

	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Emp emp1,Emp emp2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Emp.class);
		if(emp1!=null){
			if(null != emp1.getUsername() && emp1.getUsername().trim().length()>0){
				dc.add(Restrictions.like("username", emp1.getUsername(), MatchMode.ANYWHERE));
			}
			if(null != emp1.getName() && emp1.getName().trim().length()>0){
				dc.add(Restrictions.like("name", emp1.getName(), MatchMode.ANYWHERE));
			}
			if(null != emp1.getEmail() && emp1.getEmail().trim().length()>0){
				dc.add(Restrictions.like("email", emp1.getEmail(), MatchMode.ANYWHERE));
			}
			if(null != emp1.getTele() && emp1.getTele().trim().length()>0){
				dc.add(Restrictions.like("tele", emp1.getTele(), MatchMode.ANYWHERE));
			}
			if(null != emp1.getAddress() && emp1.getAddress().trim().length()>0){
				dc.add(Restrictions.like("address", emp1.getAddress(), MatchMode.ANYWHERE));
			}
			if(null != emp1.getGender()){
				dc.add(Restrictions.eq("gender", emp1.getGender()));
			}
			if(null != emp1.getBirthday()){
				dc.add(Restrictions.ge("birthday",emp1.getBirthday()));
			}
			
		}
		if(null!=emp2){
			if(emp2.getBirthday()!=null){
				dc.add(Restrictions.le("birthday", emp2.getBirthday()));
			}
			
			
		}
		return dc;
	}

	@Override
	public Emp findByUsernameAndPsw(String username,String pwd) {
		List<?> list = getHibernateTemplate().find("from Emp where username=? and pwd=?",username,pwd);
		return (Emp) (list.isEmpty()?null:list.get(0));
	}

	@Override
	public void updatePwd(String newPwd, Long uuid) {
		getHibernateTemplate().bulkUpdate("update Emp set pwd = ? where uuid = ?",newPwd,uuid);
	}

	@Override
	public List<Menu> getMenusByEmpid(Long empuuid) {
		String hql = "select m from Emp e join e.roles r join r.menus m where e.uuid= ?";
		return (List<Menu>) getHibernateTemplate().find(hql, empuuid);
	}
}
