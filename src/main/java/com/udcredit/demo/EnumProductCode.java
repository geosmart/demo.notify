package com.udcredit.demo;


/**
 * 产品服务编号-云慧眼一期-V4.2
 *
 * @author geosmart
 * @date 2016-12-27
 */
public enum EnumProductCode {
    OCR_FRONT("G1001", "身份证OCR识别(正面)"),
    OCR_BACK("G1002", "身份证OCR识别(反面)"),
    VERIFY_SIMPLE("G1003", "实名验证"),
    VERIFY_RETURN_PHOTO("G1004", "实名验证(返人像)"),
    LIVING_DETECT("G1005", "活体检测"),
    FACE_COMPARE("G1006", "人脸比对"),
    VIDEO_AUTH("G1008", "视频存证");
    private String code;
    private String desc;

    EnumProductCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EnumProductCode newInstance(String code) {
        EnumProductCode result = null;
        for (EnumProductCode productNo : EnumProductCode.values()) {
            if (productNo.getCode().equals(code)) {
                result = productNo;
                break;
            }
        }
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
