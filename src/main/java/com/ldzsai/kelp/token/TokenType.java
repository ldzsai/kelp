package com.ldzsai.kelp.token;

public enum TokenType {
   // 字符串
   STRING("String"),
   // 整数
   INTEGER("Integer"),
   // 浮点数
   FLOAT("Float"),
   // 标识符
   IDENTIFIER("Identifier"),
   // 数字
   NUMBER("Number"),
   // 加号
   PLUS("+"),
   // 减号
   MINUS("-"),
   // 乘号
   MULTIPLY("*"),
   // 除号
   DIVIDE("/"),
   // 左括号
   LPAREN("("),
   // 右括号
   RPAREN(")"),
   // 左方括号 [
   LBRACKET("["),
   // 右方括号 ]
   RBRACKET("]"),
   // 小数点
   PERIOD("."),
   // 逗号
   COMMA(","),
   // 双\单引号 "
   QUOTE("\"、'"),
   // 结束标记
   EOF("EOF");

   private final String token;

   TokenType(String token) {
      this.token = token;
   }

   public static TokenType find(char token) {
      for (TokenType type : TokenType.values()) {
         if (type.token.equals(new String(new char[] { token }))) {
            return type;
         }
      }
      return null;
   }
}