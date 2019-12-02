package nl.han.ica.icss.parser;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:
    private AST ast;

    //Use this to keep track of the parent nodes when recursively traversing the ast
    // Removed stack: Deque: A more complete and consistent set of LIFO stack operations is provided by the Deque interface and its implementations, which should be used in preference to Stack
    private Deque<ASTNode> currentContainer;

    public ASTListener() {
        ast = new AST();
        currentContainer = new ArrayDeque<>();
    }

    public AST getAST() {
        return ast;
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        this.currentContainer.push(this.ast.root);
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        this.addToTree(new Stylerule());
    }

    @Override
    public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
        this.addChildToParent(new ClassSelector(ctx.getText()));
    }

    @Override
    public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
        this.addChildToParent(new IdSelector(ctx.getText()));
    }

    @Override
    public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
        this.addChildToParent(new TagSelector(ctx.getText()));
    }

    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        // Check if the first in the stack is also a declaration, if so pop it.
        ASTNode parent = this.currentContainer.peek();
        if (parent instanceof Declaration) {
            this.currentContainer.pop();
        }
        Declaration declaration = new Declaration();
        this.addChildToParent(declaration);
        this.currentContainer.push(declaration);
    }

    @Override
    public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
        this.addChildToParent(new PropertyName(ctx.getText()));
    }

    @Override
    public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        this.addChildToParent(new PixelLiteral(ctx.getText()));
    }

    @Override
    public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        this.addChildToParent(new PercentageLiteral(ctx.getText()));
    }

    @Override
    public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        this.addChildToParent(new ColorLiteral(ctx.getText()));
    }

    @Override
    public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        this.addChildToParent(new ScalarLiteral(ctx.getText()));
    }

    @Override
    public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        this.addChildToParent(new BoolLiteral(ctx.getText()));
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        ASTNode parent = this.currentContainer.peek();
        if (parent instanceof VariableAssignment) {
            this.currentContainer.pop();
        }
        VariableAssignment variableAssignment = new VariableAssignment();
        this.addChildToParent(variableAssignment);
        this.pushToContainer(variableAssignment);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        ASTNode node = this.currentContainer.peek();
        if (node instanceof VariableAssignment) {
            this.ast.addVariable(((VariableAssignment) node).name, ((VariableAssignment) node).expression );
        }
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        this.addChildToParent(new VariableReference(ctx.getText()));
    }

    @Override
    public void enterMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
        MultiplyOperation multiplyOperation = new MultiplyOperation();
        this.addChildToParent(multiplyOperation);
        this.pushToContainer(multiplyOperation);
    }

    @Override
    public void enterAddOperation(ICSSParser.AddOperationContext ctx) {
        AddOperation addOperation = new AddOperation();
        this.addChildToParent(addOperation);
        this.pushToContainer(addOperation);
    }

    @Override
    public void enterSubstractOperation(ICSSParser.SubstractOperationContext ctx) {
        SubtractOperation subtractOperation = new SubtractOperation();
        this.addChildToParent(subtractOperation);
        this.pushToContainer(subtractOperation);
    }

    @Override
    public void enterIfClause(ICSSParser.IfClauseContext ctx) {
        ASTNode parent = this.currentContainer.peek();
        if (parent instanceof Declaration || parent instanceof VariableAssignment) {
            this.currentContainer.pop();
        }

        IfClause ifClause = new IfClause();
        this.addChildToParent(ifClause);
        this.pushToContainer(ifClause);
    }

    private void addToTree(ASTNode node) {
        this.pushToContainer(node);
        this.ast.root.addChild(node);
    }

    private void addChildToParent(ASTNode astNode) {
        if (this.currentContainer.peek() != null) {
            this.currentContainer.peek().addChild(astNode);
        }
    }

    private void pushToContainer(ASTNode node) {
        this.currentContainer.push(node);
    }
}
