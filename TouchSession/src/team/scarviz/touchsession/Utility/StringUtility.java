package team.scarviz.touchsession.Utility;

public class StringUtility {



	/**
	 * NULL‚©‹ó•¶š‚Ìê‡‚ÍTrue
	 * @param obj ‘ÎÛ
	 * @return NULL‚©‹ó•¶š‚Ìê‡‚ÍTrue
	 */
	public static boolean isNullOrEmpty(Object obj)
	{
		if(obj == null || obj.equals(""))
			return true;
		return false;
	}

	/**
	 * •¶š—ñ‚Ì”äŠr
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean compare(Object obj1 , Object obj2)
	{
		return toString(obj1).equals(toString(obj2));
	}

	/**
	 * •¶š‚Ö‚Ì•ÏŠ·
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
