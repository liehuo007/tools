package net.vicp.fyhui.tools;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import net.vicp.fyhui.tools.util.DBUtil;
import net.vicp.fyhui.tools.util.FileUtil;
import net.vicp.fyhui.tools.util.ZipUtil;

/**
 * CREATE TABLE ceb2_invt_head(
head_guid VARCHAR(36) PRIMARY KEY,
order_no VARCHAR(255),
logistics_no VARCHAR(255),
invt_no VARCHAR(255),
sys_date Timestamp,
update_date Timestamp,
note VARCHAR(255)
);


 * @author Administrator
 *
 */
public class MainApp {


	public static void main(String[] args) {
		System.err.println("all work begined");
		String cmdFormat = "my_tools.exe username/password@@jdbc:oracle|h2|mysql:thin:@//ip:port/sid sql.sql excel.xlsx|csv1.csv";
//		if(args == null||args.length!=3) {
//			System.err.println("args is error,The special format is ["+cmdFormat+"]");
//			return;
//		} 
		long beginTime = System.currentTimeMillis();
//		new MainApp().dbExportData2Excel(args);
		new MainApp().dbExportData2File(args);
//		new MainApp().testZip();
//		CmdUtil.execExe();
//		try {
//			new MainApp().testReadExcel();
//			new MainApp().testBigData();
//		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		new MainApp().testInsert();
//		System.err.println(String.format("%08d", 1));

		
		long endTime = System.currentTimeMillis();
		System.err.println("begin ==>> " + new Date(beginTime));
		System.err.println("end   ==>> " + new Date(endTime));
		System.err.println("used  ==>> "+(endTime-beginTime) +" ms");

		System.err.println("all work finished");
	}
	
	public static void test() {
		
	}

	/**
	 * 1、压缩文件，分卷大小为2.5M 未实现
	 * 压缩文件 已ok
	 * 
	 */
	public static void testZip() {
//		String sourceFilePath="D:\\Study\\Develop\\WorkSpace\\my_tools\\error.log";
		String sourceFilePath="D:\\Study\\Develop\\WorkSpace\\my_tools\\my_tools.jar";
		String name = "D:\\Study\\Develop\\WorkSpace\\my_tools\\my_tools.zip";
		String[] names = new String[] {"D:\\Study\\Develop\\WorkSpace\\my_tools\\测试excel.xlsx_000.xlsx", 
				"D:\\Study\\Develop\\WorkSpace\\my_tools\\测试excel273ff9f1-0a3d-4a4d-b63a-6d8dd05cd929.xlsx" , 
				"D:\\Study\\Develop\\WorkSpace\\my_tools\\测试excelb4ba7034-1780-4a83-b23d-fcdb3392fe22.xlsx" , 
				"D:\\Study\\Develop\\WorkSpace\\my_tools\\测试excelc3f8e484-738d-4e0c-98d4-dbec3d2ed350.xlsx" , 
				"D:\\Study\\Develop\\WorkSpace\\my_tools\\测试excelf8f3bb7f-be49-45d2-bd29-9cae5777058d.xlsx"};
//		ZipUtil.zip(new File(sourceFilePath), null);
//		ZipUtil.split(ZipUtil.zip(new File(sourceFilePath), null), "", 5*1024*1024/2);
//		ZipUtil.zip(names, null);
		ZipUtil.unzip(new File(name), "D:\\Study\\Develop\\WorkSpace\\my_tools\\11");
	}
	
	
	/**
	 * 测试ok,在用
	 * 运行
	 * java -jar tools.jar root/root@@jdbc:mysql://localhost:3306/test sql=test.sql file=测试.xlsx
	 * java -jar my_tools.jar sa/@@jdbc:h2:~/test C:\Users\Administrator\Desktop\sql_eclipse.sql D:\测试excel.xlsx
	 * java -jar my_tools.jar sa/@@jdbc:h2:tcp://localhost/~/test C:\Users\Administrator\Desktop\sql_eclipse.sql D:\测试excel.xlsx
	 * @param args
	 */
	public void dbExportData2Excel(String[] args) {
		// 输入3个参数  dbString sqlfilesaveexcel
//		dbh2=sa/@jdbc:h2:~/test
//		sql=test.sql
//		file=test.xlsx
//		args= new String[]{"ecssent/dbwork@@jdbc:oracle:thin:@192.168.1.60:1522/tybwtfw","C:\\Users\\Administrator\\Desktop\\sql_eclipse.sql","测试excel.xlsx"};  //ok
//		args= new String[]{"sa/@@jdbc:h2:tcp://localhost/~/test","C:\\Users\\Administrator\\Desktop\\sql_eclipse.sql","测试excel.xlsx"};  //ok
//		args= new String[]{"sa/@@jdbc:h2:~/test","C:\\Users\\Administrator\\Desktop\\sql_eclipse.sql","测试excel.xlsx"};  //ok
//		args= new String[]{"sa/@@jdbc:h2:~/test","sql=test.sql","file=test.xlsx"};  //ok
//		args= new String[]{"root/root@@jdbc:mysql://localhost:3306/test","sql=test.sql","file=测试.xlsx"}; //ok
		
		String dbString = args[0];
		String sqlfile = args[1].substring(args[1].indexOf("=")+1);
		String saveexcel = args[2].substring(args[2].indexOf("=")+1);
//		System.err.println("dbString ==>> " + dbString);
//		System.err.println("sqlfile ==>> " + sqlfile);
//		System.err.println("saveexcel ==>> " + saveexcel);
		String sql = FileUtil.readFileToString(sqlfile);
//		System.err.println(sql);
//		Workbook workbook = DBUtil.rs2Workbook(dbString, sql);
//		ExcelUtil.createExcel(workbook, saveexcel);

		DBUtil.rs2Workbook(dbString, sql, saveexcel);
	}

