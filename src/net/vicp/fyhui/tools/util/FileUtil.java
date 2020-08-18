package net.vicp.fyhui.tools.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;



public class FileUtil {

	public static void saveFile(byte[] bytes, String filepath) {
		if(bytes == null) {
			return;
		}
		FileOutputStream fos = null;
		File file = new File(filepath+".writing");
		try {
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
//			int count=0;
//			String filename = "";
			// 文件存在的话，新文件重命名
//			while (!file.createNewFile()) {
//				filename=filepath;
//				filename = filename.substring(0, filename.lastIndexOf("."))+"_["+count+"]"+filename.substring(filename.lastIndexOf("."));
////				filename = filename.substring(0, filename.lastIndexOf("."))+"_["+count+"]"+filename.substring(filename.lastIndexOf("."));
//				file = new File(filename+".writing");
//				count++;
//			}
			fos = new FileOutputStream(file);
			fos.write(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				File newFile = new File(file.getAbsolutePath().replace(".writing", "")); 
				int count=0;
				while(!file.renameTo(newFile)) {
					String filename = newFile.getAbsolutePath();
//					System.err.println("filename ==>>" + filename);
					if(!filename.contains("_[")) {
						filename = filename.substring(0, filename.lastIndexOf("."))+"_["+count+"]"+filename.substring(filename.lastIndexOf("."));
					} else {
						filename = filename.substring(0, filename.lastIndexOf("_[")+2)+count+filename.substring(filename.lastIndexOf("]"));
					}
					newFile = new File(filename);
					count++;
				}
				System.out.println("save a file success ["+ newFile.getAbsolutePath()+"]");
//				System.out.println("save as ["+ newFile.getAbsolutePath()+"]");
//				System.err.println("newFile ==>>" + newFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String readFileToString(String filepath) {
		return new String(readFile(filepath));
	}
	
	public synchronized static byte[] readFile(String filepath) {
//	public static byte[] readFile(String filepath) {
		File file = new File(filepath).getAbsoluteFile();
//		if(!file.exists()) {
//			file.mkdirs();
//		}
		if(file.isDirectory()) {
			System.err.println("error: path is dir, it needs file");
		}
		
         FileInputStream fis = null;
         byte[] fbytes = null;
         try {
        	 if(!file.canRead()) {
        		 return null;
        	 }
			fis = new FileInputStream(file);
			if(fis!=null) {
				int length = fis.available();
				fbytes = new byte[length];
				fis.read(fbytes);
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return fbytes;
         
	}	
	
	
	public synchronized static File[] readDir(String dirpath,final String suffix) {
//	public static File[] readDir(String dirpath,String suffix) {
	
		File file = new File(dirpath);
		if(!file.exists()) {
			file.mkdirs();
		}
		if(!file.isDirectory()) {
			System.err.println("path is not dir, it is an error,["+file.getAbsolutePath()+"]");
			return null;
		}

		
		FileFilter fileFilter = new FileFilter() {
			int maxCount = 5;
//			int maxCount = 5;
			int count=0;
//			boolean flag=true;
			@Override
			public boolean accept(File pathname) {
				if(pathname.getPath().endsWith(suffix))
				{
					if(count<maxCount) {
						pathname.renameTo(new File(pathname.getAbsoluteFile()+".reading"));
						count++;
						return true;
					}
//					if(flag) {
//						pathname.renameTo(new File(pathname.getAbsoluteFile()+".reading"));
//						flag=false;
//						return true;
//					}
					return false;
//				} else if(pathname.getPath().endsWith(".reading")) {
//					return true;
				}
				return false;
			}
		};
		
		return file.listFiles(fileFilter);
	}
	
	//效率高
	public synchronized static File[] readDir2(String dirpath,final String suffix) {
		File file = new File(dirpath);
		if(!file.exists()) {
			file.mkdirs();
		}
		if(!file.isDirectory()) {
			System.err.println("path is not dir, it is an error,["+file.getAbsolutePath()+"]");
			return null;
		}

//		if(suffix==null) {
//			suffix = ".reading";
//		}
		Path recedir = FileSystems.getDefault().getPath(dirpath);

		try {
			final File[] files = new File[1];
			Files.walkFileTree(recedir, new SimpleFileVisitor<Path>() {

				// @Override
				// public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
				// throws IOException {
				// // TODO Auto-generated method stub
				// return null;
				// }

				@Override
				public  FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//					if(suffix==null) {
//						suffix = ".reading";
//					}
					if(file.toString().endsWith(suffix)||!file.toString().endsWith(".reading")) {
						File tempfile = null;
						tempfile = new File(file.toString() + ".reading");
						// tempfile = new File(file.toString());
						file.toFile().renameTo(tempfile);
						files[0]=tempfile;
//						toFileList(files);
						return FileVisitResult.SKIP_SIBLINGS;
					}
					return FileVisitResult.CONTINUE;
				}
					// @Override
					// public FileVisitResult visitFileFailed(Path file, IOException exc) throws
					// IOException {
					// // TODO Auto-generated method stub
					// return null;
					// }
					//
					// @Override
					// public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws
					// IOException {
					// // TODO Auto-generated method stub
					// return null;
					// }
				});
			if(files[0] == null) {
				return null;
			}
			return files;
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}

	public static void copyFileWithStream(Object source,Object fileneeded){
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if((source instanceof String) && (fileneeded instanceof String)){
				fis = new FileInputStream((String)source);
				fos = new FileOutputStream((String)fileneeded);
			}
			if((source instanceof File) && (fileneeded instanceof File)){
				fis = new FileInputStream((File)source);
				fos = new FileOutputStream((File)fileneeded);
			}
			if((source instanceof String) && (fileneeded instanceof File)){
				fis = new FileInputStream((String)source);
				fos = new FileOutputStream((File)fileneeded);
			}
			if((source instanceof File) && (fileneeded instanceof String)){
				fis = new FileInputStream((File)source);
				fos = new FileOutputStream((String)fileneeded);
			}
			
			byte[] bytes = new byte[fis.available()];
			while ((fis.read(bytes))!=-1) {
				fos.write(bytes);
			}
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void copyFile(File source, File dest) {
		try {
			if(!source.isFile()){
				System.out.println("源文件不是文件：" + source.getAbsolutePath());
			}
			if(!new File(dest.getParent()).isDirectory()){
				new File(dest.getParent()).mkdirs();
				dest.createNewFile();
			}
			if(!dest.isFile()){
				dest.createNewFile();
			}else{
				System.out.println("已存在目标文件" + dest.getAbsolutePath());
			}
			copyFileWithStream(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
