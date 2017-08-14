/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.mvc.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cc.aicreate.config.mvc.exception.CustomizedException;
import cc.aicreate.mvc.handler.DeviceOperationHandler;
import cc.aicreate.mvc.service.DemoService;

/**
 * @since 2017年7月12日 下午7:47:23
 * @version $Id$
 * @author JiangJibo
 *
 */
@Service
public class DemoServiceImpl implements DemoService {

	private static final String WINDOWS_DEMO_FILE_DIR = "C:/Users/dell-7359/Desktop/新建文件夹";
	private static final String LINUX_DEMO_FILE_DIR = "/home/mgb/桌面/示教记录";
	private static final String DEMO_FILE_SUFFIX = ".txt";

	private static final String DEMO_FILE_PATH = "/home/mgb/encoder_data.txt";

	private static final String DEMO_FILE_DIR = LINUX_DEMO_FILE_DIR;

	@Autowired
	private DeviceOperationHandler doHandler;

	/* (non-Javadoc)
	 * @see cc.aicreate.massage.service.DemoService#listDemo()
	 */
	@Override
	public List<String> listDemo() throws Exception {
		File file = new File(DEMO_FILE_DIR);
		Assert.isTrue(file.isDirectory(), "示教目录");
		File[] files = file.listFiles();
		Arrays.sort(files, (a, b) -> {
			return a.lastModified() > b.lastModified() ? 1 : -1;
		});
		List<String> demos = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			demos.add(files[i].getName().substring(0, files[i].getName().lastIndexOf(".")));
		}
		return demos;
	}

	/* (non-Javadoc)
	 * @see cc.aicreate.massage.service.DemoService#startDemonstrating()
	 */
	@Override
	public boolean startDemonstrating() throws Exception {
		// return true;
		doHandler.dataRecord();
		return doHandler.getResult(5000, Boolean.class);
	}

	/* (non-Javadoc)
	 * @see cc.aicreate.massage.service.DemoService#finishDemonstrating(java.lang.String)
	 */
	@Override
	public boolean finishDemonstrating(String demoName) throws Exception {
		doHandler.stopRecord(demoName);
		List<String> demos = Arrays.asList(new File(DEMO_FILE_DIR).list());
		if (demos.contains(demoName + DEMO_FILE_SUFFIX)) {
			throw new CustomizedException("示教记录已存在");
		}
		new File(DEMO_FILE_DIR + "/" + demoName + DEMO_FILE_SUFFIX).createNewFile();
		// return true;
		return doHandler.getResult(5000, Boolean.class);
	}

	/* (non-Javadoc)
	 * @see cc.aicreate.massage.service.DemoService#showDemo(java.lang.String)
	 */
	@Override
	public boolean showDemo(String demoName) throws Exception {
		File file = new File(DEMO_FILE_DIR + "/" + demoName + DEMO_FILE_SUFFIX);
		Assert.state(file.exists(), file.getAbsolutePath() + "示教案列不存在");
		Thread.sleep(10000);
		// doHandler.reshow(demoName);
		// demoName = "/home/mgb/IS620/cmake-build-debug/encoder_data.txt";
		doHandler.reshow("encoder_data.txt");
		return doHandler.getResult(Integer.MAX_VALUE, Boolean.class);
		// return true;
	}

}
