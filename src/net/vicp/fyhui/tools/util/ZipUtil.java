package net.vicp.fyhui.tools.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 1、压缩文件*.zip
 * 2、zip文件分割，不指定默认是2.5M
 * 3、分割后zip合并
 * 4、解压zip
 * 5、批量文件压缩
 * @author Administrator
 *
 */
public class ZipUtil {
	
	/**
	 * 压缩文件
	 * @param srcFile 源文件
	 * @param destDir 目标目录，空值时，为源文件同目录
	 * @return 压缩后的文件
	 */
	public static File zip(File srcFile,String destDir) {
		File zipFile = null;
		/**
		 * 需要增加 srcFile destDir存在的判断，计划单独写到FileUtil中
		 */
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ZipEntry zipEntry = null;
		destDir = (destDir==""||destDir==null?srcFile.getParent():destDir)+File.separator;
		System.out.println("dest dir is [" + new File(destDir).getAbsolutePath()+"]");
//		System.out.println("srcFile.length() ==>> "+srcFile.length());
		try {
			fis = new FileInputStream(srcFile);
			bis = new BufferedInputStream(fis);
			byte[] buffer = new byte[2048];
			String filename = srcFile.getName();
			File newFile = new File(destDir+filename.substring(0,filename.lastIndexOf("."))+".zip");
			newFile.createNewFile();
			fos = new FileOutputStream(newFile);
			bos = new BufferedOutputStream(fos);
			zos = new ZipOutputStream(bos);
			zipEntry = new ZipEntry(filename);
			zos.putNextEntry(zipEntry);
			int length = 0;
			while((length=bis.read(buffer))!=-1) {
				zos.write(buffer,0,length);
			}
			zos.close();
			bos.close();
			fos.close();
			bis.close();
			fis.close();
			zipFile = newFile;
			System.out.println("zipFile ==>> ["+zipFile.length()+"]");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zipFile;
	}
	
	/**
	 * 批量
	 * @param names 路径
	 * @param destDir
	 * @return
	 */
	public static File zip(String[] names,String destDir) {
		File[] srcFiles = new File[names.length];
		for (int i = 0; i < names.length; i++) {
			srcFiles[i]=new File(names[i]);
		}
		return zip(srcFiles, destDir);
	}
	
	/**
	 * 批量
	 * @param srcFiles file型
	 * @param destDir
	 * @return
	 */
	public static File zip(File[] srcFiles,String destDir) {
		File zipFile = null;
		/**
		 * 需要增加 srcFile destDir存在的判断，计划单独写到FileUtil中
		 */
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ZipEntry zipEntry = null;
		destDir = (destDir==""||destDir==null?srcFiles[0].getParent():destDir)+File.separator;
		System.out.println("dest dir is [" + new File(destDir).getAbsolutePath()+"]");
		try {
			int count = srcFiles.length;
			byte[] buffer = new byte[2048];
			File newFile = new File(new File(destDir).getName()+".zip");
			fos = new FileOutputStream(newFile);
			bos = new BufferedOutputStream(fos);
			zos = new ZipOutputStream(bos);
			int length = 0;
			for (int i = 0; i < count; i++) {
				fis = new FileInputStream(srcFiles[i]);
				bis = new BufferedInputStream(fis);
				zipEntry = new ZipEntry(srcFiles[i].getName());
				zos.putNextEntry(zipEntry);
				while((length=bis.read(buffer))!=-1) {
					zos.write(buffer,0,length);
				}
				bis.close();
				fis.close();
			}
			zos.close();
			bos.close();
			fos.close();
			bis.close();
			fis.close();
			zipFile = newFile;
			System.out.println("zipFile ==>> ["+zipFile.length()+"]");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zipFile;
	}
	
	/**
	 * 分割zip
	 * @param zipFile
	 * @param destDir
	 * @param splitSize 分割大小，默认2.5MB
	 */
	public static void split(File zipFile,String destDir,long splitSize) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		destDir = (destDir==""||destDir==null?zipFile.getParent():destDir)+File.separator;
		try {
			fis = new FileInputStream(zipFile);
			bis = new BufferedInputStream(fis);
			long fileSize = zipFile.length();
			int newFileSize =(int) (splitSize==0?1024*1024*5/2:splitSize);
			int count = (int) (fileSize/newFileSize);
			int remainder = (int) (fileSize%newFileSize);
			count = remainder==0?count:(count+1);
			System.out.println("count ==>> "+count);
			RandomAccessFile raf = null;
			raf = new RandomAccessFile(zipFile,"r");
			byte[] buffer = new byte[2048];
			long beginPoint=0;
			File tempFile = null;
			String filename = zipFile.getName();
			int length=0;
			for(int i=0;i<count;i++) {
				tempFile = new File(destDir+filename+"."+String.format("%03d", (i+1)));
				tempFile.createNewFile();
				System.out.println(tempFile.getAbsolutePath());
				fos = new FileOutputStream(tempFile);
				bos = new BufferedOutputStream(fos);
				raf.seek(beginPoint); //从指定点开始读取
				while (-1!=(length=raf.read(buffer))) {
					if((beginPoint>=newFileSize*i)&&(beginPoint<newFileSize*(i+1))) {
						bos.write(buffer,0,length);
						beginPoint +=length; 
					}
				}
				bos.close();
				fos.close();
			}
			bos.close();
			fos.close();
			raf.close();
			bis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void merge(String zipPrefix) {
		
	}

	/**
	 * 解压文件到指定目录
	 * @param srcPath
	 * @param destDir
	 */
	public static void unzip(String srcPath, String destDir) {
		unzip(new File(srcPath), destDir);
	}
	/**
	 * 解压文件到指定目录
	 * @param srcFile
	 * @param destDir
	 */
	public static void unzip(File srcFile, String destDir) {
		InputStream is = null;
		FileOutputStream fos = null;
		ZipEntry zipEntry = null;
		ZipFile zipFile = null;
		destDir = (destDir==""||destDir==null?srcFile.getParent():destDir)+File.separator;
		try {
			zipFile = new ZipFile(srcFile,Charset.forName("gbk"));
//			zipFile = new ZipFile(srcFile); //Java解压文件时报错：MALFORMED,原因是压缩文件中有中文；导致错误，解决办法是设置编码：
			Enumeration<?> enumeration = zipFile.entries();
			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
//				System.out.println(zipEntry.getName());
				if(zipEntry.isDirectory()) {
					String tempPath = destDir + File.separator + zipEntry.getName();
					File dir = new File(tempPath);
					dir.mkdirs();
				} else {
					File targetFile = new File(destDir + File.separator + zipEntry.getName());
					if(!targetFile.getParentFile().exists()){
	                     targetFile.getParentFile().mkdirs();
	                 }
	                 targetFile.createNewFile();
	                 is = zipFile.getInputStream(zipEntry);
	                 fos = new FileOutputStream(targetFile);
	                 int len;
	                 byte[] buf = new byte[2048];
	                 while ((len = is.read(buf)) != -1) {
	                     fos.write(buf, 0, len);
	                 }
	                 fos.close();
	                 is.close();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
