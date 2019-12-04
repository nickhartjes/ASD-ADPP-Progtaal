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
        while (!this.currentContainer.isEmpty()) {
            ASTNode node = this.currentContainer.peek();
            if (node instanceof Stylerule) {
                break;
            }
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
        if (this.currentContainer.peek() instanceof VariableReference )
            this.currentContainer.pop();
        PixelLiteral pixelLiteral = new PixelLiteral(ctx.getText());
        this.addChildToParent(pixelLiteral);
        this.pushToContainer(pixelLiteral);
    }

    @Override
    public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        if (this.currentContainer.peek() instanceof VariableReference)
            this.currentContainer.pop();
        this.addChildToParent(new PercentageLiteral(ctx.getText()));
        PixelLiteral pixelLiteral = new PixelLiteral(ctx.getText());
        this.addChildToParent(pixelLiteral);
        this.pushToContainer(pixelLiteral);
    }

    @Override
    public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        if (this.currentContainer.peek() instanceof VariableReference)
            this.currentContainer.pop();
        ColorLiteral colorLiteral = new ColorLiteral(ctx.getText());
        this.addChildToParent(colorLiteral);
        this.pushToContainer(colorLiteral);
    }

    @Override
    public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        if (this.currentContainer.peek() instanceof VariableReference)
            this.currentContainer.pop();
        ScalarLiteral scalarLiteral = new ScalarLiteral(ctx.getText());
        this.addChildToParent(scalarLiteral);
        this.pushToContainer(scalarLiteral);
    }

    @Override
    public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        if (this.currentContainer.peek() instanceof VariableReference)
            this.currentContainer.pop();
        BoolLiteral boolLiteral = new BoolLiteral(ctx.getText());
        this.addChildToParent(boolLiteral);
        this.pushToContainer(boolLiteral);
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        while (!this.currentContainer.isEmpty()) {
            ASTNode node = this.currentContainer.peek();
            if (node instanceof Stylesheet || node instanceof Declaration) {
                break;
            }
            this.currentContainer.pop();
        }

        VariableAssignment variableAssignment = new VariableAssignment();
        this.addChildToParent(variableAssignment);
        this.pushToContainer(variableAssignment);
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        VariableReference variableReference = new VariableReference(ctx.getText());
        this.addChildToParent(variableReference);
        this.pushToContainer(variableReference);
    }

    @Override
    public void enterMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
        this.addOperation(new MultiplyOperation());
    }

    //
    @Override
    public void enterAddOperation(ICSSParser.AddOperationContext ctx) {
        this.addOperation(new AddOperation());
    }

    @Override
    public void enterSubstractOperation(ICSSParser.SubstractOperationContext ctx) {
        this.addOperation(new SubtractOperation());
    }

    @Override
    public void enterIfClause(ICSSParser.IfClauseContext ctx) {
        this.currentContainer.pop();
        this.currentContainer.pop();
        ASTNode node = this.currentContainer.peek();
        IfClause ifClause = new IfClause();
        node.addChild(ifClause);
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

    private void addOperation(Expression variableAssignment) {
        ASTNode pop = this.currentContainer.pop();
        variableAssignment.addChild(pop);
        if (this.currentContainer.peek() instanceof AddOperation || this.currentContainer.peek() instanceof SubtractOperation ) {
            Operation operation = (Operation) this.currentContainer.peek();
//            operation.rhs = operation.lhs;
//            operation.lhs = variableAssignment;
            operation.rhs = variableAssignment;
//            variableAssignment = operation;
        }
        this.addChildToParent(variableAssignment);
        this.pushToContainer(variableAssignment);
    }
}
