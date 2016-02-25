package com.example.trafficscotlandproto;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ryan on 25/02/16.
 */
public class RssParser {

    String output = null;

    public RssParser() throws ParserConfigurationException {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();

        output = "Test Parse";

        try {

//            File inputFile = new File(RssFeedHandler.getRssCurrentIncidents());
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(inputFile);
            Document doc = loadXmlFromString(RssFeedHandler.getRssCurrentIncidents());
            doc.getDocumentElement().normalize();

            output += "Root element :" + doc.getDocumentElement().getNodeName() + appendNewLine();

            NodeList nList = doc.getElementsByTagName("item");
            output += "----------------------------";
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                output += "\nCurrent Element :" + nNode.getNodeName() + appendNewLine();

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    output += "Student roll no : " + eElement.getAttribute("title") + appendNewLine();
                    output += "Description : "
                            + eElement
                            .getElementsByTagName("description")
                            .item(0)
                            .getTextContent() + appendNewLine();
                    output += "Link : "
                            + eElement
                            .getElementsByTagName("link")
                            .item(0)
                            .getTextContent() + appendNewLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            output = null;
        }

    }

    private String appendNewLine() {
        return "\n";
    }

    private Document loadXmlFromString(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
