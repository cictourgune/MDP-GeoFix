package org.tourgune.mdp.geofix.db;

public class TablesDB {
	
	private String TABLE_FT_PRODUCT_PRICE 		= "ft_product_price ftpp";
	private String FTPP_ID_ACCOMMODATION 		= "ftpp.id_accommodation";
	private String FTPP_ID_BOOKING_DATE			= "ftpp.id_booking_date";
	
	private String TABLE_D_ACCOMMODATION 		= "d_accommodation da";
	private String DA_ID_ACCOMMODATION			= "da.id_accommodation";
	private String DA_GM_COUNTRY				= "da.gm_country";
	private String DA_GM_AAL1					= "da.gm_aal1";
	private String DA_GM_AAL2					= "da.gm_aal2";
	private String DA_GM_AAL3					= "da.gm_aal3";
	private String DA_GM_AAL4					= "da.gm_aal4";
	private String DA_GM_LOCALITY				= "da.gm_locality";
	private String DA_LATITUDE					= "da.latitude";
	private String DA_LONGITUDE					= "da.longitude";

	private String TABLE_FT_GEO_MATCHING		= "ft_geo_matching ftgm";
	private String FTGM_TYPE					= "ftgm.type";
	private String FTGM_COUNTRY					= "ftgm.country";
	private String FTGM_AAL1					= "ftgm.aal1";
	private String FTGM_AAL2					= "ftgm.aal2";
	private String FTGM_AAL3					= "ftgm.aal3";
	private String FTGM_AAL4					= "ftgm.aal4";
	private String FTGM_LOCALITY				= "ftgm.locality";
	private String FTGM_BAD						= "ftgm.bad";
	private String FTGM_GOOD					= "ftgm.good";
	
	private String TABLE_NM_ACCOMMODATION_CHANNEL	= "nm_accommodation_channel nmac";
	private String NMAC_ID_ACCOMMODATION			= "nmac.id_accommodation";
	private String NMAC_ID_CHANNEL					= "nmac.id_channel";
	
	private String TABLE_D_INE					= "d_ine di";
	private String DI_AAL1						= "di.aal1";
	private String DI_AAL2						= "di.aal2";
	private String DI_LOCALITY					= "di.locality";
	
	private String TABLE_D_GEO					= "d_geo dgeo";
	private String DGEO_COUNTRY					= "dgeo.country";
	private String DGEO_AAL1					= "dgeo.aal1";
	private String DGEO_AAL2					= "dgeo.aal2";
	private String DGEO_AAL3					= "dgeo.aal3";
	private String DGEO_AAL4					= "dgeo.aal4";
	private String DGEO_LOCALITY				= "dgeo.locality";
	
	public String getTABLE_FT_PRODUCT_PRICE() {
		return TABLE_FT_PRODUCT_PRICE;
	}
	public String getFTPP_ID_ACCOMMODATION() {
		return FTPP_ID_ACCOMMODATION;
	}
	public String getFTPP_ID_BOOKING_DATE() {
		return FTPP_ID_BOOKING_DATE;
	}
	public String getTABLE_D_ACCOMMODATION() {
		return TABLE_D_ACCOMMODATION;
	}
	public String getDA_ID_ACCOMMODATION() {
		return DA_ID_ACCOMMODATION;
	}
	public String getDA_GM_COUNTRY() {
		return DA_GM_COUNTRY;
	}
	public String getDA_GM_AAL1() {
		return DA_GM_AAL1;
	}
	public String getDA_GM_AAL2() {
		return DA_GM_AAL2;
	}
	public String getDA_GM_AAL3() {
		return DA_GM_AAL3;
	}
	public String getDA_GM_AAL4() {
		return DA_GM_AAL4;
	}
	public String getDA_GM_LOCALITY() {
		return DA_GM_LOCALITY;
	}
	public String getDA_LATITUDE() {
		return DA_LATITUDE;
	}
	public String getDA_LONGITUDE() {
		return DA_LONGITUDE;
	}
	public String getTABLE_FT_GEO_MATCHING() {
		return TABLE_FT_GEO_MATCHING;
	}
	public String getFTGM_TYPE() {
		return FTGM_TYPE;
	}
	public String getFTGM_COUNTRY() {
		return FTGM_COUNTRY;
	}
	public String getFTGM_AAL1() {
		return FTGM_AAL1;
	}
	public String getFTGM_AAL2() {
		return FTGM_AAL2;
	}
	public String getFTGM_AAL3() {
		return FTGM_AAL3;
	}
	public String getFTGM_AAL4() {
		return FTGM_AAL4;
	}
	public String getFTGM_LOCALITY() {
		return FTGM_LOCALITY;
	}
	public String getFTGM_BAD() {
		return FTGM_BAD;
	}
	public String getFTGM_GOOD() {
		return FTGM_GOOD;
	}
	public String getTABLE_NM_ACCOMMODATION_CHANNEL() {
		return TABLE_NM_ACCOMMODATION_CHANNEL;
	}
	public String getNMAC_ID_ACCOMMODATION() {
		return NMAC_ID_ACCOMMODATION;
	}
	public String getNMAC_ID_CHANNEL() {
		return NMAC_ID_CHANNEL;
	}
	public String getTABLE_D_INE() {
		return TABLE_D_INE;
	}
	public String getDI_AAL1() {
		return DI_AAL1;
	}
	public String getDI_AAL2() {
		return DI_AAL2;
	}
	public String getDI_LOCALITY() {
		return DI_LOCALITY;
	}
	public String getTABLE_D_GEO() {
		return TABLE_D_GEO;
	}
	public String getDGEO_COUNTRY() {
		return DGEO_COUNTRY;
	}
	public String getDGEO_AAL1() {
		return DGEO_AAL1;
	}
	public String getDGEO_AAL2() {
		return DGEO_AAL2;
	}
	public String getDGEO_AAL3() {
		return DGEO_AAL3;
	}
	public String getDGEO_AAL4() {
		return DGEO_AAL4;
	}
	public String getDGEO_LOCALITY() {
		return DGEO_LOCALITY;
	}
}
