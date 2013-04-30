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


public class CompositionDto {

	private int id;
	private double rhythm;
	private String compositionJson;
	private String title;
	private int editing;
	private int comp_ms_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getRhythm() {
		return rhythm;
	}
	public void setRhythm(double rhythm) {
		this.rhythm = rhythm;
	}
	public String getCompositionJson() {
		return compositionJson;
	}
	public void setCompositionJson(String compositionJson) {
		this.compositionJson = compositionJson;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getEditing() {
		return editing;
	}
	public void setEditing(int editing) {
		this.editing = editing;
	}
	public int getComp_ms_id() {
		return comp_ms_id;
	}
	public void setComp_ms_id(int comp_ms_id) {
		this.comp_ms_id = comp_ms_id;
	}

	public CompositionDto(SQLItem item){
		id = item.getValueInt(Table.CompositionData.ID);
		compositionJson = item.getValueString(Table.CompositionData.COMPOSITION_JSON);
		comp_ms_id = item.getValueInt(Table.CompositionData.COMP_MS_ID);
		rhythm = item.getValueDouble(Table.CompositionData.RHYTHM);
		title = item.getValueString(Table.CompositionData.TITLE);
	}

	/**
	 *
	 * @param con
	 * @param compId
	 * @return
	 */
	public static CompositionDto select(Context con ,int compId){
		SQLBase sql = TableAccess.getInstance(con);
		List<SQLItem> items = sql.select(sql,
				Table.CompositionData.TABLE,
				Table.CompositionData.COLUMNS,
				Table.CompositionData.COMP_MS_ID + "=?", new String[]{StringUtility.toString(compId)}, null, null, null);

		if(items != null && items.size() > 0){
			return new CompositionDto(items.get(0));
		}
		return null;
	}

	/**
	 *
	 */
	public CompositionDto() {
	}

	/**
	 *
	 * @param con
	 * @param compId
	 * @return
	 */
	public static boolean isExsits(Context con , int compId){
		CompositionDto dto = select(con,compId);
		if(dto != null) return true;
		else
			return false;
	}

	/**
	 *
	 * @param con
	 * @return
	 */
	public static List<CompositionDto> getAllData(Context con){

		List<CompositionDto> retItem = new ArrayList<CompositionDto>();
		SQLBase sql = TableAccess.getInstance(con);
		List<SQLItem> items = sql.select(sql, Table.CompositionData.TABLE, Table.CompositionData.COLUMNS, null, null,null,null,null);

		for(SQLItem item : items){
			CompositionDto dto = new CompositionDto(item);
			retItem.add(dto);
		}
		return retItem;
	}

	public boolean insert(Context con){
		SQLBase sql = TableAccess.getInstance(con);
		ContentValues val = new ContentValues();
		val.put(Table.CompositionData.COMP_MS_ID, comp_ms_id);
		val.put(Table.CompositionData.COMPOSITION_JSON, compositionJson);
		val.put(Table.CompositionData.EDITING, editing);
		val.put(Table.CompositionData.RHYTHM, rhythm);
		val.put(Table.CompositionData.TITLE, title);
		sql.addArgs(val);
		if(0 <= sql.insert(sql, Table.CompositionData.TABLE))
			return true;
		else
			return false;
	}
}
