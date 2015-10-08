package org.tourgune.mdp.geofix.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.tourgune.mdp.geofix.db.Database;
import org.tourgune.mdp.geofix.db.TablesDB;
import org.tourgune.mdp.geofix.db.TablesDB_new;
import org.tourgune.mdp.geofix.db.TablesDB_old;
import org.tourgune.mdp.geofix.utils.Constants;

public class Core {

public static void geoFix(Database db, String env) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		
		System.out.println("[MDP-" + env + "] GeoFix INICIADO");
		
		TablesDB tablesDB = null;
		if (Constants.oldPricing.equals(env)) {
			tablesDB = new TablesDB_old();
		}
		if (Constants.newPricing.equals(env)) {
			tablesDB = new TablesDB_new();
		}
		
		Connection con;
		try {
			con = db.connect();
/** NOTA: Se usa el BINARY para que las comparaciones tengan en cuenta mayúsculas, minúsculas y acentos!!*/
/** NOTA2: Se usa COALESCE porque si alguno d elos valores es NULL la comparativa de BINARY no devuelve que sean iguales */
			if (con != null) {
				try {
					// 1. Seteamos el valor de "gm_aal4" en "gm_locality" en los casos en que "gm_aal4" NO SEA NULL
					// (se ha visto que es el caso de España "gm_aal4" es más fiable)
//					UPDATE d_accommodation
//					SET gm_locality = gm_aal4
//					WHERE gm_locality IS NULL
//					AND gm_aal4 IS NOT NULL
//					AND BINARY COALESCE(gm_locality, '') <> BINARY COALESCE(gm_aal4, '')
//					AND gm_country = 'Spain'
					sql	.append(" UPDATE " + tablesDB.getTABLE_D_ACCOMMODATION())
						.append(" SET " + tablesDB.getDA_GM_LOCALITY() + " = " + tablesDB.getDA_GM_AAL4())
						.append(" WHERE " + tablesDB.getDA_GM_LOCALITY() + " IS NULL ")
						.append(" AND " + tablesDB.getDA_GM_AAL4() + " IS NOT NULL ")
						.append(" AND BINARY COALESCE(" + tablesDB.getDA_GM_LOCALITY() + ", '') <> BINARY COALESCE(" + tablesDB.getDA_GM_AAL4() + ", '') ")
						.append(" AND " + tablesDB.getDA_GM_COUNTRY() + " = 'Spain'");

					ps = con.prepareStatement(sql.toString());
					int i = ps.executeUpdate();
					System.out.println("GeoFix - " + i + " localidades actualizadas desde gm_aal4 en España");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al actualizar 'gm_locality' desde 'gm_aal4' en España");
					e.printStackTrace();
				}
				
				try {
					// 2. Se corrigen GEO basándonos en la tabla "ft_geo_matching"
					// COUNTRY ESPAÑA
//					UPDATE d_accommodation da, ft_geo_matching ftgm
//					SET da.gm_country = ftgm.good
//					WHERE BINARY da.gm_country = BINARY ftgm.bad
//					AND ftgm.type = 'country'
						sql = new StringBuffer();
						sql	.append(" UPDATE " + tablesDB.getTABLE_D_ACCOMMODATION() + ", " + tablesDB.getTABLE_FT_GEO_MATCHING())
							.append(" SET " + tablesDB.getDA_GM_COUNTRY() + " = " + tablesDB.getFTGM_GOOD())
							.append(" WHERE BINARY " + tablesDB.getDA_GM_COUNTRY() + " = BINARY " + tablesDB.getFTGM_BAD())
							.append(" AND " + tablesDB.getFTGM_TYPE() + " = 'country'");

						ps = con.prepareStatement(sql.toString());
						
						int i = ps.executeUpdate();
						System.out.println("GeoFix - " + i + " COUNTRY corregidas desde la tabla ft_geo_matching");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al corregir 'gm_country' desde ft_geo_matching");
					e.printStackTrace();
				}
				
				try {
				// AAL1
					sql = new StringBuffer();
//					UPDATE d_accommodation da, ft_geo_matching ftgm
//					SET da.gm_aal1 = ftgm.good
//					WHERE (
//						BINARY da.gm_country = BINARY ftgm.country
//						AND BINARY da.gm_aal1 = ftgm.bad
//					) AND ftgm.type = 'aal1'
					sql	.append(" UPDATE " + tablesDB.getTABLE_D_ACCOMMODATION() + ", " + tablesDB.getTABLE_FT_GEO_MATCHING())
						.append(" SET " + tablesDB.getDA_GM_AAL1() + " = " + tablesDB.getFTGM_GOOD())
						.append(" WHERE (")
							.append(" BINARY " + tablesDB.getDA_GM_COUNTRY() + " = BINARY " + tablesDB.getFTGM_COUNTRY())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL1() + " = BINARY " + tablesDB.getFTGM_BAD())
						.append(" ) AND " + tablesDB.getFTGM_TYPE() + " = 'aal1'");

					ps = con.prepareStatement(sql.toString());
					
					int i = ps.executeUpdate();
					System.out.println("GeoFix - " + i + " AAL1 corregidas desde la tabla ft_geo_matching");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al corregir 'gm_aal1' desde ft_geo_matching");
					e.printStackTrace();
				}
				
				// AAL2
				try {
					sql = new StringBuffer();
//					UPDATE d_accommodation da, ft_geo_matching ftgm
//					SET da.gm_aal2 = ftgm.good
//					WHERE (
//						BINARY da.gm_country = BINARY ftgm.country 
//					 	AND BINARY da.gm_aal1 = BINARY ftgm.aal1
//						AND BINARY da.gm_aal2 = BINARY ftgm.bad
//					) AND ftgm.type = 'aal2'
					sql	.append(" UPDATE " + tablesDB.getTABLE_D_ACCOMMODATION() + ", " + tablesDB.getTABLE_FT_GEO_MATCHING())
						.append(" SET " + tablesDB.getDA_GM_AAL2() + " = " + tablesDB.getFTGM_GOOD())
						.append(" WHERE (")
							.append(" BINARY " + tablesDB.getDA_GM_COUNTRY() + " = BINARY " + tablesDB.getFTGM_COUNTRY())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL1() + " = BINARY " + tablesDB.getFTGM_AAL1())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL2() + " = BINARY " + tablesDB.getFTGM_BAD())
						.append(" ) AND " + tablesDB.getFTGM_TYPE() + " = 'aal2'");
			
