package info.archinnov.demo.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlTransient;

public class ValueWithTimestamp {

    @XmlTransient
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String value;
    private String currentDate;
    private String writeDate;

    public ValueWithTimestamp() {
    }

    public ValueWithTimestamp(String value, String writeDate) {
        this.value = value;
        this.writeDate = writeDate;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        this.currentDate=format.format(new Date());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
