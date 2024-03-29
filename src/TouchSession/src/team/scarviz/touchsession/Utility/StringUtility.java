package team.scarviz.touchsession.Utility;

public class StringUtility {



	/**
	 * NULLか空文字の場合はTrue
	 * @param obj 対象
	 * @return NULLか空文字の場合はTrue
	 */
	public static boolean isNullOrEmpty(Object obj)
	{
		if(obj == null || obj.equals(""))
			return true;
		return false;
	}

	/**
	 * 文字列の比較
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean compare(Object obj1 , Object obj2)
	{
		return toString(obj1).equals(toString(obj2));
	}

	/**
	 * 文字への変換
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
