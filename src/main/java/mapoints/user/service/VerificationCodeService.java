package mapoints.user.service;

import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.service.BaseService;
import mapoints.lib.service.Message;
import mapoints.notification.sms.SmsService;
import mapoints.user.form.PhoneNumberForm;
import mapoints.user.model.VerificationCode;
import mapoints.user.repository.VerificationCodeRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class VerificationCodeService extends BaseService<VerificationCode, VerificationCodeRepository> {
    private final Integer MAX_CODE_COUNT = 3;
    private final Integer VERIFICATION_CODE_LEN = 4;
    private final Integer CODE_VALIDITY_IN_MINUTES = 5;
    private final Integer NUMBER_OF_MINUTES_BEFORE_RETRY = 5;

    private BCryptPasswordEncoder passwordEncoder;
    private SmsService smsService;

    public void generateVerificationCode(PhoneNumberForm form){

        Optional<VerificationCode> optional = repository.findByPhoneNumberAndUserType(
                form.getPhoneNumber(),
                form.getUserType()
        );

        VerificationCode verificationCode = optional.orElseGet(VerificationCode::new);

        Date timeBeforeResend = DateUtils.addMinutes(
                verificationCode.getTimeLastSent(),
                NUMBER_OF_MINUTES_BEFORE_RETRY
        );

        if(verificationCode.getCodeCount() >= MAX_CODE_COUNT
                && timeBeforeResend.after(new Date()) ){
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "verification.code.limit-exceeded"
            );
        }

        verificationCode.setPhoneNumber(form.getPhoneNumber());
        verificationCode.setUserType(form.getUserType());
        verificationCode.setCodeCount(verificationCode.getCodeCount() +1);

        Date expiryTime = DateUtils.addMinutes(new Date(), CODE_VALIDITY_IN_MINUTES);
        verificationCode.setExpiryTime(expiryTime);

        String code = RandomStringUtils.randomNumeric(VERIFICATION_CODE_LEN);
        verificationCode.setCode(passwordEncoder.encode(code));
        verificationCode.setTimeLastSent(new Date());
        repository.save(verificationCode);

        sendCode(form, code);
    }

    private void sendCode(PhoneNumberForm form, String code){
        String msg = String.format(Message.get("verification.code.sms"), code);
        smsService.send(form.getPhoneNumber(), msg);
    }

    public void verifyVerificationCode(PhoneNumberForm form, String code){
        Optional<VerificationCode> codeOpt = repository.findByPhoneNumberAndUserType(
                form.getPhoneNumber(),
                form.getUserType()
        );
        if(codeOpt.isEmpty()){
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "verification.code.not-found"
            );
        }

        VerificationCode verificationCode = codeOpt.get();

        if(verificationCode.getExpiryTime().before(new Date())){
            repository.delete(verificationCode);
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "verification.code.expired"
            );
        }

        boolean codeMatches = passwordEncoder.matches(
                code,
                verificationCode.getCode()
        );


        if(codeMatches){
            repository.delete(verificationCode);
        }else{
            throw new CommonRuntimeException(
                ExceptionType.NOT_FOUND,
                "verification.code.invalid"
            );
        }
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }
}
