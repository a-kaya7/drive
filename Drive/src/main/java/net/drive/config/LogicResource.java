package net.drive.config;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.annotation.PostConstruct;

@Component
public class LogicResource {
	
	private Map<String , String> messages = new HashMap<>();
	
	
	  @PostConstruct
	    public void init() {
	        try {
	            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("LogicResource.xml");
	            if (inputStream == null) {
	                throw new RuntimeException("LogicResource.xml not found in classpath");
	            }

	            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
	            Element root = doc.getDocumentElement();
	            NodeList nodes = root.getChildNodes();

	            for (int i = 0; i < nodes.getLength(); i++) {
	                Node node = nodes.item(i);
	                if (node instanceof Element element) {
	                    messages.put(element.getTagName(), element.getTextContent());
	                }
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("Fehler beim Laden von LogicResource.xml", e);
	        }
	    }

	    public String getMessage(String key) {
	        return messages.getOrDefault(key, "Unbekannte Nachricht für Schlüssel: " + key);
	    }

}
