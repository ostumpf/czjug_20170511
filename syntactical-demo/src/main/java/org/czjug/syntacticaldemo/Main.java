package org.czjug.syntacticaldemo;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        printUsedTables("select x, y from my_table union select * from another_table");
    }

    private static void printUsedTables(final String statements) {
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
        final TableNameListener listener = new TableNameListener();
        walker.walk(listener, root);

        listener.getUsedTables().forEach(System.out::println);
        System.out.println("---------------------------");
    }

    static class TableNameListener extends SqlBaseListener {

        private List<String> usedTables = new ArrayList<>();

        @Override
        public void enterTable_name(final SqlParser.Table_nameContext ctx) {
            super.enterTable_name(ctx);
            usedTables.add(ctx.ID().getText());
        }
        public List<String> getUsedTables() {
            return usedTables;
        }
    }
}
