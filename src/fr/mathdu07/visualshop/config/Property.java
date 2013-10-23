package fr.mathdu07.visualshop.config;

/**
 * A config property
 *
 * @param <T> - The property's type
 */
public class Property<T> {
	
	public final String key;
	public T value;
	public final T defaultValue;
	
	public Property (String key, T defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

}
