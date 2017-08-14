/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.mvc.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 2017年7月12日 下午8:12:56
 * @version $Id$
 * @author JiangJibo
 *
 */
public abstract class HttpRequestUtils {

	/**
	 * 获取Http请求中的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAdress(HttpServletRequest request) {
		String ipAdress = request.getHeader("x-forwarded-for"); // 当使用反向代理时,会将请求IP存入此属性中
		return ipAdress != null ? ipAdress : request.getRemoteAddr();
	}

}
