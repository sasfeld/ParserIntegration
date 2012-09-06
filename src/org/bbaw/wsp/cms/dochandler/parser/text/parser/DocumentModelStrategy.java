package org.bbaw.wsp.cms.dochandler.parser.text.parser;

import java.util.List;

import org.bbaw.wsp.cms.dochandler.parser.document.GeneralDocument;
import org.bbaw.wsp.cms.dochandler.parser.document.IDocument;
import org.bbaw.wsp.cms.dochandler.parser.document.PdfDocument;

/**
 * This class realizes a DocumentModel - Strategy. That means the strategy
 * creates a new document model which is accessible by the {@link IDocument}
 * interface for each parsed document. Last change: saveFile() now uses a
 * {@link StringBuilder} to concatenate the fulltext String.
 * 
 * @author Sascha Feldmann (wsp-shk1)
 * @date 16.08.2012
 * 
 */
public class DocumentModelStrategy {

  public Object generateDocumentModel(final String startURI, final String uri, final String text) {
    IDocument document = new GeneralDocument(uri, text);
    return document;
  }

  public Object generateDocumentModel(final String startURI, final String uri, final List<String> textPages) {
    StringBuilder textBuilder = new StringBuilder();
    for (int i = 1; i <= textPages.size(); i++) {
      textBuilder.append("[page=" + i + "]\n" + textPages.get(i - 1));
    }
    IDocument document = new PdfDocument(uri, textBuilder.toString(), textPages);
    return document;
  }

}