	/**测试用**/
	public void testInsert() {
//		String dbString = "dbmysql=root/root@jdbc:mysql://localhost:3306/test";
		String dbString = "dbh2=sa/@jdbc:h2:tcp://localhost/~/test";
//		String dbString = "dbh2=sa/@jdbc:h2:~/test";
		
		String head_guid = null;
		String order_no = null;
		String logistics_no = null;
		String invt_no = null;
		String sys_date = null;
		String update_date = null;
		String note = null;
		
//		String sql = "INSERT INTO ceb2_invt_head(head_guid, order_no, logistics_no, invt_no, sys_date, update_date, note) VALUES ('964cae74-9bad-402a-9edc-cfca9c458781', 'OTqwereee', 'LTccehsi', '46122019I987654321', '2019-08-30 10:37:04', '2019-08-30 10:37:08', '备注')";
		String sql = "INSERT INTO ceb2_invt_head(head_guid, order_no, logistics_no, invt_no, sys_date, update_date, note) VALUES (?,?,?,?,?,?,?)";
		
		Connection conn = null;
		PreparedStatement pstm = null;
		Statement stmt = null;
		try {
			conn = DBUtil.createConnection(dbString);
			
			for (int i = 0; i < 500; i++) {
				head_guid = UUID.randomUUID().toString();
				order_no = "O_"+head_guid.substring(0,4)+String.format("%08d", i);
				logistics_no = "L_"+head_guid.substring(0,4)+String.format("%08d", i);
				invt_no = "46122020I"+String.format("%09d", i);
				note = "备,注, "+String.format("%08d", i);
				pstm =conn.prepareStatement(sql);
				pstm.setString(1, head_guid);
				pstm.setString(2, order_no);
				pstm.setString(3, logistics_no);
				pstm.setString(4, invt_no);
				pstm.setTimestamp(5, new Timestamp(new Date().getTime()));
				pstm.setTimestamp(6, new Timestamp(new Date().getTime()));
				pstm.setString(7, note);
				pstm.execute();
				System.err.println("running at " + String.format("%08d", i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pstm);
		}
	}
	
	public void dbExportData2File(String[] args) {
		// 输入3个参数  dbString sqlfilesaveexcel
//		dbh2=sa/@jdbc:h2:~/test
//		sql=test.sql
//		file=test.xlsx
//		args= new String[]{"ecssent/dbwork@@jdbc:oracle:thin:@192.168.1.60:1522/tybwtfw","C:\\Users\\Administrator\\Desktop\\sql_eclipse.sql","测试excel.xlsx"};  //ok
//		args= new String[]{"sa/@@jdbc:h2:tcp://localhost/~/test","C:\\Users\\Administrator\\Desktop\\sql_eclipse.sql","测试excel.xlsx"};  //ok
//		args= new String[]{"sa/@@jdbc:h2:~/test","C:\\Users\\Administrator\\Desktop\\sql_eclipse.sql","测试excel.xlsx"};  //ok
//		args= new String[]{"sa/@@jdbc:h2:~/test","sql=test.sql","file=test.xlsx"};  //ok
//		args= new String[]{"root/root@@jdbc:mysql://localhost:3306/ecssent?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8","sql=test.sql","file=测试.xlsx"}; //ok
//		args= new String[]{"system/orcl1234@@jdbc:oracle:thin:@172.16.33.183:1521/xmb","C:\\Users\\Administrator\\Desktop\\sql_eclipse.sql","测试csv.csv"};  //ok
//		mytools.exe system/orcl1234@@jdbc:oracle:thin:@172.16.33.183:1521/xmb C:\\Users\\Administrator\\Desktop\\sql_eclipse.sql 测试csv.csv 
		
		String dbString = args[0];
		String sqlfile = args[1].substring(args[1].indexOf("=")+1);
		
		String savefileName = args[2].substring(args[2].indexOf("=")+1);
		String sql = FileUtil.readFileToString(sqlfile);

		if(savefileName.toLowerCase().endsWith(".xlsx")||savefileName.toLowerCase().endsWith(".xls")) {
			DBUtil.rs2Workbook(dbString, sql, savefileName);
		} else if (savefileName.toLowerCase().endsWith(".csv")) {
			DBUtil.rs2Csv(dbString, sql, savefileName);
		}
	}
}
