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

stylerule           : (className | idName | tagName) OPEN_BRACE declaration* CLOSE_BRACE;

// Selectors
className           : CLASS_IDENT ;
idName              : ID_IDENT ;
tagName             : LOWER_IDENT;

// Declaration
declaration         : declarationName COLON declarationValue SEMICOLON;

declarationName     : LOWER_IDENT;
declarationValue    : value | multiply | sum | subtraction;

variabele           : variabeleName ASSIGNMENT_OPERATOR value SEMICOLON;

variabeleName       : CAPITAL_IDENT;

value               : PIXELSIZE | PERCENTAGE | COLOR | SCALAR | TRUE | FALSE | variabeleName ;

multiply            : value MUL value | value MUL (sum | subtraction | multiply);

sum                 : value PLUS value | value PLUS (sum | subtraction | multiply);

subtraction         : value MIN value | value MIN (sum | subtraction | multiply);
