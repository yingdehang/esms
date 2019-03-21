package com.example.elephantshopping.service.webmaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.webmaster.ViceWebmasterDao;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class ViceWebmsterService {
	@Autowired
	private ViceWebmasterDao viceWebmasterDao;

	/**
	 * 查询副站长list
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryvicemasterlist(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		map.put("page", (page - 1) * limit);
		map.put("limit", limit);
		List<Map<String, Object>> list = viceWebmasterDao.queryvicemasterlist(map);
		int count = viceWebmasterDao.queryvicemasterlistCount(map);
		map.clear();
		map.put("data", DateFormatUtils.timeforDateType(list, "REGISTERED_TIME"));
		map.put("code", 0);
		map.put("count", count);
		map.put("msg", "");
		return map;
	}

	/**
	 * 添加副站长及验证：0：操作失败，1：操作成功，2：用户已经是站长，不能成为副站长，3：推荐者手机号无效，4：用户已经是副站长,5：用户手机号未注册
	 * 
	 * @param masterPhone
	 * @param upPhone
	 * @return
	 */
	@Transactional
	public int addvicemaster(String masterPhone, String upPhone) {
		// 查询用户手机号是否为注册用户
		Map<String, Object> user = viceWebmasterDao.isregisterUser(masterPhone);
		if (null == user) {
			return 5;
		} else if (null != user.get("IS_ZZ") && user.get("IS_ZZ").toString().equals("1")) {
			return 2;
		} else if (null != user.get("IS_FZZ") && user.get("IS_FZZ").toString().equals("1")) {
			return 4;
		}

		// 如果推荐者手机号不为空；验证推荐者手机号是否有效
		if (null != upPhone && (!upPhone.equals(""))) {
			Map<String, Object> presenter = viceWebmasterDao.presenterPhone(upPhone);
			// 无效
			if (null == presenter) {
				return 3;
			}
			// 有效
			else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", presenter.get("USERS_ID"));
				map.put("uuid", UUIDUtils.randomID());
				// 查询该推荐者推荐了多少人
				int n = viceWebmasterDao.presenterNumber(upPhone);
				if (n < 5) {
					map.put("money", 1000);
				} else {
					map.put("money", 2000);
				}
				// 添加收益流水
				viceWebmasterDao.insertRunningWater(map);
				// 添加零花钱收支
				viceWebmasterDao.insertPocketMoney(map);
				// 添加推荐者零花钱
				viceWebmasterDao.addPersenterPocketMoney(map);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("masterPhone", masterPhone);
		map.put("upPhone", upPhone);
		return viceWebmasterDao.updateUserIsFzz(map);
	}

	/**
	 * 撤销副站长
	 * 
	 * @param userId
	 * @return
	 */
	public int cancelViceMaster(String userId) {
		return viceWebmasterDao.cancelViceMaster(userId);
	}
}
