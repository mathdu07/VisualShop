package fr.mathdu07.visualshop.config;

/**
 * A config property
 *
 * @param <T> - The property's type
 */
public class Property<T> {
	
	private final String key;
	private T value;
	private final T defaultValue;
	
	public Property (String key, T defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	/**
	 * @return the value of the property
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value - the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the defaultValue
	 */
	public T getDefaultValue() {
		return defaultValue;
	}

}
