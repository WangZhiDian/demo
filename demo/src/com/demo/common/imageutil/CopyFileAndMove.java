package com.demo.common.imageutil;

/** 
 * @author 王文超 
 * @version 
 * @time 2015年10月9日 下午3:02:43  
 * 类说明 :使用java对文件的复制和移动
*/ 

  
  
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
  
/**
 * 
 * @Description 实现文件的复制和移动文件、目录等  
 * @author 王文超
 * @version 
 * @time 2015年10月12日 上午9:18:16
 */
public class CopyFileAndMove {  
    /**
     * @description 文件移动
     * @time 2015年10月12日 上午9:50:34 
     * @param from 源文件
     * @param to 目的文件
     * @throws Exception
     */
    public static void fileMove(String from, String to) throws Exception {// 移动指定文件夹内的全部文件  
        try {  
            File dir = new File(from);  
            File[] files = dir.listFiles();// 将文件或文件夹放入文件集  
            if (files == null)// 判断文件集是否为空  
                return;  
            File moveDir = new File(to);// 创建目标目录  
            if (!moveDir.exists()) {// 判断目标目录是否存在  
                moveDir.mkdirs();// 不存在则创建  
            }  
            for (int i = 0; i < files.length; i++) {// 遍历文件集  
                if (files[i].isDirectory()) {// 如果是文件夹或目录,则递归调用fileMove方法，直到获得目录下的文件  
                    fileMove(files[i].getPath(), to + "\\" + files[i].getName());// 递归移动文件  
                    files[i].delete();// 删除文件所在原目录  
                }  
                File moveFile = new File(moveDir.getPath() + "\\"// 将文件目录放入移动后的目录  
                        + files[i].getName());  
                if (moveFile.exists()) {// 目标文件夹下存在的话，删除  
                    moveFile.delete();  
                }  
                files[i].renameTo(moveFile);// 移动文件  
                System.out.println(files[i] + " 移动成功");  
            }  
        } catch (Exception e) {  
            throw e;  
        }  
    }  
  
    /**
     *   
     * @author 王文超 
     * @version 
     * @description 复制目录下的文件（不包括该目录）到指定目录，会连同子目录一起复制过去。若目标目录不存在会自动创建
     * @time 2015年10月12日 上午9:16:00 
     * @param toPath 目标目录
     * @param fromPath 源目录
     * @throws FileNotFoundException 
     */
    public static void copyFileFromDir(String toPath, String fromPath) throws FileNotFoundException {  
        File file = new File(fromPath);  
        if (file.exists())
		{
        	createFile(toPath, false);// true:创建文件 false创建目录  
        	if (file.isDirectory()) {// 如果是目录  
        		copyFileToDir(toPath, listFile(file));  
        	} 
        	else {
        		copyFileToDir(toPath, file, "");
        	}
		}
        else {
			throw new FileNotFoundException("未找到要复制的源文件");
		}
    }  
  
     /**
      *   
      * @author 王文超 
      * @version 
      * @description 复制目录到指定目录,将目录以及目录下的文件和子目录全部复制到目标目录 使用本方法要传入实际路径！
      * @time 2015年10月12日 上午9:16:41 
      * @param toPath 目标目录
      * @param fromPath 源目录
     * @throws FileNotFoundException 
      */
    public static void copyDir(String toPath, String fromPath) throws FileNotFoundException {  
    	File file = new File(fromPath);// 创建文件 
        if (file.exists())
		{
        	File targetFile = new File(toPath);// 创建文件  
        	createFile(targetFile, false);// 创建目录  
        	if (targetFile.isDirectory() && file.isDirectory()) {// 如果传入是目录  
        		copyFileToDir(targetFile.getAbsolutePath() + "/" + file.getName(),  
        				listFile(file));// 复制文件到指定目录  
        	}  
        	else {
        		copyFileToDir(toPath, file, "");
        	}
		}
        else {
        	throw new FileNotFoundException("未找到要复制的源文件");
		}
    }  
  
