package net.vicp.fyhui.tools.util;

import java.io.File;

/**
 * 
 * ecssent/dbwork@@jdbc:oracle:thin:@//192.168.1.60:1522/tybwtfw sql=test.sql file=test.xlsx
   ecssent/dbwork@@jdbc:oracle:thin:@//192.168.1.60:1522/tybwtfw sql=test.sql file="t est.xlsx"
   root/root@@jdbc:mysql://localhost:3306/db_affairmanage sql=test.sql file=test.xlsx
   sa/@@jdbc:h2:~/test sql=test.sql file=test.xlsx
 * @author Administrator
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * JDBC工具类
 * 
 * @author Administrator
 */
public class DBUtil {
	/**
	 * @param dbString
	 * 		ok
	 * 		ecssent/dbwork@@jdbc:oracle:thin:@//192.168.1.60:1522/tybwtfw
	 * 		root/root@@jdbc:mysql://localhost:3306/db_affairmanage
	 * 		sa/@@jdbc:h2:~/test
	 *		sa/@@dbc:h2:tcp://localhost/~/test
	 * @return
	 */
	public static Connection createConnection(String dbString) {
		String driver = null;
		String url = null;
		String user = null;
		String password = null;
		String[] dbStrings = dbString.split("@@");
		String userpassword = dbStrings[0];
		String dburl = dbStrings[1];
		user = userpassword.substring(0, userpassword.indexOf("/"));
		password =  userpassword.substring(userpassword.indexOf("/")+1);
		url = dburl.trim();
//		url = "jdbc:oracle:thin:@192.168.1.60:1522:tybwtfw"; // ok
//		url = "jdbc:oracle:thin:@192.168.1.60:1522/tybwtfw"; // ok
		try {
			if (url.indexOf(":oracle:")!=-1) {
				driver = "oracle.jdbc.driver.OracleDriver";
//				driver = "oracle.jdbc.OracleDriver";
			} else if (url.indexOf(":mysql:")!=-1) {
				driver = "com.mysql.cj.jdbc.Driver";
			} else if (url.indexOf(":h2:")!=-1) {
				driver = "org.h2.Driver";
			} else {
				throw new Exception(url + " is not defined, the db's format is [ username/password@@jdbc:oracle:thin:@//IP:port/sid ]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("url ==>> ["+ url+"]");
		}
//		System.err.println(driver);
//		System.err.println(url);
//		System.err.println(user);
//		System.err.println(password);
		return createConnection(driver, url, user, password);
	}
	
	public static Connection createConnection(String driver,String url,String user,String password) {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("the db is " + driver+",DB Connection Opened Success!");
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("DB Connection Closed Success!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn, PreparedStatement pstm) {
		if (pstm != null) {
			try {
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		close(conn);
	}
	
	// 数据库关闭
	public static void close(Connection conn, PreparedStatement pstm, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		close(conn, pstm);
	}
	
//	public static void exec(String dbString,String sql) {
//		Connection conn = DBUtil.createConnection(dbString);
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		try {
//			pstm = conn.prepareStatement(sql);
//			rs = pstm.executeQuery();
//			DBUtil.getResult(rs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			close(conn, pstm, rs);
//		}
//	}
//	
//	public static ResultSet getResultSet(String dbString,String sql) {
//		Connection conn = DBUtil.createConnection(dbString);
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		try {
//			pstm = conn.prepareStatement(sql);
//			rs = pstm.executeQuery();
////			DBUtil.getResult(rs);
////			ExcelUtil.createWorkboot(rs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			close(conn, pstm, rs);
//		}
//		return rs;
//	}
	
	
//	public static void getResult(ResultSet rs) {
//		if (rs != null) {
//			ResultSetMetaData rsMetaData = null;
//			int column = 0;
//			boolean hasHeader = false;
//			List<String> header = null;
//			List<String> body = null;
//			List<List<String>> result = new ArrayList<List<String>>();
//			try {
//				while (rs.next()) {
//					rsMetaData = rs.getMetaData();
//					column = rsMetaData.getColumnCount();
//					body = new ArrayList<String>();
//					if(!hasHeader) {
//						header = new ArrayList<String>();
//						for (int i = 0; i < column; i++) {
//							header.add(rsMetaData.getColumnLabel(i+1));
//							System.err.print(rsMetaData.getColumnLabel(i+1)+ "\t");
//						}
//						result.add(header);
//						hasHeader = true;
//					} 
//					for (int i = 0; i < column; i++) {
//						System.err.print(String.valueOf(rs.getObject(i+1))+ "\t");
//						body.add(String.valueOf(rs.getObject(i+1)));
//					}
//					result.add(body);
//					System.err.println();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	/**
	 * 小于10w可以使用
	 * @param dbString
	 * @param sql
	 * @return
	 */
	@Deprecated
	public static Workbook rs2Workbook(String dbString,String sql) {
		Workbook workbook = null;
		Connection conn = DBUtil.createConnection(dbString);
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
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
						if(sheetnum==0||(rowSN>=sheetnum*maxRowSn&&rowSN<=(sheetnum+1)*maxRowSn)) {
							sheet = workbook.createSheet();
							sheetnum++;
							hasHeader = false;
						}
						rsMetaData = rs.getMetaData();
						column = rsMetaData.getColumnCount();
//						body = new ArrayList<String>();
						if(!hasHeader) {
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
							if(null == rs.getObject(i+1)) {
								row.createCell(i).setCellValue("");
							}else {
								row.createCell(i).setCellValue(String.valueOf(rs.getObject(i+1)));
							}
						}
//						result.add(body);
//						System.err.println();
						rowSN++;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, pstm, rs);
		}
		return workbook;
	}
	
	/**
	 * ok
	 * 20200316
	 * 每个book一个sheet，60001行（1标题+6w数据）
	 * @param dbString	db连接串
	 * @param sql	sql语句，不能加;
	 * @param path	excel 保存路径
	 */
	public static void rs2Workbook(String dbString,String sql,String path) {
		Workbook workbook = null;
		Connection conn = DBUtil.createConnection(dbString);
		PreparedStatement pstm = null;
		ResultSet rs = null;
		File file = new File(path); 
		File newfile = file;
		String tempFileName = null;
		try {
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
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
//							sheet = workbook.createSheet(); 
							sheet = workbook.createSheet("Sheet"+String.format("%03d", sheetnum)); //2020031617
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
							if(null == rs.getObject(i+1)) {
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
					ExcelUtil.createExcel(workbook, newfile.getAbsolutePath());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, pstm, rs);
		}
	}

	public static void rs2Csv(String dbString, String sql, String path) {
		Connection conn = DBUtil.createConnection(dbString);
		PreparedStatement pstm = null;
		ResultSet rs = null;
		File file = new File(path); 
		File newfile = file;
		String tempFileName = null;
		String columnSplit=",";
		String csvText = "";
		StringBuilder tempStringBuilder = null;
		try {
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			if (rs != null) {
				csvText="";
				ResultSetMetaData rsMetaData = null;
				int column = 0;
				boolean hasHeader = false;
//				List<String> header = null;
//				List<String> body = null;
//				List<List<String>> result = new ArrayList<List<String>>();
				int rowSN = 0;    //rs计数
				int maxRowSn = 1000001;    //每个sheet最大行数
				int csvnum = 0;    //sheet数量，0开始
				try {
					while (rs.next()) {
						if(csvnum==0||rowSN%(maxRowSn)==0) {
//							System.err.println("before save as ["+ file.getAbsolutePath()+"]");
							tempFileName = file.getAbsolutePath()+"_"+String.format("%03d", csvnum)+".csv";
							newfile=new File(tempFileName);
//							System.err.println("after save as ["+ newfile.getAbsolutePath()+"]");
							csvnum++;
							hasHeader = false;
						}
						rsMetaData = rs.getMetaData();
						column = rsMetaData.getColumnCount();
//						body = new ArrayList<String>();
						if(!hasHeader) {
							csvText="";
//							header = new ArrayList<String>();
							tempStringBuilder = new StringBuilder();
							for (int i = 0; i < column; i++) {
//								header.add(rsMetaData.getColumnLabel(i+1));
//								System.err.print(rsMetaData.getColumnLabel(i+1)+ "\t");
								tempStringBuilder.append(rsMetaData.getColumnLabel(i+1));
								if(i!=(column-1)) {
									tempStringBuilder.append(columnSplit);
								}
							}
							tempStringBuilder.append("\n");
//							result.add(header);
							csvText += tempStringBuilder.toString();
							hasHeader = true;
							rowSN++;
						} 
						tempStringBuilder = new StringBuilder();
						for (int i = 0; i < column; i++) {
//							System.err.print(String.valueOf(rs.getObject(i+1))+ "\t");
//							body.add(String.valueOf(rs.getObject(i+1)));
//							createCell(row, i, String.valueOf(rs.getObject(i+1)));
							tempStringBuilder.append(String.valueOf(rs.getObject(i+1)));
							if(i!=(column-1)) {
								tempStringBuilder.append(columnSplit);
							}
						}
						tempStringBuilder.append("\n");
						csvText += tempStringBuilder.toString();
//						result.add(body);
//						System.err.println();
						rowSN++;
						if(rowSN%(maxRowSn)==0) {
							FileUtil.saveFile(csvText.getBytes(), newfile.getAbsolutePath());
						}
					}
					FileUtil.saveFile(csvText.getBytes(), newfile.getAbsolutePath());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, pstm, rs);
		}
	}

}
