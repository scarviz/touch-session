package team.scarviz.touchsession.Utility;

public class BooleanUtility {

	/**
	 * BooleanŒ^‚Ö‚Ì•ÏX 0‚Æ1‚Å”»’f‚µ‚Ä‚¢‚é
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
	 * BooleanŒ^‚Ö‚Ì•ÏX 0‚Æ1‚Å”»’f‚µ‚Ä‚¢‚é
	 * @param obj
	 * @return
	 */
	public static boolean toBoolean(Object obj)
	{
		return toBoolean(obj,false);
	}

	/**
	 * BooleanŒ^‚©‚ç•¶š—ñ‚Ì•ÏX 0‚Æ1‚Å‚Ö•ÏŠ·‚µ‚Ä‚¢‚é
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
