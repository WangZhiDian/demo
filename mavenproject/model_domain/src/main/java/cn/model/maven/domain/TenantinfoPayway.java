package cn.model.maven.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * entity:TenantinfoPayway
 * @date 2017-10-18
 */
@Data
public class TenantinfoPayway implements Serializable {
	
        /** 租户支付方式表，主键（UUID） */ 
	    private String relationshipId;
        /** 租户编号 */ 
	    private String tenantId;
        /** 支付方式编号 */ 
	    private String paywayId;
        /** 调用支付需要的报文，是根据支付方式表. payway_template生成的，在真正调用是填写金额、支付流水等信息 (用模版无需存储) */ 
	    private String paywayContent;
        /** 支付方式描述 */ 
	    private String paywayDescription;
        /** 租户支付方式描述 */ 
	    private String remark;
        /** 创建者 */ 
	    private String creator;
        /** 创建时间 */ 
	    private Date createdTime;
        /** 修改者 */ 
	    private String modifier;
        /** 修改时间 */ 
	    private Date modifiedTime;

	    private PayWayDomain payWayDomain;

        private List<PayWayDomain> payWayDomainList;

}
