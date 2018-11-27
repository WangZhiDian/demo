package cn.model.maven.domain;

/**
 * lombok 工具包：帮助去掉代码中需要手动创建的getter和setter ，在编译的时候将get和set的字节码加到便以文件中
 */

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wangdian05 on 2018/6/7.
 */
@Data
public class UserBean implements Serializable {

    private String name;

    private String addr;

    private String desc;

}
