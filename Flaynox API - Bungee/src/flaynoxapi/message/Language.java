package flaynoxapi.message;

import java.util.ArrayList;

import net.md_5.bungee.config.Configuration;

public class Language {

	public static ArrayList<Language> languages;

	private String name;
	private String fileName;

	public Language(String name, String fileName) {

		this.name = name;
		this.fileName = fileName;

	}

	public String getName() {
		return name;
	}

	public String getFileName() {
		return fileName;
	}

	public String toString() {

		return getName();

	}

	@Override
	public boolean equals(Object object) {

		if (object instanceof Language) {

			Language language = (Language) object;

			return language.getName().equalsIgnoreCase(getName());

		}

		return false;

	}

	protected static void initLanguages(Configuration config) {

		languages = new ArrayList<Language>();

		for (String languageNames : config.getSection("languages").getKeys()) {

			Language language = new Language(languageNames, config.getString("languages." + languageNames));

			languages.add(language);

		}

	}

}
