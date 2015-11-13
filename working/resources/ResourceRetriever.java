package resources;

import java.util.ResourceBundle;

public class ResourceRetriever {

	private final static String DEFAULT_RESOURCE_PACKAGE = "resources/";
	private static String myLanguage = "English";

	public ResourceRetriever(String language) {
		myLanguage = language;
	}

	public static String getLabel(String key) {
		ResourceBundle myLabels = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + myLanguage);
		return myLabels.getString(key);
	}

}
