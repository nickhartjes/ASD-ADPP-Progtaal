grammar ICSS;

//--- LEXER: ---
// IF support:
IF: 'if';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';

//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---

stylesheet          :  variabele* stylerule* EOF;

stylerule           : (class | id | tag ) OPEN_BRACE declaration* CLOSE_BRACE;

// Selectors
class               : CLASS_IDENT ;
id                  : ID_IDENT ;
tag                 : LOWER_IDENT;

// Declaration
declaration         : declarationName COLON declarationValue SEMICOLON | if;
declarationName     : LOWER_IDENT;
declarationValue    : value | multiply | sum | subtraction;

variabele           : variabeleName ASSIGNMENT_OPERATOR value SEMICOLON;
variabeleName       : CAPITAL_IDENT;

value               : PIXELSIZE | PERCENTAGE | COLOR | SCALAR | boolean | variabeleName ;
boolean             : TRUE | FALSE;

// Operations
multiply            : value MUL value | value MUL (sum | subtraction | multiply);
sum                 : value PLUS value | value PLUS (sum | subtraction | multiply);
subtraction         : value MIN value | value MIN (sum | subtraction | multiply);

// Statements
if                  : IF BOX_BRACKET_OPEN (boolean | variabeleName)  BOX_BRACKET_CLOSE OPEN_BRACE declaration* CLOSE_BRACE;
