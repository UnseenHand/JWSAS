package com.vuvp.wsas.services;

import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.save.SaveService;
import org.apache.jorphan.collections.SearchByClass;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Service
public class LoadTestResultExtractionService {

    // For the first element
    // Uses Java XML DOM parser
    public long getAverageResponseTime(String xmlFilePath) throws Exception {
        // Creating the document from XML file
        File xmlFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("httpSample");

        int totalCount = 0;
        int totalSum = 0;

        // Traverse through all the elements and check if it has a "t" attribute
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element httpSampleElement = (Element) nodeList.item(i);
            if (httpSampleElement.hasAttribute("t")) {
                // If it has "t" attribute, add its value to the running total
                totalCount++;
                totalSum += Double.parseDouble(httpSampleElement.getAttribute("t"));
            }
        }

        // Calculate the average "t" attribute value
        return totalSum / totalCount;
    }

    //This method should have been the main option, but there were some
    public long getResponseTimeNA(String xmlFilePath) throws Exception {
        // Load the XML file into a HashTree object
        var file = new File(xmlFilePath);
        var hashTree = SaveService.loadTree(file); //thrown

        // Search for all HTTPSampleResult objects in the HashTree
        SearchByClass<HTTPSampleResult> search = new SearchByClass<>(HTTPSampleResult.class);
        hashTree.traverse(search);

        // Extract the response time from the first HTTPSampleResult object
        var firstElement = search
                .getSearchResults()
                .stream()
                .findFirst()
                .orElseThrow();
        return firstElement.getTime();
    }
}