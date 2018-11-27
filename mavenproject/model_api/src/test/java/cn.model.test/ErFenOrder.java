package cn.model.test;

import org.junit.Test;

/**
 * Created by wangdian05 on 2018/7/7.
 * 测试：递归解决二分数组排序
 */
public class ErFenOrder {

    @Test
    public void test1()
    {

        int[] arr = {3,4,2,1,9,8,3,6,4,3};
        sort(0, arr.length -1, arr);
    }

    public void sort(int low, int high, int arr[])
    {
        int start = low;
        int end = high;
        if(low < 0 || high > arr.length -1 || low >= high)
            return;
        int temp = arr[low];
        while (low != high)
        {
            while(low < high &temp <= arr[high]) high = high -1;
            if(low < high)arr[low] = arr[high];
            while(low < high & arr[low] <= temp) low = low + 1;
            if(low < high) arr[high] = arr[low];
        }
        arr[low] = temp;
        pringArr(arr);
        sort(start, low - 1, arr);
        sort(low + 1, end, arr);
    }

    void pringArr(int[] a)
    {
        String str = "";
        for(int i:a)
        {
            str += i + ",";
        }
        System.out.println(str);
    }

    @Test
    public void name() throws Exception {

    }
}
