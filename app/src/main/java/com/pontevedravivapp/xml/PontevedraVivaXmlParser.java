package com.pontevedravivapp.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PontevedraVivaXmlParser {

    // XML TAGS
    private static final String CHANNEL = "channel";
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CONTENT = "content";
    private static final String AUTHOR = "author";
    private static final String URL = "url";
    private static final String RSS = "rss";

    private static final String namespace = null;


    public static List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }


    private static List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, namespace, RSS);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(CHANNEL)) {
                return readChannel(parser);
            }
        }
        return null;
    }


    private static List<Entry> readChannel(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<Entry> entries = new ArrayList<Entry>();
        parser.require(XmlPullParser.START_TAG, namespace, CHANNEL);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(ITEM)) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private static Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, namespace, ITEM);
        boolean readed = false; //Hack to prevent reading title twice.

        String title = null;
        String summary = null;
        String link = null;
        String author = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals(TITLE) && !readed) {
                title = readTitle(parser);
                readed = true;
            } else if (name.equals(DESCRIPTION)) {
                summary = readSummary(parser);
            } else if (name.equals(CONTENT)) {
                link = readLink(parser);
            } else if (name.equals(AUTHOR)) {
                author = readAuthor(parser);
            } else if (name.equals(CONTENT)) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }

        return new Entry(title, summary, link, author);
    }

    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, namespace, TITLE);
        return title;
    }

    private static String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, AUTHOR);
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, namespace, AUTHOR);
        return author;
    }

    private static String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link;
        parser.require(XmlPullParser.START_TAG, namespace, CONTENT);
        link = parser.getAttributeValue(namespace, URL);
        readText(parser);
        parser.require(XmlPullParser.END_TAG, namespace, CONTENT);
        return link;
    }

    private static String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, DESCRIPTION);
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, namespace, DESCRIPTION);
        return summary;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

}
