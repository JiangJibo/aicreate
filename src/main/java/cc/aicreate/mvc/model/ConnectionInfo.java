/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.mvc.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

/**
 * @since 2017年7月12日 下午7:13:22
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class ConnectionInfo {

	/**
	 * 连接用户的IP地址
	 */
	private String ipAdress;

	/**
	 * 连接用户的最近操作时间
	 */
	private LocalDateTime lastOperationTime;

	/**
	 * 指定IP连接成功
	 * 
	 * @param ipAdress
	 */
	public void connected(String ipAdress) {
		this.ipAdress = ipAdress;
		this.lastOperationTime = LocalDateTime.now();
	}

	/**
	 * 清除用户连接信息
	 */
	public void cleanInfo() {
		this.ipAdress = null;
		this.lastOperationTime = null;
	}

	/**
	 * 更新最近连接时间
	 */
	public void updateConnectingTime() {
		this.lastOperationTime = LocalDateTime.now();
	}

	/**
	 * @return the ipAdress
	 */
	public String getIpAdress() {
		return ipAdress;
	}

	/**
	 * @return the lastOperationTime
	 */
	public LocalDateTime getLastOperationTime() {
		return lastOperationTime;
	}

}
