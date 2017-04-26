package org.czjug.codegenerationdemo;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.StringJoiner;

public class Main {
    public static void main(String[] args) {
        printStatementWithAddedWhere("select x, y from my_table", "abc123");
        printStatementWithAddedWhere("select x, y from my_table where y = 2", "abc123");
        printStatementWithAddedWhere("select x, y from my_table union select * from another_table", "abc123");
    }

    private static void printStatementWithAddedWhere(final String statements, final String customerId) {
        System.out.println(statements);
        final ANTLRInputStream input = new ANTLRInputStream(statements);
        final SqlLexer lexer = new SqlLexer(input);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final SqlParser parser = new SqlParser(tokens);
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);

        parser.addErrorListener(new DiagnosticErrorListener());
        parser.addErrorListener(new ConsoleErrorListener());
        final ParseTree root = parser.root_statement();

        final ParseTreeWalker walker = new ParseTreeWalker();

        final SelectRestrictor selectRestrictor = new SelectRestrictor(customerId);
        final SqlGenerator generator = new SqlGenerator();

        walker.walk(selectRestrictor, root);
        walker.walk(generator, root);

        System.out.println(generator.getTargetSql());
        System.out.println("---------------------------");
    }

    static class SqlGenerator extends SqlBaseListener {

        private StringJoiner targetSql = new StringJoiner(" ");

        @Override
        public void visitTerminal(final TerminalNode node) {
            super.visitTerminal(node);

            targetSql.add(node.getText());
        }

        public String getTargetSql() {
            return targetSql.toString();
        }
    }
}