					ps = con.prepareStatement(sql.toString());
					
					int i = ps.executeUpdate();
					System.out.println("GeoFix - " + i + " AAL2 corregidas desde la tabla ft_geo_matching");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al corregir 'gm_aal2' desde ft_geo_matching");
					e.printStackTrace();
				}
				
				// AAL3
				try {
					sql = new StringBuffer();
//					UPDATE d_accommodation da, ft_geo_matching ftgm
//					SET da.gm_aal3 = ftgm.good
//					WHERE (
//						BINARY da.gm_country = BINARY ftgm.country 
//				 		AND BINARY da.gm_aal1 = BINARY ftgm.aal1
//			 			AND BINARY da.gm_aal2 = BINARY ftgm.aal2 
//						AND BINARY da.gm_aal3 = BINARY ftgm.bad
//					) AND ftgm.type = 'aal3'
					sql	.append(" UPDATE " + tablesDB.getTABLE_D_ACCOMMODATION() + ", " + tablesDB.getTABLE_FT_GEO_MATCHING())
						.append(" SET " + tablesDB.getDA_GM_AAL3() + " = " + tablesDB.getFTGM_GOOD())
						.append(" WHERE (")
							.append(" BINARY " + tablesDB.getDA_GM_COUNTRY() + " = BINARY " + tablesDB.getFTGM_COUNTRY())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL1() + " = BINARY " + tablesDB.getFTGM_AAL1())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL2() + " = BINARY " + tablesDB.getFTGM_AAL2())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL3() + " = BINARY " + tablesDB.getFTGM_BAD())
						.append(" ) AND " + tablesDB.getFTGM_TYPE() + " = 'aal3'");
					ps = con.prepareStatement(sql.toString());
					
					int i = ps.executeUpdate();
					System.out.println("GeoFix - " + i + " AAL3 corregidas desde la tabla ft_geo_matching");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al corregir 'gm_aal3' desde ft_geo_matching");
					e.printStackTrace();
				}
				
				// AAL4
				try {
					sql = new StringBuffer();
//					UPDATE d_accommodation da, ft_geo_matching ftgm
//					SET da.gm_aal4 = ftgm.good
//					WHERE (
//						BINARY da.gm_country = BINARY ftgm.country 
//				 		AND BINARY da.gm_aal1 = BINARY ftgm.aal1
//			 			AND BINARY da.gm_aal2 = BINARY ftgm.aal2
//						AND BINARY da.gm_aal3 = BINARY ftgm.aal3
//						AND BINARY da.gm_aal4 = BINARY ftgm.bad
//					) AND ftgm.type = 'aal4'
					sql	.append(" UPDATE " + tablesDB.getTABLE_D_ACCOMMODATION() + ", " + tablesDB.getTABLE_FT_GEO_MATCHING())
						.append(" SET " + tablesDB.getDA_GM_AAL4() + " = " + tablesDB.getFTGM_GOOD())
						.append(" WHERE (")
							.append(" BINARY " + tablesDB.getDA_GM_COUNTRY() + " = BINARY " + tablesDB.getFTGM_COUNTRY())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL1() + " = BINARY " + tablesDB.getFTGM_AAL1())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL2() + " = BINARY " + tablesDB.getFTGM_AAL2())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL3() + " = BINARY " + tablesDB.getFTGM_AAL3())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL4() + " = BINARY " + tablesDB.getFTGM_BAD())
						.append(" ) AND " + tablesDB.getFTGM_TYPE() + " = 'aal4'");
					ps = con.prepareStatement(sql.toString());
					
					int i = ps.executeUpdate();
					System.out.println("GeoFix - " + i + " AAL4 corregidas desde la tabla ft_geo_matching");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al corregir 'gm_aal4' desde ft_geo_matching");
					e.printStackTrace();
				}
				
				// LOCALITY ESPAÑA
				try {
					sql = new StringBuffer();
//					UPDATE d_accommodation da, ft_geo_matching ftgm
//					SET da.gm_locality = ftgm.good
//					WHERE (
//						BINARY da.gm_country = BINARY ftgm.country 
//				 		AND BINARY da.gm_aal1 = BINARY ftgm.aal1
//			 			AND BINARY da.gm_aal2 = BINARY ftgm.aal2
//						#AND BINARY da.gm_aal3 = BINARY ftgm.aal3 
//						#AND BINARY da.gm_aal4 = BINARY ftgm.aal4 
//						AND BINARY da.gm_locality = BINARY ftgm.bad
//					) AND ftgm.type = 'locality'
//					AND da.gm_country = 'Spain'
					sql	.append(" UPDATE " + tablesDB.getTABLE_D_ACCOMMODATION() + ", " + tablesDB.getTABLE_FT_GEO_MATCHING())
						.append(" SET " + tablesDB.getDA_GM_LOCALITY() + " = " + tablesDB.getFTGM_GOOD())
						.append(" WHERE (")
							.append(" BINARY " + tablesDB.getDA_GM_COUNTRY() + " = BINARY " + tablesDB.getFTGM_COUNTRY())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL1() + " = BINARY " + tablesDB.getFTGM_AAL1())
							.append(" AND BINARY " + tablesDB.getDA_GM_AAL2() + " = BINARY " + tablesDB.getFTGM_AAL2())
							// En España no hay que tener en cuenta estos 2 campos porque sino, pueden no concordar (sobre todo gm_aal4)
