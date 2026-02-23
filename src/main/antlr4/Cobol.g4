grammar Cobol;


// 1. PARSER

program : statement* EOF ;

statement 
    : ifStatement
    | displayStatement ;

ifStatement
    : IF condition thenBlock
      (ELSE IF condition thenBlock)*
      (ELSE elseBlock)?
      END_IF
    ;

thenBlock 
    : statement* ;

elseBlock 
    : statement* ;

displayStatement 
    : DISPLAY (STRING | ID) ;

condition 
    : ID OPERATOR NUMBER ;


// 2. LEXER

// Palabras reservadas
IF      : 'IF' ;
ELSE    : 'ELSE' ;
END_IF  : 'END-IF' ;
DISPLAY : 'DISPLAY' ;

// Operadores y Tipos
OPERATOR : '>=' | '<=' | '>' | '<' | '=' ;
ID       : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER   : [0-9]+ ;
STRING   : '"' .*? '"' ;

WS       : [ \t\r\n]+ -> skip ;