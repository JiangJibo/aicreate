/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.config;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import cc.aicreate.config.mvc.MvcContextConfig;
import cc.aicreate.config.root.RootContextConfig;

/**
 * 对应着框架内部的web.xml文件内的配置,系统入口
 * 
 * @since 2016年12月5日 下午4:19:09
 * @version $Id$
 * @author JiangJibo
 *
 */
public class WebContextInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/* (non-Javadoc) Spring容器的配置类
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootContextConfig.class };
	}

	/* (non-Javadoc) Servlet容器的配置类(SpringMVC容器)
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses()
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { MvcContextConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	/* (non-Javadoc) 添加Filter
	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#getServletFilters()
	 */
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return new Filter[] { characterEncodingFilter };
	}

}
