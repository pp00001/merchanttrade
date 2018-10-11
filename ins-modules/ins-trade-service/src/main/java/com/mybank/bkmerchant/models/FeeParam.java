package com.mybank.bkmerchant.models;

import com.mybank.bkmerchant.constant.FeeTypeEnum;
import com.mybank.bkmerchant.constant.PayChannelEnum;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingzhu.zr on 2017/8/15.
 */
public class FeeParam {
  private PayChannelEnum channelType;
  private FeeTypeEnum feeType;
  private String feeValue;

  public FeeParam(PayChannelEnum channelType, FeeTypeEnum feeType, String feeValue) {
    this.channelType = channelType;
    this.feeType = feeType;
    this.feeValue = feeValue;
  }

  public FeeParam(){}
  
  public PayChannelEnum getChannelType() {
    return channelType;
  }

  public void setChannelType(PayChannelEnum channelType) {
    this.channelType = channelType;
  }

  public FeeTypeEnum getFeeType() {
    return feeType;
  }

  public void setFeeType(FeeTypeEnum feeType) {
    this.feeType = feeType;
  }

  public String getFeeValue() {
    return feeValue;
  }

  public void setFeeValue(String feeValue) {
    this.feeValue = feeValue;
  }


  public static String genJsonBase64(List<FeeParam> list) throws JSONException {
    List<JSONObject> arr = new ArrayList<JSONObject>();
    for (FeeParam param: list) {
      JSONObject obj = new JSONObject();
      obj.put("ChannelType", param.channelType.getChnCode());
      obj.put("FeeType", param.feeType.getFeeCode());
      obj.put("FeeValue", param.feeValue);

      arr.add(obj);

      System.out.println(obj.toString());
    }

    return new BASE64Encoder().encode(new JSONArray(arr).toString().getBytes());
  }
}
