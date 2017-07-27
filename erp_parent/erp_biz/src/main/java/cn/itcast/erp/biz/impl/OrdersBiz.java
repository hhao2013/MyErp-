package cn.itcast.erp.biz.impl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.biz.exception.ErpException;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑类
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

	private IOrdersDao ordersDao;
	private IEmpDao empDao;
	private ISupplierDao supplierDao;
	
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		super.setBaseDao(this.ordersDao);
	}

	@Override
	public void add(Orders order) {
		order.setState(Orders.STATE_CREATE);
		/*order.setType(Orders.TYPE_IN);*/
		order.setCreatetime(new Date());
		double total = 0;
		for (Orderdetail detail : order.getOrderdetails()) {
			total += detail.getMoney();
			detail.setState(Orderdetail.STATE_NOT_IN);
			detail.setOrder(order);
		}
		order.setTotalmoney(total);
		super.add(order);
	}
	@Override
	public List<Orders> getListByPage(Orders t1, Orders t2, Object param, int firstResult, int maxResults) {
		List<Orders> list = super.getListByPage(t1, t2, param, firstResult, maxResults);
		Map<Long,String> empMap = new HashMap<Long,String>();
		Map<Long,String> supplierNameMap = new HashMap<Long, String>();
		for (Orders orders : list) {
			orders.setCreaterName(getName(orders.getCreater(), empMap,empDao));
			orders.setCheckerName(getName(orders.getChecker(), empMap,empDao));
			orders.setStarterName(getName(orders.getStarter(), empMap,empDao));
			orders.setEnderName(getName(orders.getEnder(), empMap,empDao));
			orders.setSupplierName(getName(orders.getSupplieruuid(), supplierNameMap,supplierDao));
		}
		return list;
	}
	
	/*private String getEmpName(Long uuid,Map<Long,String> empMap){
		if(null == uuid){
			return null;
		}
		String empName = empMap.get(uuid);
		if(null == empName){
			empName = empDao.get(uuid).getName();
			empMap.put(uuid,empName);
		}
		return empName;
	}*/
	
