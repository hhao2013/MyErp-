package cn.itcast.erp.action;
import java.util.List;

import javax.mail.MessagingException;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;

/**
 * 仓库库存Action 
 * @author Administrator
 *
 */
public class StoredetailAction extends BaseAction<Storedetail> {

	private IStoredetailBiz storedetailBiz;

	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
		super.setBaseBiz(this.storedetailBiz);
	}
	public void storealertList(){
		List<Storealert> storealert = storedetailBiz.getStorealert();
		write(JSON.toJSONString(storealert));
	}
	public void sendStorealertMail(){
		try {
			storedetailBiz.sendStorealertMail();
			ajaxReturn(true, "发送预警邮件成功");
		} catch (MessagingException e) {
			ajaxReturn(false, "构建预警邮件失败");
			e.printStackTrace();
		} catch (ErpException e){
			ajaxReturn(false, e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			ajaxReturn(false, "发送邮件失败");
			e.printStackTrace();
		}
	}
}
