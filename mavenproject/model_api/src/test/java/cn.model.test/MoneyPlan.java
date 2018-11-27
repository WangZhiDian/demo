package cn.model.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by wangdian05 on 2018/7/6.
 * 功能：将不同数字，按照组合方式不同组装出某一个特定数字，找出所有方案。
 */
@Slf4j
public class MoneyPlan {

    @Test
    public void Test1()
    {
        int[] faceValues = {50, 20, 10, 5, 2, 1}; // 钱的面值
        int[] faceCount = {0, 0, 0, 0, 0, 0};  //钱的张数
        int totalValue = 100;

        int allcount = getSheet(totalValue, faceValues, 0, faceCount);
        System.out.println("allCount:" + allcount);
    }

    public int getSheet(int totalFaceValue, int[] singleFaceValues, int i, int[] faceCount)
    {
        int allcount = 0;
        int singleFaceValue = singleFaceValues[i];
        if(i == singleFaceValues.length - 1)
        {
            //处理最后一项，符合数字规则，刚好整除，则符合规则
            int balance = totalFaceValue%singleFaceValue;
            if(balance == 0)
            {
                faceCount[i] = totalFaceValue/singleFaceValue;
                printCount(faceCount, singleFaceValues);
                return 1;
            }else
            {
                return 0;
            }
        }

        int sheetCount = totalFaceValue / singleFaceValue;

        for(int singleFaceCount = 0; singleFaceCount <= sheetCount; singleFaceCount++)
        {
            int remain = totalFaceValue - singleFaceCount * singleFaceValue;
            faceCount[i] = singleFaceCount;
            allcount += getSheet(remain, singleFaceValues, i+1, faceCount);
        }

        return allcount;
    }

    public  void printCount(int[] sheetCount, int[] sheetValue)
    {
        for (int i = 0; i < sheetCount.length; i++)
        {
            System.out.print("" + sheetValue[i] + " *：" + sheetCount[i] +  "   ");
        }
        System.out.println("-------------------------------++");
    }


}
