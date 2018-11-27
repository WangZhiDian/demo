package cn.model.maven.dao;

import cn.model.maven.domain.TenantinfoPayway;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao层接口:TenantinfoPaywayDao,获取mybatis各种数据类型和数据的参数写法示例。
 * @date 2017-10-18
 */
public interface TenantinfoPaywayDao {

    //1 查询，没有参数，获取单独字段
    String getDesc();

    //2 查询，传递多个参数，不带注解，获取单独字段,xml中参数名称与方法中参数名称需要相同，#{}与dao中相同
    String getDescWithParam(String tenantId, String paywayId);

    //3 查询，传递多个参数，带注解，获取单独字段，xml中参数parameterType不需要写，#{}中的参数名需要与dao中注解@Param("")内的名称相同
    String getDescWithNoteParam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);

    //4 查询，传递参数Map，xml的#{}参数名称对应map中的key
    String getDescWithMapParam(Map<String, String> Params);

    //5 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回String单独字段
    String getDescWithObjectParam(TenantinfoPayway record);

    //6 查询，传递参数注解，xml的#{}参数名称对应注解中的名称，返回为单个对象，注意：返回的对象object，如果xml中的sql的返回类型为resultType，且为对象类型，那么查询出来的
    //对象中，只有bean类里面对象名称和xml的sql中select的名称相同的数据，才能被映射到返回的对象object中
    TenantinfoPayway getObjWithNoteparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);

    //7 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回为单个对象，注意：返回的数据为resultMap是，映射的select的数据可以到对象的属性当中，与6有差别
    TenantinfoPayway getObjWithObjparam(TenantinfoPayway record);

    //8 查询，传递参数对象，xml的#{}参数名称对应对象中的名称，返回为多个的列表对象
    List<TenantinfoPayway> getListObjWithObjparam(TenantinfoPayway record);

    //9 查询，传递参数值，xml的#{}参数名称对应注解中的名称，返回为多个的字符串对象
    List<String> getListStringWithObjparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);

    //10 查询，获取总数，参数为对象
    Integer getCountWithObjparam(TenantinfoPayway record);

    //11 更新，传参数，根据具体参数更新，该参数形式可以用单个参数代替，不使用对象
    Integer updateByPrimaryKey(TenantinfoPayway record);

    //12 更新，传参数，根据具体参数更新
    Integer updateByPrimaryKeySelective(TenantinfoPayway record);

    //13 新增，该方法适用于有主键自增的插入方式，标签在xml配置中
    Integer insert(TenantinfoPayway record);

    //14 新增，如果没有表字段限制，对象中有多少字段就插入多少字段
    Integer insertSelective(TenantinfoPayway record);

    //15 删除，根据某个具体的key删除记录
    Integer deleteByPrimaryKey(Object key);

    //16 删除，根据对象中传的条件来删除
    Integer delete(TenantinfoPayway record);

    //17 批量查询，查询一个列表数据,使用foreach标签
    List<String> getDescsWithParamList(List<String> list);

    //18 联合查询，查询对象中包含其他对象，其他对象为一对一
    TenantinfoPayway getUnionObjWithNoteparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);

    //19 联合查询，查询对象中包含其他对象，其他对象为一对多，列表
    TenantinfoPayway getUnionObjListWithNoteparam(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);

    //20 使用where标签过滤，该标签可以使用trim标签代替
    String getDescWithParamWhere(@Param("tenantId") String tenantId, @Param("paywayId")String paywayId);


/*
	@Select({"select payway_content from d_tenantinfo_payway where tenant_id=#{tenant_id,jdbcType=VARCHAR} and payway_id=#{payway_id,jdbcType=DECIMAL} "})
    @ResultMap("tenantinfoPaywayResultMap")
    public TenantinfoPayway getPayWayConfig(@Param("tenant_id") String tenant_id, @Param("payway_id") String payway_id);
*/

}