/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.mvc.service;

/**
 * @since 2017年7月12日 下午7:56:16
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface PreparationService {

	/**
	 * 做电机启动之后的一系列准备工作
	 * 
	 * @return
	 */
	public boolean doPreparing() throws Exception;

	/**
	 * 复位
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean home() throws Exception;

}
