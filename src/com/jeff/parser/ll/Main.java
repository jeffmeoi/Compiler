package com.jeff.parser.ll;

public class Main {

    public static void main(String[] args) {
        // 需要分析的字符串
        String context = "{ ID = NUM ; }";
        // 初始化解释器
        LLParser parser = new LLParser();
        // 解释器匹配，判断字符串是否符合syntax
        if(parser.parse(context)){
            // 若匹配，则输出解释树，tab间距为2
            parser.outputParseTree(2);
        } else {
            // 若不匹配，则输出false
            System.out.println(false);
        }
    }
}
