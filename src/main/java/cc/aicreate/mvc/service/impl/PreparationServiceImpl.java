/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.mvc.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.aicreate.mvc.handler.DeviceOperationHandler;
import cc.aicreate.mvc.model.ConnectionInfo;
import cc.aicreate.mvc.service.PreparationService;

/**
 * @since 2017年7月12日 下午7:56:25
 * @version $Id$
 * @author JiangJibo
 *
 */
@Service
public class PreparationServiceImpl implements PreparationService {

	final static Logger LOGGER = LoggerFactory.getLogger(PreparationServiceImpl.class);

	@Autowired
	private ConnectionInfo connectionInfo;

	@Autowired
	private DeviceOperationHandler doHandler;

	@Autowired
	private HttpServletRequest request;

	/* (non-Javadoc)
	 * @see cc.aicreate.mvc.service.PreparationService#doPreparing()
	 */
	@Override
	public boolean doPreparing() throws Exception {
		doHandler.doPreparing();
		return doHandler.getResult(Integer.MAX_VALUE, Boolean.class);
	}

	/* (non-Javadoc)
	 * @see cc.aicreate.massage.service.PreparationService#home()
	 */
	@Override
	public boolean home() throws Exception {
		doHandler.home();
		return doHandler.getResult(Integer.MAX_VALUE, Boolean.class);
	}

}
