package app.iot.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import app.iot.library.log.entity.DaoMaster;
import app.iot.library.log.entity.DaoSession;


/**
 * Created by danbo on 2020/9/21.
 */
public class DBManager {

    private static DBManager manager = null;

    public static synchronized DBManager getInstance() {
        if (manager == null) {
            manager = new DBManager();
        }
        return manager;
    }

    //初始化数据库(需提前调用)
    public void setup(Context context) {
        if (mDaoSession == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "iot.db", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster master = new DaoMaster(db);
            mDaoSession = master.newSession();
        }
    }

    // greenDao session
    private DaoSession mDaoSession;


    public DaoSession getDaoSession() {
        return mDaoSession;
    }

}
