package pt.ist.cm.util;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerializationTool {

	private static final Gson gson = new GsonBuilder()
			.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();

	@SuppressWarnings("unchecked")
	public static <T> T getObjectFromString(String input) {

		String fields[] = input.split(":", 2);

		try {

			Class<T> clazz = (Class<T>) Class.forName(fields[0]);

			T res = gson.fromJson(fields[1], clazz);

			return res;

		} catch (Exception e) {
			throw new RuntimeException("Error, cannot instanciate class!", e);
		}

	}

	public static <T> String getStringFromObject(T object) {

		String name = object.getClass().getName();

		String repr = gson.toJson(object);

		return name + ":" + repr + "\n";

	}
}
