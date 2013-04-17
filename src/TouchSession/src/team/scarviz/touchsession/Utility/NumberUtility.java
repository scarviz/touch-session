package team.scarviz.touchsession.Utility;

import java.text.NumberFormat;

public class NumberUtility {

	/**
	 * Number�֕ϊ��ł��邩���ׂ�
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
	 * Int�ւ̕ϊ��E���s���� MIN_VALUE���A��
	 * @param value
	 * @return
	 */
	public static int toInt(Object value){
		return toInt(value,Integer.MIN_VALUE);
	}

	/**
	 * Int�ւ̕ϊ��E���s����def���A��
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
	 * double�ւ̕ϊ��E���s����0���A��
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
	 * �ʉ݃t�H�[�}�b�g������ɕϊ����ĕԋp����
	 * @param value �l
	 * @return �ϊ��l
	 */
	public static String toMoneyFormatString(double value){
		try{
	        // �ʉݐ��l�t�H�[�}�b�g�i�f�t�H���g�j
	        NumberFormat NF = NumberFormat.getCurrencyInstance();

	        return NF.format(value);
		}
		catch(Exception e){
			return e.getMessage();
		}
	}
}
