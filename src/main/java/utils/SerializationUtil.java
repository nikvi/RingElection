package utils;

import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

import registry.ConnectionMsg;
public class SerializationUtil {
	/**
	* Convert object to byte array
	* @param object
	* @returns
	*/
	public static byte[] fromJavaToByteArray(Serializable object) {
		return SerializationUtils.serialize(object);
	}

	/**
	* Convert byte array to object
	* @param bytes
	* @return
	*/
	public static Object fromByteArrayToJava(byte[] bytes) {
		return SerializationUtils.deserialize(bytes);
	}

}
