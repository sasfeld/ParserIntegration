package bbaw.wsp.parser.accepter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;



/**
 * This abstract class offers methods to read from a config file and to accept crawled files.
 * 
 * It follows the Strategy pattern because it capsules the algorithm which check the resources.
 * 
 * @author Sascha Feldmann (wsp-shk1)
 *
 */
public abstract class ResourceAccepter {

	/**
	 * PDF file extension.
	 */
	public static final String EXT_PDF = ".pdf";
	/**
	 * DOC file extension.
	 */
	public static final String EXT_DOC = ".doc";
	/**
	 * Open document text extension.
	 */
	public static final String EXT_ODT = ".odt";
	/**
	 *  XML extension.
	 */
	public static final String EXT_XML = ".xml";
	/**
	 *  JPG extension.
	 */
	public static final String EXT_JPG = ".jpg";
	/**
	 *  TIFF extension.
	 */
	public static final String EXT_TIFF = ".tiff";
	/**
	 *  PNG extension.
	 */
	public static final String EXT_PNG = ".png";
	/**
	 *  HTML extension.
	 */
	public static final String EXT_HTML = ".html";
	/**
	 *  XHTML extension.
	 */
	public static final String EXT_XHTML = ".xhtml";
	/**
	 *  HTM extension.
	 */
	public static final String EXT_HTM = ".htm";
	/**
	 *  TXT extension.
	 */
	public static final String EXT_TXT = ".txt";
	
	protected Set<String> acceptedResources;

	/**
	 * Create a new {@link ResourceAccepter}.
	 * @param acceptedResources the set that contains the accepted resources as strings (".[extension]")
	 */
	public ResourceAccepter(final Set<String> acceptedResources) {
		this.acceptedResources = acceptedResources;
	}
	

	/**
	 * Check an (leaf)-resource. 
	 * @param uri - the URL to the (leaf-)resource
	 * @return true if the resource is accepted
	 */
	public boolean acceptLeaf(String uri) {
		for (String acceptorString : this.acceptedResources) {
			if(uri.endsWith(acceptorString)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check a node-resource (only designed to be used by the WebHarvester)
	 * @param uri - the URL of the node-resource
	 * @return ture if the node-resource is accepted
	 */
	public abstract boolean acceptNode(String uri);
	
	/**
	 * Fetch the URI's extension.
	 */
	public String getExtension(final String uri) {
		final int extPos = uri.lastIndexOf(".");
		final String extension = uri.substring(extPos, uri.length());
		
		return extension;
	}
	
	/**
	 * Check if the resource is an image.
	 * @param uri - the resource's URI.
	 * @return ture if the resource is an image.
	 */
	public boolean isImage(String uri) {		
		if(getExtension(uri).equals(EXT_JPG) || getExtension(uri).equals(EXT_TIFF)|| getExtension(uri).equals(EXT_PNG)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the resource is an eDoc.
	 * @param uri - the resource's URI.
	 * @return true if the resource is an (KOBV) eDoc.
	 */
	public boolean isEDoc(String uri) {
		File f = new File(uri);		
	
		if(f.getParentFile().getName().equals("pdf") && new File(f.getParentFile().getParentFile(), "index.html").exists()) {
			return true;
		}
		return false;
	}
	
}
