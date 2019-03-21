package com.example.elephantshopping.shiro;

import java.util.Map;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.elephantshopping.service.LoginService;

public class Realm extends AuthenticatingRealm {
	private static final Logger logger = LoggerFactory.getLogger(Realm.class);
	@Autowired
	private LoginService loginService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
		String userPhone = token.getPrincipal().toString();
		logger.info("======" + userPhone + ":用户登陆认证======");
		Map<String, Object> user = loginService.getUserInfo(userPhone);
		if (user != null) {
			String PHONE = user.get("PHONE").toString();
			String PASSWORD = user.get("PASSWORD").toString();
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(PHONE, PASSWORD, getName());
			return authenticationInfo;
		}
		return null;
	}

	/**
	 * 授权认证
	 * 
	 * @param principalCollection
	 * @return
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		String phone = principalCollection.getPrimaryPrincipal().toString();
		logger.info("======" + phone + ":用户授权认证======");
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(loginService.getUserPermission(phone));
		return simpleAuthorizationInfo;
	}

}
