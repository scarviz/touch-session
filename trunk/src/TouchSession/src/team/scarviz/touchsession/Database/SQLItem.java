package team.scarviz.touchsession.Database;

import java.util.ArrayList;
import java.util.List;

import team.scarviz.touchsession.Utility.BooleanUtility;
import team.scarviz.touchsession.Utility.StringUtility;



public class SQLItem {

	private List<KeyMap> args = new ArrayList<KeyMap>();

	public void setArgs(String column , Object value)
	{
		for(KeyMap arg : args)
		{
			if(StringUtility.compare(arg.getKey(),column))
				arg.setValue(value);
		}
	}

	public void addArgs(String column , Object value)
	{
		for(KeyMap arg : args)
		{
		if(StringUtility.compare(arg.getKey(),column))
			new Exception("すでに追加されている");
		}
		KeyMap key = new KeyMap();
		key.setKey(column);
		key.setValue(value);
		args.add(key);
	}


	private KeyMap getValue(String column)
	{
		for(KeyMap arg : args)
		{
			if(StringUtility.compare(arg.getKey(),column))
			{
				return arg;
			}
		}
		return null;
	}

	/**
	 * 文字列の返却
	 * @param column
	 * @return
	 */
	public String getValueString(String column)
	{
		KeyMap map = getValue(column);
		if(map != null)
			return map.getValueString();
		return "";
	}

	/**
	 * Intの返却
	 * @param column
	 * @return
	 */
	public int getValueInt(String column)
	{
		KeyMap map = getValue(column);
		if(map != null)
			return map.getValueInt();
		return Integer.MIN_VALUE;
	}

	/**
	 * Longの返却
	 * @param column
	 * @return
	 */
	public long getValueLong(String column)
	{
		KeyMap map = getValue(column);
		if(map != null)
			return map.getValueLong();
		return Long.MIN_VALUE;
	}

	/**
	 * Booleanの返却
	 * @param column
	 * @param defaultVal
	 * @return
	 */
	public boolean getValueBoolean(String column,boolean defaultVal)
	{
		KeyMap map = getValue(column);
		if(map != null){
			return BooleanUtility.toBoolean(map.getValueInt(),defaultVal);
		}
		return defaultVal;
	}

	/**
	 * Blobの返却
	 * @param column
	 * @return
	 */
	public byte[] getValueBlob(String column)
	{
		KeyMap map = getValue(column);
		if(map != null)
			return map.getValueBlob();
		return null;
	}

	public Object getValue(int index){
		return args.get(index).getValue();
	}
}
