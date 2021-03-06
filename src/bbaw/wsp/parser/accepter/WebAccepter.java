package bbaw.wsp.parser.accepter;

import java.util.Set;

/**
 * The WebAccepter accepts HTML resources. 
 * @author wsp-shk1 (Sascha Feldmann)
 *
 */
public class WebAccepter extends ResourceAccepter{

	/**
	 * Create a new WebAccepter.
	 * @param acceptedResources - a set that contains the accepted resources as string
	 */
	public WebAccepter(Set<String> acceptedResources) {
		super(acceptedResources);		
	}


	@Override
	public boolean acceptNode(String uri) {
		if(uri.matches("\\w+/")) {
			return true;
		}
		return false;
	}

}