    // 复制一组文件到指定目录。targetDir是目标目录，filePath是需要复制的文件路径  
    private static void copyFileToDir(String toDir, String[] filePath) {  
        if (toDir == null || "".equals(toDir)) {// 目录路径为空  
            System.out.println("参数错误，目标路径不能为空");  
            return;  
        }  
        File targetFile = new File(toDir);  
        if (!targetFile.exists()) {// 如果指定目录不存在  
            targetFile.mkdir();// 新建目录  
        } else {  
            if (!targetFile.isDirectory()) {// 如果不是目录  
                System.out.println("参数错误，目标路径指向的不是一个目录！");  
                return;  
            }  
        }  
        for (int i = 0; i < filePath.length; i++) {// 遍历需要复制的文件路径  
            File file = new File(filePath[i]);// 创建文件  
            if (file.isDirectory()) {// 判断是否是目录  
                copyFileToDir(toDir + "/" + file.getName(), listFile(file));// 递归调用方法获得目录下的文件  
//                System.out.println("复制文件 " + file);  
            } else {  
                copyFileToDir(toDir, file, "");// 复制文件到指定目录  
            }  
        }  
    }  
  
    private static void copyFileToDir(String toDir, File file, String newName) {// 复制文件到指定目录  
        String newFile = "";  
        if (newName != null && !"".equals(newName)) {  
            newFile = toDir + "/" + newName;  
        } else {  
            newFile = toDir + "/" + file.getName();  
        }  
        File tFile = new File(newFile);  
        copyFile(tFile, file);// 调用方法复制文件  
    }  
  
    private static void copyFile(File toFile, File fromFile) {// 复制文件  
        if (toFile.exists()) {// 判断目标目录中文件是否存在  
            System.out.println("文件" + toFile.getAbsolutePath() + "已经存在，跳过该文件！");  
            return;  
        } else {  
            createFile(toFile, true);// 创建文件  
        }  
        InputStream is = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bfos = null;
        try {  
            is = new FileInputStream(fromFile);// 创建文件输入流  
            bis=new BufferedInputStream(is);
            fos = new FileOutputStream(toFile);// 文件输出流  
            bfos=new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];// 字节数组  
            int size = 0;
            while ((size = bis.read(buffer)) != -1) {// 将文件内容写到文件中  
            	bfos.write(buffer,0,size); 
            	bfos.flush();//使用BufferedOutputStream一定要使用这个，否则会导致复制的内容不正确
            }  
            bis.close();
            bfos.close();
            is.close();// 输入流关闭  
            fos.close();// 输出流关闭  
        } catch (FileNotFoundException e) {// 捕获文件不存在异常  
            e.printStackTrace();  
        } catch (IOException e) {// 捕获异常  
            e.printStackTrace();  
        }finally{
        	try{
        		if(bis!=null)bis.close();
        		if(bfos!=null)bfos.close();
        		if(is!=null)is.close();// 输入流关闭  
        		if(fos!=null)fos.close();// 输出流关闭
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	  
        }
    }  
  
    private static String[] listFile(File dir) {// 获取文件绝对路径  
        String absolutPath = dir.getAbsolutePath();// 声获字符串赋值为路传入文件的路径  
        String[] paths = dir.list();// 文件名数组
        String[] files=null;
        if (paths.length!=0)
		{
        	files = new String[paths.length];// 声明字符串数组，长度为传入文件的个数  
        	for (int i = 0; i < paths.length; i++) {// 遍历显示文件绝对路径  
        		files[i] = absolutPath + "/" + paths[i];  
        	}  
		}
        return files;  
    }  
  
    private static void createFile(String path, boolean isFile) {// 创建文件或目录  
        createFile(new File(path), isFile);// 调用方法创建新文件或目录  
    }  
  
