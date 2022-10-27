package org.rone.web.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期对象，用来定义日期的格式
 * @author rone
 */
public class DateVO {
    private String des;
    @JsonSerialize(using = DateJsonSerializer.class)
    private Date date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date2;

    public DateVO(String des, Date date, Date date2) {
        this.des = des;
        this.date = date;
        this.date2 = date2;
    }

    @Override
    public String toString() {
        return "DateVO{" +
                "des='" + des + '\'' +
                ", date=" + date +
                ", date2=" + date2 +
                '}';
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    private static class DateJsonSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            jsonGenerator.writeString(simpleDateFormat.format(date));
        }
    }
}
