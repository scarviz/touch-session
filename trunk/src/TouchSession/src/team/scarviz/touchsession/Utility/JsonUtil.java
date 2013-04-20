package team.scarviz.touchsession.Utility;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import team.scarviz.touchsession.Data.SoundRhythmData;

public class JsonUtil {

	public static String createJsonRhythm(List<SoundRhythmData> sounds) throws JSONException{
		JSONArray jsonArray = new JSONArray();

		JSONObject kv = new JSONObject();
		String value = "[";

		for(SoundRhythmData sound : sounds){
			if(sound.getSoundId() >= 0)
				jsonArray.put(sound.getSoundId());
			else
				value += ",";
		}
		value = value.substring(0, value.length() - 1);
		value +="]";
		kv.put("composition", value);
		jsonArray.put(kv);
		return jsonArray.toString();
	}

	public static List<SoundRhythmData> createRhythmJson(String jsonString) throws JSONException{
		List<SoundRhythmData> rhythms = new ArrayList<SoundRhythmData>();
		JSONObject jsonobject = new JSONObject(jsonString);
		JSONArray kv = jsonobject.getJSONArray("composition");

		for(int i = 0;i<kv.length();i++){
			JSONObject json = kv.getJSONObject(i);
			SoundRhythmData rhythm = new SoundRhythmData();
			rhythm.setSoundId(NumberUtility.toInt(json.toString(), -1));
			json.toString();
		}
		return rhythms;
	}
}
