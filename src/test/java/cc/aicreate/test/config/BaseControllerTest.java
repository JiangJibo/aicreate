/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.test.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import cc.aicreate.config.mvc.MvcContextConfig;
import cc.aicreate.config.mvc.exception.CustomizedException;
import cc.aicreate.config.root.RootContextConfig;

/**
 * @since 2016年12月8日 下午4:45:26
 * @version $Id$
 * @author JiangJibo
 *
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootContextConfig.class, MvcContextConfig.class })
public abstract class BaseControllerTest {

	protected Gson gson;

	private MockMvc mockMvc;
	protected MockHttpSession session;

	protected Object mappedController;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@Before()
	public void setup() {
		gson = new Gson();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); // 初始化MockMvc对象
		init();
	}

	/**
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 */
	public String getRequest(String urlTemplate, Object... urlVariables) {
		return doRequest(RequestMethod.GET, null, null, urlTemplate, urlVariables);
	}

	/**
	 * @param contentType
	 * @param content
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 */
	public String postRequest(MediaType contentType, String content, String urlTemplate, Object... urlVariables) {
		return doRequest(RequestMethod.POST, contentType, content, urlTemplate, urlVariables);
	}

	/**
	 * @param content
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 */
	public String postRequest(String content, String urlTemplate, Object... urlVariables) {
		return doRequest(RequestMethod.POST, MediaType.APPLICATION_JSON, content, urlTemplate, urlVariables);
	}

	/**
	 * @param contentType
	 * @param content
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 */
	public String putRequest(MediaType contentType, String content, String urlTemplate, Object... urlVariables) {
		return doRequest(RequestMethod.PUT, contentType, content, urlTemplate, urlVariables);
	}

	/**
	 * @param content
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 */
	public String putRequest(String content, String urlTemplate, Object... urlVariables) {
		return doRequest(RequestMethod.PUT, MediaType.APPLICATION_JSON, content, urlTemplate, urlVariables);
	}

	/**
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 */
	public String deleteRequest(String urlTemplate, Object... urlVariables) {
		return doRequest(RequestMethod.DELETE, null, null, urlTemplate, urlVariables);
	}

	/**
	 * @param filePath
	 * @return
	 */
	public String readJsonFile(String filePath) {
		BufferedReader bufferReader = null;
		try {
			File file = new File(filePath);
			long length = file.length();
			if (length > 5 * 1024 * 1024) {
				throw new IllegalArgumentException("试图读取的文件大小超过5M");
			}
			return IOUtils.toString(new FileInputStream(file), "UTF-8");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(filePath, e);
		} finally {
			try {
				if (null != bufferReader) {
					bufferReader.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 
	 * @param method
	 * @param contentType
	 * @param content
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 */
	protected String doRequest(RequestMethod method, MediaType contentType, String content, String urlTemplate, Object... urlVariables) {
		checkMappedController(urlTemplate);
		MockHttpServletRequestBuilder builder;
		if (method == RequestMethod.GET) {
			builder = MockMvcRequestBuilders.get(urlTemplate, urlVariables);
		} else if (method == RequestMethod.POST) {
			builder = MockMvcRequestBuilders.post(urlTemplate, urlVariables);
		} else if (method == RequestMethod.PUT) {
			builder = MockMvcRequestBuilders.put(urlTemplate, urlVariables);
		} else if (method == RequestMethod.DELETE) {
			builder = MockMvcRequestBuilders.delete(urlTemplate, urlVariables);
		} else {
			throw new UnsupportedOperationException(method.name());
		}
		if (content != null) {
			builder.contentType(contentType).content(content);
		}
		try {
			return mockMvc.perform(builder).andReturn().getResponse().getContentAsString();
		} catch (Exception e) {
			return hanldeRequestException(e, method, contentType, content, urlTemplate, urlVariables);
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 *            文件域名称
	 * @param data
	 *            数据
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public String fileUpload(String file, String path, String urlTemplate, Object... urlVariables) {
		checkMappedController(urlTemplate);
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload(urlTemplate, urlVariables);
		try {
			builder.file(file, FileUtils.readFileToByteArray(new File(path)));
			return mockMvc.perform(builder).andReturn().getResponse().getContentAsString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param e
	 * @param method
	 * @param contentType
	 * @param content
	 * @param urlTemplate
	 * @param urlVariables
	 * @return
	 */
	protected String hanldeRequestException(Exception e, RequestMethod method, MediaType contentType, String content, String urlTemplate,
			Object... urlVariables) {
		return null;
	}

	/**
	 * 校验请求的路由是否匹配指定的Controller,主要根据Controller上的@RequestMapping()来校验
	 * 
	 * @param urlTemplate
	 */
	private void checkMappedController(String urlTemplate) {
		Assert.notNull(mappedController, "必须指明当前ControllerTest映射到哪个Controller");
		Class<?> clazz;
		if (AopUtils.isCglibProxy(mappedController) || AopUtils.isAopProxy(mappedController)) {
			clazz = AopUtils.getTargetClass(mappedController);
		} else {
			clazz = mappedController.getClass();
		}
		RequestMapping ann = clazz.getAnnotation(RequestMapping.class);
		Assert.notNull(ann, "mappedController必须含有[RequestMapping]注解");
		boolean mapped = false;
		for (String value : ann.value()) {
			if (value.contains("{")) {
				value = value.substring(0, value.indexOf("{") + 1); // 当@RequestMapping("/user/{id}")形式时,截取"/user"
			}
			if (urlTemplate.startsWith(value)) {
				mapped = true;
				break;
			}
		}
		if (!mapped) {
			throw new CustomizedException("请求路由[" + urlTemplate + "]不匹配Controller：" + mappedController.getClass().getName());
		}
	}

	/**
	 * 模板方法,由子类重写以决定是否在运行之前登录
	 */
	protected abstract void init();

}
