/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.config.mvc;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import cc.aicreate.config.mvc.exception.CustomizedExceptionResolver;
import cc.aicreate.config.mvc.interceptor.ConnectionInterceptor;

/**
 * @since 2016年12月5日 下午4:20:35
 * @version $Id$
 * @author JiangJibo
 *
 */
// @Configuration
@EnableAsync
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "cc.aicreate.mvc")
public class MvcContextConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	private static ApplicationContext mvcContext;

	final static Logger LOGGER = LoggerFactory.getLogger(MvcContextConfig.class);

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	/**
	 * 定义文件上传的处理器
	 * 
	 * @return
	 */
	@Bean("multipartResolver")
	public CommonsMultipartResolver commonsMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10 * 1024 * 1024);
		return multipartResolver;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureMessageConverters(java.util.List)
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		converters.add(new MappingJackson2HttpMessageConverter());
		converters.add(new MappingJackson2XmlHttpMessageConverter());
	}

	/* (non-Javadoc) 配置内容协商机制,比如@ResponseBody注解返回的是什么类型的数据,是json还是xml还是String等等
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureContentNegotiation(org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer)
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(true).useJaf(false).favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(true)
				.defaultContentType(MediaType.APPLICATION_JSON);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureDefaultServletHandling(org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer)
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/* (non-Javadoc)  添加拦截器,推荐使用MappedInterceptor
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		ConnectionInterceptor connIter = new ConnectionInterceptor();
		registry.addInterceptor(new MappedInterceptor(connIter.getIncludePatterns(), connIter.getExcludePatterns(), connIter));
	}

	/* (non-Javadoc) 将自定义异常处理器添加到容器中
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureHandlerExceptionResolvers(java.util.List)
	 */
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(new CustomizedExceptionResolver());
	}

	/* (non-Javadoc) 将自定义的Formatter格式化器添加到容器中
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addFormatters(org.springframework.format.FormatterRegistry)
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureAsyncSupport(org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer)
	 */
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(300 * 1000);
		configurer.setTaskExecutor(threadPoolTaskExecutor);
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		MvcContextConfig.mvcContext = applicationContext;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry)
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("GET", "POST", "DELETE", "PUT").maxAge(3600);

	}

	/**
	 * @return the mvcContext
	 */
	public static ApplicationContext getMvcContext() {
		return mvcContext;
	}

}
