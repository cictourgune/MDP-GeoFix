package org.torugune.mdp.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FileLogger extends Logger {

	public static final int FILE	= 0;
	
	private String file;
	private FileOutputStream fileDescriptor;
	
	public FileLogger(String loggerName) throws IOException {
		super(loggerName);
		
		this.file = _defaultFileName();
		this.fileDescriptor = null;
		
		try{
			_initialize();
		}catch(FileNotFoundException e){
			
		}
	}

	public FileLogger(String loggerName, Map<String, String> params) throws IOException {
		super(loggerName, params);
		
		this.file = params.containsKey(FILE) ? params.get(FILE) : _defaultFileName();
		this.fileDescriptor = null;
		
		try{
			_initialize();
		}catch(FileNotFoundException e){
			
		}
	}

	@Override
	protected void _close() {
		try{
			if(this.fileDescriptor != null)
				this.fileDescriptor.close();
		}catch(IOException e){}
	}

	@Override
	protected void _logEntry(String entry) {
		if(this.fileDescriptor != null){
			try{
				byte[] crlf = System.getProperty("line.separator").getBytes();
				byte[] output = entry.getBytes();
				fileDescriptor.write(output);
				fileDescriptor.write(crlf);
			}catch(IOException e){
				System.err.println("ERROR: Could not log entry to file \"" + this.file + "\"");
			}
		}
	}
	
	@Override
	protected void _logEntry(String entry, LogPriority priority) {
		_logEntry(entry);
	}
	
	private String _defaultFileName(){
		return this.loggerName + "_" + new SimpleDateFormat("ddMMyyyyHHmm").format(new Date()) + ".log";
	}
	
	private void _initialize() throws FileNotFoundException{
		this.fileDescriptor = new FileOutputStream(new File(this.file));
	}
}
