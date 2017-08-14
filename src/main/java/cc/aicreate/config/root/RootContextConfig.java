/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.config.root;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @since 2017年1月10日 上午8:52:37
 * @version $Id$
 * @author JiangJibo
 *
 */
@EnableAsync
public class RootContextConfig {

	/**
	 * Spring内部自定义线程池,可以对@Async注解以及Controler返回的Callable,WebAsyncTask和DeferredResult等Spring内异步线程的支持
	 * 
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setKeepAliveSeconds(300);
		executor.setQueueCapacity(30);
		executor.setThreadNamePrefix("Spring-ThreadPool#");
		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

}
