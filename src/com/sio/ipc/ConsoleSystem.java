package com.sio.ipc;

import java.io.Console;

public abstract class ConsoleSystem implements CommandLine {
	private static final String CURRENT_DIR = System.getProperty("user.dir");
	private static final String PREFIX = "API@%s:~> ";
	private static final java.io.Console console = System.console();
	
	protected String getCurrentDIR(){
		return CURRENT_DIR;
	}
	
	protected String getPrefix(){
		return PREFIX;
	}
	
	protected Console getConsole(){
		return console;
	}
}
