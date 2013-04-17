package team.scarviz.touchsession.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLBase extends SQLiteOpenHelper
{
	protected static Context mContext;

	private SQLiteDatabase db;
	private List<ContentValues> values;
	public SQLBase(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext =  context;
		}
	/**
	 * * �f�[�^��}������
	 * * @param table �}������e�[�u����
	 * * @return �}���s/�}���ł��Ȃ������ꍇ�u-1�v
	 * */
	public long insert(SQLBase engine,String table) {
		if(!beginTransaction(engine)) return -1;
		long rowID = 0;
		try{
			if(values != null)
			{
				for(ContentValues val : values)
					rowID = db.insert(table, null, val);
			}
			db.setTransactionSuccessful();
			}catch(Exception e){
//				LogUtilityBase.outException(mContext,e);
			}finally{
				if(!endTransaction(db))
					return -1;
				values = null;
			}
			return rowID;
	}

	/**
	 * * �f�[�^��}������(���O�p�E���s������Log.e�ɂ���)
	 * * @param table �}������e�[�u����
	 * * @return �}���s/�}���ł��Ȃ������ꍇ�u-1�v
	 * */
	public long insertLog(SQLBase engine,String table) {
			if(!beginTransaction(engine)) return -1;
			long rowID = 0;
			try{
				if(values != null)
				{
					for(ContentValues val : values)
						rowID = db.insert(table, null, val);
				}
				db.setTransactionSuccessful();
				}catch(Exception e){
					Log.e("SQLLOG_INSERT_ERROR", e.getMessage());
				}finally{
					if(!endTransaction(db)) return -1;
					values = null;
					}
				return rowID;
	}


	/**
	 * replace����
	 * ID�����݂���ƃA�b�v�f�[�g/���݂��Ȃ���Insert
	 * @param engine Engine
	 * @param table �e�[�u����
	 * @nullValue �K�{�J�����̊���Null�l
	 * @return
	 */
	public long replace(SQLBase engine,String table,String nullValue)
	{
		if(!beginTransaction(engine)) return -1;
			try{
				long count = 0;
				if(values != null)
				{

					for(ContentValues val : values)
						count += db.replace(table, nullValue, val);
				}
				db.setTransactionSuccessful();
				return count;
				}catch(Exception e){
//					LogUtilityBase.outException(mContext,e);
					return -1;
				}finally{
					if(!endTransaction(db)) return -1;
					values = null;
				}
	}
	/**
	 * * �e�[�u���̃f�[�^���폜����
	 * * @param table �폜����e�[�u����
	 * * @param whereClause �폜����
	 * @param paramCol �o�C���h�ϐ��@�K��?�ƃJ�������̏����𓯂��ɂ��邱��
	 * */
	public int delete(SQLBase engine,String table,String whereClause, String[] paramCol) {
		if(!beginTransaction(engine)) return -1;
		int rowCount = 0;
			try{
				List<String> param = null;
				if(values != null)
				{

					for(ContentValues val : values)
					{
						if(paramCol != null)
						{
							param = new ArrayList<String>();
						for(String st : paramCol)
							param.add(val.getAsString(st));
						 rowCount += db.delete(table, whereClause, param.toArray(new String[0]));
						}
						else
							 rowCount += db.delete(table, whereClause,null);
					}
				}
				db.setTransactionSuccessful();
			}catch(Exception e){
				Log.e("delete error ", e.getMessage());
//				LogUtilityBase.outException(mContext,e);
				return -1;
			}finally{
				if(!endTransaction(db)) return -1;
				values = null;
				}
		return rowCount;
	}

	/**
	 * �A�b�v�f�[�g�����{����
	 * @param table �A�b�v�f�[�g�Ώۃe�[�u��
	 * @param whereClause ������
	 * @param paramCol �o�C���h�ϐ��@�K��?�ƃJ�������̏����𓯂��ɂ��邱��
	 */
	public int update(SQLBase engine,String table, String whereClause, String[] paramCol) {
		int pa = 0;
		if(!beginTransaction(engine)) return -1;
			try{

				List<String> param = null;

				if(values != null)
				{
					for(ContentValues val : values)
					{
						param = new ArrayList<String>();

						if(paramCol != null){
							for(String st : paramCol)
								param.add(val.getAsString(st));
							 pa += db.update(table, val, whereClause, param.toArray(new String[0]));
						}
						else
							 pa += db.update(table, val, whereClause, null);
					}
				}
				db.setTransactionSuccessful();
				return pa;
				}catch(Exception e){
//					LogUtilityBase.outException(mContext,e);
				}finally{
					if(!endTransaction(db)) return -1;
					values = null;
					}
				return pa;
				}

	/**
	 * Update����
	 * @param table			�e�[�u����
	 * @param whereClause	Where��
	 */
	public int update(SQLBase engine,String table, String whereClause) {
		return this.update(engine,table, whereClause,null);
	}

	 /**
     * * �}��/�X�V�p�̃f�[�^��ǉ�����
     * * @param data �}������l
     * */
	public void addArgs(ContentValues val) {
		if(values  == null)
			values = new ArrayList<ContentValues>();
		values.add(val);
	}

	/**
	 * �p�����[�^�̃Z�b�g
	 * @param val
	 */
	public void setArgs(List<ContentValues> val) {
		values = val;
	}

	/**
	 * Select����
	 * @param table			�e�[�u����
	 * @param columns		�J����
	 * @param selection		Where��
	 * @param selectionArgs	Where�o�C���h�ϐ�
	 * @param groupBy		GroupBy
	 * @param having
	 * @param orderBy		Order
	 * @return
	 */
	public List<SQLItem> select(SQLBase engine,String table,KeyMap[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
	{
		try
		{
		db = engine.getReadableDatabase();


		Cursor cursor = db.query(table,KeyMap.getKeys(columns),selection,
				selectionArgs,groupBy,having,orderBy);

		List<SQLItem> array = new ArrayList<SQLItem>();
		while(cursor.moveToNext())
		{
			SQLItem args = new SQLItem();
			int count = 0;
			for(KeyMap item : columns){
				switch(item.getType()){
				case Blob :
					args.addArgs(item.getKey(), cursor.getBlob(count));break;
				case Int :
					args.addArgs(item.getKey(), cursor.getInt(count));break;
				case Long :
					args.addArgs(item.getKey(), cursor.getLong(count));break;
				default :
					args.addArgs(item.getKey(), cursor.getString(count));break;
				}
				count++;
			}
			array.add(args);
		}
		if(cursor != null)
			cursor.close();
		cursor = null;
		return array;
		}
		catch(Exception e)
		{
//			LogUtilityBase.outException(mContext, e);
			return null;
		}
	}

	/**
	 * Select����
	 * @param table			�e�[�u����
	 * @param columns		�J����
	 * @param selection		Where��
	 * @param selectionArgs	Where�o�C���h�ϐ�
	 * @param groupBy		GroupBy
	 * @param having
	 * @param orderBy		Order
	 * @return
	 */
	public Cursor getSelectCursor(SQLBase engine,String table,KeyMap[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
	{
		try
		{
			db = engine.getReadableDatabase();
			Cursor cursor = db.query(table,KeyMap.getKeys(columns),selection,
					selectionArgs,groupBy,having,orderBy);
			return cursor;
		}
		catch(Exception e)
		{
//			LogUtilityBase.outException(mContext, e);
			return null;
		}
	}

	/**
	 * �J�[�\�������
	 * @param cursor �J�[�\��
	 */
	public void cursorClose(Cursor cursor){
		if(cursor != null)
			cursor.close();
		if(db.isOpen())
			db.close();

	}

	/**
	 * RawQuery�擾�pSQL
	 * @param sql		SQL
	 * @param columns	�J����
	 * @return
	 */
	public List<SQLItem> selectRawQuery(SQLBase engine,String sql , KeyMap[] columns)
	{
		try
		{
		db = engine.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql,null);
		List<SQLItem> array = new ArrayList<SQLItem>();
		while(cursor.moveToNext())
		{
			SQLItem args = new SQLItem();
			int count = 0;
			for(KeyMap item : columns){
				switch(item.getType()){
				case Blob :
					args.addArgs(item.getKey(), cursor.getBlob(count));break;
				case Int :
					args.addArgs(item.getKey(), cursor.getInt(count));break;
				case Long :
					args.addArgs(item.getKey(), cursor.getLong(count));break;
				default :
					args.addArgs(item.getKey(), cursor.getString(count));break;
				}
				count++;
			}
			array.add(args);
		}
		cursor.close();
		db.close();
		cursor = null;
		return array;
		}
		catch(Exception e)
		{
//			LogUtilityBase.outException(mContext, e);
			return null;
		}
	}

	/**
	 * SQL�̔��s
	 * @param engine
	 * @param sqlString
	 * @return
	 */
	public boolean execSQL(SQLBase engine , String sqlString){
		if(!beginTransaction(engine)) return false;

		try{

			db.execSQL(sqlString);
			db.setTransactionSuccessful();
			return true;
			}catch(Exception e){
//				LogUtilityBase.outException(mContext,e);
				return false;
			}finally{
				if(!endTransaction(db)) return false;
				}
		}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
	}
	/**
	 * �A�b�v�O���[�h����
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
	}

	/**
	 * �g�����U�N�V�����̊J�n����
	 * @param engine	DB�G���W��
	 * @return
	 */
	private boolean beginTransaction(SQLBase engine){
		try{
			db = engine.getWritableDatabase();
			db.beginTransaction();
		}catch(Exception e){
		//	LogUtilityBase.outException(mContext, e);
			return false;
		}
		return true;
	}

	/**
	 * �g�����U�N�V�����̏I������
	 * @param db	�f�[�^�x�[�X
	 * @return
	 */
	private boolean endTransaction( SQLiteDatabase db){
		try{
			if(db.isOpen() && db.inTransaction())
				db.endTransaction();
			else
				return false;
		}catch(Exception e){
//			LogUtilityBase.outException(mContext, e);
			return false;
		}
		return true;
	}
}
