grammar Cobol;

// ---------------------------------------------------------
// 1. REGLAS DEL PARSER (Estructura Lógica - El AST)
// ---------------------------------------------------------
program : statement* EOF ;

statement : ifStatement
          | displayStatement ;

ifStatement : IF condition statement* (ELSE statement*)? END_IF ;

displayStatement : DISPLAY (STRING | ID) ;

condition : ID OPERATOR NUMBER ;

// ---------------------------------------------------------
// 2. REGLAS DEL LEXER (Tokens / Vocabulario)
// ---------------------------------------------------------

// Palabras reservadas
IF      : 'IF' ;
ELSE    : 'ELSE' ;
END_IF  : 'END-IF' ;
DISPLAY : 'DISPLAY' ;

// Operadores y Tipos
OPERATOR : '>' | '<' | '=' | '>=' | '<=' ;
ID       : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER   : [0-9]+ ;
STRING   : '"' .*? '"' ;

// Ignorar espacios en blanco y saltos de línea
WS       : [ \t\r\n]+ -> skip ;