package org.tourgune.mdp.geofix.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.tourgune.mdp.geofix.utils.Configuration;
import org.tourgune.mdp.geofix.utils.Constants;


public class Database {

	private String host;
	private String port;
	private String db;
	private String username;
	private String password;
	
	private Connection con;
	
	public Database(String env) {
		if (Constants.oldPricing.equals(env)) {
			host = Configuration.getInstance().getProperty(Constants.OLD_DB_HOST);
			port = Configuration.getInstance().getProperty(Constants.OLD_DB_PORT);
			db = Configuration.getInstance().getProperty(Constants.OLD_DB_DB);
			username = Configuration.getInstance().getProperty(Constants.OLD_DB_USER);
			password = Configuration.getInstance().getProperty(Constants.OLD_DB_PASS);
		}
		if (Constants.newPricing.equals(env)) {
			host = Configuration.getInstance().getProperty(Constants.NEW_DB_HOST);
			port = Configuration.getInstance().getProperty(Constants.NEW_DB_PORT);
			db = Configuration.getInstance().getProperty(Constants.NEW_DB_DB);
			username = Configuration.getInstance().getProperty(Constants.NEW_DB_USER);
			password = Configuration.getInstance().getProperty(Constants.NEW_DB_PASS);
		}
	}
	
	public Connection connect() throws Exception {
		con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, username, password);
		} catch (SQLException se) {
			System.out.println("[MDP] Database connection ERROR");
			throw se;
		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
			throw e;
		}
		return con;
	}
	
	public Connection disconnect() throws Exception{
		try {
			if(con != null){
				con.close();
				con = null;
			}
		} catch(Exception e) {
			System.err.println("[MDP] Database connection ERROR");
			throw e;
		}
		return con;
	}
}
