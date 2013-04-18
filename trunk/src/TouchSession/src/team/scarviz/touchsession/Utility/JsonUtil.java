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
				value += "\"" + sound.getSoundId() + "\",";
			else
				value += "\"\",";
		}

		value = value.substring(0, value.length() - 1);
		value +="]";
		kv.put("composition", value);
		jsonArray.put(kv);
		return jsonArray.toString();
	}

	public static List<SoundRhythmData> createRhythmJson(String jsonString) throws JSONException{
		List<SoundRhythmData> rhythms = new ArrayList<SoundRhythmData>();
		JSONArray jsonArray = new JSONArray(jsonString);
		JSONObject kv = jsonArray.getJSONObject(0);
		String comp = kv.getString("composition");

		String[] array = comp.split(",");

		for(String str : array){
			SoundRhythmData rhythm = new SoundRhythmData();
			String onpu = str.replace("[", "").replace("]", "").replace("\"", "");
			rhythm.setSoundId(NumberUtility.toInt(onpu, -1));
			rhythms.add(rhythm);
		}

		return rhythms;
	}

}
