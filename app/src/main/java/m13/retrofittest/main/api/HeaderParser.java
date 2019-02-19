package m13.retrofittest.main.api;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */


public class HeaderParser {
    private static final String SEPARATOR = ","; //$NON-NLS-1$
    private static final String DOUBLE_SEPARATOR = ";"; //$NON-NLS-1$

    public static String getLinkToNextPage(Response response) {
        Headers headers = response.headers();
        String linkHeader = headers.get("link");
        String next = "";
        if (linkHeader != null) {
            String[] links = linkHeader.split(SEPARATOR);
            links: for (String link : links) {
                String[] segments = link.split(DOUBLE_SEPARATOR);
                if (segments.length < 2)
                    continue;
                String linkPart = segments[0].trim();
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
                    continue;
                linkPart = linkPart.substring(1, linkPart.length() - 1);

                linkElements: for (int i = 1; i < segments.length; i++) {
                    String[] rel = segments[i].trim().split("="); //$NON-NLS-1$
                    if (rel.length < 2) //|| !META_REL.equals(rel[0]))
                        continue;

                    String relValue = rel[1];
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                        relValue = relValue.substring(1, relValue.length() - 1);
                    if (relValue.equals("next")) {
                        next = linkPart;
                        break links;
                    }
                    //if (META_FIRST.equals(relValue)) first = linkPart;
                    //else if (META_LAST.equals(relValue)) last = linkPart;
                    //else if (META_NEXT.equals(relValue)) next = linkPart;
                    //else if (META_PREV.equals(relValue)) prev = linkPart;
                }
            }
        }
        //} else {
        //next = response.getHeader(HEADER_NEXT);
        //last = response.getHeader(HEADER_LAST);
        //}
        return next;
    }

    public static String getNextPageURL(Response response){
        Headers headers = response.headers();
        String linkHeader = headers.get("link");
        return getLinkElementByRel(linkHeader, "next");
    }

    private static String getLinkElementByRel(String linkHeader, String requiredRel){
        Log.wtf("GithubAPI", "getLInkElementByRel: linkHeader: " + linkHeader);
        Log.wtf("GithubAPI", "getLInkElementByRel: requiredRel: " + requiredRel);
        String linkElement = "";
        if (linkHeader != null) {
            String[] links = linkHeader.split(SEPARATOR);
            links: for (String link : links) {
                String[] segments = link.split(DOUBLE_SEPARATOR);
                if (segments.length < 2)
                    continue;
                String linkPart = segments[0].trim();
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
                    continue;
                linkPart = linkPart.substring(1, linkPart.length() - 1);

                linkElements: for (int i = 1; i < segments.length; i++) {
                    String[] rel = segments[i].trim().split("="); //$NON-NLS-1$
                    if (rel.length < 2) //|| !META_REL.equals(rel[0]))
                        continue;

                    String relValue = rel[1];
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                        relValue = relValue.substring(1, relValue.length() - 1);
                    if (relValue.equals(requiredRel)) {
                        linkElement = linkPart;
                        break links;
                    }
                    //if (META_FIRST.equals(relValue)) first = linkPart;
                    //else if (META_LAST.equals(relValue)) last = linkPart;
                    //else if (META_NEXT.equals(relValue)) linkElement = linkPart;
                    //else if (META_PREV.equals(relValue)) prev = linkPart;
                }
            }
        }
        //} else {
        //linkElement = response.getHeader(HEADER_NEXT);
        //last = response.getHeader(HEADER_LAST);
        //}
        Log.wtf("GithubAPI", "getLInkElementByRel: linkElement: " + linkElement);
        return linkElement;
    }

    public static Integer getLastPageNumber(Response response) {
        Headers headers = response.headers();
        String linkHeader = headers.get("link");
        String lastPageLink = getLinkElementByRel(linkHeader, "last");
        return getIntegerEndOfString(lastPageLink);
    }

    private static Integer getIntegerEndOfString(String s) {
        Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
        Matcher matcher = lastIntPattern.matcher(s);
        if (matcher.find()) {
            String someNumberStr = matcher.group(1);
            return Integer.parseInt(someNumberStr);
        }
        return 1;
    }


}
