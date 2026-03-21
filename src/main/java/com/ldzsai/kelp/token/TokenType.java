package com.ldzsai.kelp.token;

/**
 * Token类型枚举
 */
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

   // 算术运算符
   PLUS("+"),
   MINUS("-"),
   MULTIPLY("*"),
   DIVIDE("/"),
   MODULO("%"),
   POWER("**"),
   INTEGER_DIVIDE("//"),

   // 比较运算符
   EQUALS("=="),
   NOT_EQUALS("!="),
   GREATER_THAN(">"),
   LESS_THAN("<"),
   GREATER_OR_EQUAL(">="),
   LESS_OR_EQUAL("<="),

   // 逻辑运算符
   LOGICAL_AND("&&"),
   LOGICAL_OR("||"),
   LOGICAL_NOT("!"),

   // 位运算符
   BIT_AND("&"),
   BIT_OR("|"),
   BIT_XOR("^"),
   BIT_NOT("~"),
   LEFT_SHIFT("<<"),
   RIGHT_SHIFT(">>"),
   UNSIGNED_RIGHT_SHIFT(">>>"),

   // 三元运算符
   QUESTION("?"),
   COLON(":"),

   // 分隔符
   LPAREN("("),
   RPAREN(")"),
   LBRACKET("["),
   RBRACKET("]"),
   PERIOD("."),
   COMMA(","),
   // 双\单引号 "
   QUOTE("\"、'"),
   // 结束标记
   EOF("EOF");

   private final String token;

   TokenType(String token) {
      this.token = token;
   }

   public static TokenType find(String tokenStr) {
      if (tokenStr == null || tokenStr.isEmpty()) {
         return null;
      }

      // 优先检查多字符运算符
      if (tokenStr.length() >= 2) {
         for (TokenType type : TokenType.values()) {
            if (type.token.equals(tokenStr) && type.token.length() >= 2) {
               return type;
            }
         }
      }

      // 单字符匹配
      char ch = tokenStr.charAt(0);
      for (TokenType type : TokenType.values()) {
         if (type.token.length() == 1 && type.token.charAt(0) == ch) {
            return type;
         }
      }
      return null;
   }
}
