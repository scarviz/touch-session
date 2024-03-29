package team.scarviz.touchsession.Utility;

public class BooleanUtility {

	/**
	 * Boolean型への変更 0と1で判断している
	 * @param obj
	 * @return
	 */
	public static boolean toBoolean(Object obj,boolean defaultValue)
	{
		if(NumberUtility.toInt(obj) == 1)
			return true;
		else if(NumberUtility.toInt(obj) == 0)
			return false;

		return defaultValue;
	}

	/**
	 * Boolean型への変更 0と1で判断している
	 * @param obj
	 * @return
	 */
	public static boolean toBoolean(Object obj)
	{
		return toBoolean(obj,false);
	}

	/**
	 * Boolean型から文字列の変更 0と1でへ変換している
	 * @param val
	 * @return
	 */
	public static int boolToInt(boolean val)
	{
		if(val)
			return 1;
		else
			return 0;
	}
}
