package com.ldzsai.kelp.api;

import com.ldzsai.kelp.expression.*;

/**
 * 抽象语法树操作的访问者模式接口。
 * 支持在不修改 Expression 子类的前提下添加新操作（求值、优化、格式化输出等）。
 *
 * @param <T> 访问者的返回类型
 */
public interface ExpressionVisitor<T> {
    T visitBinary(BinaryOperation expr);
    T visitUnary(UnaryOperation expr);
    T visitTernary(TernaryOperation expr);
    T visitVariable(Variable expr);
    T visitFunctionCall(FunctionCall expr);
    T visitArrayAccess(ArrayAccess expr);
    T visitObjectKeyAccess(ObjectKeyAccess expr);
    T visitIntegerLiteral(IntegerLiteral expr);
    T visitFloatLiteral(FloatLiteral expr);
    T visitStringLiteral(StringLiteral expr);
}
