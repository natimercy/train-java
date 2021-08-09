package hw.topevery.autoconfigure;

/**
 * todo
 *
 * @author hq
 * @date 2021-05-21
 */
public class TencentSmsSenderImpl implements SmsSender {

    private SmsProperties.SmsMessage smsMessage;

    public TencentSmsSenderImpl(SmsProperties.SmsMessage smsMessage) {
        this.smsMessage = smsMessage;
    }

    @Override
    public boolean send(String message) {
        System.out.println(smsMessage.toString() + "开始发送短信==》短信内容：" + message);
        return true;
    }
}
