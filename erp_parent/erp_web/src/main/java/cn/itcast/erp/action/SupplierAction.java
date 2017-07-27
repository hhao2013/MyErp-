package cn.itcast.erp.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Supplier;

/**
 * 供应商Action 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {
	private File file;
	private String fileFileName;
	private String fileContextType;
	public void setFile(File file) {
		this.file = file;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public void setFileContextType(String fileContextType) {
		this.fileContextType = fileContextType;
	}

	private ISupplierBiz supplierBiz;
	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		super.setBaseBiz(this.supplierBiz);
	}
	@Override
	public void list() {
		String q = getQ();
		Emp loginUser = getLoginUser();
		if(null!=loginUser){
			if(null==getT1()){
				setT1(new Supplier());
			}
			getT1().setName(q);;
			super.list();
		}
	}

	public void doImport(){
		if(!"application/vnd.ms-excel".equals(fileContextType)){
			if(!fileFileName.endsWith(".xls")){
				ajaxReturn(false,"不是excel文件xls类型");
				return;
			}
		}
		try {
			supplierBiz.doImport(new FileInputStream(file));
			ajaxReturn(true,"上传成功");
		} catch (ErpException e) {
			ajaxReturn(false, e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			ajaxReturn(false, "上传失败");
			e.printStackTrace();
		}
	}
	public void export(){
		String fileName="";
		if("1".equals(getT1().getType())){
			fileName="供应商.xls";
		}
		if("2".equals(getT1().getType())){
			fileName="客户.xls";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
			try {
				response.setHeader("Content-Disposition","attachment;filename="+new String(fileName.getBytes(),"ISO-8859-1"));
				supplierBiz.export(response.getOutputStream(), getT1());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
