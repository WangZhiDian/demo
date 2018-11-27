package cn.model.maven.service.impl;

import cn.model.maven.dao.TenantinfoPaywayDao;
import cn.model.maven.domain.TenantinfoPayway;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdian05 on 2018/6/19.
 */
@Slf4j
@Component
public class MybatisTestService
{
    @Autowired
    private TenantinfoPaywayDao tenantinfoPaywayDao;

    //1 查询，没有参数，获取单独字段
    public String getDesc()
    {
        String desc = tenantinfoPaywayDao.getDesc();
        log.debug("desc: {}", desc);
        return desc;
    }

    //2 查询，传递多个参数，不带注解，获取单独字段,xml中参数名称与方法中参数名称需要相同，#{}与dao中相同
    public String getDescWithParam(String tenantId, String paywayId)
    {
        log.debug("tenantId:{}, paywayId:{}", tenantId, paywayId);
        String ret = tenantinfoPaywayDao.getDescWithParam(tenantId, paywayId);
        return ret;
    }

    //3 查询，传递多个参数，带注解，获取单独字段，xml中参数parameterType不需要写，#{}中的参数名需要与dao中注解@Param("")内的名称相同
    public String getDescWithNoteParam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId)
    {
        log.debug("tenantId:{}, paywayId:{}", tenantId, paywayId);
        String ret = tenantinfoPaywayDao.getDescWithNoteParam(tenantId, paywayId);
        return ret;
    }

    //4 查询，传递参数Map，xml的#{}参数名称对应map中的key
    public String getDescWithMapParam(Map<String, String> Params)
    {
        log.debug("map:", Params.toString());
        String ret = tenantinfoPaywayDao.getDescWithMapParam(Params);
        return ret;
    }

    //5 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回String单独字段
    public String getDescWithObjectParam(TenantinfoPayway record)
    {
        log.debug("record:", record.toString());
        String ret = tenantinfoPaywayDao.getDescWithObjectParam(record);
        return ret;
    }

    //6 查询，传递参数注解，xml的#{}参数名称对应注解中的名称，返回为单个对象
    public TenantinfoPayway getObjWithNoteparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId)
    {
        log.debug("tenantId:{}, paywayId:{}", tenantId, paywayId);
        TenantinfoPayway tenantinfoPayway = tenantinfoPaywayDao.getObjWithNoteparam(tenantId, paywayId);
        return  tenantinfoPayway;
    }

    //7 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回为单个对象
    public TenantinfoPayway getObjWithObjparam(TenantinfoPayway record)
    {
        log.debug("record:", record.toString());
        TenantinfoPayway tenantinfoPayway = tenantinfoPaywayDao.getObjWithObjparam(record);
        return tenantinfoPayway;
    }

    //8 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回为多个的列表对象
    public List<TenantinfoPayway> getListObjWithObjparam(TenantinfoPayway record)
    {
        log.debug("record:", record);
        List<TenantinfoPayway> list = tenantinfoPaywayDao.getListObjWithObjparam(record);
        return list;
    }

    //9 查询，传递参数对象，xml的#{}参数名称对应注解中的名称，返回为多个的字符串对象
    public List<String> getListStringWithObjparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId)
    {
        log.debug("tenantId:{}, paywayId:{}", tenantId, paywayId);
        List<String> list = tenantinfoPaywayDao.getListStringWithObjparam(tenantId, paywayId);
        return list;
    }

    //10 查询，获取总数，参数为对象
    public Integer getCountWithObjparam(TenantinfoPayway record)
    {
        log.debug("record:", record.toString());
        Integer ret = tenantinfoPaywayDao.getCountWithObjparam(record);
        return ret;
    }

    //11 更新，传参数，根据具体参数更新，该参数形式可以用单个参数代替，不使用对象
    public Integer updateByPrimaryKey(TenantinfoPayway record)
    {
        log.debug("record:", record.toString());
        Integer ret = tenantinfoPaywayDao.updateByPrimaryKey(record);
        return ret;
    }

    //12 更新，传参数，根据具体参数更新
    public Integer updateByPrimaryKeySelective(TenantinfoPayway record)
    {
        log.debug("record:", record.toString());
        Integer ret = tenantinfoPaywayDao.updateByPrimaryKeySelective(record);
        return ret;
    }

    //13 新增，该方法适用于有主键自增的插入方式，标签在xml配置中
    public Integer insert(TenantinfoPayway record)
    {
        log.debug("record:", record.toString());
        Integer ret = tenantinfoPaywayDao.insert(record);
        return ret;
    }

    //14 新增，如果没有表字段限制，对象中有多少字段就插入多少字段
    public Integer insertSelective(TenantinfoPayway record)
    {
        log.debug("record:", record.toString());
        Integer ret = tenantinfoPaywayDao.insertSelective(record);
        return ret;
    }

    //15 删除，根据某个具体的key删除记录
    public Integer deleteByPrimaryKey(Object key)
    {
        log.debug("key:", key);
        Integer ret = tenantinfoPaywayDao.deleteByPrimaryKey(key);
        return ret;
    }

    //16 删除，根据对象中传的条件来删除
    public Integer delete(TenantinfoPayway record)
    {
        log.debug("record:", record.toString());
        Integer ret = tenantinfoPaywayDao.delete(record);
        return ret;
    }

    //17 批量查询，查询一个列表数据,使用foreach标签
    public List<String> getDescsWithParamList(List<String> list)
    {
        log.debug("list:", list.toString());
        List<String> listRet = tenantinfoPaywayDao.getDescsWithParamList(list);
        return listRet;
    }

    //18 联合查询，查询对象中包含其他对象，其他对象为一对一
    public TenantinfoPayway getUnionObjWithNoteparam(String tenantId, String paywayId)
    {
        log.debug("tenantId:{}, paywayId:{}", tenantId, paywayId);
        TenantinfoPayway tenantinfoPayway = tenantinfoPaywayDao.getUnionObjWithNoteparam(tenantId, paywayId);
        return tenantinfoPayway;
    }

    //19 联合查询，查询对象中包含其他对象，其他对象为一对多，列表对象包含在tenantinfoPayway对象中
    public TenantinfoPayway getUnionObjListWithNoteparam(String tenantId, String paywayId)
    {
        log.debug("tenantId:{}, paywayId:{}", tenantId, paywayId);
        TenantinfoPayway tenantinfoPayway = tenantinfoPaywayDao.getUnionObjListWithNoteparam(tenantId, paywayId);
        return tenantinfoPayway;
    }

    //20 使用where标签过滤，该标签可以使用trim标签代替
    public String getDescWithParamWhere(String tenantId, String paywayId)
    {
        log.debug("tenantId:{}, paywayId:{}", tenantId, paywayId);
        String ret = tenantinfoPaywayDao.getDescWithParamWhere(tenantId, paywayId);
        return ret;
    }


}
