package com.qminder.assignment.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;

/**
 * Cache configuration for burger joint results. <br>
 * Once the joints retrieved, they can be kept for 1 hour not to call too much Foursquare APIs
 *
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

	@Bean
	public Caffeine caffeineConfig() {
	    return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES);
	}
	
	@Bean
	public CacheManager cacheManager(Caffeine caffeine) {
	    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
	    caffeineCacheManager.setCaffeine(caffeine);
	    return caffeineCacheManager;
	}
}
