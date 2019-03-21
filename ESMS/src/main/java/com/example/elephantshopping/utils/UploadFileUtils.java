package com.example.elephantshopping.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import main.java.com.UpYun;

public class UploadFileUtils 
{
	
	/**
	 * 上传文件
	 * @param multipartFile
	 * @param folderName
	 * @return
	 * @throws IOException
	 */
	public static String uploadFile(MultipartFile multipartFile,String folderName) throws IOException
	{
		String filePath = multipartFile.getOriginalFilename();//上传的图片的路径
		String fileSuffix= filePath.substring(filePath.lastIndexOf(".")+1,filePath.length());//截取出图片后缀名
		String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//随机定义图片名
		String location = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		File file = new File(location+fileName+"."+fileSuffix);
		multipartFile.transferTo(file);
		UpYun upyun = new UpYun("imagexb","xbtiangou","xbtiangou");
		upyun.setDebug(false);
		upyun.setTimeout(30);
		upyun.setContentMD5(UpYun.md5(file));
		String path = "/xbsc/"+folderName+"/"+fileName+"."+fileSuffix;
		upyun.writeFile(path, file,true);
		file.delete();
		return "http://imagexb.test.upcdn.net"+path;
	}
	
	/**
	 * 删除上传的文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path)
	{
		if(path!=null&&!path.equals(""))
		{
			String url = path.substring(path.indexOf("/xbsc"), path.length());
			UpYun upyun = new UpYun("imagexb","xbtiangou","xbtiangou");
			boolean b = upyun.deleteFile(url);
			return b;
		}
		else
		{
			return false;
		}
	}
	
}
