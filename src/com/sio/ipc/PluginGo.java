package com.sio.ipc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sio.plugin.Terminal;

public class PluginGo implements PluginThread {
	private static final int EXECUTIONS = 8;
	private static final int THREAD_LIMIT = 10;
	private static final int THREAD_PRIOPRITY = Thread.MIN_PRIORITY;
	private IPCm ipc = IPCm.instance;
	private ExecutorService threadPool;
	private double thread_interval;
	private Iterator<Terminal> _terminal_;
	private boolean pluginRunning;
	public PluginGo() {
		threadPool = Executors.newFixedThreadPool(THREAD_LIMIT);
		ipc.reloadPlugins();
	}
	
	@Override
	public void loadAndInit() {
		{
			Terminal terminal = null;
			while ((terminal = ipc.getUnresolvedTerminal()) != null){
				final Terminal t = terminal;
				Runnable runnable = new Runnable() {
					public void run() {
						t.beforeStart();
						t.start();
						t.afterStart();
						
						try {
							Thread.currentThread().join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				};
				threadPool.execute(runnable);
				
			}
		}
	}
	
	@Override
	public void go() {
		pluginRunning = true;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		{
//			init itorator
			Collection<Terminal> terminals = cloneTerminalCollection();	
			
//			calculate simple rest
			{
				if(terminals != null && terminals.size()>0){
					thread_interval = (THREAD_LIMIT/terminals.size()) * (100/THREAD_LIMIT)*0.01;		//performance
					thread_interval = (1000/EXECUTIONS)*thread_interval;
				}
			}
			
			if(terminals.size()>0){
				for(int i=0; i<THREAD_LIMIT; i++){
					if(i<terminals.size()){
						Runnable runnable = new Runnable() {
							@Override
							public void run() {
								Thread.currentThread().setPriority(THREAD_PRIOPRITY);
								while(pluginRunning){
									Terminal terminal = runOptions();
									runEvent(terminal);
									try {
										Thread.sleep((long) thread_interval);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						};
						threadPool.execute(runnable);
					}
				}
			}
		}
	}
	
	private synchronized Terminal runOptions(){
		if(_terminal_.hasNext()){
			if(_terminal_.hasNext()){
				return _terminal_.next();
			}
			return null;
		} else {
			cloneTerminalCollection();
			if(_terminal_.hasNext()){
				return _terminal_.next();
			}
			return null;
		}
	}
	
	private void runEvent(Terminal terminal){
		if(terminal != null){
			terminal.onEvent();
		}
	}
	
	private Collection<Terminal> cloneTerminalCollection(){
		Collection<Terminal> terminals = new HashSet<>();
		terminals.addAll(ipc.getLoadedTerminals().values());
		_terminal_ = terminals.iterator();
		return terminals;
	}
	
	@Override
	public void stop() {
		pluginRunning = false;
		Map<String, Terminal> terminals = ipc.getLoadedTerminals();
		for (Terminal terminal : terminals.values()){
			final Terminal t = terminal;
			Runnable runnable = new Runnable() {
				public void run() {
					t.beforeStop();
					t.stop();
				}
			};
			threadPool.execute(runnable);
			
		}
		ipc.reloadPlugins();
	}

	@Override
	public void reset() {
		stop();
		loadAndInit();
		go();
	}

}
