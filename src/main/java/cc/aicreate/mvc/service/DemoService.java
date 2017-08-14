/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.mvc.service;

import java.util.List;

/**
 * @since 2017年7月12日 下午7:47:05
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface DemoService {

	/**
	 * 获取示教案列列表
	 * 
	 * @return
	 */
	public List<String> listDemo() throws Exception;

	/**
	 * 开始示教
	 * 
	 * @return
	 */
	public boolean startDemonstrating() throws Exception;

	/**
	 * 结束示教,保持示教文件
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean finishDemonstrating(String demoName) throws Exception;

	/**
	 * 开始演示
	 * 
	 * @param demoName
	 * @return
	 */
	public boolean showDemo(String demoName) throws Exception;

}
