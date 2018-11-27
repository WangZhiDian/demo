package cn.model.api;

import cn.model.maven.domain.TenantinfoPayway;
import cn.model.maven.service.impl.MybatisTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.*;

/**
 * Created by wangdian05 on 2018/6/19.
 */
@Slf4j
@Path("/rest")
public class MybatisTestRest {

    @Autowired
    MybatisTestService mybatisTestService;

//    //@1 查询，没有参数，获取单独字段
    @GET
    @Path("/getDesc")
    @Produces("application/json;charset=utf-8")
    public String getDesc(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getDesc
        String ret = mybatisTestService.getDesc();
        return ret;
    }
//    //2 查询，传递多个参数，不带注解，获取单独字段,xml中参数名称与方法中参数名称需要相同，#{}与dao中相同
//    String getDescWithParam(String tenantId, String paywayId);
    @GET
    @Path("/getDescWithParam")
    @Produces("application/json;charset=utf-8")
    public String getDescWithParam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getDescWithParam
        String tenantId = "TK0000", paywayId ="10000";
        String ret = mybatisTestService.getDescWithParam(tenantId, paywayId);
        return ret;
    }

//    //@3 查询，传递多个参数，带注解，获取单独字段，xml中参数parameterType不需要写，#{}中的参数名需要与dao中注解@Param("")内的名称相同
//    String getDescWithNoteParam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);
    @GET
    @Path("/getDescWithNoteParam")
    @Produces("application/json;charset=utf-8")
    public String getDescWithNoteParam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getDescWithNoteParam
        String tenantId = "TK0000", paywayId ="10000";
        String ret = mybatisTestService.getDescWithNoteParam(tenantId, paywayId);
        return ret;
    }

//    //@4 查询，传递参数Map，xml的#{}参数名称对应map中的key
//    String getDescWithMapParam(Map<String, String> Params);
    @GET
    @Path("/getDescWithMapParam")
    @Produces("application/json;charset=utf-8")
    public String getDescWithMapParam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getDescWithMapParam
        String tenantId = "TK0000", paywayId ="10000";
        Map map = new HashMap<String, String>();
        map.put("tenantId", tenantId); map.put("paywayId", paywayId);
        String ret = mybatisTestService.getDescWithMapParam(map);
        return ret;
    }

//    //@5 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回String单独字段
//    String getDescWithObjectParam(TenantinfoPayway record);
    @GET
    @Path("/getDescWithObjectParam")
    @Produces("application/json;charset=utf-8")
    public String getDescWithObjectParam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getDescWithObjectParam
        String tenantId = "TK0000", paywayId ="10000";
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setTenantId(tenantId); tenantinfoPayway.setPaywayId(paywayId);
        String ret = mybatisTestService.getDescWithObjectParam(tenantinfoPayway);
        return ret;
    }

//    //@6 查询，传递参数注解，xml的#{}参数名称对应注解中的名称，返回为单个对象
//    TenantinfoPayway getObjWithNoteparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);
    @GET
    @Path("/getObjWithNoteparam")
    @Produces("application/json;charset=utf-8")
    public String getObjWithNoteparam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getObjWithNoteparam
        String tenantId = "TK0000", paywayId ="10000";
        TenantinfoPayway tenantinfoPayway = mybatisTestService.getObjWithNoteparam(tenantId, paywayId);
        return tenantinfoPayway.getPaywayDescription();
    }

//    //@7 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回为单个对象
//    TenantinfoPayway getObjWithObjparam(TenantinfoPayway record);
    @GET
    @Path("/getObjWithObjparam")
    @Produces("application/json;charset=utf-8")
    public String getObjWithObjparam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getObjWithObjparam
        String tenantId = "TK0000", paywayId ="10000";
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setTenantId(tenantId); tenantinfoPayway.setPaywayId(paywayId);
        tenantinfoPayway = mybatisTestService.getObjWithObjparam(tenantinfoPayway);
        return tenantinfoPayway.getPaywayDescription();
    }

