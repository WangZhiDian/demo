package cn.model.maven.service.impl;

import cn.model.maven.dao.TenantinfoPaywayDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by wangdian05 on 2018/6/13.
 */
@Slf4j
@Controller
public class ConfigPropertyService {

    @Autowired
    private TenantinfoPaywayDao tenantinfoPaywayDao;


    public String getPropertyTenantinfo(String key)
    {
        String desc = tenantinfoPaywayDao.getDesc();
//        log.debug("desc: {}", desc);
        return desc;
    }



}
