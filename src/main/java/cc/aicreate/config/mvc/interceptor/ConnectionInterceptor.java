/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.config.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cc.aicreate.config.mvc.MvcContextConfig;
import cc.aicreate.config.mvc.exception.CustomizedException;
import cc.aicreate.mvc.model.ConnectionInfo;
import cc.aicreate.mvc.util.HttpRequestUtils;

/**
 * 确保当前只有一个用户在操作设备
 * 
 * @since 2017年7月12日 下午6:37:49
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ConnectionInterceptor extends HandlerInterceptorAdapter {

	final static Logger LOGGER = LoggerFactory.getLogger(ConnectionInterceptor.class);

	private final String[] includePatterns = {};
	private final String[] excludePatterns = { "/connection/**" };

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ipAdress = HttpRequestUtils.getIpAdress(request);
		ConnectionInfo userInfov = MvcContextConfig.getMvcContext().getBean("userInfo", ConnectionInfo.class);
		String connectedIpAdress = userInfov.getIpAdress();
		if (connectedIpAdress != ipAdress) {
			throw new CustomizedException("IP：" + ipAdress + "尝试在:" + connectedIpAdress + "控制期间越权操控");
		}
		userInfov.updateConnectingTime();
		return true;
	}

	/**
	 * @return the includePatterns
	 */
	public String[] getIncludePatterns() {
		return includePatterns;
	}

	/**
	 * @return the excludePatterns
	 */
	public String[] getExcludePatterns() {
		return excludePatterns;
	}

}
