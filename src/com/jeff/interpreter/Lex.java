package com.jeff.interpreter;

import com.jeff.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lex implements Iterator<Token> {


    private static final Pattern KEYWORD = Pattern.compile("^((int)|(real)|(if)|(then)|(else)|(while))$");
    private static final Pattern DELIMITER = Pattern.compile("^([(){};,])$");
    private static final Pattern INT_NUM = Pattern.compile("^\\d+$");
    private static final Pattern REAL_NUM = Pattern.compile("^\\d+\\.\\d+$");
    private static final Pattern OPERATOR = Pattern.compile("^((==)|(<=)|(>=)|(!=)|[+\\-/*=<>])$");
    private static final Pattern IDENTIFIER = Pattern.compile("^([a-zA-Z]+)$");

    private final List<String> tokenStringList;
    private final List<Token> tokenList;
    private int currPos = -1;

    public Lex(String context) {
        tokenStringList = StringUtils.separateBySpace(context);
        tokenList = new ArrayList<>();
        next();
    }

    /**
     * 根据属性值判断token类型
     * @param value 属性值
     * @return token类型字符串
     */
    public static TokenType getTokenType(String value){
        Matcher keywordMatcher = KEYWORD.matcher(value);
        Matcher delimiterMatcher = DELIMITER.matcher(value);
        Matcher intMatcher = INT_NUM.matcher(value);
        Matcher realMatcher = REAL_NUM.matcher(value);
        Matcher operatorMatcher = OPERATOR.matcher(value);
        Matcher identifierMatcher = IDENTIFIER.matcher(value);
        if(delimiterMatcher.find())
            return TokenType.DELIMITER;
        else if(keywordMatcher.find())
            return TokenType.KEYWORD;
        else if(intMatcher.find())
            return TokenType.INT_NUM;
        else if(realMatcher.find())
            return TokenType.REAL_NUM;
        else if(operatorMatcher.find())
            return TokenType.OPERATOR;
        else if(identifierMatcher.find())
            return TokenType.IDENTIFIER;
        return TokenType.ERROR;
    }


    /**
     * 匹配token，并且将移到下一个token
     * @param tokenStr 需要匹配的token类型
     * @return lexeme
     */
    public Token match(String tokenStr){
        Token token = current();
        if(!token.match4Ex4Grammar(tokenStr))
            throw new NotMatchedException(token, tokenStr);
        next();
        return token;
    }


    /**
     * ex2的文法
     * 匹配token，并且将移到下一个token
     * @param tokenStr 需要匹配的token类型
     * @return lexeme
     */
    public Token match2(String tokenStr){
        Token token = current();
        if(!token.match4Ex2Grammar(tokenStr))
            throw new NotMatchedException(token, tokenStr);
        next();
        return token;
    }

    /**
     * 是否还有下一个token
     * @return true为存在，false为不存在
     */
    @Override
    public boolean hasNext() {
        return currPos < tokenStringList.size();
    }

    public Token current(){
        return tokenList.get(currPos);
    }

    public int getCurrPos() {
        return currPos;
    }

    public void setCurrPos(int currPos) {
        this.currPos = currPos;
    }

    /**
     * 返回下一个token
     * @return token，不存在返回null
     */
    public Token next(){
        if(currPos + 1 < tokenList.size()) {
            return tokenList.get(++currPos);
        } else if(currPos == tokenStringList.size() - 1) {
            currPos++;
            Token end = new Token(TokenType.END, "$", null);
            tokenList.add(end);
            return end;
        }
        String lexeme = tokenStringList.get(++currPos);
        TokenType type = getTokenType(lexeme);
        Number lexVal = null;
        if(type == TokenType.INT_NUM)
            lexVal = Integer.parseInt(lexeme);
        else if(type == TokenType.REAL_NUM)
            lexVal = Double.parseDouble(lexeme);
        Token token = new Token(type, lexeme, lexVal);
        tokenList.add(token);
        return token;
    }

}
