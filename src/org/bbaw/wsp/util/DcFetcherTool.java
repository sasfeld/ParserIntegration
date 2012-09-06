/**
 * Several classes which offers methods that are used often.
 */
package org.bbaw.wsp.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bbaw.wsp.cms.dochandler.parser.metadata.MetadataRecord;

import de.mpg.mpiwg.berlin.mpdl.exception.ApplicationException;

/**
 * This tool class provides methods to fetch DC fields into a given
 * {@link MetadataRecord}. Last change: - Added fields documentType, isbn,
 * creationDate, publishingDate - 06.09.12: throws {@link ApplicationException}
 * now
 * 
 * @author Sascha Feldmann (wsp-shk1)
 * 
 */
public class DcFetcherTool {

  /**
   * This class reads from an URL and fetches the DC tags directly (with String
   * operations).
   * 
   * It's designed for the eDoc server.
   * 
   * @param srcUrl
   *          - the basic {@link URL}
   * @param mdRecord
   *          - the {@link MetadataRecord} to fill
   * @return the complete {@link MetadataRecord}
   * @throws ApplicationException
   *           if the index.html cannot get read.
   */
  public static MetadataRecord fetchHtmlDirectly(final URL srcUrl, final MetadataRecord mdRecord) throws ApplicationException {
    InputStream in;
    try {
      in = srcUrl.openStream();
      Scanner scanner = new Scanner(in);
      scanner.useDelimiter("\n"); // delimiter via line break
      StringBuilder builder = new StringBuilder();
      while (scanner.hasNext()) {
        builder.append(scanner.next()); // concat to one String
      }
      String line = builder.toString();
      StringBuilder creatorBuilder = new StringBuilder(); // fix: more than one
                                                          // creator
      Pattern p = Pattern.compile("<META NAME=\"(.*?)\" CONTENT=\"(.*?)\">(?i)"); // meta
                                                                                  // pattern
      for (Matcher m = p.matcher(line); m.find();) {
        String tag = m.group(1);
        String content = m.group(2);
        if (tag.equals("DC.Date.Creation_of_intellectual_content")) { // creation
                                                                      // date
          Calendar cal = new GregorianCalendar();
          cal.set(Calendar.YEAR, Integer.parseInt(content));
          cal.set(Calendar.DAY_OF_YEAR, 1);
          cal.set(Calendar.HOUR, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          mdRecord.setCreationDate(cal.getTime());
        } else if (tag.equals("DC.Title")) {
          mdRecord.setTitle(content);
        } else if (tag.equals("DC.Creator")) {
          if (creatorBuilder.toString().length() == 0) {
            creatorBuilder.append(content);
          } else {
            creatorBuilder.append(" ; " + content);
          }
          mdRecord.setCreator(creatorBuilder.toString());
        } else if (tag.equals("DC.Subject")) {
          mdRecord.setSwd(content); // DC.Subject follows the
                                    // Schlagwortnormdatei
        } else if (tag.equals("DC.Description")) {
          mdRecord.setDescription(content);
        } else if (tag.equals("DC.Identifier")) {
          if (content.contains("http://")) {
            mdRecord.setUri(content);
          } else if (content.contains("urn:")) {
            mdRecord.setUrn(content);
          }
        }
      }

      Pattern p2 = Pattern.compile("(?i)<TD class=\"frontdoor\" valign=\"top\"><B>(.*?)</B></TD>.*?<TD class=\"frontdoor\" valign=\"top\">(.*?)</TD><");
      for (Matcher m = p2.matcher(line); m.find();) {
        String key = m.group(1);
        String value = m.group(2).trim();
        System.out.println("key: "+key);
        System.out.println("value: "+value);
        if (key.contains("Freie Schlagwörter")) {
          mdRecord.setSubject(value);
        } else if (key.contains("DDC-Sachgruppe")) {
          mdRecord.setDdc(value);
        } else if (key.contains("Sprache")) {
          mdRecord.setLanguage(value);
        } else if (key.contains("Dokumentart")) {
          mdRecord.setDocumentType(value);
        } else if (key.contains("Publikationsdatum")) {
          final int day = Integer.parseInt(value.substring(0, value.indexOf(".")));
          final int month = Integer.parseInt(value.substring(value.indexOf(".") + 1, value.lastIndexOf(".")));
          final int year = Integer.parseInt(value.substring(value.lastIndexOf(".") + 1));

          Calendar cal = new GregorianCalendar();
          cal.set(year, month, day);
          mdRecord.setPublishingDate(cal.getTime());
        } else if (key.contains("ISBN")) {
          mdRecord.setIsbn(value);
        } else if (key.contains("Institut")) {
          mdRecord.setPublisher(value);
        } else if (key.contains("Collection")) {
          Pattern pColl = Pattern.compile("(?i)<a.*?>(.*?)</a>");
          Matcher mColl = pColl.matcher(value);
          mColl.find();
          String collections = mColl.group(1);

          mdRecord.setCollectionNames(collections);
        }
      }
      in.close();
      return mdRecord;
    } catch (IOException e) {
      throw new ApplicationException("Problem while parsing " + srcUrl + " for DC tags " + e.getMessage());
    }

  }

}