//    //@8 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回为多个的列表对象
//    List<TenantinfoPayway> getListObjWithObjparam(TenantinfoPayway record);
    @GET
    @Path("/getListObjWithObjparam")
    @Produces("application/json;charset=utf-8")
    public String getListObjWithObjparam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getListObjWithObjparam
        String tenantId = "TK0000", paywayId ="10000";
        List<TenantinfoPayway> list = new ArrayList<TenantinfoPayway>();
        list = mybatisTestService.getListObjWithObjparam(null);
        return list.size() + "";
    }

//    //@9 查询，传递参数对象，xml的#{}参数名称对应注解中的名称，返回为多个的字符串对象
//    List<String> getListStringWithObjparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);
    @GET
    @Path("/getListStringWithObjparam")
    @Produces("application/json;charset=utf-8")
    public String getListStringWithObjparam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getListStringWithObjparam
        String tenantId = "TK0000", paywayId ="10000";
        List<String> list = new ArrayList<String>();
        list = mybatisTestService.getListStringWithObjparam(tenantId, paywayId);
        return list.size() + "";
    }

//    //@10 查询，获取总数，参数为对象
//    Integer getCountWithObjparam(TenantinfoPayway record);
    @GET
    @Path("/getCountWithObjparam")
    @Produces("application/json;charset=utf-8")
    public String getCountWithObjparam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getCountWithObjparam
        String tenantId = "TK0000", paywayId ="10000";
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setTenantId(tenantId); tenantinfoPayway.setPaywayId(paywayId);
        Integer ret = mybatisTestService.getCountWithObjparam(tenantinfoPayway);
        return ret + "";
    }

//    //@11 更新，传参数，根据具体参数更新，该参数形式可以用单个参数代替，不使用对象
//    Integer updateByPrimaryKey(TenantinfoPayway record);
    @GET
    @Path("/updateByPrimaryKey")
    @Produces("application/json;charset=utf-8")
    public String updateByPrimaryKey(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/updateByPrimaryKey
        String tenantId = "TK0000", paywayId ="10000";
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setTenantId(tenantId); tenantinfoPayway.setPaywayId(paywayId);
        tenantinfoPayway.setPaywayDescription("支付宝移动端测试验证updateByPrimaryKey");
        tenantinfoPayway.setModifier("updateByPrimaryKey");
        tenantinfoPayway.setRelationshipId("92");
        Integer ret = mybatisTestService.updateByPrimaryKey(tenantinfoPayway);
        return ret + "";
    }

//    //@12 更新，传参数，根据具体参数更新
//    Integer updateByPrimaryKeySelective(TenantinfoPayway record);
    @GET
    @Path("/updateByPrimaryKeySelective")
    @Produces("application/json;charset=utf-8")
    public String updateByPrimaryKeySelective(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/updateByPrimaryKeySelective
        String tenantId = "TK0000", paywayId ="10000";
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setTenantId(tenantId); tenantinfoPayway.setPaywayId(paywayId);
        tenantinfoPayway.setPaywayDescription("支付宝移动端测试验证updateByPrimaryKeySelective");
        Integer ret = mybatisTestService.updateByPrimaryKeySelective(tenantinfoPayway);
        tenantinfoPayway.setModifier("updateByPrimaryKeySelective");
        return ret + "";
    }

//    //13 新增，该方法适用于有主键自增的插入方式，标签在xml配置中
//    Integer insert(TenantinfoPayway record);
    @GET
    @Path("/insert")
    @Produces("application/json;charset=utf-8")
    public String insert(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/insert
        String tenantId = "TK0000", paywayId ="10000";
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setTenantId(tenantId); tenantinfoPayway.setPaywayId(paywayId);
        tenantinfoPayway.setRelationshipId("94"); tenantinfoPayway.setPaywayDescription("TestMybatis insert");
        tenantinfoPayway.setCreator("insert"); tenantinfoPayway.setCreatedTime(new Date());
        Integer ret = mybatisTestService.insert(tenantinfoPayway);
        return ret + "";
    }

