package com.example.elephantshopping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.elephantshopping.service.operationsManage.ExperienceService;

/**
 * 礼包数量管理线程
 * @author XB
 *
 */
public class ActivityAmountControl implements Runnable{
	private String id;
	private ExperienceService experienceService;
	
	public ActivityAmountControl(String id,ExperienceService experienceService){
		this.id = id;
		this.experienceService = experienceService;
	}
	
	@Override
	public void run() {
		String now="";
		String time="";
		int stop = 0;
		Map<String,Object> map = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		do{
			map = experienceService.getSetTime(id);
			now = sdf.format(new Date());
			time = sdf.format(map.get("TIME"));
			stop = (int) map.get("STOP");
		}while(!now.equals(time)&&stop==0);
		if(stop==0){
			String acId = (String) map.get("RELATIONID");
			if(map.get("FIXED")!=null){//固定值
				int fixed = (int) map.get("FIXED");
				experienceService.changeActivityNumber(acId, fixed);
			}else{//增减值
				int changed = (int) map.get("CHANGED");
				int number = experienceService.getActivityNumber(acId);
				if(number+changed<0){
					experienceService.changeActivityNumber(acId,0);
				}else{
					experienceService.addActivityNumber(acId,changed);
				}
			}
		}
		experienceService.deleteSetTime(id);//删除上线设置
	}

}
