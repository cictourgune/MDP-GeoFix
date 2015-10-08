package org.torugune.mdp.log;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseLogger extends Logger {

	public static final String DB_HOST	= "db_host";
	public static final String DB_PORT	= "db_port";
	public static final String DB_NAME 	= "db_name";
	public static final String DB_USER	= "db_user";
	public static final String DB_PASS	= "db_pass";
	
	private Connection dbConn;
	
	private String dbHost, dbPort;
	private String dbUser, dbPass;
	private String dbName;
	
	private String tmpModule;
	
	private int lastRefCode;
	
	public DatabaseLogger(String loggerName) throws IOException {
		super(loggerName);
		
		this.dbHost = database_ip;
		this.dbPort = database_port;
		this.dbName = database_name;
		this.dbUser = database_user;
		this.dbPass = database_pass;
		
		this.tmpModule = null;
		this.lastRefCode = -1;
		
		_initConnection();
	}
	
	public DatabaseLogger(String loggerName, Map<String, String> params) throws IOException {
		super(loggerName, params);
		
		this.dbHost = (params.containsKey(DB_HOST) ? params.get(DB_HOST) : database_ip);
		this.dbPort = (params.containsKey(DB_PORT) ? params.get(DB_PORT) : database_port);
		this.dbName = (params.containsKey(DB_NAME) ? params.get(DB_NAME) : database_name);
		this.dbUser = (params.containsKey(DB_USER) ? params.get(DB_USER) : database_user);
		this.dbPass = (params.containsKey(DB_PASS) ? params.get(DB_PASS) : database_pass);
		
		this.tmpModule = null;
		this.lastRefCode = -1;
		
		_initConnection();
	}
	
	public void setReferenceCode(String html){
		String sql = "INSERT INTO error_refs (html) VALUES (?)";
		
		try{
			if(this.dbConn != null) {
				PreparedStatement ps = this.dbConn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				
				ps.setString(1, html);
				ps.executeUpdate();
				
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next())
					this.lastRefCode = rs.getInt(1);
				
				rs.close();
				ps.close();
			}else
				System.err.println("DatabaseLogger: Database connection was NULL");
		}catch(SQLException e){
			System.err.println("EXCEPTION 'SQLException': " + e.getMessage());
		}
	}
	
	public void clearReferenceCode() {
		this.lastRefCode = -1;
	}
	
	@Override
	public void log(String description, String hotelId, LogPriority priority) {
		/*
		 * Al loguear a la base de datos no queremos que salga todo el churro
		 * "[fecha - hora] (prio.) - etc." que le pone delante.
		 * Es mucho más elegante usar las diferentes columnas de la tabla para
		 * registrar cada cosa.
		 * 
		 * Por ahora, la tabla "scraping_errors" tiene el siguiente formato:
		 * 	+------+-------------+---------+-----------------------+
		 *  |  id  |  timestamp  |  hotel  |      description      |
		 *  +------+-------------+---------+-----------------------+
		 * Donde 'id' y 'timestamp' se generan automáticamente. El campo 'id' es AUTO INCREMENT
		 * y el campo 'timestamp' resuelve por defecto a CURRENT_TIMESTAMP.
		 * 
		 * Lo único que tenemos que hacer es registrar la descripción (campo 'description')
		 * y un identificador del hotel en el cual se ha generado la incidencia.
		 * 
		 * Donde antes logueábamos el módulo que genera la entrada, ahora
		 * logueamos el hotel ('hotelId').
		 */
		synchronized(this) {
			if(!hotelId.isEmpty())
				this.tmpModule = hotelId;
			
			_logEntry(description);
			
			this.tmpModule = null;
		}
	}

	@Override
	protected void _close() {
		try{
			if(this.dbConn != null)
				this.dbConn.close();
		}catch(SQLException e){
			this.dbConn = null;
		}
	}

	@Override
	protected void _logEntry(String entry) {
		String sql = (this.tmpModule == null ?
				"INSERT INTO scraping_errors (task, description, ref_code) VALUES (?, ?, ?)" :
				"INSERT INTO scraping_errors (task, description, hotel, ref_code) VALUES (?, ?, ?, ?)");
		
		try{
			if(this.dbConn != null){
				PreparedStatement ps = this.dbConn.prepareStatement(sql);
				
				ps.setString(1, this.loggerName);
				ps.setString(2, entry);
				
				if(this.tmpModule != null){
					ps.setString(3, this.tmpModule);
					ps.setInt(4, (this.lastRefCode == -1 ? 0 : this.lastRefCode));
				}else
					ps.setInt(3, (this.lastRefCode == -1 ? 0 : this.lastRefCode));
				
				ps.executeUpdate();
				
				ps.close();
			}else{
				System.err.println("DatabaseLogger: Database connection was NULL");
			}
		}catch(SQLException e){
			System.err.println("EXCEPTION SQLException: " + e.getMessage());
			System.err.println("\tSQL statement: " + sql);
		}
	}
	
	@Override
	protected void _logEntry(String entry, LogPriority priority) {
		_logEntry(entry);
	}
	
	private void _initConnection() {
		try {
			this.dbConn = DriverManager.getConnection("jdbc:mysql://" + this.dbHost + ":" + this.dbPort + "/" + this.dbName, this.dbUser, this.dbPass);
		} catch (SQLException e) {
			this.dbConn = null;
		}
	}
}
