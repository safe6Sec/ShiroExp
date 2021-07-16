package cn.safe6.core;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author yhy
 * @date 2021/3/26 16:53
 * @github https://github.com/yhy0
 * 映射批量检查界面中的表格，信息基本类
 */

public class VulInfo {
    private final SimpleStringProperty id = new SimpleStringProperty();
    private final SimpleStringProperty target = new SimpleStringProperty();
    private final SimpleStringProperty isVul = new SimpleStringProperty();
    private final SimpleStringProperty length = new SimpleStringProperty();

    public VulInfo(String id, String target,String length, String isVul) {
        setId(id);
        setTarget(target);
        setLength(length);
        setIsVul(isVul);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getTarget() {
        return target.get();
    }

    public void setTarget(String target) {
        this.target.set(target);
    }

    public String getIsVul() {
        return isVul.get();
    }

    public void setIsVul(String isVul) {
        this.isVul.set(isVul);
    }

    public String getLength() {
        return this.length.get();
    }

    public void setLength(String length) {
        this.length.set(length);
    }

}
