package com.sio.ipc;

import java.io.Console;
import java.io.File;

public class DefaultConsole extends ConsoleSystem {
	private boolean isStart;
	private Console console = getConsole();
	
	@Override
	public void startGatherCommand() {
		if(!isStart){
			isStart = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(isStart){
						int dir_sub_index = getCurrentDIR().lastIndexOf(File.separatorChar);
						if(dir_sub_index>0){
							console.printf(getPrefix(), getCurrentDIR().substring(dir_sub_index));
						} else {
							console.printf(getPrefix(), "root");
						}
						String comd = console.readLine();
						if(comd != null && comd.length()>0)
							command(comd);
					
					}
				}
			}).start();
		}
		
	}
	
	private void command(String commd){
		switch(commd){
			case "flowstack":
				stopGatherCommand();
				break;
			default:
				console.printf("unkown command:" + commd + "\r\n");
				break;
		}
		
	}
	
	@Override
	public void stopGatherCommand() {
		isStart = false;
	}

}
