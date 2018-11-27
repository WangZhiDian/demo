package cn.model.maven.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wangdian05 on 2018/6/15.
 */
@Data
public class PayWayDomain implements Serializable{

    private String paywayId;

    private String paywayName;

    private String displayName;

}
