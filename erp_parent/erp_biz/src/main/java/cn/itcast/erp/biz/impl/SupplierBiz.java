package cn.itcast.erp.biz.impl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
/**
 * 供应商业务逻辑类
 * @author Administrator
 *
 */
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

	private ISupplierDao supplierDao;
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
		super.setBaseDao(this.supplierDao);
	}

	@Override
	public void export(OutputStream os, Supplier t1) {
		List<Supplier> list = supplierDao.getList(t1, null, null);
		HSSFWorkbook hs = new HSSFWorkbook();
		HSSFSheet sheet = null;
		if("1".equals(t1.getType())){
			sheet = hs.createSheet("供应商");
		}
		if("2".equals(t1.getType())){
			sheet = hs.createSheet("客户");
		}
		HSSFRow createRow = sheet.createRow(0);
		String[] hearsName = {"名称","联系地址","联系人","联系电话","Email"};
		int[] columns = {4000,8000,2000,3000,8000};
		HSSFCell createCell = null;
		for (int i = 0; i < hearsName.length; i++) {
			createCell = createRow.createCell(i);
			createCell.setCellValue(hearsName[i]);
			sheet.setColumnWidth(i, columns[i]);
		}
		
		int i = 1;
		for (Supplier supplier : list) {
			createRow = sheet.createRow(i);
			createRow.createCell(0).setCellValue(supplier.getName());
			createRow.createCell(1).setCellValue(supplier.getAddress());
			createRow.createCell(2).setCellValue(supplier.getContact());
			createRow.createCell(3).setCellValue(supplier.getTele());
			createRow.createCell(4).setCellValue(supplier.getEmail());
			i++;
		}
		try {
			hs.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				hs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	@Override
	public void doImport(InputStream is) throws Exception {
		HSSFWorkbook hfw = null;
		try {
			hfw = new HSSFWorkbook(is);
			HSSFSheet sheetAt = hfw.getSheetAt(0);
			String type="";
			if("供应商".equals(sheetAt.getSheetName())){
				type = "1";
			}else if("客户".equals(sheetAt.getSheetName())){
				type = "2";
			}else{
				throw new ErpException("工作表名称不正确");
			}
			int lastRowNum = sheetAt.getLastRowNum();
			Supplier supplier = null;
			HSSFRow row = null;
			for (int i = 1; i <= lastRowNum; i++) {
				supplier = new Supplier();
				row = sheetAt.getRow(i);
				supplier.setName(row.getCell(0).getStringCellValue());
				List<Supplier> list = supplierDao.getList(null, supplier, null);
				if(list.size()>0){
					supplier = list.get(0);
				}
				supplier.setAddress(row.getCell(1).getStringCellValue());
				supplier.setContact(row.getCell(2).getStringCellValue());
				supplier.setTele(row.getCell(3).getStringCellValue());
				supplier.setEmail(row.getCell(4).getStringCellValue());
				if(list.size()==0){
					supplier.setType(type);
					supplierDao.add(supplier);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=hfw){
				hfw.close();
			}
		}
	}
	
}