//							.append(" AND BINARY " + tablesDB.getDA_GM_AAL3() + " = BINARY " + tablesDB.getFTGM_AAL3())
//							.append(" AND BINARY " + tablesDB.getDA_GM_AAL4() + " = BINARY " + tablesDB.getFTGM_AAL4())
							.append(" AND BINARY " + tablesDB.getDA_GM_LOCALITY() + " = BINARY " + tablesDB.getFTGM_BAD())
						.append(" ) AND " + tablesDB.getFTGM_TYPE() + " = 'locality'")
						.append(" AND " + tablesDB.getDA_GM_COUNTRY() + " = 'Spain'");
					ps = con.prepareStatement(sql.toString());
					
					int i = ps.executeUpdate();
					System.out.println("GeoFix - " + i + " LOCALITY ESPAÑA corregidas desde la tabla ft_geo_matching");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al corregir 'gm_locality' ESPAÑA desde ft_geo_matching");
					e.printStackTrace();
				}
/**
 * Comentamos lo siguiente ya que no tiene sentido. En otros países no se trabaja a nivel de locality
 * sino en cada caso a diferentes niveles (gm_aal1, gm_aal2, gm_aal3).
 * Las correcciones que haya que hacer se derivan al stored procedure "crawler_geofix"					
 */
