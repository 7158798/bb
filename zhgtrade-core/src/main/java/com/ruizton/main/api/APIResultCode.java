package com.ruizton.main.api;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/25
 */
public enum APIResultCode {
    Code_200("200", "成功"),

    /*-------------接口交互错误---------------*/
    Code_101("101", "必填参数不能为空"),
    Code_102("102", "API key不存在"),
    Code_103("103", "API已禁止使用"),
    Code_104("104", "权限已关闭"),
    Code_105("105", "权限不足"),
    Code_106("106", "签名不匹配"),

    /*---------------业务错误----------------*/
    Code_201("201", "虚拟币不存在"),
    Code_202("202", "虚拟币不能充值和提款"),
    Code_203("203", "虚拟币还没分配到钱包地址"),
    Code_204("204", "取消挂单失败（部分成交或全部已成交）"),
    Code_205("205", "交易数量不能小于0.0001"),
    Code_206("206", "交易价格不能小于0.0001"),
    Code_207("207", "虚拟币未开放交易"),
    Code_208("208", "人民币余额不足"),
    Code_209("209", "交易密码错误"),
    Code_210("210", "交易价格不在限价区间内"),
    Code_211("211", "虚拟币余额不足"),
    Code_212("212", "交易总金额不能大于50000"),

    /*---------------非合理错误---------------*/
    Code_401("401", "非法参数"),
    Code_402("402", "系统异常");

    private String code;
    private String message;

    APIResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static APIResultCode get(String code){
        for(APIResultCode obj : values()){
            if(code.equals(obj.getCode())){
                return obj;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    public static void main(String[] args) {
        System.out.println(APIResultCode.Code_101);
    }
}
