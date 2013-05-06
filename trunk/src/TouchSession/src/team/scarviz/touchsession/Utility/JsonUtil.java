package team.scarviz.touchsession.Utility;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import team.scarviz.touchsession.Data.SoundRhythmData;
import team.scarviz.touchsession.Dto.CompositionDto;
import android.content.Context;

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


	public static String createJsonCompose(CompositionDto dto) throws JSONException{
		JSONObject jsonobj = new JSONObject();


		List<SoundRhythmData> rhythms = createRhythmJson(dto.getCompositionJson());

		jsonobj.put("title", dto.getTitle());
		jsonobj.put("rhythm",dto.getRhythm());


		JSONArray jsonArray = new JSONArray();
		for(SoundRhythmData sound : rhythms){
			jsonArray.put(sound.getSoundId());
		}
		jsonobj.put("composition", jsonArray);

		return jsonobj.toString();

	}

	public static List<CompositionDto> createComposeJson(Context con,String jsonString) throws JSONException{
		List<CompositionDto> retList = new ArrayList<CompositionDto>();

		JSONObject jsonobject = new JSONObject(jsonString);
		JSONArray kv = jsonobject.getJSONArray("compdatalist");

		for(int i = 0;i<kv.length();i++){
			CompositionDto dto = new CompositionDto();
			dto.setComp_ms_id(kv.getJSONObject(i).getInt("compid"));
			dto.setTitle(kv.getJSONObject(i).getString("title"));
			dto.setRhythm(kv.getJSONObject(i).getDouble("rhythm"));
			dto.setEditing(0);
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("composition", kv.getJSONObject(i).getJSONArray("composition"));
			dto.setCompositionJson(jsonobj.toString());
			retList.add(dto);
		}

		return retList;
	}

	public static List<Integer> createSoundIdsJson(String jsonString) throws JSONException{
		List<Integer> retList = new ArrayList<Integer>();
		JSONObject jsonobject = new JSONObject(jsonString);
		JSONArray kv = jsonobject.getJSONArray("soundidlist");

		for(int i = 0;i<kv.length();i++){
			retList.add(kv.getInt(i));
		}
		return retList;
	}
}
