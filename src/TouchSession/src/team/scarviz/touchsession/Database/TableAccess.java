package team.scarviz.touchsession.Database;





import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class TableAccess extends SQLBase {


	protected static SQLBase engine;

	public TableAccess(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		try{
			Table.createDB(db);
		}
		finally{
		}
		super.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Table.upGradeDB(db,oldVersion);
		// TODO 自動生成されたメソッド・スタブ
		super.onUpgrade(db, oldVersion, newVersion);
	}


	 public static SQLBase getInstance(Context context) {
		 if(engine == null){
			 engine = new TableAccess(context, Table.DBNAME,null,Table.DBVERSION);
			 }
		 return engine;
		 }

}