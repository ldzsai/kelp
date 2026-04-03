package com.ldzsai.kelp.expression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.Operator;
import com.ldzsai.kelp.api.ExpressionVisitor;

class ExpressionVisitorTest {

    @Test
    void testBinaryOperationVisitor() {
        Expression ast = new BinaryOperation(
            new IntegerLiteral(1),
            Operator.ADD,
            new IntegerLiteral(2)
        );

        ExpressionVisitor<String> describer = new ExpressionVisitor<String>() {
            @Override public String visitBinary(BinaryOperation expr) {
                return "(" + expr.getLeft().accept(this) + " " + expr.getOperator().getSymbol() + " " + expr.getRight().accept(this) + ")";
            }
            @Override public String visitUnary(UnaryOperation expr) { return expr.describe(); }
            @Override public String visitTernary(TernaryOperation expr) { return expr.describe(); }
            @Override public String visitVariable(Variable expr) { return expr.getName(); }
            @Override public String visitFunctionCall(FunctionCall expr) { return expr.describe(); }
            @Override public String visitArrayAccess(ArrayAccess expr) { return expr.describe(); }
            @Override public String visitObjectKeyAccess(ObjectKeyAccess expr) { return expr.describe(); }
            @Override public String visitIntegerLiteral(IntegerLiteral expr) { return String.valueOf(expr.getValue()); }
            @Override public String visitFloatLiteral(FloatLiteral expr) { return String.valueOf(expr.getValue()); }
            @Override public String visitStringLiteral(StringLiteral expr) { return "'" + expr.getValue() + "'"; }
        };

        assertEquals("(1 + 2)", ast.accept(describer));
    }

    @Test
    void testDescribeIntegerLiteral() {
        Expression lit = new IntegerLiteral(42);
        assertEquals("42", lit.describe());
    }

    @Test
    void testDescribeFloatLiteral() {
        Expression lit = new FloatLiteral(3.14);
        assertEquals("3.14", lit.describe());
    }

    @Test
    void testDescribeVariable() {
        Expression var = new Variable("x");
        assertEquals("x", var.describe());
    }

    @Test
    void testDescribeStringLiteral() {
        Expression str = new StringLiteral("hello");
        assertEquals("'hello'", str.describe());
    }

    @Test
    void testDescribeBinaryOperation() {
        Expression ast = new BinaryOperation(
            new Variable("a"),
            Operator.ADD,
            new Variable("b")
        );
        assertEquals("(a + b)", ast.describe());
    }

    @Test
    void testDescribeUnaryOperation() {
        Expression ast = new UnaryOperation("-", new Variable("x"));
        assertEquals("-x", ast.describe());
    }

    @Test
    void testDescribeTernaryOperation() {
        Expression ast = new TernaryOperation(
            new Variable("cond"),
            new IntegerLiteral(1),
            new IntegerLiteral(0)
        );
        assertEquals("(cond ? 1 : 0)", ast.describe());
    }

    @Test
    void testIntegerLiteralGetValue() {
        IntegerLiteral lit = new IntegerLiteral(42);
        assertEquals(42, lit.getValue());
    }

    @Test
    void testFloatLiteralGetValue() {
        FloatLiteral lit = new FloatLiteral(3.14);
        assertEquals(3.14, lit.getValue(), 0.001);
    }

    @Test
    void testStringLiteralGetValue() {
        StringLiteral lit = new StringLiteral("hello");
        assertEquals("hello", lit.getValue());
    }

    @Test
    void testVariableGetName() {
        Variable var = new Variable("myVar");
        assertEquals("myVar", var.getName());
    }
}
