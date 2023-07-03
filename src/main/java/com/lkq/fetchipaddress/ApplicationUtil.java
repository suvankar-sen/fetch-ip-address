package com.lkq.fetchipaddress;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

public class ApplicationUtil {
	public static void printStack(Logger log, Exception e) {
		String stacktrace = ExceptionUtils.getStackTrace(e);
		log.error(stacktrace);
	}
}
