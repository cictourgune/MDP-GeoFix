package org.torugune.mdp.log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @todo
 * 	- Revisar los métodos push(), print() y println(). Quizás sea mejor cambiar los nombres.
 */
public abstract class Logger {

	public static String DATE_FORMAT = "date_format";
	public static String TIME_FORMAT = "time_format";
	
	protected String loggerName;
	protected StringBuffer logBuffer;
	
	protected SimpleDateFormat timeFormatter;
	protected String dateFormat, timeFormat;
	
	protected long runningThread;
	
	public Logger(String loggerName) throws IOException {
		this.loggerName = loggerName;
		this.logBuffer = null;
		this.dateFormat = "dd/MM/yyyy";
		this.timeFormat = "HH:mm:ss";
		this.runningThread = -1;
		try{
			this.timeFormatter = new SimpleDateFormat(dateFormat + " '-' " + timeFormat);
		}catch(IllegalArgumentException e){
			System.err.println(e.getMessage());
			throw new IOException("Formato de fecha/hora invalido.");
		}
	}
	
	public Logger(String loggerName, Map<String, String> params) throws IOException {
		this.loggerName = loggerName;
		this.logBuffer = null;
		this.dateFormat = params.containsKey(DATE_FORMAT) ? params.get(DATE_FORMAT) : "dd/MM/yyyy";
		this.timeFormat = params.containsKey(TIME_FORMAT) ? params.get(TIME_FORMAT) : "HH:mm:ss";
		this.runningThread = -1;
		try{
			this.timeFormatter = new SimpleDateFormat(dateFormat + " '-' " + timeFormat);
		}catch(IllegalArgumentException e){
			System.err.println(e.getMessage());
			throw new IOException("Formato de fecha/hora invalido.");
		}
	}
	
	/*	Métodos a implementar por las subclases de Logger	*/
	protected abstract void _close();
	protected abstract void _logEntry(String entry);
	protected abstract void _logEntry(String entry, LogPriority priority);
	
	public void close(){
		_close();
		loggerName = null;
		timeFormatter = null;
		logBuffer = null;
	}
	
	/*	Logs de una línea	*/
	public void log(String description){
		log(description, "", LogPriority.INFO);
	}
	public void log(String description, String module){
		log(description, module, LogPriority.INFO);
	}
	public void log(String description, LogPriority priority){
		log(description, "", priority);
	}
	public void log(String description, String module, LogPriority priority){
		if(timeFormatter instanceof SimpleDateFormat){
			String date = timeFormatter.format(new Date());
			
			String entry = _generateLogEntryLine(date, priority);
			entry += module + " - ";
			entry += description;
			
			_logEntry(entry, priority);
		}
	}
	
	/*	Logs por partes/Logs de varias líneas	*/
	
	public synchronized void push(){
		push("", LogPriority.INFO);
	}
	public synchronized void push(String module){
		push(module, LogPriority.INFO);
	}
	public synchronized void push(LogPriority priority){
		push("", priority);
	}
	public synchronized void push(String module, LogPriority priority){
		if(logBuffer instanceof StringBuffer && timeFormatter instanceof SimpleDateFormat){
			String date = timeFormatter.format(new Date());
			
			String entry = _generateLogEntryLine(date, priority);
			entry += module + " - ";
			
			entry += logBuffer.toString();
			logBuffer = null;
			
			_logEntry(entry, priority);
		}
		
		if(this.runningThread != -1){
			this.runningThread = -1;
			notifyAll();
		}
	}
	
	/*
	 * Quita todos los espacios antes de nada.
	 */
	public synchronized Logger print(String description){
		while(this.runningThread != Thread.currentThread().getId() &&
				this.runningThread != -1){
			try {
				wait();		// Esperar hasta que el logBuffer quede libre
			} catch (InterruptedException e) {
				e.printStackTrace();
				return this;
			}
		}
		
		this.runningThread = Thread.currentThread().getId();	// Asígnale el Logger al hilo actual
		
		if(logBuffer == null)
			logBuffer = new StringBuffer();
		
		logBuffer.append(" " + description.trim());
		
		return this;	// Para hacer encadenamiento
	}
	/*
	 * No ponemos espacio al final.
	 * De alguna manera, obligamos a llamar a print(), as�:
	 * 		logger.newline().print("Mensaje");
	 */
	public synchronized Logger newline(){
		while(this.runningThread != Thread.currentThread().getId() &&
				this.runningThread != -1){
			try {
				wait();		// Esperar hasta que el logBuffer quede libre
			} catch (InterruptedException e) {
				e.printStackTrace();
				return this;
			}
		}
		
		this.runningThread = Thread.currentThread().getId();	// Asígnale el Logger al hilo actual
		
		if(logBuffer == null)
			logBuffer = new StringBuffer();
		else
			logBuffer.append(System.getProperty("line.separator") + "    |");
		
		return this;	// Para hacer encadenamiento
	}
	
	private String _generateLogEntryLine(String date, LogPriority priority){
		StringBuffer line = new StringBuffer("[" + date + "] (");
		switch(priority){
		case INFO:
			line.append("INFO");
			break;
		case WARNING:
			line.append("WARNING");
			break;
		case ERROR:
			line.append("ERROR");
			break;
		}
		line.append(") ");
		return line.toString();
	}
}
