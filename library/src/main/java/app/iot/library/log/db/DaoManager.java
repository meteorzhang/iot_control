package app.iot.library.log.db;


import android.util.Log;

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

    public List<OperateRecord> query(String no, boolean isDelete) {
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


    /**
     * 更新数据库
     *
     * @param entity
     */
    public void update(OperateRecord entity) {
        OperateRecordDao dao = DBManager.getInstance().getDaoSession().getOperateRecordDao();
        if (dao.hasKey(entity)) {
            dao.update(entity);
        }
    }

    /**
     * 清空单个-会到历史记录
     */
    public void delete(OperateRecord entity) {
        try {
            entity.setIsDeleted(true);
            entity.setDeleteTime(System.currentTimeMillis());
            update(entity);
        } catch (Exception e) {
        }
    }

    /**
     * 清空全部-会到历史记录
     */
    public void deleteAll() {
        List<OperateRecord> all = query(null, false);
        for (OperateRecord record : all) {
            record.setIsDeleted(true);
            record.setDeleteTime(System.currentTimeMillis());
            update(record);
        }
    }

    /**
     * 根据时间清空全部-会到历史记录
     */
    public void deleteDateAll(Long time) {
        List<OperateRecord> all = query(null, false);
        for (OperateRecord record : all) {
            Log.e("eeee", "拿到的时间" + record.getTime());
            if (time > record.getTime()) {
                record.setIsDeleted(true);
                record.setDeleteTime(System.currentTimeMillis());
                update(record);
            }
        }
    }

    /**
     * 恢复单个操作记录
     *
     * @param record
     */
    public void restoreSingle(OperateRecord record) {
        record.setIsDeleted(false);
        record.setDeleteTime(-1L);
        update(record);
    }

    /**
     * 恢复全部删除记录
     */
    public void restoreAll() {
        List<OperateRecord> all = query(null, true);
        for (OperateRecord record : all) {
            record.setIsDeleted(false);
            record.setDeleteTime(-1L);
            update(record);
        }
    }
}
