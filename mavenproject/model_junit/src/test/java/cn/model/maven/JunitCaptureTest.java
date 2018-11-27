package cn.model.maven;

import mockit.Capturing;
import mockit.Expectations;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

interface  IPrivilege{ public boolean isAllow(int userid); }

/**
 * junit mock对象的时候，如接口或者父类，有时候需要影响其之类的行为
 */
public class JunitCaptureTest {

    int testUserid = 123;

    //写一个测试接口实现类继承接口
    IPrivilege privilegeManager1 = new IPrivilege() {
        @Override
        public boolean isAllow(int userid) {
            if(userid == testUserid)
                return true;
            else
                return false;
        }
    };


    @Test
    public void testCapturing(@Capturing IPrivilege privilegeManager)
    {
        //在该类中mock接口类的方法，jmockit会实现它并影响他的之类,该方法修改的是privilegeManager,但对象privilegeManager1的返回同样修改了
        //影响的是输入参数为方法中参数的条件测试是，固定返回的值
        new Expectations(){
            {
                privilegeManager.isAllow(32);
                result = true;
            }
        };

        assertTrue(privilegeManager1.isAllow(32));
    }

    @Test
    public void testWithoutCaptruing()
    {
        assertTrue(!privilegeManager1.isAllow(321));
    }


}
