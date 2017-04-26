grammar Sql;

options {
	language = Java;

}

import LexerRules;

root_statement
    : any_statement (SEMI any_statement?)*
    ;

any_statement
    : select_statement
 //   | update_statement
    ;

select_statement
    : SELECT column_list FROM table_name
    where_statement?
    (UNION select_statement_without_order)?
    order_expression?
    ;

where_statement
    : WHERE expression
    ;

order_expression
    : ORDER expression
    ;

select_statement_without_order
    : LPAREN select_statement RPAREN ID
    |SELECT column_list FROM table_name
         (WHERE expression)?
         (UNION select_statement_without_order)?
    ;

column_list
    : column_reference (COMMA column_reference)*
    ;

column_reference
    : ASTERISK
    | expression
    ;

expression
    : expression_item expression_trailing?
    ;

expression_trailing
    : (OPERATOR_BIN | EQ) expression_item expression_trailing?
    | (PLUS | MINUS) expression_item expression_trailing?
    ;

expression_item
    : ID
    | value
    | LPAREN expression RPAREN
    ;

value
    : STRING_LIT | NUMBER_LIT | FALSE | TRUE | NULL
    ;

table_name
    : ID
    ;