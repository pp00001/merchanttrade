package com.mybank.bkmerchant.models;

import lombok.Data;
import sun.misc.BASE64Encoder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jingzhu.zr on 2017/8/15.
 */
@Data
public class BankCardParam{
  String bankCardNo;
  String bankCertName;
  String accountType;
  String contactLine;
  String branchName;
  String branchProvince;
  String branchCity;
  String certType;
  String certNo;
  String cardHolderAddress;

  public BankCardParam() {
  }

  public BankCardParam(String bankCardNo, String bankCertName, String accountType, String contactLine, String branchName, String branchProvince, String branchCity, String certType, String certNo, String cardHolderAddress) {
    this.bankCardNo = bankCardNo;
    this.bankCertName = bankCertName;
    this.accountType = accountType;
    this.contactLine = contactLine;
    this.branchName = branchName;
    this.branchProvince = branchProvince;
    this.branchCity = branchCity;
    this.certType = certType;
    this.certNo = certNo;
    this.cardHolderAddress = cardHolderAddress;
  }

  public String genJsonBase64() throws JSONException {
    JSONObject obj = new JSONObject();

    obj.put("BankCardNo", bankCardNo);
    obj.put("BankCertName", bankCertName);
    obj.put("AccountType", accountType);
    obj.put("ContactLine", contactLine);
    obj.put("BranchName", branchName);
    obj.put("BranchProvince", branchProvince);
    obj.put("BranchCity", branchCity);
    obj.put("CertType", certType);
    obj.put("CertNo", certNo);
    obj.put("CardHolderAddress", cardHolderAddress);

    System.out.println("BankCardParam：" + obj.toString());
    
    return new BASE64Encoder().encode(obj.toString().getBytes());
  }
}

