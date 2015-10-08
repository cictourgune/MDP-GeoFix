package org.torugune.mdp.log;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

public class EchoLogger extends Logger {

	public static final int OUTPUT		= 0;
	
	public EchoLogger(String loggerName) throws IOException {
		super(loggerName);
	}

	public EchoLogger(String loggerName, Map<String, String> params) throws IOException {
		super(loggerName, params);
	}
	
	@Override
	protected void _close() {
		// Not implemented.
	}

	@Override
	protected void _logEntry(String entry) {
		_logEntry(entry, LogPriority.INFO);
	}
	
	@Override
	protected void _logEntry(String entry, LogPriority priority) {
		PrintStream printer = null;
		
		switch(priority){
		case ERROR:
			printer = _getLogPrinter("stderr");
			break;
		case INFO:
		case WARNING:
			printer = _getLogPrinter("stdout");
			break;
		}
		
		if(printer != null)
			printer.println(entry);
	}
	
	private PrintStream _getLogPrinter(String outParam){
		PrintStream printer = null;
		switch(outParam){
		case "stdout":
			printer = System.out;
			break;
		case "stderr":
			printer = System.err;
			break;
		}
		return printer;
	}
}
