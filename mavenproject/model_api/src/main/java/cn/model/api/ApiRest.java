package cn.model.api;

import cn.model.maven.commons.cache.service.CacheService;
import cn.model.maven.commons.utils.StringUtil;
import cn.model.maven.service.impl.ConfigPropertyService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * Created by wangdian05 on 2018/6/7.
 */
@Slf4j
@Path("/api")
public class ApiRest {

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Autowired
    private CacheService cache;

    private static final Logger logger = LoggerFactory.getLogger("wechat");

    @GET
    @Path("/getTenantInfo")
    @Produces("application/json;charset=utf-8")
    public String getTenantInfo(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/api/getTenantInfo
//        channel=wb&
//        applicationId=1015162035835015168&
//        applicationToken=b3c2f0d76d9b0c156129de1c20a86353&
//        proposal_id=30963699&
//        prepay_id=9CA9AF91BD23BC43ACB1E692A57F43C4
//        &pay_order_id=100000000064141
//        &prepay_token=3c2876f2f4033019751934f867a2d3eb
//        &token=5950b619f83388ecedfd71c73dd6dcc5
        String channel = request.getParameter("channel");
        String applicationId = request.getParameter("applicationId");
        String applicationToken = request.getParameter("applicationToken");
        String proposal_id = request.getParameter("proposal_id");
        String prepay_id = request.getParameter("prepay_id");
        String pay_order_id = request.getParameter("pay_order_id");
        String prepay_token = request.getParameter("prepay_token");
        String token = request.getParameter("token");

        log.info(".......getTenantInfo........stsrt");
        log.error("333333");
        logger.info("aaa:" + "dddddd");
        System.out.println("---------");
        String desc = cache.getString("desc");
//        if(StringUtil.isNotEmpty(desc))
//            return desc;
        desc = configPropertyService.getPropertyTenantinfo("");
        if(StringUtil.isNotEmpty(desc))
            cache.setString("desc", desc, 10*60);

        log.info(".......getTenantInfo........end");

        return desc;
    }

}
