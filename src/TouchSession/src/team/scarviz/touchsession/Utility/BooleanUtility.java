package team.scarviz.touchsession.Utility;

public class BooleanUtility {

	/**
	 * Boolean�^�ւ̕ύX 0��1�Ŕ��f���Ă���
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
	 * Boolean�^�ւ̕ύX 0��1�Ŕ��f���Ă���
	 * @param obj
	 * @return
	 */
	public static boolean toBoolean(Object obj)
	{
		return toBoolean(obj,false);
	}

	/**
	 * Boolean�^���當����̕ύX 0��1�ł֕ϊ����Ă���
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
