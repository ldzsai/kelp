package com.ldzsai.kelp;

import java.util.ArrayList;
import java.util.List;

import com.ldzsai.kelp.expression.ArrayAccess;
import com.ldzsai.kelp.expression.BinaryOperation;
import com.ldzsai.kelp.expression.Expression;
import com.ldzsai.kelp.expression.FloatLiteral;
import com.ldzsai.kelp.expression.FunctionCall;
import com.ldzsai.kelp.expression.IntegerLiteral;
import com.ldzsai.kelp.expression.ObjectKeyAccess;
import com.ldzsai.kelp.expression.StringLiteral;
import com.ldzsai.kelp.expression.Variable;
import com.ldzsai.kelp.token.Token;
import com.ldzsai.kelp.token.TokenType;

/**
 * 表达式解析器
 */
public class Parser {
    private final List<Token> tokens;
    private int currentTokenIndex;

    public Parser(List<Token> tokens) {
        if (tokens == null) {
            throw new IllegalArgumentException("Tokens cannot be null");
        }
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    /**
     * 构建抽象语法树
     * 
     * @return 表达式列表
     */
    public List<Expression> buildAst() throws KelpException {
        try {
            List<Expression> expressions = new ArrayList<>();
            while (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getType() != TokenType.EOF) {
                Expression expression = parseExpression();
                if (expression == null) {
                    continue;
                }
                expressions.add(expression);
            }
            return expressions;
        } catch (Exception e) {
            if (e instanceof KelpException) {
                throw e;
            }
            throw new KelpException("Error building AST: " + e.getMessage(), e);
        }
    }

    /**
     * 解析表达式
     * 
     * @return 表达式
     */
    private Expression parseExpression() throws KelpException {
        if (currentTokenIndex >= tokens.size()) {
            return null;
        }
        
        Expression expr = parseTerm();

        while (currentTokenIndex < tokens.size() && isAddSubOp(currentToken())) {
            Token token = consumeToken();
            Expression right = parseTerm();
            Operator op = Operator.parse(token.getValue().toString());
            expr = new BinaryOperation(expr, op, right);
        }

        return expr;
    }

    /**
     * 解析加减法表达式
     * 
     * @return 表达式
     */
    private Expression parseTerm() throws KelpException {
        Expression expr = parseFactor();

        while (currentTokenIndex < tokens.size() && isMulDivOp(currentToken())) {
            Token token = consumeToken();
            Expression right = parseFactor();
            Operator op = Operator.parse(token.getValue().toString());
            expr = new BinaryOperation(expr, op, right);
        }

        return expr;
    }

    /**
     * 解析原子\单因子表达式
     * 
     * @return 表达式
     */
    private Expression parseFactor() throws KelpException {
        if (currentTokenIndex >= tokens.size()) {
            throw new KelpException("Unexpected end of expression");
        }
        
        Token token = currentToken();
        if (token.getType() == TokenType.NUMBER || token.getType() == TokenType.FLOAT
                || token.getType() == TokenType.INTEGER) {
            consumeToken();
            Number numberValue = (Number) token.getValue();
            if (numberValue instanceof Integer) {
                return new IntegerLiteral(numberValue.intValue());
            } else if (numberValue instanceof Double) {
                return new FloatLiteral(numberValue.doubleValue());
            } else {
                throw new KelpException("Unexpected number type at position " + currentTokenIndex);
            }
        } else if (token.getType() == TokenType.IDENTIFIER) {
            consumeToken();
            return parseChainableExpression(token.getValue().toString());
        } else if (token.getType() == TokenType.LPAREN) {
            consumeToken(); // Consume '('
            Expression expr = parseExpression();
            if (currentTokenIndex >= tokens.size() || consumeToken().getType() != TokenType.RPAREN) {
                throw new KelpException("Expected ')'");
            }
            return expr;
        } else if (token.getType() == TokenType.STRING || token.getType() == TokenType.QUOTE) {
            consumeToken();
            return new StringLiteral((String) token.getValue());
        }
        throw new KelpException("Invalid token at position " + currentTokenIndex + ": " + token.getType());
    }

    /**
     * 解析链式表达式
     *
     * @param baseIdentifier 基础标识符
     * @return 表达式
     */
    private Expression parseChainableExpression(String baseIdentifier) throws KelpException {
        Expression expr = new Variable(baseIdentifier);
        while (currentTokenIndex < tokens.size()) {
            Token token = currentToken();
            if (token.getType() == TokenType.PERIOD) {
                consumeToken(); // Consume '.'
                
                if (currentTokenIndex >= tokens.size()) {
                    throw new KelpException("Expected an identifier after '.'");
                }
                
                Token nextToken = currentToken();

                if (nextToken.getType() != TokenType.IDENTIFIER) {
                    throw new KelpException("Expected an identifier after '.'");
                }

                String identifier = nextToken.getValue().toString();
                consumeToken(); // Consume identifier

                // 检查下一个token是否是左括号
                if (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.LPAREN) {
                    // 处理为方法调用
                    expr = parseMethodCall(expr, identifier);
                } else {
                    // 处理为属性访问
                    Expression keyExpr = new StringLiteral(identifier);
                    expr = new ObjectKeyAccess(expr, keyExpr);
                }
            } else if (token.getType() == TokenType.LBRACKET) {
                expr = parseArrayOrMapAccess(expr);
            } else {
                break;
            }
        }
        return expr;
    }

    /**
     * 解析方法调用
     * 
     * @param target     目标表达式
     * @param methodName 方法名
     * @return 表达式
     */
    private Expression parseMethodCall(Expression target, String methodName) throws KelpException {
        List<Expression> arguments = new ArrayList<>();

        if (currentToken().getType() == TokenType.LPAREN) {
            consumeToken(); // Consume '('
            if (currentTokenIndex < tokens.size() && currentToken().getType() != TokenType.RPAREN) {
                arguments.add(parseExpression());
                while (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.COMMA) {
                    consumeToken(); // Consume ','
                    arguments.add(parseExpression());
                }
            }
            
            if (currentTokenIndex >= tokens.size()) {
                throw new KelpException("Expected ')'");
            }
            
            if (consumeToken().getType() != TokenType.RPAREN) {
                throw new KelpException("Expected ')'");
            }
        }

        return new FunctionCall(target, methodName, arguments);
    }

    /**
     * 解析数组\Map访问
     * 
     * @param target 目标表达式
     * @return 表达式
     */
    private Expression parseArrayOrMapAccess(Expression target) throws KelpException {
        if (currentTokenIndex >= tokens.size()) {
            throw new KelpException("Unexpected end of expression when parsing array access");
        }
        
        consumeToken(); // Consume '['
        
        if (currentTokenIndex >= tokens.size()) {
            throw new KelpException("Unexpected end of expression when parsing array access");
        }
        
        Expression keyExpression = parseExpression();
        
        if (currentTokenIndex >= tokens.size()) {
            throw new KelpException("Expected ']' but reached end of expression");
        }
        
        if (consumeToken().getType() != TokenType.RBRACKET) { // Consume ']'
            throw new KelpException("Expected ']'");
        }

        if (keyExpression instanceof IntegerLiteral) {
            return new ArrayAccess(target, keyExpression);
        } else {
            return new ObjectKeyAccess(target, keyExpression);
        }
    }

    /**
     * 判断是否是加减法运算符
     * 
     * @param token Token
     * @return 是否是加减法运算符
     */
    private boolean isAddSubOp(Token token) {
        return token.getType() == TokenType.PLUS || token.getType() == TokenType.MINUS;
    }

    /**
     * 判断是否是乘除法运算符
     * 
     * @param token Token
     * @return 是否是乘除法运算符
     */
    private boolean isMulDivOp(Token token) {
        return token.getType() == TokenType.MULTIPLY || token.getType() == TokenType.DIVIDE;
    }

    /**
     * 获取下一个Token
     * 
     * @return Token
     */
    private Token consumeToken() throws KelpException {
        if (currentTokenIndex >= tokens.size()) {
            throw new KelpException("Unexpected end of tokens");
        }
        Token token = currentToken();
        currentTokenIndex++;
        return token;
    }

    /**
     * 获取解析器当前正在处理的标记(token)
     * 
     * @return 当前正在处理的token
     */
    private Token currentToken() {
        if (currentTokenIndex >= tokens.size()) {
            return new Token(TokenType.EOF, null);
        }
        return tokens.get(currentTokenIndex);
    }
}