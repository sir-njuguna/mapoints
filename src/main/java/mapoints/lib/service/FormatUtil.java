package mapoints.lib.service;


import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class FormatUtil {
    public final static int BIG_DECIMAL_SCALE = 20;
    public final static int BIG_DECIMAL_PRECISION = 30;
    public final static MathContext ROUND_DOWN_MATH_CONTEXT = new MathContext(
            BIG_DECIMAL_SCALE,
            RoundingMode.DOWN
    );

    public final static MathContext ROUND_UP_MATH_CONTEXT = new MathContext(
            BIG_DECIMAL_SCALE,
            RoundingMode.UP
    );

    public final static String AFRICA_NAIROBI_ZONE = "Africa/Nairobi";

    /**
     * format currency based on symbol
     */
    public static String formatCurrency(BigDecimal amount, String currencyCode) {
        final NumberFormat NUM_FORMAT_WITH_COMMA_SEP_2DP = new DecimalFormat("#,###.00");
        return currencyCode + " "+ NUM_FORMAT_WITH_COMMA_SEP_2DP.format(amount);
    }

    public static String getHumanReadableDateTime(Date date){
        return getHumanReadableDateTime(date,TimeZone.getTimeZone(AFRICA_NAIROBI_ZONE));
    }

    public static String getHumanReadableDateTime(Date date, TimeZone timeZone){
        final SimpleDateFormat HUMAN_READABLE_DATE_TIME_FORMAT = new SimpleDateFormat("E dd MMM yyyy, hh:mm a");
        HUMAN_READABLE_DATE_TIME_FORMAT.setTimeZone(timeZone);
        return HUMAN_READABLE_DATE_TIME_FORMAT.format(date);
    }

    public static Date getISODate(String dateStr) throws ParseException {
        return  new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
    }

    public static String getISODateStr(Date date){
        return getISODateStr(date,TimeZone.getTimeZone(AFRICA_NAIROBI_ZONE));
    }

    public static String getISODateStr(Date date, TimeZone timeZone){
        final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        ISO_DATE_FORMAT.setTimeZone(timeZone);
        return ISO_DATE_FORMAT.format(date);
    }

    public static String getISODateTimeStr(Date date, TimeZone timeZone){
        final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ISO_DATE_FORMAT.setTimeZone(timeZone);
        return ISO_DATE_FORMAT.format(date);
    }

    public static String toPercentage(Object number){
        final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("#.00%");
        return PERCENTAGE_FORMAT.format(number);
    }

    public static BigDecimal getRoundedUpValue(BigDecimal value) {
        return value.setScale(0, BigDecimal.ROUND_UP);
    }

    public static BigDecimal getRoundedDownValue(BigDecimal value) {
        return value.setScale(0, BigDecimal.ROUND_DOWN);
    }


    @Nullable
    public static String internationalizePhoneNumber(String msisdn, String region){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(msisdn, region);
            String formattedPhoneNumber = phoneUtil.format(phoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            return formattedPhoneNumber.replaceAll("\\s+", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
