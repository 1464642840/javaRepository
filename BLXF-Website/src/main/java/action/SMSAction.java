package action;

import cn.emay.TelphoneUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Random;

/**
 * @Auther: blxf
 * @Date: 2019-08-10 10:43
 * @Description:腾讯云SMS,注册时发送验证码
 */
@Controller("smsAction")
@Scope("prototype")
public class SMSAction extends BaseAction {

    // 需要发送短信的手机号码
    private String phoneNumber;


    public String sendRegSMS() {
        System.out.println("phoneNumber:" + phoneNumber);
        String code = getNumber(4);
        System.out.println("code:" + code);
        String params[] = {code};
        try {
            TelphoneUtil.getResult("【BLXF】您的验证码是" + code, phoneNumber.trim());
            //验证码放入session
            session.put("code", code);
        } catch (Exception e) {
            // 网络 IO 错误
            e.printStackTrace();
        }
        return NONE;
    }

    public String getNumber(int n) {
        String str = "1234567890qwertyuioplkjhgfdsazxcvbnm";
        Random r = new Random();
        String ss = "";
        for (int i = 0; i < n; i++) {
            char c = str.charAt(r.nextInt(str.length()));
            ss = ss + c;
        }
        return ss;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
