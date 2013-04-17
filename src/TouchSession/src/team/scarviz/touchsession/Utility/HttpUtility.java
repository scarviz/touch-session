package team.scarviz.touchsession.Utility;


import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import team.scarviz.touchsession.Dto.SoundDto;
import android.content.Context;


public class HttpUtility {

	/**
	 * Sound Data Upload
	 * @param con
	 * @param zandaka
	 * @return
	 */
	public static SoundDto uploadSoundData(Context con , int zandaka){
			HttpClient httpclient = new DefaultHttpClient();
			SoundDto sDto = null;
			try {
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					params.add(new BasicNameValuePair("zandaka", StringUtility.toString(zandaka)));
//					String result = post(httpclient,"url",params,false);
//				String json = get(httpclient,"url?key=" + result,false);
				sDto = new SoundDto();
				sDto.setOwner(1);
			} catch (Exception e) {
			} finally {
				httpclient.getConnectionManager().shutdown();
			}

			return sDto;
	}


		/**
		 * ポスト処理
		 * @param httpclient
		 * @param url
		 * @param params
		 * @param out
		 * @return
		 * @throws Exception
		 */
		private static String post(HttpClient httpclient, String url, List<NameValuePair> params, boolean out) throws Exception {
			HttpPost post = new HttpPost(url);
		    post.setEntity(new UrlEncodedFormEntity(params));
		    HttpResponse response  = httpclient.execute(post);
		    String result = EntityUtils.toString(response.getEntity());
		    post.abort();
		    return result;
		}


		/**
		 * Get
		 * @param httpclient
		 * @param url
		 * @param out
		 * @return
		 * @throws Exception
		 */
		private static String get(HttpClient httpclient, String url, boolean out) throws Exception {
			HttpGet get = new HttpGet(url);
		    HttpResponse response  = httpclient.execute(get);
		    String result = EntityUtils.toString(response.getEntity());
		    get.abort();
		    return result;
		}

}
