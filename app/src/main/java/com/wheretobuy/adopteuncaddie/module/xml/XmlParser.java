package com.wheretobuy.adopteuncaddie.module.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
public abstract class XmlParser {
    protected static final String ns = null;

    protected static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    // For the tags title and summary, extracts their text values.
    protected static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    protected static String readCompressedXml(XmlPullParser parser, String beacon, String attribute) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, beacon);
        String content = parser.getAttributeValue(null, attribute);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, beacon);
        return content;
    }

    protected static String readFullXml(XmlPullParser parser, String beacon) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, beacon);
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, beacon);
        return content;
    }

}
