package com.vteba.utils.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 从Excel中导数据进入系统
 * @author yinlei 
 * date 2012-6-25 下午1:46:47
 */
public class ExcelImportUtils {
	
	/**
	 * 验证导入的Excel的标题是否符合要求
	 * @param is form中的输入流
	 * @param titles excel的title标准
	 * @return
	 * @author yinlei
	 * date 2012-6-25 下午1:50:14
	 */
	public static boolean checkExcelTitle(InputStream is, String[] titles) {
		boolean checkFlag = false;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0); // 获得第一个sheet的内容
			HSSFRow row = sheet.getRow(0); // 获得第一个行的内容
			if (row == null) {
				checkFlag = false;
			} else {
				for (int i = 0; i < titles.length; i++) {
					HSSFCell cell = row.getCell(i);
					if (cell != null) {
						//是否与给定标题相等
						if (!titles[i].equals(cell.getRichStringCellValue().toString())) {
							checkFlag = false;
							break;
						}
					} else {
						checkFlag = false;
						break;
					}
					if (i == titles.length - 1)
						checkFlag = true;
				}
			}
		} catch (IOException e) {
			checkFlag = false;
		}
		return checkFlag;
	}

	/**
	 * 将Excel中的数据转换为Object，对数据做初步验证
	 * @param is form中输入流
	 * @param clazz 实体类
	 * @param columns excel中导入的实体类的栏位name
	 * @return 封装好的对象List<T>
	 * @author yinlei
	 * date 2012-6-25 下午2:23:15
	 */
	public static <T> List<T> makeExcelToObject(InputStream is,Class<T> clazz, String[] columns) {
		List<T> retList = new ArrayList<T>();//要返回的数据
		String errorInfo = "";//错误信息
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0); // 获得第一个sheet的内容.
			int j = 1;//行数
			HSSFRow row = sheet.getRow(j); // 获得第一个行的内容
			while (row != null) { // 判断是否是最后一行记录
				// 实例化类
				T obj = clazz.newInstance();
				Map<String, Object> propertyMap = new HashMap<String, Object>();
				int enable = 0;
				//判断Excel的某一行是否全是空的
				int proNameLen = columns.length;
				for (int i = 0; i < columns.length; i++) {// 第一行内容
					HSSFCell cell = row.getCell(i);
					
					if (cell != null) {
						//检查标题是否符合要求
						//Map<String, Object> map = checkColums(i, cell, propertyMap, titles[i],protyNamelen);
						//errorInfo += (String)map.get("errorInfo");
						//protyNamelen = (Integer)map.get("prolen");
						propertyMap.put(columns[i], cell);
						enable++;
					} else {
						propertyMap.put(columns[i], null);
						proNameLen = proNameLen - 1;
					}

				}
				
				if(enable < 2){
					errorInfo += "该行数据不符合要求。";
				}
				
				if (StringUtils.isNotBlank(errorInfo)){//有错误信息
					propertyMap.put("flag", 1);
				} else {
					propertyMap.put("flag", 0);
				}
				propertyMap.put("errorInfo", errorInfo);
				if(proNameLen > 0){//该行不是空，作为对象处理
					//根据map构造对象
					BeanUtils.populate(obj, propertyMap);
					retList.add(obj);
				}
				j++;
				row = sheet.getRow(j);//取下一行数据
				errorInfo = "";//新的一行，将错误信息清空
			}
		
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}

	/**
	 * 对每一行，做数据验证处理
	 * @param columsId
	 * @param cell
	 * @param map
	 * @param protyName
	 */
	protected static Map<String, Object> checkColums(int columsId, HSSFCell cell, Map<String, Object> map, String protyName,Integer protyNamelen) {
		String errorInfo = "";
		//String[] columNames = null;
		//String[] params = null;
		//int[] columLens = null;
		/*if ("0".equals(zylx)) {
			columNames = Constants.EXCELPROPERTY.GP_COLUMN_NAMES;
			params = Constants.EXCELPROPERTY.GP_PARAMS;
			columLens = Constants.EXCELPROPERTY.GP_COLUMN_LENS;
		} else {
			columNames = Constants.EXCELPROPERTY.JJ_COLUMN_NAMES;
			params = Constants.EXCELPROPERTY.JJ_PARAMS;
			columLens = Constants.EXCELPROPERTY.JJ_COLUMN_LENS;
		}
		String arg = "";
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			if ("sl1".equals(protyName)) {
				arg = DoubleConvert.convertToNumberInput(cell.getNumericCellValue());
			} else if ("sl2".equals(protyName)) {
				arg = DoubleConvert.convertToWeightInput(cell.getNumericCellValue());
				//arg=DoubleConvert.convertToString(cell.getNumericCellValue());
			} else if ("gpdj".equals(protyName) || "xsdj".equals(protyName)) {
				arg = DoubleConvert.convertToPriceInput(cell.getNumericCellValue());
			} else if("gg".equals(protyName)){//如果规格全部输入了数字，不能当做数字处理 yinlei 20111028
				arg = String.valueOf(cell.getNumericCellValue()).trim().replaceAll("'", "''");
			}else {
				arg = DoubleConvert.convertToString(cell.getNumericCellValue());
			}
		} else {
			arg = cell.getRichStringCellValue().toString().trim().replaceAll("'", "''");
		}
		if (StringUtil.isLen(arg.trim(), columLens[columsId])) {
			if(protyName.equals("rkrq") || protyName.equals("cghtstr3")){
				arg=arg.replace("年", "-");
				arg=arg.replace("月", "-");
				arg=arg.replace("日", "");
			}
			arg = StringUtil.getStrLen(arg.trim(), columLens[columsId]+1);
			errorInfo += columNames[columsId] + "值过长!";
		}
		if (StringUtil.isNull(arg.trim())) {
			protyNamelen = new Integer(protyNamelen.intValue() - 1);
		}
		map.put(params[columsId], arg);*/
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("errorInfo", errorInfo);
		map2.put("prolen", protyNamelen);
		return map2;
	}
}
