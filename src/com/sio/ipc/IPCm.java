package com.sio.ipc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import com.sio.plugin.Terminal;

public class IPCm implements Loadable {
	private static final String CLIENT_DIR = "./plugins";
	private File directory;
	private FileFilter fileFilter;
	private Map<String,Class<?>> classes;
	private Map<String,Terminal> loadedTerminals;
	
	public static IPCm instance = new IPCm();
	
	private IPCm() {
		classes = new HashMap<>();
		loadedTerminals = new HashMap<>();
		directory = new File(CLIENT_DIR);
		fileFilter = new DefaultFileFilter();
		if(!directory.exists()){
			directory.mkdirs();
		}
	}
	
	private void loadClasses() throws MalformedURLException {
		if(directory.exists() && directory.isDirectory()){
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			Stack<File> stack = new Stack<>();
			stack.push(directory);
			while(!stack.isEmpty()){
				File path = stack.pop();
				URL[] urls = new URL[]{path.toURI().toURL()};
				try (URLClassLoader loader = new URLClassLoader(urls);) {
					File[] classFiles = path.listFiles(fileFilter);
					for(File classFile : classFiles){
						String className = classFile.getName();
		            	className = className.substring(0, className.indexOf("."));
						if(!classes.containsKey(className)){
							if (classFile.isDirectory()) {  
				                stack.push(classFile);  
				            } else {
				            	if(classFile.getName().endsWith(".class")){
				            		Class<?> c = loader.loadClass(className);
				            		classes.put(className, c);
				            	} else if (classFile.getName().endsWith(".java")){
				            		int err = compiler.run(System.in, System.out, System.err, classFile.getCanonicalPath());
				            		if(err == 0){
				            			System.out.println("compiled file: " + classFile.getCanonicalPath());
				            			Class<?> c =  loader.loadClass(className);
				            			classes.put(className, c);
				            		}
				            	}
				            }
						}
					}
				
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			
		}
	}

	@Override
	public Terminal getUnresolvedTerminal() {
		Collection<String> keys = classes.keySet();
		if(keys.size()>0){
			for(String key : keys){
				try {
					Terminal terminal = (Terminal)classes.get(key).newInstance();
					classes.remove(key);
					loadedTerminals.put(key, terminal);
					return terminal;
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} 
		return null;
	}

	@Override
	public void clear() {
		classes.clear();
	}

	private class DefaultFileFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			if(pathname.getName().endsWith(".class") || pathname.getName().endsWith(".java")){
				return true;
			}
			return false;
		}
		
	}

	@Override
	public Map<String,Terminal> getLoadedTerminals() {
		return loadedTerminals;
	}

	@Override
	public void reloadPlugins() {
		classes.clear();
		loadedTerminals.clear();
		try {
			loadClasses();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
