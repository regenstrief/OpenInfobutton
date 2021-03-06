/**
 * ...
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2010-2014 OpenInfobutton Project, Biomedical Informatics, University of Utah<br>
 * Contact: {@code <andrew.iskander@utah.edu>}<br>
 * Biomedical Informatics<br>
 * 421 Wakara Way, Ste 140
 * Salt Lake City, UT 84108-3514<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version May 5, 2014
 */
package org.openinfobutton.service.profile;

import java.util.ArrayList;
import java.util.List;

import org.openinfobutton.schemas.kb.KnowledgeResourceProfile;


public class ResourceProfileProvider {
	
	private static ResourceProfileProvider instance;
	
	private List<KnowledgeResourceProfile> profiles;

	private ResourceProfileProvider () {
		
		profiles = new ArrayList<KnowledgeResourceProfile>();
		ResourceProfileLoaderNew rpn = new ResourceProfileLoaderNew();
		profiles.addAll(rpn.getProfiles());
	}
	
	public static ResourceProfileProvider getInstance() {

		if (instance == null) {
			
			instance = new ResourceProfileProvider();
		}
		
		return instance;
	}
	
	public List<KnowledgeResourceProfile> getProfiles() {
		
		return profiles;
	}
	
	public void setProfiles (List<KnowledgeResourceProfile> profiles) {
		
		this.profiles = profiles;
		if (profiles.isEmpty()) {
			resetInstance();
		}
	}
	
	public static void resetInstance() {
		
		instance = null;
	}
}
