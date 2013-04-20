package smarttechaward2013.scarviz.touringnavi.Logic;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * WebServiceのユーティリティクラス
 */
public class WebServiceUtils {
	
	/**
	 * JSONObjectを取得する
	 * @param path　WebServiceのURL文字列
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public static JSONObject GetJSON(String path) throws JSONException, IOException{
		InputStream in = null;
		HttpURLConnection conn = null;
		
		try{
			// URL文字列からURLを生成する
			URL url = new URL(path.toString());
			// コネクションを生成する
			conn = (HttpURLConnection) url.openConnection();
			// 接続タイムアウトを設定(20000ミリ秒)
			conn.setConnectTimeout(20000);
			// 読み込みタイムアウトを設定(20000ミリ秒)
			conn.setReadTimeout(20000);
			
			// HTTPレスポンスコードを取得
			int resCD = conn.getResponseCode();
			// レスポンスコードが正常の場合
			if(resCD >= 200 && resCD <= 206){
				in = conn.getInputStream();
				
				// コネクションから取得用のStreamを生成し、文字列を取得
				String jsonStr = new String(getByteArrayFromStream(in));
				// 取得した文字列からJSONObjectを生成する
				return new JSONObject(jsonStr);
			}
			else {
				throw new IOException("ERR CD :" + resCD);
			}
		}
		finally {
			if(in != null) {
				try {
					in.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(conn != null) {
				conn.disconnect();
			}
		}
	}
	
	/**
	 * Streamをbyte配列に変換する
	 * @param in
	 * @return
	 */
    public static byte[] getByteArrayFromStream(InputStream in) {
        byte[] line = new byte[512];
        byte[] result = null;
        ByteArrayOutputStream out = null;
        int size = 0;
        try {
            out = new ByteArrayOutputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            while (true) {
                size = bis.read(line);
                if (size < 0) {
                    break;
                }
                out.write(line, 0, size);
            }
            result = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * JSONObject method=postを実行し、結果を取得する
     * @param path WebServiceのURL文字列
     * @param post JSONの文字列(JSONObject.toString()後)
     * @param apikey API Key
     * @return returned JSON object.
     * @throws JSONException
     * @throws IOException
     */
    public static JSONObject PostJSONwithApiKey(String path, JSONObject post, String apikey)
        throws JSONException, IOException {

        return PostJSONwithApiKey(path, post.toString(), apikey);
    }

    /**
     * JSONObject method=postを実行し、結果を取得する
     * @param path
     * @param post
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static JSONObject PostJSON(String path, String post)
            throws JSONException, IOException {
        return PostJSONwithApiKey(path, post, null);
    }

    /**
     * JSONObject method=postを実行し、結果を取得する
     * @param path
     * @param post
     * @param apikey
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static JSONObject PostJSONwithApiKey(String path, String post, String apikey)
        throws JSONException, IOException {
        InputStream in = null;
        HttpURLConnection conn = null;

        try{
            URL url = new URL(path.toString());
            conn = (HttpURLConnection)url.openConnection();
            
            // 接続タイムアウトを設定(20000ミリ秒)
            conn.setConnectTimeout(20000);
            // 読み込みタイムアウトを設定(20000ミリ秒)
            conn.setReadTimeout(20000);

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; char-set: UTF-8");
            if (apikey != null) {            	
                conn.setRequestProperty("Authorization", "key=" + apikey);
            }
            
            PrintStream ps = new PrintStream(conn.getOutputStream());
            ps.print(post);
            ps.close();

            // HTTPレスポンスコードを取得
            int resCD = conn.getResponseCode();
            // レスポンスコードが正常の場合
            if(resCD >= 200 && resCD <= 206){
                in = conn.getInputStream();

                // コネクションから取得用のStreamを生成し、文字列を取得
                String jsonStr = new String(getByteArrayFromStream(in));
                // 取得した文字列からJSONObjectを生成する
                return new JSONObject(jsonStr);
            }
            else {
                throw new IOException("ERR CD :" + resCD);
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
