package com.king.run.activity.statistics.model;


/**
 * @author xwk
 */
public class ItemBar {
    private String value;
    private String unit;
    private String describe;

    public ItemBar(String value, String unit, String describe) {
        this.value = value;
        this.unit = unit;
        this.describe = describe;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
