package app.iot.library.log.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;

@Entity(nameInDb = "operate_record")
public class OperateRecord implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;
    @Property
    private String account;
    @Property
    private int type;
    @Property
    private boolean isSuccess;
    @Property
    private String content;

    //设备绑定信息
    @Property
    private String equipType;
    @Property
    private String equipModel;
    @Property
    private String deviceType;
    @Property
    private String deviceModel;
    @Property
    private String equipNo;

    //更换绑定后的设备编号
    @Property
    private String deviceNo;
    @Property
    private String newDeviceNo;

    @Property
    private long time;
    @Property
    private String remark;

    @Property
    private boolean isDeleted;
    @Property
    private long deleteTime;

    //检查设备用
    public OperateRecord(Long id, String account, int type, boolean isSuccess,
                         String content,String deviceNo, long time, String remark) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.isSuccess = isSuccess;
        this.content = content;
        this.deviceNo = deviceNo;
        this.time = time;
        this.remark = remark;
    }

    //解绑
    public OperateRecord(Long id, String account, int type, boolean isSuccess,
                         String content, String equipNo, String deviceNo, long time, String remark) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.isSuccess = isSuccess;
        this.content = content;
        this.equipNo = equipNo;
        this.deviceNo = deviceNo;
        this.time = time;
        this.remark = remark;
    }

    //绑定用
    public OperateRecord(Long id, String account, int type, boolean isSuccess,
                         String content, String equipType, String equipModel, String deviceType,
                         String deviceModel, String equipNo, long time, String remark) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.isSuccess = isSuccess;
        this.content = content;
        this.equipType = equipType;
        this.equipModel = equipModel;
        this.deviceType = deviceType;
        this.deviceModel = deviceModel;
        this.equipNo = equipNo;
        this.time = time;
        this.remark = remark;
    }

    //更换设备用
    public OperateRecord(Long id, String account, int type, boolean isSuccess,
                         String content,String equipNo, String deviceNo, String newDeviceNo, long time, String remark) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.isSuccess = isSuccess;
        this.content = content;
        this.equipNo = equipNo;
        this.deviceNo = deviceNo;
        this.newDeviceNo = newDeviceNo;
        this.time = time;
        this.remark = remark;
    }

    @Generated(hash = 1916721392)
    public OperateRecord(Long id, String account, int type, boolean isSuccess, String content, String equipType,
            String equipModel, String deviceType, String deviceModel, String equipNo, String deviceNo, String newDeviceNo,
            long time, String remark, boolean isDeleted, long deleteTime) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.isSuccess = isSuccess;
        this.content = content;
        this.equipType = equipType;
        this.equipModel = equipModel;
        this.deviceType = deviceType;
        this.deviceModel = deviceModel;
        this.equipNo = equipNo;
        this.deviceNo = deviceNo;
        this.newDeviceNo = newDeviceNo;
        this.time = time;
        this.remark = remark;
        this.isDeleted = isDeleted;
        this.deleteTime = deleteTime;
    }

    @Generated(hash = 123602266)
    public OperateRecord() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getIsSuccess() {
        return this.isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEquipType() {
        return this.equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getEquipModel() {
        return this.equipModel;
    }

    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceNo() {
        return this.deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getEquipNo() {
        return this.equipNo;
    }

    public void setEquipNo(String equipNo) {
        this.equipNo = equipNo;
    }

    public String getNewDeviceNo() {
        return this.newDeviceNo;
    }

    public void setNewDeviceNo(String newDeviceNo) {
        this.newDeviceNo = newDeviceNo;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getDeleteTime() {
        return this.deleteTime;
    }

    public void setDeleteTime(long deleteTime) {
        this.deleteTime = deleteTime;
    }

}
