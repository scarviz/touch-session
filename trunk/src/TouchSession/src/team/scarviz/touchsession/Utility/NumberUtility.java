package team.scarviz.touchsession.Utility;

import java.text.NumberFormat;

public class NumberUtility {

	/**
	 * Numberへ変換できるか調べる
	 * @param value
	 * @return
	 */
	public static boolean isNumber(String value){
		try
		{
			Integer.valueOf(value);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}


	/**
	 * Intへの変換・失敗時は MIN_VALUEが帰る
	 * @param value
	 * @return
	 */
	public static int toInt(Object value){
		return toInt(value,Integer.MIN_VALUE);
	}

	/**
	 * Intへの変換・失敗時はdefが帰る
	 * @param value
	 * @return
	 */
	public static int toInt(Object value,int def){
		try{
			return Integer.valueOf(StringUtility.toString(value));
		}
		catch(Exception e){
			return def;
		}
	}

	/**
	 * doubleへの変換・失敗時は0が帰る
	 * @param value
	 * @return
	 */
	public static double toDoubleNullofZero(String value){
		try{
			return Double.valueOf(value);
		}
		catch(Exception e){
			return 0;
		}
	}

	/**
	 * 通貨フォーマット文字列に変換して返却する
	 * @param value 値
	 * @return 変換値
	 */
	public static String toMoneyFormatString(double value){
		try{
	        // 通貨数値フォーマット（デフォルト）
	        NumberFormat NF = NumberFormat.getCurrencyInstance();

	        return NF.format(value);
		}
		catch(Exception e){
			return e.getMessage();
		}
	}
}
