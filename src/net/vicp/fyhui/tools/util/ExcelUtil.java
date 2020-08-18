package net.vicp.fyhui.tools.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.druid.util.StringUtils;

/**
 * 生产、解析Excel文件
 * 20200310 最新 ok
 * @author Administrator
 *
 */
public class ExcelUtil {

	/**
	 * createExcel(workbook,path)
	 * createCell(row,column,value)
	 */
	/**
	 * OK
	 * @param workbook
	 * @param pathname
	 */
	public static void createExcel(Workbook workbook,String pathname) {
		createExcel(workbook, new File(pathname));
	}
	/**
	 * OK
	 * @param workbook
	 * @param file
	 */
	public static void createExcel(Workbook workbook,File file) {
		FileOutputStream fos = null;
		file = file.getAbsoluteFile();
		try {
			if(!file.exists()) {
				if(file.getParentFile().exists()) {
					if(!file.getParentFile().isDirectory()) {
//						file.renameTo(new File(file.getAbsolutePath()+"_bak"));
						System.out.println("目标文件存在，保存失败，请重新指定文件名");
					}
				} else {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			// 将创建的内容写到指定的Excel文件中
			workbook.write(fos);
			fos.flush();
			fos.close();
			System.out.println("save as ["+ file.getAbsolutePath()+"]");
		} catch (FileNotFoundException e) {
			String filename = file.getAbsolutePath();
			filename = filename.substring(0,filename.lastIndexOf("."))+(UUID.randomUUID().toString())+filename.substring(filename.lastIndexOf("."));
			File newfile = new File(filename);
			try {
				newfile.createNewFile();
				fos = new FileOutputStream(newfile);
				workbook.write(fos);
				fos.flush();
				fos.close();
				System.out.println("["+ file.getAbsolutePath()+"] is reading, can not write!! so we write a new file ["+ newfile.getAbsolutePath()+"]");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createCell(Row row, int column,Object value) {
		Cell cell = row.createCell((short)column);
	    String cellValue = "";
	    if (value != null) {
	      cellValue = String.valueOf(value);
	    }
	    cell.setCellValue(cellValue);
	}
	
	/**
	 * <10w可以使用
	 * @param rs
	 * @param workbook
	 */
	public static void rs2Workbook(ResultSet rs,Workbook workbook) {
		Sheet sheet = null;
		Row row = null;
		if (rs != null) {
			workbook = new XSSFWorkbook();
			ResultSetMetaData rsMetaData = null;
			int column = 0;
			boolean hasHeader = false;
//			List<String> header = null;
//			List<String> body = null;
//			List<List<String>> result = new ArrayList<List<String>>();
			int rowSN = 0;    //rs计数
			int maxRowSn = 60001;    //每个sheet最大行数
			int sheetnum = 0;    //sheet数量，0开始
			try {
				while (rs.next()) {
					if(sheetnum==0||(rowSN>=sheetnum*maxRowSn&&rowSN<=(sheetnum+1)*maxRowSn)) {
						sheet = workbook.createSheet();
						sheetnum++;
						hasHeader = false;
					}
					rsMetaData = rs.getMetaData();
					column = rsMetaData.getColumnCount();
//					body = new ArrayList<String>();
					if(!hasHeader) {
						row = sheet.createRow(rowSN-(sheetnum-1)*maxRowSn);
//						row = sheet.createRow(rowSN);
//						header = new ArrayList<String>();
						for (int i = 0; i < column; i++) {
//							header.add(rsMetaData.getColumnLabel(i+1));
//							System.err.print(rsMetaData.getColumnLabel(i+1)+ "\t");
//							ExcelUtil.createCell(row, i, rsMetaData.getColumnLabel(i+1));
							row.createCell(i).setCellValue(rsMetaData.getColumnLabel(i+1));
						}
//						result.add(header);
						hasHeader = true;
						rowSN++;
					} 
					row = sheet.createRow(rowSN-(sheetnum-1)*maxRowSn);
					for (int i = 0; i < column; i++) {
//						System.err.print(String.valueOf(rs.getObject(i+1))+ "\t");
//						body.add(String.valueOf(rs.getObject(i+1)));
//						ExcelUtil.createCell(row, i, String.valueOf(rs.getObject(i+1)));
						if(StringUtils.isEmpty(String.valueOf(rs.getObject(i+1)))) {
							row.createCell(i).setCellValue("");
						}else {
							row.createCell(i).setCellValue(String.valueOf(rs.getObject(i+1)));
						}
					}
//					result.add(body);
//					System.err.println();
					rowSN++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ok
	 * 20200316
	 * 每个book一个sheet，60001行（1标题+6w数据）
	 * @param rs
	 * @param path
	 * 100w行数据
	 * 1、创建excel file
	 * 2、创建 workbook
	 * 3、创建 sheet 处理0-6w行
	 * 4、保存 workbook
	 * 5、打开file，获取workboot
	 * 6、创建 sheet 处理 6w-12w行
	 * 7、保存 workbook
	 * 8、5-6-7循环
	 * @throws IOException 
	 */
	public static void rs2Workbook2(ResultSet rs,String path) {
		Workbook workbook = null;
		File file = new File(path); 
		File newfile = file;
		String tempFileName = null;
		Sheet sheet = null;
		Row row = null;
		if (rs != null) {
			workbook = new XSSFWorkbook();
			ResultSetMetaData rsMetaData = null;
			int column = 0;
			boolean hasHeader = false;
//				List<String> header = null;
//				List<String> body = null;
//				List<List<String>> result = new ArrayList<List<String>>();
			int rowSN = 0;    //rs计数
			int maxRowSn = 60001;    //每个sheet最大行数
			int sheetnum = 0;    //sheet数量，0开始
			try {
				while (rs.next()) {
					if(sheetnum==0||rowSN%(maxRowSn)==0) {
//						if(sheetnum==0||(rowSN>=sheetnum*maxRowSn&&rowSN<=(sheetnum+1)*maxRowSn)) {
//							System.err.println("before save as ["+ file.getAbsolutePath()+"]");
						tempFileName = file.getAbsolutePath()+"_"+String.format("%03d", sheetnum)+".xlsx";
						newfile=new File(tempFileName);
//							System.err.println("after save as ["+ newfile.getAbsolutePath()+"]");
						workbook = new XSSFWorkbook();
						sheet = workbook.createSheet();
						sheetnum++;
						hasHeader = false;
//							System.err.println("sheetnum ["+ sheetnum+"]");
					}
					rsMetaData = rs.getMetaData();
					column = rsMetaData.getColumnCount();
//						body = new ArrayList<String>();
					if(!hasHeader) {
//							System.err.println("rowSN ["+ rowSN+"]");
//							System.err.println("sheetnum ["+ sheetnum+"]");
						row = sheet.createRow(rowSN-(sheetnum-1)*maxRowSn);
//							row = sheet.createRow(rowSN);
//							header = new ArrayList<String>();
						for (int i = 0; i < column; i++) {
//								header.add(rsMetaData.getColumnLabel(i+1));
//								System.err.print(rsMetaData.getColumnLabel(i+1)+ "\t");
//								createCell(row, i, rsMetaData.getColumnLabel(i+1));
							row.createCell(i).setCellValue(rsMetaData.getColumnLabel(i+1));
						}
//							result.add(header);
						hasHeader = true;
						rowSN++;
					} 
					row = sheet.createRow(rowSN-(sheetnum-1)*maxRowSn);
					for (int i = 0; i < column; i++) {
//							System.err.print(String.valueOf(rs.getObject(i+1))+ "\t");
//							body.add(String.valueOf(rs.getObject(i+1)));
//							createCell(row, i, String.valueOf(rs.getObject(i+1)));
						if(StringUtils.isEmpty(String.valueOf(rs.getObject(i+1)))) {
							row.createCell(i).setCellValue("");
						}else {
							row.createCell(i).setCellValue(String.valueOf(rs.getObject(i+1)));
						}
					}
//						result.add(body);
//						System.err.println();
					rowSN++;
					if(rowSN%(maxRowSn)==0) {
//							fos = new FileOutputStream(newfile);
//							// 将创建的内容写到指定的Excel文件中
//							workbook.write(fos);
//							fos.flush();
//							fos.close();
//							System.out.println("save as ["+ newfile.getAbsolutePath()+"]");
						ExcelUtil.createExcel(workbook, newfile.getAbsolutePath());
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ExcelUtil.createExcel(workbook, newfile.getAbsolutePath());
		}
	}
	
	/**
	 * 读取excel文件数据
	 * @param path
	 */
	public static void	readExcel(String path) {
		FileInputStream fis = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		try {
			fis = new FileInputStream(path);
			if (path.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			} else if (path.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			Iterator<Sheet> iteratorSheet = workbook.sheetIterator();
			while (iteratorSheet.hasNext()) {
				sheet = iteratorSheet.next();
				Iterator<Row> iteratorRow = sheet.iterator();
				while (iteratorRow.hasNext()) {
					row = iteratorRow.next();
					Iterator<Cell> iteratorCell = row.iterator();
					while (iteratorCell.hasNext()) {
						cell = iteratorCell.next();
//						System.err.print(String.valueOf(cell.getRichStringCellValue()));
						System.err.print("\t");
					}
					System.out.println();
				}
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
