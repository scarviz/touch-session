package team.scarviz.touchsession.Utility;

public class StringUtility {



	/**
	 * NULL���󕶎��̏ꍇ��True
	 * @param obj �Ώ�
	 * @return NULL���󕶎��̏ꍇ��True
	 */
	public static boolean isNullOrEmpty(Object obj)
	{
		if(obj == null || obj.equals(""))
			return true;
		return false;
	}

	/**
	 * ������̔�r
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean compare(Object obj1 , Object obj2)
	{
		return toString(obj1).equals(toString(obj2));
	}

	/**
	 * �����ւ̕ϊ�
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj)
	{
		if(isNullOrEmpty(obj))
			return "";
		else
			return obj.toString();
	}
}
