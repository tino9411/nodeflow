package com.nodeflow.prompts;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PromptRegistry {

    private static final Map<String, String> templates = new HashMap<>();
    private static final String PROMPTS_DIRECTORY = "src/main/resources/prompts";

    static {
        loadPromptsFromDirectory();
    }

    /**
     * Loads all XML prompts from the prompts directory into the registry.
     */
    private static void loadPromptsFromDirectory() {
        File folder = new File(PROMPTS_DIRECTORY);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".xml")) {
                    loadPromptTemplate(file);
                }
            }
        } else {
            System.err.println("Prompt directory not found: " + PROMPTS_DIRECTORY);
        }
    }

    /**
     * Parses a single XML prompt file and adds it to the registry.
     *
     * @param file The XML file containing the prompt template.
     */
    
    private static void loadPromptTemplate(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            String promptName = file.getName().replace(".xml", "");
            String templateContent = extractTemplateContent(doc);

            templates.put(promptName, templateContent);
        } catch (Exception e) {
            System.err.println("Failed to load prompt template from " + file.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Extracts template content from the parsed XML document.
     *
     * @param doc The XML Document object representing the prompt.
     * @return The template content as a String.
     */
    private static String extractTemplateContent(Document doc) {
        Element templateElement = (Element) doc.getElementsByTagName("template").item(0);

        // Extract content with placeholders intact (assuming CDATA sections)
        return templateElement.getTextContent().trim();
    }

    /**
     * Retrieves a prompt template by name.
     * @param name The name of the template.
     * @return The template content, or a default message if not found.
     */
    public static String getTemplate(String name) {
        return templates.getOrDefault(name, "Analyze this data:\n{DATA}");
    }
    

}
