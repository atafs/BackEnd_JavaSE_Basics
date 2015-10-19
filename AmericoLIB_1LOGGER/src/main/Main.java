package main;

import log.LogMessage;

public class Main {
	
	//LOG4J LOGGER
	private static LogMessage logger = new LogMessage();
	
	//MAIN
	public static void main(String[] args) {
		System.out.println("I AM HERE");
		logger.getLog().info("I AM HERE");
		logger.getLog().debug("I AM HERE");
	}
}
