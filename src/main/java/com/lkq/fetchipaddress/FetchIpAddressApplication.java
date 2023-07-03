package com.lkq.fetchipaddress;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class FetchIpAddressApplication implements CommandLineRunner{
	private final IpAddressService ipAddressService;
	private final TaskScheduler scheduler;
	private final List<String> replaceWords = Arrays.asList("http://", "https://", "ftp://", "sftp://", "www.");

	public FetchIpAddressApplication(IpAddressService ipAddressService, TaskScheduler scheduler) {
		super();
		this.ipAddressService = ipAddressService;
		this.scheduler = scheduler;
	}

	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		SpringApplication.run(FetchIpAddressApplication.class, args);
		log.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("EXECUTING : fetching IP from command line runner");
		collectionAsStream(args)
			.forEach(arg ->{
				String url = resolveURL(arg);
				if(StringUtils.isNoneEmpty(url)) {
					// "lkq.eu.janraincapture.com"
					IpAddressService.IPFetcher ipFetcher = ipAddressService.getFetcher(url);
					scheduler.scheduleWithFixedDelay(ipFetcher::getIpsByInet, Duration.ofSeconds(3));
					//scheduler.scheduleWithFixedDelay(ipAddressService::getIpsByNSLookup, Duration.ofSeconds(3));
				}
			});
	}

	private String resolveURL(String arg) {
		AtomicReference<String> atomicReference = new AtomicReference<>(arg); 
		replaceWords.stream().forEach(word -> {
			atomicReference.set(atomicReference.get().replace(word, ""));
		});
		String url = atomicReference.get();
		int indexOfSlash = url.indexOf("/");
		if(indexOfSlash != -1) {
			url = url.
					substring(0, url.indexOf("/"));
		}
		
		indexOfSlash = url.indexOf(":");
		if(indexOfSlash != -1) {
			url = url.
					substring(0, url.indexOf(":"));
		}
		
		return url;
	}

	Stream<String> collectionAsStream(String[] array) {
	    return array == null 
	      ? Stream.empty() 
	      : Arrays.stream(array).parallel();
	}
}
