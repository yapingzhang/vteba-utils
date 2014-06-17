package com.vteba.utils.common;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 导出Excel数据工具类
 * @author yinlei 
 * date 2012-6-25 下午1:46:16
 */
public class ExcelExportUtils {
	private String dataFormat	= "yyyy/MM/dd hh:mm:ss";//日期格式
	private String[] title;//标题

	public ExcelExportUtils() {
	}

	public ExcelExportUtils(String[] title) {
		this.title = title;
	}
	
	/**
	 * 导出Excel
	 * @param dataList 要导出的数据
	 * @param sheetname 工作表名字
	 * @return Excel
	 * @author yinlei
	 * date 2012-6-25 下午12:55:57
	 */
	public HSSFWorkbook makeObjectToExcel(List<Object[]> dataList, String sheetname) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetname);
		createExcelHeader(sheet);
		createExcel(wb, sheet, dataList);
		return wb;
	}
	
	/**
	 * 创建表头(标题)
	 * @param sheet
	 * 2012-5-18上午09:10:01
	 * @author: yinlei
	 */
	private void createExcelHeader(HSSFSheet sheet) {
		for (short i = 0; i < title.length; i++) {
			setStringValue(sheet, (short)0, i, title[i]);
		}
	}
	
	/**
	 * 根据数据产生Excel
	 * @param wb Excel
	 * @param sheet 工作表
	 * @param dataList 要导出的数据
	 * @author: yinlei
	 * 2012-5-18上午09:12:33
	 */
	private void createExcel(HSSFWorkbook wb, HSSFSheet sheet, List<Object[]> dataList) {
		for (short i = 1; i <= dataList.size(); i++) {
			Object[] object = dataList.get(i - 1);
			for (short j = 0; j < object.length; j++) {
				this.doSetCell(wb, sheet, i, j, object[j]);
			}
		}
	}
	
	/**
	 * 设置cell值
	 * @param wb excel
	 * @param sheet 工作表
	 * @param rowNum 行数
	 * @param colNum 列数
	 * @param value 单元格值
	 * @author: yinlei
	 * 2012-5-18上午09:12:15
	 */
	private void doSetCell(HSSFWorkbook wb, HSSFSheet sheet, short rowNum, short colNum, Object value) {
		if (value != null) {
			if (value instanceof Number) {
				setDoubleValue(sheet, rowNum, colNum, Double.valueOf(value.toString()));
			} else if (value instanceof String) {
				setStringValue(sheet, rowNum, colNum, value.toString());
			} else if (value instanceof Date) {
				HSSFCellStyle cellStyle = wb.createCellStyle();
				setDateValue(sheet, cellStyle, rowNum, colNum, (Date) value);
			}
		}
	}
	
	/**
	 * 设置double型值
	 * @param sheet 工作表
	 * @param rowNum 行数
	 * @param colNum 列数
	 * @param value Double值
	 * @author: yinlei
	 * 2012-5-18上午09:10:48
	 */
	private void setDoubleValue(HSSFSheet sheet, short rowNum, short colNum, Double value) {
		HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}
	
	/**
	 * 设置日期型值
	 * @param sheet sheet
	 * @param cellStyle 单元格样式
	 * @param rowNum 行数
	 * @param colNum 列数
	 * @param value 日期值
	 * @author: yinlei
	 * 2012-5-18上午09:11:10
	 */
	private void setDateValue(HSSFSheet sheet, HSSFCellStyle cellStyle, short rowNum, short colNum, Date value) {
		// 指定日期显示格式
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(dataFormat));
		HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
		// 设定单元格日期显示格式
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}
	
	/**
	 * 设置string型值
	 * @param sheet sheet
	 * @param rowNum 行数
	 * @param colNum 列数
	 * @param value String值
	 * @author: yinlei
	 * 2012-5-18上午09:11:32
	 */
	private void setStringValue(HSSFSheet sheet, short rowNum, short colNum, String value) {
		HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		HSSFRichTextString str = new HSSFRichTextString(value);
		cell.setCellValue(str);
	}

	/**
	 * 获得指定cell
	 * @param sheet sheet
	 * @param rowNum 行数
	 * @param colNum 列数
	 * @author: yinlei
	 * 2012-5-18上午09:24:04
	 */
	private HSSFCell getMyCell(HSSFSheet sheet, int rowNum, int colNum) {
		HSSFRow row = sheet.getRow(rowNum);
		if (null == row) {
			row = sheet.createRow(rowNum);
		}
		HSSFCell cell = row.getCell(colNum);
		if (null == cell) {
			cell = row.createCell(colNum);
		}
		return cell;
	}
	
	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

}