/*	private String getSupplierName(Long uuid,Map<Long,String> supplierNameMap){
		if(null==uuid){
			return null;
		}
		String supplierName = supplierNameMap.get(uuid);
		if(null ==supplierName){
			supplierName = supplierDao.get(uuid).getName();
			supplierNameMap.put(uuid,supplierName);
		}
		return supplierName;
	}*/

	@Override
	public void doCheck(Long uuid, Long empuuid) {
		Orders orders = ordersDao.get(uuid);
		if(!Orders.STATE_CREATE.equals(orders.getState())){
			throw new ErpException("亲，您的订单已经审核过了");
		}
		orders.setChecker(empuuid);
		orders.setChecktime(new Date());
		orders.setState(orders.STATE_CHECK);
	}
	@Override
	public void doStart(Long uuid, Long empuuid) {
		Orders orders = ordersDao.get(uuid);
		if(!Orders.STATE_CHECK.equals(orders.getState())){
			throw new ErpException("亲，您的订单已经确认过了");
		}
		orders.setStarter(empuuid);
		orders.setStarttime(new Date());
		orders.setState(orders.STATE_START);
	}

	@SuppressWarnings("resource")
	@Override
	public void exportById(OutputStream os, Long uuid) throws Exception {
			Orders orders = ordersDao.get(uuid);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			String title = "";
			String title2 = "";
			if(Orders.TYPE_OUT.equals(orders.getType())){
				title = "销  售  单";
				title2 = "客户";
			}
			if(Orders.TYPE_IN.equals(orders.getType())){
				title = "采  购  单";
				title2 = "供应商";
			}
			HSSFSheet sheet = hssfWorkbook.createSheet(title);
			//创建行 行的索引，从0开始
			HSSFRow row = null;
			//单元格的样式
			HSSFCellStyle style_content = hssfWorkbook.createCellStyle();
			style_content.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
			style_content.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
			HSSFCellStyle style_title = hssfWorkbook.createCellStyle();
			//复制样式
			style_title.cloneStyleFrom(style_content);
			
			//边框
			style_content.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style_content.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style_content.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style_content.setBorderTop(HSSFCellStyle.BORDER_THIN);
			//内容的字体
			HSSFFont font_content = hssfWorkbook.createFont();
			font_content.setFontName("宋体");
			font_content.setFontHeightInPoints((short)12);
			style_content.setFont(font_content);
			//标题的字体 黑体
			HSSFFont font_title = hssfWorkbook.createFont();
			font_title.setFontName("黑体");
			font_title.setFontHeightInPoints((short)18);
			style_title.setFont(font_title);
			
			HSSFCell cell = null;
			sheet.createRow(0).createCell(0).setCellStyle(style_title);;//标题行
			//明细数量
			int size = orders.getOrderdetails().size();
			int rowCnt = 9 + size;
			//行高
			sheet.getRow(0).setHeight((short)1000);
			for(int i = 2; i <= rowCnt; i++){
				row = sheet.createRow(i);
				row.setHeight((short)500);//内容区域的行高
				for(int col = 0; col < 4; col++){
					cell = row.createCell(col);
					//设置单元格的样式
					cell.setCellStyle(style_content);
				}
			}
			//日期格式
			HSSFDataFormat df = hssfWorkbook.createDataFormat();
			HSSFCellStyle style_date = hssfWorkbook.createCellStyle();
			style_date.cloneStyleFrom(style_content);
			style_date.setDataFormat(df.getFormat("yyyy-MM-dd"));
			//设置日期格式
			for(int i = 3; i <= 6; i++){
				sheet.getRow(i).getCell(1).setCellStyle(style_date);
			}
			
			//合并单元格
			//1.开始的行索引
			//2.结束的行索引
			//3.列的开始索引
			//4.线束的列的索引
			//标题
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
			//供应商
			sheet.addMergedRegion(new CellRangeAddress(2,2,1,3));
			//订单明细
			sheet.addMergedRegion(new CellRangeAddress(7,7,0,3));
			sheet.getRow(0).getCell(0).setCellValue(title);
			sheet.getRow(2).getCell(0).setCellValue(title2);
			sheet.getRow(2).getCell(1).setCellValue(supplierDao.getName(orders.getSupplieruuid()));
			
			sheet.getRow(3).getCell(0).setCellValue("下单日期");
			setDate(sheet.getRow(3).getCell(1),orders.getCreatetime());
			sheet.getRow(4).getCell(0).setCellValue("审核日期");
			setDate(sheet.getRow(4).getCell(1),orders.getChecktime());
			sheet.getRow(5).getCell(0).setCellValue("采购日期");
			setDate(sheet.getRow(5).getCell(1),orders.getStarttime());
			sheet.getRow(6).getCell(0).setCellValue("入库日期");
			setDate(sheet.getRow(6).getCell(1),orders.getEndtime());
			sheet.getRow(3).getCell(2).setCellValue("经办人");
			sheet.getRow(3).getCell(3).setCellValue(empDao.getName(orders.getCreater()));
			sheet.getRow(4).getCell(2).setCellValue("经办人");
			sheet.getRow(4).getCell(3).setCellValue(empDao.getName(orders.getChecker()));
			sheet.getRow(5).getCell(2).setCellValue("经办人");
			sheet.getRow(5).getCell(3).setCellValue(empDao.getName(orders.getStarter()));
			sheet.getRow(6).getCell(2).setCellValue("经办人");
			sheet.getRow(6).getCell(3).setCellValue(empDao.getName(orders.getEnder()));
			
			sheet.getRow(7).getCell(0).setCellValue("订单明细");
			sheet.getRow(8).getCell(0).setCellValue("商品");
			sheet.getRow(8).getCell(1).setCellValue("数量");
			sheet.getRow(8).getCell(2).setCellValue("价格");
			sheet.getRow(8).getCell(3).setCellValue("金额");
			
			//列宽
			for(int i = 0; i < 4; i++){
				sheet.setColumnWidth(i, 5000);
			}
					
			//明细内容
			int i = 9;
			for(Orderdetail od : orders.getOrderdetails()){
				row = sheet.getRow(i);
				row.getCell(0).setCellValue(od.getGoodsname());
				row.getCell(1).setCellValue(od.getPrice());
				row.getCell(2).setCellValue(od.getNum());
				row.getCell(3).setCellValue(od.getMoney());
				i++;
			}
			//合计
			sheet.getRow(rowCnt).getCell(0).setCellValue("合计");
			sheet.getRow(rowCnt).getCell(3).setCellValue(orders.getTotalmoney());
			
			hssfWorkbook.write(os);
			hssfWorkbook.close();
	}
	private void setDate(Cell cel, Date date){
		if(null != date){
			cel.setCellValue(date);
		}
	}
}
