package com.xhtt;

import com.xhtt.common.utils.RedisUtils;
import com.xhtt.modules.cfg.entity.CfgBaseRegionEntity;
import com.xhtt.modules.cfg.service.CfgBaseRegionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.List;

@SpringBootApplication
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
//@Import({DynamicDataSourceConfigHikari.class})
//@MapperScan("com.xhtt.modules.**.dao")
//@EnableAsync//开启异步
@EnableScheduling//开启定时任务
//@EnableAspectJAutoProxy(proxyTargetClass=true, exposeProxy=true)
//@EnableRabbit //开启rabbitemq
@EnableOpenApi
public class AdminApplication implements ApplicationRunner {
	@Autowired
   private CfgBaseRegionService cfgBaseRegionService;
	@Autowired
	private RedisUtils redisUtils;
	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<CfgBaseRegionEntity> list =cfgBaseRegionService.getXzqyCountAndTown();
		if(CollectionUtils.isNotEmpty(list)){
			list.forEach(x->{
				redisUtils.set(x.getRegionCode(),x.getName(),-1);
			});
		}
	}
}
