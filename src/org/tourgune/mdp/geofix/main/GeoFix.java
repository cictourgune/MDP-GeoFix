package org.tourgune.mdp.geofix.main;

import java.util.ArrayList;
import java.util.List;

import org.tourgune.mdp.geofix.db.Database;

public class GeoFix {
	
	private static List<String> envOptions = new ArrayList<String>(); //"old/new"
			
	public static void main(String[] args) {
		fillEnvOptions();
		
		try {
			String env = null;
			try {
				env = args[0];
				if (!envOptions.contains(env)) {
					System.out.println("[MDP-Reports] FATAL ERROR - Main. Argument 'env' " + envOptions + " is needed.");
					System.exit(0);
				} 
			} catch (ArrayIndexOutOfBoundsException aioobe) { // Si no se pasan params pega una excepción
				System.out.println("[MDP-Reports] FATAL ERROR - Main. Argument 'env' " + envOptions + " is needed.");
				System.exit(0);
			}
		
			Database db = new Database(env);
			
			Core.geoFix(db, env);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void fillEnvOptions() {
		envOptions.add("old");
		envOptions.add("new");
	}
}
