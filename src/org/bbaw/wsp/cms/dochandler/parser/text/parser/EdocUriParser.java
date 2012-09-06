package org.bbaw.wsp.cms.dochandler.parser.text.parser;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A (static) class which offers methods to parse an special eDoc uri. It's used
 * by the {@link EdocUriParser}.
 * 
 * @author Sascha Feldmann (wsp-shk1)
 * @date 15.08.2012
 * 
 */
public class EdocUriParser {

  /**
   * Return the {@link URL} to the associated index.html file. This contains the
   * metadata.
   * 
   * @param docURI
   *          the (input) uri (the uri to be parsed) of the eDoc.
   * @return the {@link URL} to the index.html file
   */
  public static URL getIndexURI(final URL docURI) {
    int lastSlash = docURI.toString().lastIndexOf("pdf/");
    String newString = docURI.toString().substring(0, lastSlash) + "index.html";

    URL indexURI;
    try {
      indexURI = new URL(newString);
      return indexURI;
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }
}
