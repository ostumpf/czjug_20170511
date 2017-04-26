# Compiler Construction using Java and ANTLR - examples presented at CZ JUG session on 2017-05-11

## Requirements
* JDK 1.8
* Maven

## Build and Run
cd to desired project directory and run:
```
mvn clean package exec:java
```

## lexical-demo
Builds the grammar parser and uses it to print lexical tokens recognized in a hard-coded SQL query.

## syntactical-demo
Builds the grammar parser, uses it to process hard-coded SQL query and prints the table names used in the query.

## code-generation-demo
Builds the grammar parser and uses it to process hard-coded SQL query. The resulting AST is then modified so that 
every SELECT statement contains a WHERE condition with "customer_id = 'abc123'" expression. 
