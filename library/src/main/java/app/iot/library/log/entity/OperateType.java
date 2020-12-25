package app.iot.library.log.entity;

/**
 * Created by danbo on 2020/12/21.
 */
public enum OperateType {
    BIND(0,"设备绑定"), UNBIND(1,"设备解绑"), SWITCH(2,"部件更换"), CHECK(3,"设备检查");

    public int value;
    public String title;
    // 构造方法
    private OperateType(int value,String title) {
        this.value = value;
        this.title = title;
    }

}
