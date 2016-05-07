package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import rating_model.Review;

public class XMLInput {

	// put in filepath of the document and the tag name which mark a review e.g.
	// "review"
	public static String[] readReviewData(String path, String tagname) {
		BufferedReader br;
		ArrayList<String> content = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null && line != "") {
				if (line.contains("<" + tagname)) {

				} else if (line.contains("</" + tagname)) {
					sb.append(System.lineSeparator());
					content.add(sb.toString());
					sb = new StringBuilder();

				} else {
					sb.append(line);
				}
				line = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content.toArray(new String[content.size()]);
	}

	// put in filepath of the document and the tag name which mark a review e.g.
	// "review"
	public static ArrayList<Review> readReviewDataAsList(String path,
			String tagname) {
		ArrayList<Review> revs=new ArrayList<Review>();
		try {
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName(tagname);

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
			
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					Review review=new Review(eElement.getAttribute("id"),nNode.getTextContent());
					revs.add(review);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return revs;
	}
}
