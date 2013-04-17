package team.scarviz.touchsession.Dto;

import java.util.ArrayList;
import java.util.List;

import team.scarviz.touchsession.Database.SQLBase;
import team.scarviz.touchsession.Database.SQLItem;
import team.scarviz.touchsession.Database.Table;
import team.scarviz.touchsession.Database.TableAccess;
import android.content.Context;

public class CompositionDto {

	private int id;
	private int soundId;
	private String soundFilePath;
	private int owner;

	public CompositionDto() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public CompositionDto(SQLItem item){
		id = item.getValueInt(Table.SoundData.ID);
		soundId = item.getValueInt(Table.SoundData.SOUND_ID);
		soundFilePath = item.getValueString(Table.SoundData.SOUND_FILE_PATH);
		owner = item.getValueInt(Table.SoundData.OWNER);
	}


	public static List<CompositionDto> getAllData(Context con){

		List<CompositionDto> retItem = new ArrayList<CompositionDto>();
		SQLBase sql = TableAccess.getInstance(con);
		List<SQLItem> items = sql.select(sql, Table.SoundData.TABLE, Table.SoundData.COLUMNS, null, null,null,null,null);
		//ほんとはNULLCHECKいる

		for(SQLItem item : items){
			retItem.add(new CompositionDto(item));
		}
		return retItem;
	}
}
