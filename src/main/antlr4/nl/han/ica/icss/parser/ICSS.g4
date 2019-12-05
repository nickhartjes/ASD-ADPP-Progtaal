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
stylesheet
    : variableAssignment* stylerule* EOF;

stylerule
    : selector styleruleScope;

styleruleBody
    : styleruleStatement*;

styleruleStatement
    : variableAssignment
    | declaration
    | ifClause;

styleruleScope
    : OPEN_BRACE styleruleBody CLOSE_BRACE;

// Selectors
selector
    : classSelector
    | idSelector
    | tagSelector;
classSelector
    : CLASS_IDENT;
idSelector
    : ID_IDENT;
tagSelector
    : LOWER_IDENT;

// Declaration
declaration
    : propertyName COLON expression SEMICOLON;

propertyName
    : LOWER_IDENT;

// Literals
literal
    : pixelLiteral
    | percentageLiteral
    | colorLiteral
    | scalarLiteral
    | boolLiteral
    | variableReference;

pixelLiteral
    : PIXELSIZE;
percentageLiteral
    : PERCENTAGE;
colorLiteral
    : COLOR;
scalarLiteral
    : SCALAR;
boolLiteral
    : TRUE | FALSE;

// Variable
variableAssignment
    : variableReference ASSIGNMENT_OPERATOR expression SEMICOLON;
variableReference
    : CAPITAL_IDENT;

// Expressions
expression
    : left=expression multiplyOperation right=expression
    | left=expression substractOperation right=expression
    | left=expression addOperation right=expression
    | ifClause
    | variableReference
    | literal
    | variableAssignment;

// If
ifClause
    : IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE styleruleScope;

// Operators
multiplyOperation
    : MUL;
addOperation
    : PLUS;
substractOperation
    : MIN;
