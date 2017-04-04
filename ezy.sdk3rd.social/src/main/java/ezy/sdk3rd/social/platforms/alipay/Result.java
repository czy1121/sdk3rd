package ezy.sdk3rd.social.platforms.alipay;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class Result {


    private static final Map<String, String> sResultStatus;
    static {
        sResultStatus = new HashMap<String, String>();
        sResultStatus.put("9000", "支付成功");
        sResultStatus.put("8000", "支付结果确认中");
        sResultStatus.put("4000", "系统异常");
        sResultStatus.put("4001", "数据格式不正确");
        sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
        sResultStatus.put("4004", "该用户已解除绑定");
        sResultStatus.put("4005", "绑定失败或没有绑定");
        sResultStatus.put("4006", "订单支付失败");
        sResultStatus.put("4010", "重新绑定账户");
        sResultStatus.put("6000", "支付服务正在进行升级操作");
        sResultStatus.put("6001", "用户中途取消支付操作");
        sResultStatus.put("7001", "网页支付失败");
    }

    public String result;
    public String resultStatus;
    public String memo;
    public String resultText;

    public Result(Map<String, String> raw) {

        if (raw == null || raw.isEmpty()) {
            return;
        }
        for (String key : raw.keySet()) {
            if (TextUtils.equals(key, "resultStatus")) {
                resultStatus = raw.get(key);
            } else if (TextUtils.equals(key, "result")) {
                result = raw.get(key);
            } else if (TextUtils.equals(key, "memo")) {
                memo = raw.get(key);
            }
        }
        if (sResultStatus.containsKey(resultStatus)) {
            resultText = sResultStatus.get(resultStatus);
        } else {
            resultText = "支付失败";
        }
    }

    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
    public boolean isSuccess() {
        return "9000".equals(resultStatus);
    }
    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
    public boolean isPending() {
        return "8000".equals(resultStatus);
    }
    public boolean isCancelled() {
        return "6001".equals(resultStatus);
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo + "};result={" + result + "}";
    }

}
