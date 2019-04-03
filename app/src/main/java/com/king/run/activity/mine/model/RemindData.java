package com.king.run.activity.mine.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * 作者：shuizi_wade on 2018/1/5 10:19
 * 邮箱：674618016@qq.com
 */
@Table("remind")
public class RemindData implements Serializable {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    @Column("repet")
    private String repet;
    @Column("repetStr")
    private String repetStr;
    @Column("hour")
    private int hour;
    @Column("min")
    private int min;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepet() {
        return repet;
    }

    public void setRepet(String repet) {
        this.repet = repet;
    }

    public String getRepetStr() {
        return repetStr;
    }

    public void setRepetStr(String repetStr) {
        this.repetStr = repetStr;
    }
}