//    //@14 新增，如果没有表字段限制，对象中有多少字段就插入多少字段
//    Integer insertSelective(TenantinfoPayway record);
    @GET
    @Path("/insertSelective")
    @Produces("application/json;charset=utf-8")
    public String insertSelective(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/insertSelective
        String tenantId = "TK0000", paywayId ="10000";
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setTenantId(tenantId); tenantinfoPayway.setPaywayId(paywayId);
        tenantinfoPayway.setRelationshipId("92"); tenantinfoPayway.setPaywayDescription("TestMybatis insertSelective");
        tenantinfoPayway.setCreator("insertSelective"); tenantinfoPayway.setCreatedTime(new Date());
        Integer ret = mybatisTestService.insertSelective(tenantinfoPayway);
        return ret + "";
    }

//    //@15 删除，根据某个具体的key删除记录
//    Integer deleteByPrimaryKey(Object key);
    @GET
    @Path("/deleteByPrimaryKey")
    @Produces("application/json;charset=utf-8")
    public String deleteByPrimaryKey(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/deleteByPrimaryKey
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setRelationshipId("92");
        Integer ret = mybatisTestService.deleteByPrimaryKey(tenantinfoPayway);
        return ret + "";
    }

//    //@16 删除，根据对象中传的条件来删除
//    Integer delete(TenantinfoPayway record);
    @GET
    @Path("/delete")
    @Produces("application/json;charset=utf-8")
    public String delete(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/delete
        String tenantId = "TK0000", paywayId ="10001";
        TenantinfoPayway tenantinfoPayway = new TenantinfoPayway();
        tenantinfoPayway.setTenantId(tenantId); tenantinfoPayway.setPaywayId(paywayId);
        Integer ret = mybatisTestService.delete(tenantinfoPayway);
        return ret + "";
    }

//    //@17 批量查询，查询一个列表数据,使用foreach标签
//    List<String> getDescsWithParamList(List<String> list);
    @GET
    @Path("/getDescsWithParamList")
    @Produces("application/json;charset=utf-8")
    public String getDescsWithParamList(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getDescsWithParamList
        List<String> list = new ArrayList<String>();
        list.add("94"); list.add("95"); list.add("96");
        list = mybatisTestService.getDescsWithParamList(list);
        log.debug("list:" + list.get(0).toString());
        return list.toString();
    }

//    //@18 联合查询，查询对象中包含其他对象，其他对象为一对一
//    TenantinfoPayway getUnionObjWithNoteparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);
    @GET
    @Path("/getUnionObjWithNoteparam")
    @Produces("application/json;charset=utf-8")
    public String getUnionObjWithNoteparam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getUnionObjWithNoteparam
        String tenantId = "TK0001", paywayId ="106";
        TenantinfoPayway tenantinfoPayway = mybatisTestService.getUnionObjWithNoteparam(tenantId, paywayId);
        log.debug("tenantinfoPayway:" + tenantinfoPayway.toString());
        return tenantinfoPayway.toString();
    }

//    //@19 联合查询，查询对象中包含其他对象，其他对象为一对多，列表
//    TenantinfoPayway getUnionObjListWithNoteparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);
    @GET
    @Path("/getUnionObjListWithNoteparam")
    @Produces("application/json;charset=utf-8")
    public String getUnionObjListWithNoteparam(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getUnionObjListWithNoteparam
        String tenantId = "TK0001", paywayId ="106";
        TenantinfoPayway tenantinfoPayway = mybatisTestService.getUnionObjListWithNoteparam(tenantId, paywayId);
        log.debug("tenantinfoPayway:" + tenantinfoPayway.toString());
        return tenantinfoPayway.toString();
    }

//    //@20 使用where标签过滤，该标签可以使用trim标签代替
//    String getDescWithParamWhere(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);
    @GET
    @Path("/getDescWithParamWhere")
    @Produces("application/json;charset=utf-8")
    public String getDescWithParamWhere(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getDescWithParamWhere
        String tenantId = "TK0000", paywayId ="10000";
        String ret = mybatisTestService.getDescWithParamWhere(tenantId, paywayId);
        log.debug("ret:" + ret);
        return ret;
    }

}
