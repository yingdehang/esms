package com.example.elephantshopping.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.entity.Post;
import com.example.elephantshopping.service.TestService;
import com.example.elephantshopping.utils.UploadFileUtils;

@Controller
@RequestMapping("test")
public class TestController {
	@Autowired
	private TestService testService;

	@RequestMapping("getAll")
	@ResponseBody
	public List<Map<String, Object>> getAll() {
		return testService.getAll();
	}

	@RequestMapping("activity")
	public ModelAndView toactivity(ModelAndView mv, String name) {
		mv.setViewName("/activity/" + name);
		return mv;
	}

	@RequestMapping("totest")
	public ModelAndView toIndex(ModelAndView mv) {
		// System.out.println("跳转主页中......");
		List<Post> plist = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			plist.add(new Post("1", i, "theme", "pcontent", new Date(),
					"http://img.zcool.cn/community/0117e2571b8b246ac72538120dd8a4.jpg@1280w_1l_2o_100sh.jpg",
					"filename2", "uid"));
		}
		mv.addObject("plist", plist);
		mv.setViewName("/test/test");
		return mv;
	}

	@RequestMapping("toUploadTest")
	public ModelAndView toUploadTest(ModelAndView mv) {
		mv.setViewName("/uploadTest");
		return mv;
	}

	@RequestMapping("upload")
	public void upload(@RequestParam("license") MultipartFile license) throws IllegalStateException, IOException {
		System.out.println(UploadFileUtils.uploadFile(license, "goods"));

	}
}
