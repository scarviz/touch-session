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

		JSONObject jsonobj = new JSONObject();

		for(SoundRhythmData sound : sounds){
			jsonArray.put(sound.getSoundId());
		}
		jsonobj.put("composition", jsonArray);
		return jsonobj.toString();
	}

	public static List<SoundRhythmData> createRhythmJson(String jsonString) throws JSONException{
		List<SoundRhythmData> rhythms = new ArrayList<SoundRhythmData>();
		JSONObject jsonobject = new JSONObject(jsonString);
		JSONArray kv = jsonobject.getJSONArray("composition");

		for(int i = 0;i<kv.length();i++){
			SoundRhythmData rhythm = new SoundRhythmData();
			rhythm.setSoundId(kv.getInt(i));
			rhythms.add(rhythm);
		}
		return rhythms;
	}
}
