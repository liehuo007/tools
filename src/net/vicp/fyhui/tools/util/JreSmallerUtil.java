package net.vicp.fyhui.tools.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class JreSmallerUtil {

	public static void main(String[] args) {
		
//		System.out.println(new File("D:\\Study\\Develop\\WorkSpace\\my_tools\\jre1.8.0_161\\lib\\rt\\java\\lang\\Object.class").getAbsolutePath());
//		JreSmallerUtil.test();
		
//		String[] jarArr = new String[] { "rt", "charsets" };
//		String sourceDir = "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre1.8.0_161\\lib\\";
//		String outDir = "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre8small\\";
//		String usedClassListFile = "D:\\Study\\Develop\\WorkSpace\\my_tools\\class_demo.txt";
//		CopyClass obj = new JreSmallerUtil().new CopyClass(sourceDir,outDir, jarArr);
//		obj.readAndCopy(usedClassListFile);
		
		JreSmallerUtil.fetchRelatedClassInNativeJre();
	}
	
	public static void test() {
		String path = "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre1.8.0_161\\lib\\rt\\java\\lang\\Object.class";
		try {
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			// 读一行
			String sp = br.readLine();
			while(sp != null) {
				System.err.println(sp);
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public static void fetchRelatedClassInNativeJre() {
		String usedClassListFile = "D:\\Study\\Develop\\WorkSpace\\my_tools\\class.txt";
		String sourceDir = "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre1.8.0_161\\lib\\";
		String outDir = "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre8small\\lib\\";
		
		List<String> list_Jar = new ArrayList<String>();
		List<String> list_ClassName = new ArrayList<String>();
		List<String> list_ClassWholePath = new ArrayList<String>();
		
		try {
			FileInputStream fis = new FileInputStream(usedClassListFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line=null;
			String className = null;
			String jarName = null;
			String classWholename = null;
			String startsWithLoaded="[Loaded ";
			String startsWithOpened="[Opened ";
			String fromFlagString = " from ";
			while ((line = br.readLine()) !=null) {
//				System.err.println(line);
				if(line.startsWith(startsWithLoaded)) {
					className = line.substring(line.indexOf(startsWithLoaded)+startsWithLoaded.length(),line.indexOf(fromFlagString));
//					System.err.println("className ==>>["+ className);
					jarName = line.substring(line.indexOf(fromFlagString)+fromFlagString.length(),line.length()-1);
//					System.err.println("jarName ==>>["+ jarName);
					classWholename = jarName.substring(0,jarName.length()-".jar".length())+"\\"+className.replace(".", "\\")+".class";
//					System.err.println("classWholename ==>>["+ classWholename);
					list_ClassName.add(className);
					list_ClassWholePath.add(classWholename);
				} else if (line.startsWith(startsWithOpened)) {
					jarName = line.substring(line.indexOf(startsWithOpened)+startsWithOpened.length(),line.length()-1);
					if(!list_Jar.contains(jarName)) {
						list_Jar.add(jarName);
					}
				}
			}
			System.out.println(list_Jar);
			for(String name:list_ClassName) {
				
			}
			for(String name:list_ClassWholePath) {
				File file = new File(name);
				File destFile = new File(name.replace(sourceDir, outDir));
				System.err.println(destFile.getAbsolutePath());
				FileUtil.copyFile(file, destFile);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void fetchRelatedClassInJre2() {
		String jreSourceDir="D:\\Study\\Develop\\WorkSpace\\my_tools\\jre1.8.0_161";
		String sourceDir = "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre1.8.0_161\\lib\\";
		String outDir = "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre8small\\";
		String usedClassListFile = "D:\\Study\\Develop\\WorkSpace\\my_tools\\class.txt";
		
		int count_L = 0;
		int loopCount = 0;
		// 源文件位置，打开它
		try {
			FileInputStream fis = new FileInputStream(usedClassListFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			// 输出文件位置
			FileOutputStream fos = new FileOutputStream(outDir);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			// 读一行
			String sp = br.readLine();
			// 只要没有读到文件尾就一直执行
			while (sp != null) {
				// 只读取以"[L"为开头的行
				if (sp.substring(0, 2).equals("[L")) {
					// 以空格来分隔这个行，返回的字符串数组中的第二个就是我们需要的信息
					String s = sp.split(" ")[1];
					StringBuilder bs = new StringBuilder(s);
					// 只是个测试输出，可以不加
					System.out.println(bs);
					// 循环遍历这个字符串，修改它，使它变成我们需要的格式
					for (int i = 0; i < bs.length(); i++) {
						char ch = bs.charAt(i);
						// 简化循环，因为我们得到的信息很有规律。只要出现大写的字母，就说明已经到了不需要执行的时候了。
						if (ch >= 65 && ch < 91)
							break;
						// 把'.'替换成'/',当然，代码中是因为方法的参数要求。
						if (ch == '.') {
							bs.replace(i, i + 1, "/");
						}
						// 这个是循环的执行此时。
						loopCount++;
					}
					// 这里在输出你的文件信息。加工后用于后续操作。
					bw.write(bs.toString() + '\n');
					// 程序需要的类文件数目。
					count_L++;
				}
				// 读行
				sp = br.readLine();
			}
			// 两个测试输出
			System.out.println(count_L);
			System.out.println(loopCount);
			// 千万要把两个文件关闭！！！
			// 千万要把两个文件关闭！！！
			// 千万要把两个文件关闭！！！
			// 重要的事情说三遍，如果没有关闭，数据可能不能完全输出。
			// 个人认为这个和数据的大小有一定关系。还和输出数据的格式有关系。虽然我说不清楚，但是，一定要关闭，不然就要炸，boom~~
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public class CopyClass {
		private String source = "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre1.8.0_161\\lib\\"; // 类源目录
		private String dest =  "D:\\Study\\Develop\\WorkSpace\\my_tools\\jre8small\\"; // 类拷贝目的目录
		String[] jarArr = new String[] { "rt", "charsets" };
	 
		/***
		 * 
		 * @param source
		 *            类源目录
		 * @param dest
		 *            类拷贝目的目录
		 * @param jarArr
		 *            需要的提取的jar文件
		 */
		public CopyClass(String source, String dest, String[] jarArr) {
			this.source = source;
			this.dest = dest;
			this.jarArr = jarArr;
		}
	 
	 
		/***
		 * @param logName
		 *            提取class明细
		 */
		public void readAndCopy(String logName) {
			int count = 0; // 用于记录成功拷贝的类数
			try {
				FileInputStream fi = new FileInputStream(logName);
				InputStreamReader ir = new InputStreamReader(fi);
				BufferedReader br = new BufferedReader(ir);
	 
				String string = br.readLine();
				while (string != null) {
					if (copyClass(string) == true)
						count++;
					else
						System.out.println("ERROR " + count + ": " + string);
					string = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				System.out.println("ERROR: " + e);
			}
			System.out.println("count: " + count);
	 
		}
	 
		/**
		 * 从原jar路径提取相应的类到目标路径，如将java/lang/CharSequence类从rt目录提取到rt1目录
		 * 
		 * @param string
		 *            提取类的全路径
		 * @return
		 * @throws IOException
		 */
		public boolean copyClass(String string) throws IOException {
			System.err.println(string);
			String classDir = string.substring(0, string.lastIndexOf("/"));
			String className = string.substring(string.lastIndexOf("/") + 1, string.length()) + ".class";
	 
			FileOutputStream fout;
			FileInputStream fin;
			boolean result = false;
	 
			for (String jar : jarArr) {
				File srcFile = new File(source + "/" + jar + "/" + classDir + "/" + className);
				if (!srcFile.exists()) {
					continue;
				}
	 
				byte buf[] = new byte[256];
				fin = new FileInputStream(srcFile);
	 
				/* 目标目录不存在,创建 */
				File destDir = new File(dest + "/" + jar + "1/" + classDir);
				if (!destDir.exists())
					destDir.mkdirs();
	 
				File destFile = new File(destDir + "/" + className);
				fout = new FileOutputStream(destFile);
				int len = 0;
				while ((len = fin.read(buf)) != -1) {
					fout.write(buf, 0, len);
				}
				fout.flush();
				result = true;
				break;
			}
			return result;
		}
	}
}
