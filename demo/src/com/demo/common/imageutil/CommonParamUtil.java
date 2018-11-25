package com.demo.common.imageutil;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.common.redisUtil.RedisManager;

//import com.taikang.redis.RedisManager;
//import com.taikang.utils.FileFunc;

public class CommonParamUtil {
	/**���ջ���*/
	public static final RedisManager rma = new RedisManager("claimService", "tka_claimService");
	/**�ϴ�ͼƬ*/
	public static final RedisManager  rm=new RedisManager("claimservice", "FileUpload");
	/**���˻���*/
	public static final RedisManager rmc = new RedisManager("claimService", "tkc_claimService");
	/**����+���ջ���*/
//	public static final RedisManager rmac = new RedisManager("claimService", "tkac_claimService");
	/**MD5����key*/
//	public static final String keys = FileFunc.getProValue("data/config/ipconfig.properties", "md5s");
	/**tkc*/
	public static final String tkc = "TKC";
	/**tka*/
	public static final String tka = "TKA";
	/**tkac*/
	public static final String tkac = "TKAC";
	/**֧����*/
	public static final String account_type_alipay = "alipay";
	/**���п�*/
	public static final String account_type_bank = "bank";
	/**�ϴ�Ӱ������*/
	public static final String[] pageBank = {"bank","bankpay"};
	public static final String[] pageCid = {"cid1","cid2"};
	public static final String[] pageData = {"diagnosis","beneficiary","invoice","sickrecord","claimapply","supply","other","death","pathology","benecid","tkaother"};
	public static final String[] pageSupply = {"supply"};
	/**ͼƬ��ʱ����·��*/
//	public static final String localpath = "claim/";
	public static final String localpath = "wechatbind/";
	
//	/**ͼƬnas����·��(����)*/
//	public static final String NASPath = "/claim/";
//	/**ͼƬnas����·��(���)*/
	public static final String NASPath = "/claim/"+CommonUtil.getDateStrOld(new Date(), "yyyyMMdd")+"/";//mobilenfsdta
	/**΢��*/
	public static final String channel_we = "WE";
	/**֧����*/
	public static final String channel_aplipay = "ALIPAY";
	/**app*/
	public static final String channel_app = "APP";
	/**�������*/
	public static final String[] tkc_params = {"ins_cidnumber","ins_cidtype","ins_mobile","ins_name","applyname","applymobile","cidnumber_decrypt","cidtype","coop_id","member_id","accidentResult","relationship","accidentDate","claim_operatetype","branch_no","company_no","claim_flag","sign"};
	/**�������*/
	public static final String[] tka_params = {"ins_cidnumber","ins_cidtype","ins_mobile","ins_name","applyname","applymobile","cidnumber_decrypt","cidtype","coop_id","member_id","tka_accidentResult","relationship","tka_accidentDate","claim_operatetype","tka_branch_no","tka_company_no","address","policy_no","lost_money","claim_desc","claim_flag","tka_name","tka_cidnumber","tka_cidtype","tka_mobile","sign","tka_accidentResult_desc","yzmCode"};
	/**����+�������*/
	public static final String[] tkac_params = {"ins_cidnumber","ins_cidtype","ins_mobile","ins_name","applyname","applymobile","cidnumber_decrypt","cidtype","coop_id","member_id","accidentResult","tka_accidentResult","relationship","accidentDate","tka_accidentDate","claim_operatetype","branch_no","tka_branch_no","company_no","tka_company_no","address","policy_no","lost_money","claim_desc","claim_flag","tka_name","tka_cidnumber","tka_cidtype","tka_mobile","sign","tka_accidentResult_desc","yzmCode"};
	/**���ղ�ѯ�ӿڵ�ַ*/
//	public static final String queryClaim =  FileFunc.getProValue("data/config/ipconfig.properties", "queryClaim");//"http://10.130.201.92/query/resources/shareClaimQuery/";
//	/**���ս��׽ӿڵ�ַ*/
//	public static final String shareClaim =  FileFunc.getProValue("data/config/ipconfig.properties", "shareClaim");//"http://10.130.201.235:8082/feCommon/resources/shareClaim/";
//	/**����Ӱ���ϴ��ӿڵ�ַ*/
//	public static final String uploadClaim =  FileFunc.getProValue("data/config/ipconfig.properties", "uploadClaim");//"http://10.130.201.64:7007/Image/websiteUpload";
//	/**���ս��׽ӿڵ�ַ*/
//	public static final String mdmPartyIdCas =  FileFunc.getProValue("data/config/ipconfig.properties", "mdmPartyIdCas");//"http://corelb3.core.online.taikang.com/mdmSupport/resources/mdm/";
//	/**des����*/
//	public static final String cas_des =  FileFunc.getProValue("data/config/ipconfig.properties", "cas_des");//"12345678";
//	/**token����*/
//	public static final String cas_token =  FileFunc.getProValue("data/config/ipconfig.properties", "cas_token");//"78A79D1FAD767A4DBA961263E296FA60E42A645BAAA9C243B7477979F37ED71B";
//	/**ͨ��log*/
	public static final Logger common_log=LoggerFactory.getLogger("wechatLog");
	/**���սӿ�Log*/
	public static final Logger tka_class_log=LoggerFactory.getLogger("TKAService");
	/**������ݿ�Log*/
	public static final Logger dao_log=LoggerFactory.getLogger("Dao");
	/**MDMServicelog*/
	public static final Logger mdm_log=LoggerFactory.getLogger("MDMService");
	
}
