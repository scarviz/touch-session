package team.scarviz.touchsession.Database;

import java.util.ArrayList;
import java.util.List;

import team.scarviz.touchsession.Utility.NumberUtility;
import team.scarviz.touchsession.Utility.StringUtility;


public class KeyMap {
	public enum Type{
		String,
		Int,
		Long,
		Blob,
		Double,
	}
	private String key;
	private Object value;



	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public int getValueInt(){
		return NumberUtility.toInt(value);
	}

	public String getValueString(){
		return StringUtility.toString(value);
	}

	public Double getValueReal(){
		return NumberUtility.toDoubleNullofZero(StringUtility.toString(value));
	}

	public byte[] getValueBlob(){
		if(value == null)
			return null;
		else
			return (byte[])value;
	}

	public long getValueLong(){
		if(value == null)
			return Long.MIN_VALUE;
		else
			return Long.valueOf(StringUtility.toString(value));
	}

	public Type getType(){
		return (Type)getValue();
	}

	public KeyMap(String key, int value) {
		super();
		this.key = key;
		this.value = value;
	}

	public KeyMap(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public KeyMap(String key, byte[] value) {
		super();
		this.key = key;
		this.value = value;
	}

	public KeyMap(String key,Type type) {
		super();
		this.key = key;
		this.value = type;
	}

	public KeyMap(){
	}

	public static String[] getKeys(KeyMap[] key_maps){

		List<String> ret = new ArrayList<String>();
		for(KeyMap k : key_maps){
			ret.add(k.getKey());
		}
		return ret.toArray(new String[0]);
	}


}
