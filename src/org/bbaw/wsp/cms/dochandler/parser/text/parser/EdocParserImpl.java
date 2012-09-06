package org.bbaw.wsp.cms.dochandler.parser.text.parser;

import java.net.URL;

import org.bbaw.wsp.cms.dochandler.parser.document.PdfDocument;
import org.bbaw.wsp.cms.dochandler.parser.metadata.MetadataRecord;
import org.bbaw.wsp.util.DcFetcherTool;

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
public class EdocParserImpl extends PdfParserImpl {
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
   * Parse an eDoc and return the object returned by the {@link ISaveStrategy} .
   * 
   * @return Object returned by the {@link ISaveStrategy}
   * @throws ApplicationException
   * @throws IllegalArgumentException
   *           if the uri is null or empty.
   * @throws IllegalStateException
   *           if the {@link ISaveStrategy} wasn't set before.
   */
  public Object parse(final String startUri, final String uri) throws ApplicationException {
    // Parse eDoc
    final Object parsedDoc = super.parsePages(startUri, uri);

    // Parse index.html and save DC fields.
    if (parsedDoc instanceof PdfDocument) {
      final PdfDocument parsedPDF = (PdfDocument) parsedDoc;
      MetadataRecord metadata = parsedPDF.getMetadata();
      if(metadata == null) {
        metadata = new MetadataRecord();
      }
      URL srcUrl;
      srcUrl = this.resourceReader.getURI(uri);
      srcUrl = EdocUriParser.getIndexURI(srcUrl);
      metadata = DcFetcherTool.fetchHtmlDirectly(srcUrl, metadata);
      parsedPDF.setMetadata(metadata);
      return parsedPDF;
    }

    return parsedDoc;
  }
}
