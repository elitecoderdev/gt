package cl.gob.sna.gtime.processor.config;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public KeyGenerator multiplyKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				return method.getName() + "_" + Arrays.toString(params);
			}
		};
		
	}

}
