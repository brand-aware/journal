/**
 * 
 * @author wontzer
 *
 * brand_aware
 * ??? - 2019
 * 
 */
package core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Properties {
	
	private String root;

	private URL background;
	private URL logo;
	private URL altLogo;
	private URL company;
	private URL company_iframe;
	
	private String entryTitlesPath;
	private String entriesPath;
	private String filtersPath;
	
	private final String SEPERATOR = "##!#";
	private final String NULL_ENTRY_LIST = "<create new entry>";
	
	public Properties(String path, String userDir) throws IOException {
		root = path;		
		background = getClass().getResource("/img/background.png");
		logo = getClass().getResource("/img/logo.png");
		altLogo = getClass().getResource("/img/alt_logo.png");
		company = getClass().getResource("/img/company.png");
		company_iframe = getClass().getResource("/img/company_iframe");
		filtersPath = root + File.separator + "data" + File.separator + "filters.txt";
		
		//changes included to meet new Windows requirements
		String appData = userDir + File.separator + "AppData" + File.separator + "Local";
		String companyData = appData + File.separator + "brand-aware";
		File folderComp = new File(companyData);
		if(!folderComp.exists()) {
			folderComp.mkdir();
		}
		String productData = companyData + File.separator + "journal";
		File folderProduct = new File(productData);
		if(!folderProduct.exists()) {
			folderProduct.mkdir();
		}
		
		entryTitlesPath = productData + File.separator + "entry_titles";
		entriesPath = productData + File.separator + "entries";
	}
	
	public String getRoot() {
		return root;
	}
	public URL getBackground() {
		return background;
	}
	public URL getLogo() {
		return logo;
	}
	public URL getAltLogo() {
		return altLogo;
	}
	public URL getCompany() {
		return company;
	}
	public URL getCompanyIFrame() {
		return company_iframe;
	}
	public String getEntryTitlesPath() {
		return entryTitlesPath;
	}
	public String getEntriesPath() {
		return entriesPath;
	}
	public String getFiltersPath() {
		return filtersPath;
	}
	public String getSeperatorString() {
		return SEPERATOR;
	}
	public String getNullEntryString() {
		return NULL_ENTRY_LIST;
	}
}
