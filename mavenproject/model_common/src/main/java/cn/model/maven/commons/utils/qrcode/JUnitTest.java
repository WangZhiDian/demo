package cn.model.maven.commons.utils.qrcode;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wangdian05 on 2018/6/29.
 */
public class JUnitTest {

    @Autowired
    private QrCodeService qrCodeService;

    public void test1()
    {
        String deeplink ="the Test content like a Url";
        byte[] qrcode = qrCodeService.writeToByte(deeplink);
        String base64str = Base64.encode(qrcode);
        deeplink = "data:image/jpg;base64," + base64str;
        //1 该deeplink的base64编码返回前端，前端页面的src标签填充该编码，直接展示图片
        //2 base64编码用于网络传输

    }


}
