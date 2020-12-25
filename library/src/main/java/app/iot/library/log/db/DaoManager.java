package app.iot.library.log.db;


import java.util.List;

import app.iot.library.DBManager;
import app.iot.library.log.entity.OperateRecord;
import app.iot.library.log.entity.OperateRecordDao;

public class DaoManager {

    private static DaoManager mInstance;

    private DaoManager() {
    }

    public static DaoManager instance() {
        synchronized (DaoManager.class) {
            if (mInstance == null) {
                mInstance = new DaoManager();
            }
        }
        return mInstance;
    }

    public void insertOrReplace(OperateRecord entity) {
        if (hasKey(entity)) {
            update(entity);
        } else {
            DBManager.getInstance().getDaoSession().insertOrReplace(entity);
        }
    }

    private boolean hasKey(OperateRecord entity) {
        return entity.getId() != null;
    }

    public List<OperateRecord> query(String no,boolean isDelete) {
        if (no == null || no.equals("")) {
            return DBManager.getInstance().getDaoSession().getOperateRecordDao().queryBuilder().where(OperateRecordDao.Properties.IsDeleted.eq(isDelete)).orderDesc(OperateRecordDao.Properties.Time).list();

        }
        return DBManager.getInstance().getDaoSession().getOperateRecordDao().queryBuilder()
                .where(OperateRecordDao.Properties.IsDeleted.eq(isDelete))
                .whereOr(
                        OperateRecordDao.Properties.EquipNo.like("%" + no + "%"),
                        OperateRecordDao.Properties.DeviceNo.like("%" + no + "%")
                ).orderDesc(OperateRecordDao.Properties.Time).list();

    }


    public void update(OperateRecord entity) {
        OperateRecordDao dao = DBManager.getInstance().getDaoSession().getOperateRecordDao();
        if (dao.hasKey(entity)) {
            dao.update(entity);
        }
    }

    public void delete(OperateRecord entity) {
        try {
            DBManager.getInstance().getDaoSession().delete(entity);
        } catch (Exception e) {
        }
    }

    public void deleteAll() {
        List<OperateRecord> all = query(null,false);
        for(OperateRecord record : all){
            record.setIsDeleted(true);
            record.setDeleteTime(System.currentTimeMillis());
            update(record);
        }
    }

    public void restoreAll() {
        List<OperateRecord> all = query(null,true);
        for(OperateRecord record : all){
            record.setIsDeleted(false);
            record.setDeleteTime(-1L);
            update(record);
        }
    }
}
