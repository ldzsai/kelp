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
import com.ldzsai.kelp.expression.TernaryOperation;
import com.ldzsai.kelp.expression.UnaryOperation;
import com.ldzsai.kelp.expression.Variable;
import com.ldzsai.kelp.token.Token;
import com.ldzsai.kelp.token.TokenType;

/**
 * 表达式解析器
 * 支持运算符优先级：幂运算 &gt; 乘除模 &gt; 加减 &gt; 位移 &gt; 比较 &gt; 等价 &gt; 位运算 &gt; 逻辑与 &gt;
 * 逻辑或 &gt; 三元
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
     * 解析表达式 - 最低优先级
     * 处理三元运算符
     */
    private Expression parseExpression() throws KelpException {
        if (currentTokenIndex >= tokens.size()) {
            return null;
        }

        Expression expr = parseOr();

        // 检查三元运算符
        if (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.QUESTION) {
            consumeToken(); // consume '?'
            Expression trueValue = parseExpression();

            if (currentTokenIndex >= tokens.size()) {
                throw new KelpException("Expected ':' in ternary expression");
            }

            if (consumeToken().getType() != TokenType.COLON) {
                throw new KelpException("Expected ':' in ternary expression");
            }

            Expression falseValue = parseExpression();
            expr = new TernaryOperation(expr, trueValue, falseValue);
        }

        return expr;
    }

    /**
     * 解析逻辑或 (||)
     */
    private Expression parseOr() throws KelpException {
        Expression left = parseAnd();

        while (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.LOGICAL_OR) {
            Token token = consumeToken();
            Expression right = parseAnd();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析逻辑与 (&&)
     */
    private Expression parseAnd() throws KelpException {
        Expression left = parseBitOr();

        while (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.LOGICAL_AND) {
            Token token = consumeToken();
            Expression right = parseBitOr();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析位或 (|)
     */
    private Expression parseBitOr() throws KelpException {
        Expression left = parseBitXor();

        while (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.BIT_OR) {
            Token token = consumeToken();
            Expression right = parseBitXor();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析位异或 (^)
     */
    private Expression parseBitXor() throws KelpException {
        Expression left = parseBitAnd();

        while (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.BIT_XOR) {
            Token token = consumeToken();
            Expression right = parseBitAnd();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析位与 (&)
     */
    private Expression parseBitAnd() throws KelpException {
        Expression left = parseEquality();

        while (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.BIT_AND) {
            Token token = consumeToken();
            Expression right = parseEquality();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析等价比较 (==, !=)
     */
    private Expression parseEquality() throws KelpException {
        Expression left = parseComparison();

        while (currentTokenIndex < tokens.size() && isEqualityOp(currentToken())) {
            Token token = consumeToken();
            Expression right = parseComparison();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析比较运算 (>, <, >=, <=)
     */
    private Expression parseComparison() throws KelpException {
        Expression left = parseShift();

        while (currentTokenIndex < tokens.size() && isComparisonOp(currentToken())) {
            Token token = consumeToken();
            Expression right = parseShift();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析位移运算 (<<, >>, >>>)
     */
    private Expression parseShift() throws KelpException {
        Expression left = parseAdditive();

        while (currentTokenIndex < tokens.size() && isShiftOp(currentToken())) {
            Token token = consumeToken();
            Expression right = parseAdditive();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析加法运算 (+, -)
     */
    private Expression parseAdditive() throws KelpException {
        Expression left = parseMultiplicative();

        while (currentTokenIndex < tokens.size() && isAddSubOp(currentToken())) {
            Token token = consumeToken();
            Expression right = parseMultiplicative();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析乘法运算 (*, /, %, //)
     */
    private Expression parseMultiplicative() throws KelpException {
        Expression left = parsePower();

        while (currentTokenIndex < tokens.size() && isMulDivOp(currentToken())) {
            Token token = consumeToken();
            Expression right = parsePower();
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析幂运算 (**) - 右结合
     */
    private Expression parsePower() throws KelpException {
        Expression left = parseUnary();

        if (currentTokenIndex < tokens.size() && currentToken().getType() == TokenType.POWER) {
            Token token = consumeToken();
            Expression right = parsePower(); // 右结合，递归调用
            Operator op = Operator.parse(token.getValue().toString());
            left = new BinaryOperation(left, op, right);
        }

        return left;
    }

    /**
     * 解析一元运算符 (-, !, ~)
     */
    private Expression parseUnary() throws KelpException {
        if (currentTokenIndex < tokens.size()) {
            Token token = currentToken();

            if (token.getType() == TokenType.MINUS) {
                consumeToken();
                Expression operand = parseUnary(); // 支持链式一元运算符
                return new UnaryOperation("-", operand);
            }

            if (token.getType() == TokenType.LOGICAL_NOT) {
                consumeToken();
                Expression operand = parseUnary();
                return new UnaryOperation("!", operand);
            }

            if (token.getType() == TokenType.BIT_NOT) {
                consumeToken();
                Expression operand = parseUnary();
                return new UnaryOperation("~", operand);
            }
        }

        return parsePrimary();
    }

    /**
     * 解析基本表达式（数字、字符串、标识符、括号表达式）
     */
    private Expression parsePrimary() throws KelpException {
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

    // 判断运算符的方法
    private boolean isEqualityOp(Token token) {
        TokenType type = token.getType();
        return type == TokenType.EQUALS || type == TokenType.NOT_EQUALS;
    }

    private boolean isComparisonOp(Token token) {
        TokenType type = token.getType();
        return type == TokenType.GREATER_THAN || type == TokenType.LESS_THAN
                || type == TokenType.GREATER_OR_EQUAL || type == TokenType.LESS_OR_EQUAL;
    }

    private boolean isShiftOp(Token token) {
        TokenType type = token.getType();
        return type == TokenType.LEFT_SHIFT || type == TokenType.RIGHT_SHIFT
                || type == TokenType.UNSIGNED_RIGHT_SHIFT;
    }

    private boolean isAddSubOp(Token token) {
        return token.getType() == TokenType.PLUS || token.getType() == TokenType.MINUS;
    }

    private boolean isMulDivOp(Token token) {
        TokenType type = token.getType();
        return type == TokenType.MULTIPLY || type == TokenType.DIVIDE
                || type == TokenType.MODULO || type == TokenType.INTEGER_DIVIDE;
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
