package team.scarviz.touchsession.Utility;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import team.scarviz.touchsession.Dto.CompositionDto;
import team.scarviz.touchsession.Dto.SoundDto;
import android.content.Context;
import android.os.Environment;
import android.util.Log;


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
				List<NameValuePair> params = new ArrayList<NameValuePair>();
					String result = post(httpclient,"http://gosrv.scarviz.net:59702/touchsession/nfcsound?nfcdata=" + zandaka,params,false);
					JSONObject json = new JSONObject(result);
					String soundId = json.getString("soundid");

					String path = getSoundSavePath(soundId);
					if(download(con, "http://gosrv.scarviz.net:59702/touchsession/reqsounddata?soundid=" + soundId,path ));
					if(!SoundDto.isExsits(con, NumberUtility.toInt(soundId))){
						sDto = new SoundDto();
						sDto.setSoundId(NumberUtility.toInt(soundId));
						sDto.setSoundFilePath(path);
						sDto.setOwner(1);
					}
			} catch (Exception e) {
				Log.d("err", e.getMessage());
			} finally {
				httpclient.getConnectionManager().shutdown();
			}

			return sDto;
	}


	private static String getSoundSavePath(String soundId){
		File f = Environment.getExternalStorageDirectory();
		String path = f.getAbsolutePath() + "/touchsession/" + soundId;
		return path;
	}

	/**
	 * Composition Data Upload & Download
	 * @param con
	 * @param zandaka
	 * @return
	 */
	public static CompositionDto uploadCompositionData(Context con , CompositionDto comp){
			HttpClient httpclient = new DefaultHttpClient();
			try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("json", JsonUtil.createJsonCompose(comp)));
					String result = post(httpclient,"http://gosrv.scarviz.net:59702/touchsession/regcompdata",params,false);
					JSONObject json = new JSONObject(result);
					String compId = json.getString("compid");
					comp.setComp_ms_id(NumberUtility.toInt(compId));
					return comp;
			} catch (Exception e) {
			} finally {
				httpclient.getConnectionManager().shutdown();
			}

			return null;
	}

	/**
	 * Composition Data Upload & Download
	 * @param con
	 * @param zandaka
	 * @return
	 */
	public static List<CompositionDto> getCompositionData(Context con ){
			HttpClient httpclient = new DefaultHttpClient();
			try {
				String result = get(httpclient,"http://gosrv.scarviz.net:59702/touchsession/reqcomplist",false);


				//サウンドデータのダウンロードとデータベースへの挿入処理
				List<Integer> sounds = JsonUtil.createSoundIdsJson(result);
				for(int soundId : sounds){
					String path = getSoundSavePath(StringUtility.toString(soundId));
					if(download(con, "http://gosrv.scarviz.net:59702/touchsession/reqsounddata?soundid=" + soundId,path ));
					if(!SoundDto.isExsits(con, NumberUtility.toInt(soundId))){
						SoundDto sDto = new SoundDto();
						sDto.setSoundId(NumberUtility.toInt(soundId));
						sDto.setSoundFilePath(path);
						sDto.setOwner(0);
					}
				}

				//取得した拍子データをInsert
				List<CompositionDto> comps = JsonUtil.createComposeJson(con, result);
				for(CompositionDto comp : comps){
					if(!CompositionDto.isExsits(con, comp.getComp_ms_id())){
						comp.insert(con);
					}
				}

				return CompositionDto.getAllData(con);
			} catch (Exception e) {
			} finally {
				httpclient.getConnectionManager().shutdown();
			}

			return null;
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

		private static boolean download(Context con,String downloadUrl , String fileName){
			try{
			int responseCode = 0;
		    int BUFFER_SIZE = 10240;
		    URI url = URI.create(downloadUrl);

		    HttpClient hClient = new DefaultHttpClient();
		    HttpGet hGet = new HttpGet();
		    HttpResponse hResp = null;

		    hClient.getParams().setParameter("http.connection.timeout", new Integer(15000));

		    hGet.setURI(url);

		    hResp = hClient.execute(hGet);

		    responseCode = hResp.getStatusLine().getStatusCode();
//		    String tmp = con.getCacheDir().getAbsolutePath() + "/wav.tmp";
		    String tmp = fileName;
		    if (responseCode == HttpStatus.SC_OK) {
		        File file = new File(tmp);
		        InputStream is = hResp.getEntity().getContent();
		        BufferedInputStream in = new BufferedInputStream(is, BUFFER_SIZE);
		        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file, false), BUFFER_SIZE);

		        byte buf[] = new byte[BUFFER_SIZE];
		        int size = -1;
		        while((size = in.read(buf)) != -1) {
		            out.write(buf, 0, size);
		        }
		        out.flush();

		        out.close();
		        in.close();
//		        File outFIle = new File(fileName);
//		        file.renameTo(outFIle);
				return true;
		    }
		    }catch(Exception e){
		    	Log.d("http error", e.getMessage());
		    }finally{

		    }

			return false;
		}

}
