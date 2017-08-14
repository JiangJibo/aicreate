/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.mvc.controller;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cc.aicreate.mvc.service.DemoService;

/**
 * @since 2017年7月12日 下午7:48:01
 * @version $Id$
 * @author JiangJibo
 *
 */
@RestController
@RequestMapping("/demos")
public class DemoController {

	@Autowired
	private DemoService demoService;

	/**
	 * 获取示教案列列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<String> listDemo() throws Exception {
		return demoService.listDemo();
	}

	/**
	 * 开始记录
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/record", method = RequestMethod.PUT)
	public boolean startRecording() throws Exception {
		return demoService.startDemonstrating();
	}

	/**
	 * 保存记录
	 * 
	 * @param demoName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/record/{demoName}", method = RequestMethod.POST)
	public boolean finishRecording(@PathVariable String demoName) throws Exception {
		return demoService.finishDemonstrating(demoName);
	}

	/**
	 * 演示
	 * 
	 * @param demoName
	 * @throws Exception
	 */
	@RequestMapping(value = "/{demoName}", method = RequestMethod.PUT)
	public Callable<Boolean> showDemo(@PathVariable String demoName) throws Exception {
		return () -> {
			return demoService.showDemo(demoName);
		};
	}

}
