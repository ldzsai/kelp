package com.ldzsai.kelp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ldzsai.kelp.token.Token;
import com.ldzsai.kelp.token.TokenType;

/**
 * 词法分析器（标记器）。
 * 从输入字符串中提取 ${...} 表达式块，然后将内部表达式
 * 分解为标记流。
 */
public class Lexer {
    private final String input;
    private int position;

    public Lexer(String input) {
        this.input = input != null ? input : "";
        this.position = 0;
    }

    /**
     * 将输入字符串分解为标记列表。
     *
     * @return 标记列表
     * @throws KelpException 词法错误时抛出异常
     */
    public List<Token> tokenize() throws KelpException {
        try {
            List<Token> tokens = new ArrayList<>();

            // 提取表达式块位置
            List<Integer> chunkPos = extractExpressionPositions(input);

            if (chunkPos.isEmpty()) {
                // 无表达式——将整个输入视为字面字符串
                tokens.add(new Token(TokenType.STRING, input));
            } else {
                for (int start : chunkPos) {
                    int startPos = position;
                    int endPos = start;

                    // 添加表达式前的字面字符串
                    String val = input.substring(startPos, endPos);
                    if (!val.isEmpty()) {
                        tokens.add(new Token(TokenType.STRING, val));
                    }

                    // 对表达式内容进行词法分析（跳过 ${)
                    position = start + 2;
                    Token token;
                    do {
                        token = nextToken();
                        tokens.add(token);
                    } while (token.getType() != TokenType.EOF && position < input.length()
                            && input.charAt(position) != '}');

                    // 跳过闭合的 }
                    if (position < input.length() && input.charAt(position) == '}') {
                        position++;
                    }
                }

                // 添加末尾的字面字符串
                if (position < input.length()) {
                    String val = input.substring(position);
                    if (!val.isEmpty()) {
                        tokens.add(new Token(TokenType.STRING, val));
                    }
                }
            }

            tokens.add(new Token(TokenType.EOF, null));
            return tokens;
        } catch (KelpException e) {
            throw e;
        } catch (Exception e) {
            throw new KelpException("Error tokenizing input: " + e.getMessage(), e);
        }
    }

    /**
     * 从当前位置获取下一个标记。
     */
    private Token nextToken() throws KelpException {
        skipWhitespace();
        if (position >= input.length()) {
            return new Token(TokenType.EOF, null);
        }

        char ch = input.charAt(position);

        // 优先匹配三字符运算符，再匹配双字符运算符
        // 确保 >>> 在 >> 之前被匹配
        if (position + 2 < input.length()) {
            String threeChars = input.substring(position, position + 3);
            TokenType type = matchThreeCharOperator(threeChars);
            if (type != null) {
                position += 3;
                return new Token(type, threeChars);
            }
        }

        // 匹配双字符运算符
        if (position + 1 < input.length()) {
            String twoChars = input.substring(position, position + 2);
            TokenType type = matchTwoCharOperator(twoChars);
            if (type != null) {
                position += 2;
                return new Token(type, twoChars);
            }
        }

        // 单字符处理
        switch (ch) {
            case '+':
                position++;
                return new Token(TokenType.PLUS, "+");
            case '-':
                position++;
                return new Token(TokenType.MINUS, "-");
            case '*':
                position++;
                return new Token(TokenType.MULTIPLY, "*");
            case '/':
                position++;
                return new Token(TokenType.DIVIDE, "/");
            case '%':
                position++;
                return new Token(TokenType.MODULO, "%");
            case '(':
                position++;
                return new Token(TokenType.LPAREN, "(");
            case ')':
                position++;
                return new Token(TokenType.RPAREN, ")");
            case '.':
                position++;
                return new Token(TokenType.PERIOD, ".");
            case ',':
                position++;
                return new Token(TokenType.COMMA, ",");
            case '[':
                position++;
                return new Token(TokenType.LBRACKET, "[");
            case ']':
                position++;
                return new Token(TokenType.RBRACKET, "]");
            case '?':
                position++;
                return new Token(TokenType.QUESTION, "?");
            case ':':
                position++;
                return new Token(TokenType.COLON, ":");
            case '!':
                position++;
                return new Token(TokenType.LOGICAL_NOT, "!");
            case '&':
                position++;
                return new Token(TokenType.BIT_AND, "&");
            case '|':
                position++;
                return new Token(TokenType.BIT_OR, "|");
            case '^':
                position++;
                return new Token(TokenType.BIT_XOR, "^");
            case '~':
                position++;
                return new Token(TokenType.BIT_NOT, "~");
            case '<':
                position++;
                return new Token(TokenType.LESS_THAN, "<");
            case '>':
                position++;
                return new Token(TokenType.GREATER_THAN, ">");
            case '"':
            case '\'':
                return parseQuotedString();
            default:
                if (Character.isDigit(ch)) {
                    return parseNumber();
                } else if (Character.isLetter(ch) || ch == '_') {
                    return parseIdentifier();
                }
                throw new KelpException("Invalid character at position " + position + ": " + ch);
        }
    }

