package com.utils;

/**
 * Created by oaosj on 2016/7/22 0022.
 */
public class SearchUtils {
    public static int RESULT = -1;
    public static int searchString(String source,String target){
        if(source.length()<target.length()){
            throw new UnsupportedOperationException("卧槽、别乱输入！");
        }
        if(source.contains(target)){
            searchMac(source,target);
            return RESULT;
        }else {
            System.out.println(target + "not found");
            return RESULT;
        }

    }



    public static void searchMac(String source,String target){
        int sLength = source.length();
        int tLength = target.length();
        int cursor = 0;
        String subSource = source.substring(cursor, cursor+tLength);

        for(int i=0;i<sLength;i++){
            int result = match(subSource,target);
            if(result == tLength){
                System.out.print("find "+target+" range ");
                System.out.print(cursor+" to ");
                System.out.println(cursor+result);
                RESULT = cursor+result;
                break;
            }else{
                cursor =cursor + result;
                subSource = source.substring(cursor, cursor+tLength);
            }
            if(cursor>=sLength){
                System.out.println("done . not found!");
            }
        }




    }

    public static int match(String subSource,String target) {

        for(int i = 0;i<target.length();i++){
            if(matchChar(subSource.charAt(i), target.charAt(i))){
                continue;
            }else {
                return i+1;
            }
        }

        return target.length();
    }


    public static boolean matchChar(char a,char b) {
        return a==b?true:false;
    }
}
