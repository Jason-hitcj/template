package cn.edu.hitsz.compiler.lexer;

import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;



/**
 * TODO: 实验一: 实现词法分析
 * <br>
 * 你可能需要参考的框架代码如下:
 *
 * @author jason
 * @see Token 词法单元的实现
 * @see TokenKind 词法单元类型的实现
 */
public class LexicalAnalyzer {
    private final SymbolTable symbolTable;

    public LexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    private List<String> buf = new ArrayList<>();
    private List<Character> charlist = new LinkedList<>();
    private int p = 0;
    private List<Token> tokenLab = new LinkedList<>();

    /**
     * 从给予的路径中读取并加载文件内容
     *
     * @param path 路径
     */
    public void loadFile(String path) {
        // TODO: 词法分析前的缓冲区实现
        // 可自由实现各类缓冲区
        // 或直接采用完整读入方法

        //按行读取文件
        this.buf = FileUtils.readLines(path);
        int i;
        //按照字符存入charlist，方便自动机检查
        for (String s : buf) {
            for (i = 0; i < s.length(); i++) {
                charlist.add(s.charAt(i));
            }
        }
        //添加'$'作为结束标志
        charlist.add('$');
    }

    /**
     * 执行词法分析, 准备好用于返回的 token 列表 <br>
     * 需要维护实验一所需的符号表条目, 而得在语法分析中才能确定的符号表条目的成员可以先设置为 null
     */
    public void run() {
        // TODO: 自动机实现的词法分析过程
        while (true){
            //ch存储字符，作为自动机跳转判断
            char ch;
            //p作为链表末指针
            ch = charlist.get(p++);

            //跳过空格
            while(ch == ' '){
                ch = charlist.get(p++);
            }

            //字母判断
            if (Character.isLowerCase(ch) || Character.isUpperCase(ch)){
                //临时存储
                StringBuilder string = new StringBuilder();
                while(Character.isLetterOrDigit(ch)){
                    //采用拼接方式将字符组成字符串
                    string.append(ch);
                    ch = charlist.get(p++);
                }
                //尾指针回退
                p--;
                //判断是否为标识符
                if (TokenKind.isAllowed(string.toString())){
                    tokenLab.add(Token.simple(string.toString()));
                }
                else{
                    tokenLab.add(Token.normal("id", string.toString()));
                }


            }
            //数字判断
            else if (Character.isDigit(ch)){
                //同上
                StringBuilder string = new StringBuilder();
                while (Character.isDigit(ch)){
                    string.append(ch);
                    ch = charlist.get(p++);
                }
                //尾指针回退
                p--;
                tokenLab.add(Token.normal("IntConst", string.toString()));

            }
            //结束判断，跳出while循环
            else if (ch == '$'){
                tokenLab.add(Token.eof());
                break;
            }
            //其他字符
            else{
                switch(ch){
                    case '+':
                        tokenLab.add(Token.simple(String.valueOf(ch)));break;
                    case '-':
                        tokenLab.add(Token.simple(String.valueOf(ch)));break;
                    case '*':
                        tokenLab.add(Token.simple(String.valueOf(ch)));break;
                    case '/':
                        tokenLab.add(Token.simple(String.valueOf(ch)));break;
                    case '=':
                        tokenLab.add(Token.simple(String.valueOf(ch)));break;
                    case ';':
                        tokenLab.add(Token.simple("Semicolon"));break;
                    case '(':
                        tokenLab.add(Token.simple(String.valueOf(ch)));break;
                    case ')':
                        tokenLab.add(Token.simple(String.valueOf(ch)));break;
                    case ',':
                        tokenLab.add(Token.simple(String.valueOf(ch)));break;
                    default:
                }
            }
        }
    }

    /**
     * 获得词法分析的结果, 保证在调用了 run 方法之后调用
     *
     * @return Token 列表
     */
    public Iterable<Token> getTokens() {
        // TODO: 从词法分析过程中获取 Token 列表
        // 词法分析过程可以使用 Stream 或 Iterator 实现按需分析
        // 亦可以直接分析完整个文件
        // 总之实现过程能转化为一列表即可
        return tokenLab;

    }

    public void dumpTokens(String path) {
        FileUtils.writeLines(
            path,
            StreamSupport.stream(getTokens().spliterator(), false).map(Token::toString).toList()
        );
    }


}
