package mapoints.lib.form;

import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class BaseDatedFetchForm extends BaseFetchForm{
    private Date startDate;
    private Date endDate;
    private TimeZone timeZone;

    public Date getStartDate() {
        return startDate == null ? new Date(0) : startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate == null ? new Date() : endDate;
    }

    public void setEndDate(Date endDate) {
        if(endDate == null){
            endDate = new Date();
        }

        this.endDate = DateUtils.addMilliseconds(
            DateUtils.ceiling(endDate, Calendar.DATE), -1
        );
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
