package cn.model.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdian05 on 2018/8/10.
 */
public class Sort {

    @Test
    public void juintTest()
    {
        int[] arr = new int[]{3, 4, 1, 9, 7};
        print(arr);

//        bubbleSort(arr);
//        selectSort(arr);
//        insertSort(arr);

        ArrayList list = new ArrayList();
        list.add(3);list.add(4);list.add(1);list.add(9);list.add(7);
        mergeSort(list);
        pringArrayList(list);

    }



    // 冒泡排序 小的上浮------------------------------------------------
    public void bubbleSort(int[] arr)
    {
        for(int i = 0; i < arr.length; i++)
        {
            for(int j = 0; j < arr.length - i -1; j++)
            {
                int temp;
                int ai = arr[i], aj = arr[j];
                if(arr[j] < arr[j+1])
                {
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
            print(arr);
        }
    }

    //选择排序----------------------------------------------------
    public void selectSort(int[] arr)
    {
        for(int i = 0; i< arr.length; i++)
        {
            for (int j = i+1; j < arr.length; j++)
            {
                int ai = arr[i], aj = arr[j];
                int temp;
                if(arr[i] > arr[j])
                {
                    temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
            print(arr);
        }
    }

    //插入排序----------------------------------------------------------
    public void insertSort(int[] arr)
    {
        for(int i = 1; i < arr.length; i++)
        {
            int temp = arr[i];int flag = i;
            for (int j = i; j > 0; j--)
            {
                int ai = arr[i], aj = arr[j];
                if(temp > arr[j-1])
                {
                    arr[j] = arr[j-1];
                    flag = j-1;
                }else
                    break;
            }
            arr[flag] = temp;
            print(arr);
        }
    }

    //归并排序----------------------------------------------
    public List mergeSort(List list)
    {
        int len = list.size();
        if(len < 2)
            return list;
        List left =  list.subList(0, len / 2 );
        List right = list.subList(len/2, len);

        return merge(mergeSort(left), mergeSort(right));
    }

    public List merge(List left, List right)
    {
        pringArrayList(left);
        pringArrayList(right);
        ArrayList ret = new ArrayList();
        int leftI = 0, rightI = 0;
        while (leftI < left.size() && rightI < right.size())
        {
            while (leftI < left.size() && rightI < right.size() && (int)left.get(leftI) < (int)right.get(rightI))
            {
                ret.add(left.get(leftI));
                leftI++;
            }
            while (leftI < left.size() && rightI < right.size() && (int)left.get(leftI) > (int)right.get(rightI))
            {
                ret.add(right.get(rightI));
                rightI++;
            }

        }
        if(leftI < left.size())
            ret.addAll(left.subList(leftI, left.size()));
        if(rightI < right.size())
            ret.addAll(right.subList(rightI, right.size()));

        pringArrayList(ret);
        return ret;
    }

    //希尔排序-------------------------------------------------------


    //---------------------------------------------------------------
    public void print(int[] arr)
    {
        for(int i = 0;i < arr.length; i++)
        {
            System.out.print(arr[i] + "  ");
        }
        System.out.println("");
    }

    public void pringArrayList(List arr)
    {
        for(int i = 0;i < arr.size(); i++)
        {
            System.out.print(arr.get(i) + "  ");
        }
        System.out.println("");
    }

}
