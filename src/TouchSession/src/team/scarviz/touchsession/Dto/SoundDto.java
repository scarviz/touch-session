package team.scarviz.touchsession.Dto;

import java.util.ArrayList;
import java.util.List;

import team.scarviz.touchsession.Database.SQLBase;
import team.scarviz.touchsession.Database.SQLItem;
import team.scarviz.touchsession.Database.Table;
import team.scarviz.touchsession.Database.TableAccess;
import team.scarviz.touchsession.Utility.StringUtility;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;

public class SoundDto {

	private int id;
	private int soundId;
	private String soundFilePath;
	private int owner;

	private int soundColor;






	public int getSoundColor() {
		return soundColor;
	}

	public void setSoundColor(int soundColor) {
		this.soundColor = soundColor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSoundId() {
		return soundId;
	}

	public void setSoundId(int soundId) {
		this.soundId = soundId;
	}

	public String getSoundFilePath() {
		return soundFilePath;
	}

	public void setSoundFilePath(String soundFilePath) {
		this.soundFilePath = soundFilePath;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public SoundDto() {
	}

	public SoundDto(SQLItem item){
		id = item.getValueInt(Table.SoundData.ID);
		soundId = item.getValueInt(Table.SoundData.SOUND_ID);
		soundFilePath = item.getValueString(Table.SoundData.SOUND_FILE_PATH);
		owner = item.getValueInt(Table.SoundData.OWNER);
	}

	public static SoundDto select(Context con ,int soundId){
		SQLBase sql = TableAccess.getInstance(con);
		List<SQLItem> items = sql.select(sql,
				Table.SoundData.TABLE,
				Table.SoundData.COLUMNS,
				Table.SoundData.SOUND_ID + "=?", new String[]{StringUtility.toString(soundId)}, null, null, null);

		if(items != null && items.size() > 0){
			return new SoundDto(items.get(0));
		}
		return null;
	}


	public static List<SoundDto> getAllData(Context con){

		List<SoundDto> retItem = new ArrayList<SoundDto>();
		SQLBase sql = TableAccess.getInstance(con);
		List<SQLItem> items = sql.select(sql, Table.SoundData.TABLE, Table.SoundData.COLUMNS, null, null,null,null,null);
		//�ق�Ƃ�NULLCHECK����

		int count = 0;
		for(SQLItem item : items){
			SoundDto dto = new SoundDto(item);

			int patern = count % 5;
			dto.soundColor = Color.WHITE;
			switch(patern){
			case 0:	dto.soundColor = Color.RED; break;
			case 1: dto.soundColor = Color.BLUE; break;
			case 2: dto.soundColor = Color.CYAN; break;
			case 3: dto.soundColor = Color.GREEN; break;
			case 4: dto.soundColor = Color.MAGENTA; break;
			}

			retItem.add(dto);
			count++;
		}
		return retItem;
	}

	public boolean insert(Context con){
		SQLBase sql = TableAccess.getInstance(con);
		ContentValues val = new ContentValues();
		val.put(Table.SoundData.OWNER, owner);
		val.put(Table.SoundData.SOUND_FILE_PATH, soundFilePath);
		val.put(Table.SoundData.SOUND_ID, soundId);
		sql.addArgs(val);
		if(0 <= sql.insert(sql, Table.SoundData.TABLE))
			return true;
		else
			return false;
	}

	public boolean update(Context con){
		SQLBase sql = TableAccess.getInstance(con);
		ContentValues val = new ContentValues();
		val.put(Table.SoundData.OWNER, owner);
		val.put(Table.SoundData.SOUND_FILE_PATH, soundFilePath);
		val.put(Table.SoundData.SOUND_ID, soundId);
		sql.addArgs(val);
		if(0 <= sql.update(sql, Table.SoundData.TABLE,Table.SoundData.ID + "=" + this.soundId))
			return true;
		else
			return false;
	}


	public static boolean isExsits(Context con , int soundId){
		SoundDto dto = select(con,soundId);
		if(dto != null) return true;
		else
			return false;
	}


	public static  SoundDto getSoundData(Context con , int soundId){
		SQLBase sql = TableAccess.getInstance(con);
		List<SQLItem> item = sql.select(sql,
				Table.SoundData.TABLE,
				Table.SoundData.COLUMNS,
				Table.SoundData.SOUND_ID +"=?",
				new String[]{StringUtility.toString(soundId)}, null,null,null);

		if(item == null || item.size() <= 0) return null;

		return new SoundDto(item.get(0));

	}
}