    private TokenType matchTwoCharOperator(String str) {
        switch (str) {
            case "==": return TokenType.EQUALS;
            case "!=": return TokenType.NOT_EQUALS;
            case ">=": return TokenType.GREATER_OR_EQUAL;
            case "<=": return TokenType.LESS_OR_EQUAL;
            case "&&": return TokenType.LOGICAL_AND;
            case "||": return TokenType.LOGICAL_OR;
            case "<<": return TokenType.LEFT_SHIFT;
            case ">>": return TokenType.RIGHT_SHIFT;
            case "**": return TokenType.POWER;
            case "//": return TokenType.INTEGER_DIVIDE;
            default: return null;
        }
    }

    private TokenType matchThreeCharOperator(String str) {
        switch (str) {
            case ">>>": return TokenType.UNSIGNED_RIGHT_SHIFT;
            default: return null;
        }
    }

    private Token parseNumber() throws KelpException {
        int startPos = position;
        boolean hasDot = false;
        while (position < input.length()
                && (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
            if (input.charAt(position) == '.') {
                if (hasDot) {
                    throw new KelpException("Invalid number format at position " + position);
                }
                hasDot = true;
            }
            position++;
        }
        String numberStr = input.substring(startPos, position);
        try {
            if (hasDot) {
                return new Token(TokenType.FLOAT, Double.parseDouble(numberStr));
            }
            return new Token(TokenType.INTEGER, Integer.parseInt(numberStr));
        } catch (NumberFormatException e) {
            throw new KelpException("Invalid number format: " + numberStr);
        }
    }

    private Token parseQuotedString() throws KelpException {
        char quoteChar = input.charAt(position);
        int startPos = position;
        position++; // 跳过起始引号
        while (position < input.length() && input.charAt(position) != quoteChar) {
            if (input.charAt(position) == '\\' && position + 1 < input.length()) {
                position += 2;
            } else {
                position++;
            }
        }
        if (position >= input.length()) {
            throw new KelpException("Unterminated quoted string starting at position " + startPos);
        }
        position++; // 跳过结束引号
        String value = input.substring(startPos + 1, position - 1);

        // 处理转义序列
        value = value.replace("\\\"", "\"");
        value = value.replace("\\'", "'");
        value = value.replace("\\n", "\n");
        value = value.replace("\\r", "\r");
        value = value.replace("\\t", "\t");

        return new Token(TokenType.QUOTE, value);
    }

    private Token parseIdentifier() {
        int startPos = position;
        while (position < input.length()
                && (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
            position++;
        }
        String identifier = input.substring(startPos, position);
        return new Token(TokenType.IDENTIFIER, identifier);
    }

    private List<Integer> extractExpressionPositions(String input) {
        List<Integer> exps = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            exps.add(matcher.start());
        }
        return exps;
    }

    private void skipWhitespace() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }

    /**
     * @deprecated 请使用 {@link #tokenize()} 替代。
     */
    @Deprecated
    public List<Token> tokenizer() throws KelpException {
        return tokenize();
    }
}
