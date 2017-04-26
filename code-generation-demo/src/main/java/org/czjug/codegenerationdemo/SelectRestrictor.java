package org.czjug.codegenerationdemo;

import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public class SelectRestrictor extends SqlBaseListener {
    private final CommonTokenFactory commonTokenFactory = new CommonTokenFactory();
    private final String customerId;

    public SelectRestrictor(final String customerId) {
        this.customerId = customerId;
    }

    @Override
    public void enterSelect_statement(final SqlParser.Select_statementContext ctx) {
        super.enterSelect_statement(ctx);

        handleWhereConditions(ctx);
    }

    @Override
    public void enterSelect_statement_without_order(final SqlParser.Select_statement_without_orderContext ctx) {
        super.enterSelect_statement_without_order(ctx);

        handleWhereConditions(ctx);
    }

    private void handleWhereConditions(final ParserRuleContext ctx) {
        final ParserRuleContext whereExpression = getWhereExpression(ctx);
        if (whereExpression == null) {
            addWhere(ctx);
        } else {
            addCondition(whereExpression, true);
        }
    }

    private void addWhere(final ParserRuleContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            final ParseTree child = ctx.getChild(i);
            if (child instanceof SqlParser.Table_nameContext) {
                final Token whereToken = commonTokenFactory.create(SqlLexer.WHERE, "WHERE");
                final TerminalNode whereTerminalNode = new TerminalNodeImpl(whereToken);

                final ParserRuleContext whereExpression = new SqlParser.ExpressionContext(ctx, 0);

                whereExpression.addChild(whereTerminalNode);
                addCondition(whereExpression, false);

                ctx.children.add(i + 1, whereExpression);
            }
        }
    }

    private void addCondition(final ParserRuleContext whereExpression, final boolean withAnd) {
        if (withAnd) {
            final Token andToken = commonTokenFactory.create(SqlLexer.AND, "AND");
            final TerminalNode andTerminalNode = new TerminalNodeImpl(andToken);
            whereExpression.children.add(andTerminalNode);
        }

        final Token columnNameToken = commonTokenFactory.create(SqlLexer.ID, "customer_id");
        final TerminalNode columnNameTerminalNode = new TerminalNodeImpl(columnNameToken);

        final Token eqToken = commonTokenFactory.create(SqlLexer.EQ, "=");
        final TerminalNode eqTerminalNode = new TerminalNodeImpl(eqToken);

        final Token customerIdToken = commonTokenFactory.create(SqlLexer.STRING_LIT, "'" + customerId + "'");
        final TerminalNode customerIdTerminalNode = new TerminalNodeImpl(customerIdToken);

        whereExpression.children.add(columnNameTerminalNode);
        whereExpression.children.add(eqTerminalNode);
        whereExpression.children.add(customerIdTerminalNode);
    }

    private ParserRuleContext getWhereExpression(final ParserRuleContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            final ParseTree child = ctx.getChild(i);
            if (child instanceof SqlParser.Where_statementContext) {
                final SqlParser.Where_statementContext whereStatementContext = (SqlParser.Where_statementContext)child;
                return whereStatementContext.expression();
            }
        }

        return null;
    }
}
