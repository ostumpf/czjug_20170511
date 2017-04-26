package org.czjug.lexicaldemo;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        printLexerTokens("select x from my_table");
        printLexerTokens("select func(x) from my_table join other_table on a = '1'");
    }

    private static void printLexerTokens(final String statements) throws IOException {
        System.out.println(statements);
        final ANTLRInputStream input = new ANTLRInputStream(statements);
        final SqlLexer lexer = new SqlLexer(input);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        tokens.fill();

        tokens.getTokens().forEach(System.out::println);
        System.out.println("---------------------------");
    }
}