//				// LOCALITY RESTO PAÍSES
//					sql = new StringBuffer();
////					UPDATE d_accommodation da, ft_geo_matching ftgm
////					SET da.gm_locality = ftgm.good
////					WHERE (
////						BINARY da.gm_country = BINARY ftgm.country 
////				 		AND BINARY da.gm_aal1 = BINARY ftgm.aal1
////			 			AND BINARY da.gm_aal2 = BINARY ftgm.aal2
////						AND BINARY da.gm_aal3 = BINARY ftgm.aal3 
////						AND BINARY da.gm_aal4 = BINARY ftgm.aal4 
////						AND BINARY da.gm_locality = BINARY ftgm.bad
////					) AND ftgm.type = 'locality'
////					AND da.gm_country <> 'Spain'
//					sql	.append(" UPDATE " + tablesDB.getTABLE_D_ACCOMMODATION() + ", " + tablesDB.getTABLE_FT_GEO_MATCHING())
//						.append(" SET " + tablesDB.getDA_GM_LOCALITY() + " = " + tablesDB.getFTGM_GOOD())
//						.append(" WHERE (")
//							.append(" BINARY " + tablesDB.getDA_GM_COUNTRY() + " = BINARY " + tablesDB.getFTGM_COUNTRY())
//							.append(" AND BINARY " + tablesDB.getDA_GM_AAL1() + " = BINARY " + tablesDB.getFTGM_AAL1())
//							.append(" AND BINARY " + tablesDB.getDA_GM_AAL2() + " = BINARY " + tablesDB.getFTGM_AAL2())
//							.append(" AND BINARY " + tablesDB.getDA_GM_AAL3() + " = BINARY " + tablesDB.getFTGM_AAL3())
//							.append(" AND BINARY " + tablesDB.getDA_GM_AAL4() + " = BINARY " + tablesDB.getFTGM_AAL4())
//							.append(" AND BINARY " + tablesDB.getDA_GM_LOCALITY() + " = BINARY " + tablesDB.getFTGM_BAD())
//						.append(" ) AND " + tablesDB.getFTGM_TYPE() + " = 'locality'")
//						.append(" AND " + tablesDB.getDA_GM_COUNTRY() + " <> 'Spain'");
//					ps = con.prepareStatement(sql.toString());
//					
//					i = ps.executeUpdate();
//					System.out.println("GeoFix - " + i + " LOCALITY RESTO PAÍSES corregidas desde la tabla ft_geo_matching");
				
				// 3. Revisamos los "gm_locality" de España que están mal y miramos si sus "gm_aal4"
				// sí son correctos para actualizar con dicha info el "gm_locality"
				try {
					fixSpainLocalitiesFromAAL4(env, tablesDB, db, con);
					System.out.println("GeoFix - Localidades ERRÓNEAS corregidas desde 'gm_aal4' ESPAÑA");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al corregir 'gm_locality' ESPAÑA NO VÁLIDAS desde 'gm_aal4'");
					e.printStackTrace();
				}
					
				// 4. Llamamos al stored procedure "mdp_crawler_geofix" para intentar corregir país <> 'Spain'
				try {
					storedProcedureGeofix(con);
					System.out.println("GeoFix - STORED PROCEDURE ejecutado en RESTO DE PAÍSES");
				} catch (Exception e) {
					System.out.println("GeoFix - ERROR al corregir 'gm_locality' ESPAÑA desde ft_geo_matching");
				}
					
			} else {
				throw new Exception("Database connection NULL");
			}	
			con = db.disconnect();
		} catch (Exception e) {
			try {
				con = db.disconnect();
			} catch (Exception e2) {
				throw e2;
			}
			throw e;
		}
		System.out.println("[MDP-" + env + "] GeoFix FINALIZADO CORRECTAMENTE");
	}

	private static void fixSpainLocalitiesFromAAL4(String env, TablesDB tablesDB, Database db, Connection con) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
