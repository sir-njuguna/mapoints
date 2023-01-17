package mapoints.payment.model;

import java.util.Date;

public class TransactionTrace {
    private Date time;
    private String traceName;

    public enum TRACE_NAME{
        BEFORE_ENDPOINT_CALL,
        ON_ENDPOINT_CALL_SUCCESS,
        ON_ENDPOINT_CALL_FAILURE,
        ON_RESPONSE_SERIALIZATION_SUCCESS,
        ON_RESPONSE_SERIALIZATION_FAILURE
    }

    public TransactionTrace() { }

    public TransactionTrace(TRACE_NAME name) {
        setTime(new Date());
        setTraceName(name.toString());
    }

    public TransactionTrace(Date time, TRACE_NAME name) {
        setTime(time);
        setTraceName(name.toString());
    }

    public TransactionTrace(Date time, String traceName) {
        setTime(time);
        setTraceName(traceName);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }
}
