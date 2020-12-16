package com.wheretobuy.adopteuncaddie.module.xml;

import android.util.Xml;

import androidx.lifecycle.AndroidViewModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class XmlSourceParser extends XmlParser
{
    public static List<DevelopmentSource> parse(InputStream in) throws XmlPullParserException, IOException {
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

    private static List<DevelopmentSource> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<DevelopmentSource> entries = new ArrayList<DevelopmentSource>();

        parser.require(XmlPullParser.START_TAG, ns, "sources");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("category")) {
//                entries.add(readEntry(parser));
                entries.addAll(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private static List<DevelopmentSource> readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<DevelopmentSource> sources = new ArrayList<DevelopmentSource>();

        parser.require(XmlPullParser.START_TAG, ns, "category");
        String category = parser.getAttributeValue(null, "title");
        String subcategory = null;
        List<Map.Entry<String, String>> links = new ArrayList<Map.Entry<String, String>>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();

            //            System.out.println("Line:" + name);

            if (name.equals("subcategory")) { sources.add(readSubcategory(parser, category)); }
            else if (name.equals("source")) { links.add(readLink(parser)); }
            else { skip(parser); }
        }
        if(links.size() > 0) { sources.add(new DevelopmentSource(category, null, links)); }
//        return (new DevelopmentSource(category, null, links));
        return sources;
    }

    private static String readCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "category");
        String title = parser.getAttributeValue(null, "title");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "category");
        return title;
    }

    // Processes source tags in the feed.
    private static DevelopmentSource readSubcategory(XmlPullParser parser, String category) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "subcategory");
        List<Map.Entry<String, String>> links = new ArrayList<Map.Entry<String, String>>();
        String subcategory = parser.getAttributeValue(null, "title");
//        parser.nextTag();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("source")) { links.add(readLink(parser)); }
            else { skip(parser); }
        }
        return new DevelopmentSource(category, subcategory, links);
    }

    // Processes link tags in the feed.
    private static Map.Entry<String, String> readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "source");
        String description = parser.getAttributeValue(null, "description");
        String link = parser.getAttributeValue(null, "link");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "source");
        return new AbstractMap.SimpleEntry<String, String>(description, link);
    }

    public static class DevelopmentSource {

        private String category;
        private String subcategory;
        private List<Map.Entry<String, String>> links;

        public DevelopmentSource(String category, String subcategory, List<Map.Entry<String, String>> links)
        {
            this.category = category;
            this.subcategory = subcategory;
            this.links = links;
        }

        public void addLink(String description, String url)
        {
            this.links.add(new AbstractMap.SimpleEntry<String, String>(description, url));
        }

        public String getCategory() { return this.category; }
        public String getSubcategory() { return this.subcategory; }
        public List<Map.Entry<String, String>> getLinks() { return this.links; }

    }
}
