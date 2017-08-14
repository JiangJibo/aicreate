/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cc.aicreate.mvc.service.PreparationService;
import cc.aicreate.mvc.util.ExecLinuxCMD;

/**
 * 准备阶段
 * 
 * @since 2017年7月12日 下午7:49:13
 * @version $Id$
 * @author JiangJibo
 *
 */
@RestController
@RequestMapping("/preparation")
public class PreparationController {

	@Autowired
	private PreparationService preparationService;

	/**
	 * 复位
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/home", method = RequestMethod.PUT)
	public boolean home() throws Exception {
		return preparationService.home();
	}

	/**
	 * 执行页面输入的指令
	 * 
	 * @param command
	 */
	@RequestMapping(value = "/command/{command}", method = RequestMethod.PUT)
	public String executeCommand(@PathVariable String command) {
		return ExecLinuxCMD.exec(command).toString();
	}

}
