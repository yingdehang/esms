package com.example.elephantshopping.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
* @ClassName: SendSMSUtils  
* @Description: TODO(短信工具类)  
* @author XB  
* @date 2018年2月27日  
*
 */
public class SendSMSUtils 
{
	public static String sendHttpPost(String code,String param,String phone){  
        HttpURLConnection conn = null;  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String responseStr = null;  
        try {  
            // 建立连接  
            URL httpUrl = new URL("http://api.sms.cn/sms/?ac=send&uid=tiangouxb&pwd=a2a720f45165a28e4134fecbdcc38299&mobile="+phone+"&content=本次验证码为"+code+"，请尽快填写以便使用【象本商城】");  
            conn = (HttpURLConnection)httpUrl.openConnection();  
            //设置基本属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            conn.setDoInput(true);  
            conn.setDoOutput(true);  
            //conn.setRequestMethod("POST");  
            //设置超时时间  
            conn.setConnectTimeout(30000);  
            conn.setReadTimeout(30000);  
            //输出流传参  
            out = new PrintWriter(conn.getOutputStream());  
            out.print(new String(param.getBytes(), "UTF-8"));  
            out.flush();  
            // 定义输入流读取响应  
            in = new BufferedReader(  
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));  
            // 读取响应数据  
            String line = "";  
            StringBuffer result = new StringBuffer();  
            while ((line = in.readLine()) != null) {  
                result.append(line);  
            }  
            responseStr = result.toString();  
        }catch(MalformedURLException e){  
        	e.printStackTrace();
        }catch (Exception e) {  
        	e.printStackTrace();
        }finally{  
            try {  
                //释放资源  
                if (null != in){  
                    in.close();  
                }  
                if (null != out){  
                    out.close();  
                }  
                if (null != conn){  
                    conn.disconnect();  
                }  
            } catch (IOException e) {  
            	e.printStackTrace();
            }  
        }  
        return responseStr;  
    } 
	
	
	/**
	 * 体验礼包过期提醒
	 */
	public static String orverdue(String phone){  
        HttpURLConnection conn = null;  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String responseStr = null;  
        try {  
            // 建立连接  
            URL httpUrl = new URL("http://api.sms.cn/sms/?ac=send&uid=tiangouxb&pwd=a2a720f45165a28e4134fecbdcc38299&mobile="+phone+"&content=您领取的体验礼包将在明晚24点过期，快来看看吧，及时激活才能继续领取奖励金哦！【象本商城】");  
            conn = (HttpURLConnection)httpUrl.openConnection();  
            //设置基本属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            conn.setDoInput(true);  
            conn.setDoOutput(true);  
            //conn.setRequestMethod("POST");  
            //设置超时时间  
            conn.setConnectTimeout(30000);  
            conn.setReadTimeout(30000);  
            //输出流传参  
            out = new PrintWriter(conn.getOutputStream());  
            out.flush();  
            // 定义输入流读取响应  
            in = new BufferedReader(  
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));  
            // 读取响应数据  
            String line = "";  
            StringBuffer result = new StringBuffer();  
            while ((line = in.readLine()) != null) {  
                result.append(line);  
            }  
            responseStr = result.toString();  
        }catch(MalformedURLException e){  
        	e.printStackTrace();
        }catch (Exception e) {  
        	e.printStackTrace();
        }finally{  
            try {  
                //释放资源  
                if (null != in){  
                    in.close();  
                }  
                if (null != out){  
                    out.close();  
                }  
                if (null != conn){  
                    conn.disconnect();  
                }  
            } catch (IOException e) {  
            	e.printStackTrace();
            }  
        }  
        return responseStr;  
    }  
	
	/**
	 * 体验礼包激活提醒
	 */
	public static String activateRemind(String phone,String name){  
        HttpURLConnection conn = null;  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String responseStr = null;  
        try {  
            // 建立连接  
            URL httpUrl = new URL("http://api.sms.cn/sms/?ac=send&uid=tiangouxb&pwd=a2a720f45165a28e4134fecbdcc38299&mobile="+phone+"&content=您已经成功购买了"+name+"礼包，系统将暂停自动签到，请务必每日手动签到领取奖励金。【象本商城】");  
            conn = (HttpURLConnection)httpUrl.openConnection();  
            //设置基本属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            conn.setDoInput(true);  
            conn.setDoOutput(true);  
            //conn.setRequestMethod("POST");  
            //设置超时时间  
            conn.setConnectTimeout(30000);  
            conn.setReadTimeout(30000);  
            //输出流传参  
            out = new PrintWriter(conn.getOutputStream());  
            out.flush();  
            // 定义输入流读取响应  
            in = new BufferedReader(  
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));  
            // 读取响应数据  
            String line = "";  
            StringBuffer result = new StringBuffer();  
            while ((line = in.readLine()) != null) {  
                result.append(line);  
            }  
            responseStr = result.toString();  
        }catch(MalformedURLException e){  
        	e.printStackTrace();
        }catch (Exception e) {  
        	e.printStackTrace();
        }finally{  
            try {  
                //释放资源  
                if (null != in){  
                    in.close();  
                }  
                if (null != out){  
                    out.close();  
                }  
                if (null != conn){  
                    conn.disconnect();  
                }  
            } catch (IOException e) {  
            	e.printStackTrace();
            }  
        }  
        return responseStr;  
    }
}
