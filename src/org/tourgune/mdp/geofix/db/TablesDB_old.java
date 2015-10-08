package org.tourgune.mdp.geofix.db;


public class TablesDB_old extends TablesDB {
	
	@Override
	public String getTABLE_D_ACCOMMODATION() {
		// TODO Auto-generated method stub
		return "d_hotel dh";
	}
	@Override
	public String getDA_GM_COUNTRY() {
		// TODO Auto-generated method stub
		return "dh.gm_country";
	}
	@Override
	public String getDA_GM_AAL1() {
		return "dh.gm_aal1";
	}
	@Override
	public String getDA_GM_AAL2() {
		return "dh.gm_aal2";
	}
	@Override
	public String getDA_GM_AAL3() {
		return "dh.gm_aal3";
	}
	@Override
	public String getDA_GM_AAL4() {
		return "dh.gm_aal4";
	}
	@Override
	public String getDA_GM_LOCALITY() {
		return "dh.gm_locality";
	}
	@Override
	public String getTABLE_FT_GEO_MATCHING() {
		// TODO Auto-generated method stub
		return "ft_geo_matching ftgm";
	}
	@Override
	public String getFTGM_TYPE() {
		// TODO Auto-generated method stub
		return "ftgm.type";
	}
	public String getFTGM_COUNTRY() {
		return "ftgm.country";
	}
	public String getFTGM_AAL1() {
		return "ftgm.aal1";
	}
	public String getFTGM_AAL2() {
		return "ftgm.aal2";
	}
	public String getFTGM_AAL3() {
		return "ftgm.aal3";
	}
	public String getFTGM_AAL4() {
		return "ftgm.aal4";
	}
	public String getFTGM_LOCALITY() {
		return "ftgm.locality";
	}
	@Override
	public String getFTGM_BAD() {
		// TODO Auto-generated method stub
		return "ftgm.bad";
	}
	@Override
	public String getFTGM_GOOD() {
		// TODO Auto-generated method stub
		return "ftgm.good";
	}
}