//		UPDATE pricing_DB.d_accommodation da, pricing_DB.d_ine di
//		SET da.gm_locality = da.gm_aal4
//		WHERE (
//			BINARY di.aal1 = BINARY da.gm_aal1
//			AND BINARY di.aal2 = BINARY da.gm_aal2
//			AND BINARY di.locality = BINARY da.gm_aal4
//		) AND da.id_accommodation IN (
//				XXXXXX
//		)
		sql	.append(" UPDATE pricing_DB.d_accommodation da, pricing_DB.d_ine di ")
			.append(" SET da.gm_locality = da.gm_aal4 ")
			.append(" WHERE (")
				.append(" BINARY di.aal1 = BINARY da.gm_aal1 ")
				.append(" AND BINARY di.aal2 = BINARY da.gm_aal2 ")
				.append(" AND BINARY di.locality = BINARY da.gm_aal4 ")
			.append(") ");
		try {
			con = db.connect();
			if (con != null) {
				rs	= getBadGeo(env, tablesDB, con);
				if (rs.first()) {
					sql	.append(" AND da.id_accommodation IN (");
					rs.beforeFirst();
					while (rs.next()) {
						String idAccommodation = rs.getString(tablesDB.getDA_ID_ACCOMMODATION());
						sql.append(idAccommodation + ",");
					}
					sql.deleteCharAt(sql.length()-1); // Borramos la última coma
					sql.append(")");
					ps = con.prepareStatement(sql.toString());
					ps.executeUpdate();
				}
			}
		} catch (Exception e) {
			try {
				con = db.disconnect();
			} catch (Exception e2) {
				throw e2;
			}
			throw e;
		}
	}
	
	// TODO reutilizar el método que se usa en MDP-Reports Geo
	private static ResultSet getBadGeo(String env, TablesDB tablesDB, Connection con) throws Exception {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		
		// Se seleccionan las tuplas que no se encuentran detectadas en la tabla "d_ine"
			sql	.append(" SELECT " + tablesDB.getDA_ID_ACCOMMODATION() + ", " + tablesDB.getDA_GM_COUNTRY()+ ", " + tablesDB.getDA_GM_AAL1() 
						+ ", " + tablesDB.getDA_GM_AAL2() + ", " + tablesDB.getDA_GM_AAL3() + ", " + tablesDB.getDA_GM_AAL4() 
						+ ", " + tablesDB.getDA_GM_LOCALITY())
				.append(" FROM " +  tablesDB.getTABLE_D_ACCOMMODATION());
//				SELECT da.id_accommodation, da.gm_country, da.gm_aal1, da.gm_aal2, da.gm_aal3, da.gm_aal4, da.gm_locality
//				FROM pricing_DB.d_accommodation da
			
			// Si es old, hay que filtrar sólo hoteles de id_canal = 1 y añadimos
			// tabla ft_scraping para mirar sólo hoteles que hayan generado precios hoy
			if (Constants.oldPricing.equals(env)) {
				sql	.append(" JOIN " + tablesDB.getTABLE_FT_PRODUCT_PRICE() 
						+ " ON " + tablesDB.getFTPP_ID_ACCOMMODATION() + " = " + tablesDB.getDA_ID_ACCOMMODATION())
					.append(" JOIN " + tablesDB.getTABLE_NM_ACCOMMODATION_CHANNEL()
						+ " ON " + tablesDB.getNMAC_ID_ACCOMMODATION() + " = " + tablesDB.getDA_ID_ACCOMMODATION());
//				JOIN ft_scraping fts ON dh.id_hotel = fts.id_hotel
//				JOIN nm_id_hotel_canal nmhc ON dh.id_hotel = nmhc.id_hotel
			}
				sql	.append(" LEFT JOIN " + tablesDB.getTABLE_D_INE() + " ON (")
						.append(" BINARY " + tablesDB.getDI_AAL1() + " = BINARY " + tablesDB.getDA_GM_AAL1())
						.append(" AND BINARY " + tablesDB.getDI_AAL2() + " = BINARY " + tablesDB.getDA_GM_AAL2())
						.append(" AND BINARY " + tablesDB.getDI_LOCALITY() + " = BINARY " + tablesDB.getDA_GM_LOCALITY())
					.append(")")
					.append(" WHERE " + tablesDB.getDI_AAL1() + " IS NULL AND " + tablesDB.getDI_AAL2() + " IS NULL AND " + tablesDB.getDI_LOCALITY() + " IS NULL ")				
					.append(" AND COALESCE(" + tablesDB.getDA_GM_COUNTRY() + ", '') = 'Spain' ");	
//				LEFT JOIN pricing_DB.d_ine di ON (
//				BINARY di.aal1 = BINARY da.gm_aal1
//				AND BINARY di.aal2 = BINARY da.gm_aal2
//				AND BINARY di.locality = BINARY da.gm_locality
//			) 
//			WHERE di.aal1 IS NULL AND di.aal2 IS NULL AND di.locality IS NULL
//			AND COALESCE(da.gm_country, '') = 'Spain'
				
			// SI es OLD, filtramos sólo los id_canal = 1 y revisamos los hoteles SIN GEO que tengan precios hoy
			if (Constants.oldPricing.equals(env)) {
				sql	.append(" AND " + tablesDB.getDA_LATITUDE() + " IS NOT NULL AND " + tablesDB.getDA_LONGITUDE() + " IS NOT NULL ")
					.append(" AND " + tablesDB.getFTPP_ID_BOOKING_DATE() + " = CURDATE() ")
					.append(" AND " + tablesDB.getNMAC_ID_CHANNEL() + " = 1 ");
			}
			
			sql	.append(" GROUP BY " + tablesDB.getDA_ID_ACCOMMODATION());
//			GROUP BY da.id_accommodation;
			
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();	
		
			return rs;
	}
	
	private static void storedProcedureGeofix (Connection con) throws Exception {
		
		PreparedStatement ps = null;
		StringBuffer sql = new StringBuffer();
		
		sql	.append(" CALL mdp_crawler_geofix()");
		
		ps = con.prepareStatement(sql.toString());
		ps.execute();
		
	}
}
