package team.scarviz.touchsession.Database;


import team.scarviz.touchsession.Database.KeyMap.Type;
import android.database.sqlite.SQLiteDatabase;


public class Table {
	public static final String DBNAME = "database.db";
	public static final int DBVERSION = 1;


	public interface SoundData {
		public static final String TABLE = "SOUND_DATA_TB";
		public static final String ID = "_ID";
		public static final String SOUND_ID = "SOUND__ID";
		public static final String SOUND_FILE_PATH = "SOUND_FILE_PATH";
		public static final String OWNER = "OWNER";

		public static final KeyMap[] COLUMNS ={
			new KeyMap(ID,Type.Int),
			new KeyMap(SOUND_ID,Type.Int),
			new KeyMap(SOUND_FILE_PATH,Type.String),
			new KeyMap(OWNER,Type.Int),
		};
	}

	public interface CompositionData {
		public static final String TABLE = "COMPOSITION_DATA_TB";
		public static final String ID = "_ID";
		public static final String RHYTHM = "RHYTHM";
		public static final String COMPOSITION_JSON = "COMPOSITION_JSON";
		public static final String TITLE = "TITLE";
		public static final String EDITING = "EDITING";
		public static final String COMP_MS_ID = "COMP_MS_ID";

		public static final KeyMap[] COLUMNS ={
			new KeyMap(ID,Type.Int),
			new KeyMap(RHYTHM,Type.Double),
			new KeyMap(COMPOSITION_JSON,Type.String),
			new KeyMap(TITLE,Type.String),
			new KeyMap(EDITING,Type.Int),
			new KeyMap(COMP_MS_ID,Type.Int),

		};
	}


	/**
	 * OnCreateèàóù
	 * @param db
	 */
	public static void createDB(SQLiteDatabase db)
	{

		try{
			db.execSQL("create table "+
					Table.SoundData.TABLE + " ( " +
					Table.SoundData.ID + " integer primary key autoincrement, " +
					Table.SoundData.SOUND_ID + " integer not null, " +
					Table.SoundData.SOUND_FILE_PATH + " text, " +
					Table.SoundData.OWNER + " integer not null )");

			db.execSQL("create table "+
					Table.CompositionData.TABLE + " ( " +
					Table.CompositionData.ID + " integer primary key autoincrement, " +
					Table.CompositionData.RHYTHM + " real not null, " +
					Table.CompositionData.COMPOSITION_JSON + " text, " +
					Table.CompositionData.TITLE + " text, " +
					Table.CompositionData.COMP_MS_ID + " integer, " +
					Table.CompositionData.EDITING + " integer not null )");
		}
		finally
		{
		}
	}


		/**
		 * OnUpgradeèàóù
		 * @param db
		 */
		public static void upGradeDB(SQLiteDatabase db , int oldVersion)
		{

		}
}
