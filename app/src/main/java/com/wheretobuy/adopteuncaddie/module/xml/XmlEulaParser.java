package com.wheretobuy.adopteuncaddie.module.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlEulaParser extends XmlParser {
    public static List<Map.Entry<String, String>> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private static List<Map.Entry<String, String>> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>();

        parser.require(XmlPullParser.START_TAG, ns, "articles");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("article")) {
                entries.add(readArticle(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private static Map.Entry<String, String> readArticle(XmlPullParser parser) throws XmlPullParserException, IOException {
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<String, String>(null, null);
        parser.require(XmlPullParser.START_TAG, ns, "article");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("title")) {
                entry = new AbstractMap.SimpleEntry<String, String>(readFullXml(parser, "title"), null);
            }
            else if (name.equals("content")) {
                entry.setValue(readFullXml(parser, "content"));
            }
            else { skip(parser); }
        }
        return entry;
    }

//    private static Map.Entry<String, String> readArticle(XmlPullParser parser) throws XmlPullParserException, IOException {
//        parser.require(XmlPullParser.START_TAG, ns, "title");
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
//            String name = parser.getName();
//            if (name.equals("title")) {
//                entry = new AbstractMap.SimpleEntry<String, String>(readTitle(parser), null);
//            }
//            else if (name.equals("content")) {
//                entry.setValue(readContent(parser));
////                links.add(readLink(parser));
//            }
//            else { skip(parser); }
//        }
//        return entry;
//    }


}
