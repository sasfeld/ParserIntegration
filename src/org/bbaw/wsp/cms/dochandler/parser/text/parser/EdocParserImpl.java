package org.bbaw.wsp.cms.dochandler.parser.text.parser;

import org.bbaw.wsp.cms.dochandler.parser.document.IDocument;
import org.bbaw.wsp.cms.dochandler.parser.document.PdfDocument;
import org.bbaw.wsp.cms.dochandler.parser.metadata.MetadataRecord;
import org.bbaw.wsp.util.EdocIndexMetadataFetcherTool;

import de.mpg.mpiwg.berlin.mpdl.exception.ApplicationException;

/**
 * This class parses an eDoc. An eDoc consists of a basic pdf file and an
 * index.html file which contains the associated metadata. It's represented by
 * the folder structure: [year] / [eDocID] - index.html - /pdf/[eDoc.pdf]
 * 
 * @author Sascha Feldmann (wsp-shk1)
 * @date 15.08.2012
 * 
 */
public class EdocParserImpl extends HtmlParserImpl {
  private static EdocParserImpl instance;

  /**
   * Return the only existing instance. The instance uses an Apache PdfBox
   * stripper.
   * 
   * @return
   */
  public static EdocParserImpl getInstance() {
    if (instance == null) {
      return new EdocParserImpl();
    }
    return instance;
  }

  private EdocParserImpl() {
    super();
  }

  /**
   * Parse an eDoc and return the object returned by the {@link ISaveStrategy}
   * 
   * @param startUri
   *          the URI where the harvesting was started.
   * @param uri
   *          the URI to the eDoc's index.html (which contains the reference to
   *          the eDoc).
   * 
   * @return Object returned by the {@link ISaveStrategy}
   * @throws ApplicationException
   */
  public Object parse(final String startUri, final String uri) throws ApplicationException {
    // Parse eDoc index
    final Object parsedDocIndex = super.parse(startUri, uri);

    if (parsedDocIndex instanceof IDocument) {
      MetadataRecord metadata = new MetadataRecord();

      EdocIndexMetadataFetcherTool.fetchHtmlDirectly(uri, metadata);

      String eDocUrl = metadata.getRealDocUrl();

      if (eDocUrl != null) {
        // Parse eDoc
        System.out.println("eDocUrl: " + eDocUrl);
        final Object parsedEDoc = PdfParserImpl.getInstance().parsePages(startUri, eDocUrl);
        if (parsedEDoc instanceof PdfDocument) {
          final PdfDocument parsedPDF = (PdfDocument) parsedEDoc;
          parsedPDF.setMetadata(metadata);

          return parsedPDF;
        }
      }
      else {
        throw new ApplicationException("Couldn't fetch the eDoc's URL from the file: "+uri);
      }
    }

    return null;
  }

  public static void main(String[] args) throws ApplicationException {
    EdocParserImpl eDocParser = new EdocParserImpl();
    String uri = "C:/Dokumente und Einstellungen/wsp-shk1/Eigene Dateien/opus32_bbaw_volltexte_20120607/volltexte/2006/1/index.html";
    String startUri = "C:/Dokumente und Einstellungen/wsp-shk1/Eigene Dateien/opus32_bbaw_volltexte_20120607/volltexte/2006/1/";
    IDocument doc = (IDocument) eDocParser.parse(startUri, uri);
    System.out.println(doc);
  }
}
