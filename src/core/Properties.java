/**
 * 
 * @author mike802
 *
 * brand_aware
 * ??? - 2019
 * 
 */
package core;

import java.io.File;
import java.io.IOException;

public class Properties {
	
	private String root;

	private String background;
	private String logo;
	private String altLogo;
	private String company;
	private String company_iframe;
	
	private String entryTitlesPath;
	private String entriesPath;
	private String filtersPath;
	
	private final String SEPERATOR = "##!#";
	private final String NULL_ENTRY_LIST = "<create new entry>";
	
	public Properties(String path, String userDir) throws IOException {
		root = path;		
		background = root + File.separator + "img" + File.separator + "background.png";
		logo = root + File.separator + "img" + File.separator + "logo.png";
		altLogo = root + File.separator + "img" + File.separator + "alt_logo.png";
		company = root + File.separator + "img" + File.separator + "company.png";
		company_iframe = root + File.separator + "img" + File.separator + "company_iframe";
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
	public String getBackground() {
		return background;
	}
	public String getLogo() {
		return logo;
	}
	public String getAltLogo() {
		return altLogo;
	}
	public String getCompany() {
		return company;
	}
	public String getCompanyIFrame() {
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
