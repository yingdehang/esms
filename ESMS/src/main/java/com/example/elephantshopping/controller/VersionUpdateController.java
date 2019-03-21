package com.example.elephantshopping.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.elephantshopping.service.VersionUpdateService;

/**
 * 版本更新下载Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("versionUpdate")
public class VersionUpdateController {
	@Autowired
	private VersionUpdateService versionUpdateService;
	
	/**
	 * 下载apk
	 * @param dType
	 * @param request
	 * @param response
	 */
	@RequestMapping("getApk")
	public void getApk(String dType,HttpServletRequest request, HttpServletResponse response){
		String dId = UUID.randomUUID().toString().replaceAll("-", "");
		versionUpdateService.record(dId,dType);//下载记录
		// Get your file stream from wherever.  
        String fullPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/xbsc.apk";  
        File downloadFile = new File(fullPath);  
  
        ServletContext context = request.getServletContext();  
  
        // get MIME type of the file  
        String mimeType = context.getMimeType(fullPath);  
        if (mimeType == null) {  
            // set to binary type if MIME mapping not found  
            mimeType = "application/octet-stream";  
//            System.out.println("context getMimeType is null");  
        }  
//        System.out.println("MIME type: " + mimeType);  
  
        // set content attributes for the response  
        response.setContentType(mimeType);  
        response.setContentLength((int) downloadFile.length());  
  
        // set headers for the response  
        String headerKey = "Content-Disposition";  
        String headerValue = String.format("attachment; filename=\"%s\"",  
                downloadFile.getName());  
        response.setHeader(headerKey, headerValue);  
  
        // Copy the stream to the response's output stream.  
        try {  
            InputStream myStream = new FileInputStream(fullPath);  
            IOUtils.copy(myStream, response.getOutputStream());  
            response.flushBuffer();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
//	@RequestMapping("get")
//	public void get(HttpServletResponse res){
//		String fileName = "xbsc.apk";
//	    res.setHeader("content-type", "application/octet-stream");
//	    res.setContentType("application/octet-stream");
//	    res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//	    byte[] buff = new byte[1024];
//	    BufferedInputStream bis = null;
//	    OutputStream os = null;
//	    try {
//	      os = res.getOutputStream();
//	      bis = new BufferedInputStream(new FileInputStream(new File(ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/xbsc.apk")));
//	      int i = bis.read(buff);
//	      while (i != -1) {
//	        os.write(buff, 0, buff.length);
//	        os.flush();
//	        i = bis.read(buff);
//	      }
//	    } catch (IOException e) {
//	      e.printStackTrace();
//	    } finally {
//	      if (bis != null) {
//	        try {
//	          bis.close();
//	        } catch (IOException e) {
//	          e.printStackTrace();
//	        }
//	      }
//	    }
//	  }
}
