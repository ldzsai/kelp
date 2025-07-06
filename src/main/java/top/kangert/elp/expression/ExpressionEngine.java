package top.kangert.elp.expression;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpressionEngine {
    private Environment env;

    private Lexer lexer;

    private Parser parser;

    private List<Token> tokens;

    // 新增缓存
    private final Map<String, List<Expression>> cache = new ConcurrentHashMap<>();

    // 执行耗时
    private long lastExecutionTime = 0;

    public ExpressionEngine(Environment env) {
        this.env = env;

    }

    /**
     * 执行表达式
     * 
     * @param exp 表达式
     * @return 执行结果
     * @throws Exception 异常
     */
    public Object execute(String exp) throws Exception {
        // 记录开始时间
        long startTime = System.nanoTime();

        // 缓存命中判断
        List<Expression> ast = cache.get(exp);
        if (ast == null) {
            tokenizer(exp);
            ast = parser.buildAst();
            // 缓存未命中时解析并存储
            cache.put(exp, ast); 
        }

        StringBuilder result = new StringBuilder();
        for (Expression expression : ast) {
            result.append(expression.evaluate(env));
        }

        // 记录结束时间并计算耗时（单位：毫秒）
        lastExecutionTime = (System.nanoTime() - startTime) / 1_000_000;

        return result.toString();
    }

    /**
     * 分词
     * 
     * @param exp 表达式
     */
    private void tokenizer(String exp) {
        this.lexer = new Lexer(exp);
        this.tokens = lexer.tokenizer();
        this.parser = new Parser(tokens);
    }

    /**
     * 获取最近一次执行耗时（毫秒）
     */
    public long getLastExecutionTime() {
        return lastExecutionTime;
    }
}
