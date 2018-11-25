/** 
 * @author 王文超 
 * @time 2016年4月22日 下午2:39:16  
 * 类说明 
*/ 
package com.demo.common.mybatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

/** 
 * @author 王文超
 * @Description
 * @time 2016年4月22日 下午2:39:16 
 */
public class BatchSqlSessionTemplate  extends SqlSessionTemplate
{
	/**
	 * @param sqlSessionFactory
	 * @param executorType
	 */
	public BatchSqlSessionTemplate(SqlSessionFactory sqlSessionFactory)
	{
		super(sqlSessionFactory, ExecutorType.BATCH);
	}

}