    private static void createFile(File file, boolean isFile) {// 创建文件  
        if (!file.exists()) {// 如果文件不存在  
            if (!file.getParentFile().exists())// 如果文件父目录不存在   
            {
                createFile(file.getParentFile(), false);  //调用递归
            } 
//            else 
            {// 存在文件父目录  
                if (isFile)// 创建文件   
                {
                    try 
                    {  
                        file.createNewFile();// 创建新文件  
                    } 
                    catch (IOException e) 
                    {  
                        e.printStackTrace();  
                    }  
                } 
                else 
                {  
                    file.mkdir();// 创建目录  
                }  
            }  
        } //if end 
    }//createFile end
   /**
	 * @author 王文超
	 * @version
	 * @description 删除一个目录和目录下的所有文件和文件夹
	 * @time 2015年10月27日 下午8:38:05
	 * @param path
	 *            目录
	 */
	public static void delall(String path)
	{
		File file = new File(path);
		if (file.exists())
		{

			File[] files = file.listFiles();
			if (files != null)
			{
				for (File f : files)
				{
					delall(f.getAbsolutePath());
				}
			}
			if (file.isFile() || isexmpleDriectory(file))
			{
				file.delete();
			}
		}
	}
	/**
	 * @author 王文超 
	 * @description 删除一个目录下的所有文件和文件夹,不会删除该文件夹
	 * @time 2015年12月25日 上午11:28:42 
	 * @param path
	 */
	public static void delFromDir(String path)
	{
		File file = new File(path);
		if (file.exists())
		{
			File[] files = file.listFiles();
			if (files != null)
			{
				for (File f : files)
				{
					delall(f.getAbsolutePath());
				}
			}
		}
	}
	private static boolean isexmpleDriectory(File file)
	{
		File[] files = file.listFiles();
		if (files != null && files.length > 0)
		{
			return false;
		}
		return true;
	}
    public static void main(String[] args) throws FileNotFoundException {// java程序主入口处  
//        String fromPath = "D:\\MyDocuments\\wangwc11\\桌面\\结果类.java";// 源路径  
//        String toPath = "E:\\123/123/4232/12/test";// 目标路径  
//        CopyFileAndMove cm = new CopyFileAndMove();
//        
////        System.out.println("1.移动文件：从路径 " + fromPath + " 移动到路径 " + toPath);  
////        try {  
////            cm.fileMove(fromPath, toPath);// 调用方法实现文件的移动  
////        } catch (Exception e) {  
////        System.out.println("移动文件出现问题" + e.getMessage());  
////        }  
//        Date firstDate=new Date();
//        System.out.println("2.复制目录 " + toPath + " 下的文件（不包括该目录）到指定目录" + fromPath  
//                + " ，会连同子目录一起复制过去。"+new Date());  
//        cm.copyDir(toPath, fromPath);// 调用方法实现目录复制  
//        
////       System.out.println("3.复制目录 " + fromPath + "到指定目录 " + toPath  
////                + " ,将目录以及目录下的文件和子目录全部复制到目标目录");
////       cm.copyDir(toPath, fromPath);// 调用方法实现目录以用目录下的文件和子目录全部复制             
//       Date seconDate=new Date();
//        System.out.println("时间差"+(seconDate.getTime()-firstDate.getTime())/1000L);
//    	System.out.println("111");
//    	File file  = new File("D:\\1");
//    	System.out.println(file.listFiles().length);
//    	delDir(new File("D:\\1"),new File("D:\\2"));
//    	List<String> list = getPaths(new File("D:\\1"),new File("D:\\2"));
//    	for(String str : list){
//    		System.out.println(str);
//    	}
    	copyDirIn2Dir(new File("D:\\MyDocuments\\liumf07\\桌面\\20170619_微保\\与财险接口"),new File("D:\\MyDocuments\\liumf07\\桌面\\20170619_微保\\223"));
    	copyFile2Dir(new File("D:\\MyDocuments\\liumf07\\桌面\\20170619_微保\\微保待解决.txt"),new File("D:\\MyDocuments\\liumf07\\桌面\\20170619_微保\\223"));
        }  
    /**
	 * 删除目录
	 * @param fileDir
	 * @return
	 */
	public static boolean delDir(File fileDir){
		try {
			System.out.println("删除：fileDir="+fileDir.getAbsolutePath());
			FileUtils.deleteDirectory(fileDir);
			System.out.println("删除完成...");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 从fileDir1目录中删除fileDir2目录中的文件
	 * @param fileDir1:nas目录
	 * @param fileDir2:loacl目录
	 * @return
	 */
	public static boolean delDir(File fileDir1,File fileDir2){
		try {
			System.out.println("删除：fileDir="+fileDir1.getPath());
			File[] files1 = fileDir1.listFiles();
			File[] files2 = fileDir2.listFiles();
			for(File file2:files2){
				String fileName2 = file2.getName();
				for(File file1 : files1){
					String fileName1 = file1.getName();
					if(fileName1.equals(fileName2)){
						file1.delete();
						break;
					}
				}
			}
			System.out.println("删除完成...");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 从fileDir1目录中删除file名字的文件
	 * @param fileDir1:nas目录
	 * @param fileDir2:loacl目录
	 * @return
	 */
	public static boolean delFile(File fileDir1,File file){
		try {
			System.out.println("删除：fileDir="+fileDir1.getPath());
			File[] files1 = fileDir1.listFiles();
			String name = file.getName();
			for(File file1 : files1){
				String fileName1 = file1.getName();
				if(fileName1.equals(name)){
					file1.delete();
					break;
				}
			}
			System.out.println("删除完成...");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 从fileDir目录中删除除filePath文件的所有文件
	 * @param fileDir1:loacl目录
	 * @param filePath:文件
	 * @return
	 */
	public static boolean delFilesExceptFile(String logKey,File fileDir,File filePath){
		try {
			CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},删除{}以外的图片开始",logKey,fileDir,filePath.getAbsolutePath());
			File[] files = fileDir.listFiles();
			for(File file:files){
				if(!filePath.getName().equals(file.getName())){
					file.delete();
					break;
				}
			}
			CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},删除{}以外的图片时结束",logKey,fileDir,filePath.getAbsolutePath());
		} catch (Exception e) {
			CommonParamUtil.common_log.error("logKey:{},imagesPath:{},删除{}以外的图片时异常",logKey,fileDir,filePath.getAbsolutePath(),e);
			return false;
		}
		return true;
	}
	/**
	 * 获取fileDir1中的包含fileDir2文件的绝对路径集合
	 * @param fileDir1:nas目录
	 * @param fileDir2:loacl目录
	 * @return
	 */
	public static List<String> getPaths(File fileDir1,File fileDir2){
		System.out.println("获取文件集合：fileDir="+fileDir1.getPath());
		List<String> fileList = new ArrayList<String>();
		File[] files2 = fileDir2.listFiles();
		String filePath = fileDir1.getAbsolutePath();
		for(File file2:files2){
			fileList.add(filePath+"/"+file2.getName());
		}
		System.out.println("获取完成...");
		return fileList;
	}
	/**
	 * 把目录中的所有文件kopy到指定目录
	 * @param fileDir1
	 * @param fileDir2
	 * @return
	 */
	public static boolean copyDir2Dir(File file1,File file2){
		try {
			FileUtils.copyDirectory(file1, file2);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 把单个文件kopy到指定目录
	 * @param fileDir1
	 * @param fileDir2
	 * @return
	 */
	public static boolean copyFile2Dir(File file,File fileDir){
		try {
			FileUtils.copyFileToDirectory(file, fileDir);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 把单个文件kopy到指定目录
	 * @param fileDir1
	 * @param fileDir2
	 * @return
	 */
	public static boolean copyDirIn2Dir(File fileDir1,File fileDir2){
		try {
			FileUtils.copyDirectoryToDirectory(fileDir1, fileDir2);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}  