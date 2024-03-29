package nl.han.ica.icss.parser;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.SelectorSeperator;
import nl.han.ica.icss.ast.selectors.TagSelector;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

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
        ASTNode parent = this.currentContainer.peek();
        while (parent instanceof Declaration || parent instanceof VariableAssignment) {
            this.currentContainer.pop();
            parent = this.currentContainer.peek();
        }
        Declaration declaration = new Declaration();
        this.addChildToParent(declaration);
        this.pushToContainer(declaration);
    }

    @Override
    public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
        this.addChildToParent(new PropertyName(ctx.getText()));
    }

    @Override
    public void enterLiteral(ICSSParser.LiteralContext ctx) {
        ASTNode parent = this.currentContainer.peek();
        while (parent instanceof Operation && (((Operation) parent).lhs != null && ((Operation) parent).rhs != null)) {
            this.currentContainer.pop();
            parent = this.currentContainer.peek();
        }
    }

    @Override
    public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        PixelLiteral pixelLiteral = new PixelLiteral(ctx.getText());
        this.addChildToParent(pixelLiteral);
    }

    @Override
    public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        PercentageLiteral pixelLiteral = new PercentageLiteral(ctx.getText());
        this.addChildToParent(pixelLiteral);
    }

    @Override
    public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        ColorLiteral colorLiteral = new ColorLiteral(ctx.getText());
        this.addChildToParent(colorLiteral);
    }

    @Override
    public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        ScalarLiteral scalarLiteral = new ScalarLiteral(ctx.getText());
        this.addChildToParent(scalarLiteral);
    }

    @Override
    public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        boolean value = ctx.children.stream().anyMatch(x -> x.getText().equals("TRUE"));

        BoolLiteral boolLiteral = new BoolLiteral(value);
        if (ctx.children.stream().anyMatch(x -> x instanceof ICSSParser.NotContext)) {
            boolLiteral = new BoolLiteral(value, true);
        }
        this.addChildToParent(boolLiteral);
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
        super.exitVariableAssignment(ctx);

        // Add variabel to AST list
        ASTNode node = this.currentContainer.peek();
        if (node instanceof VariableAssignment) {
            this.ast.addVariable(((VariableAssignment) node).name, ((VariableAssignment) node).expression);
        } else if (node instanceof Operation) {
            this.currentContainer.pop();
            this.exitVariableAssignment(ctx);
        }
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        VariableReference variableReference = new VariableReference(ctx.getText());
        this.addChildToParent(variableReference);
    }

    @Override
    public void enterExpression(ICSSParser.ExpressionContext ctx) {
        List children = ctx.children;
        if (children.size() > 1) {
            if (children.get(1) instanceof ICSSParser.AddOperationContext) {
                AddOperation multiplyOperation = new AddOperation();
                this.addChildToParent(multiplyOperation);
                this.pushToContainer(multiplyOperation);
            } else if (children.get(1) instanceof ICSSParser.MultiplyOperationContext) {
                MultiplyOperation multiplyOperation = new MultiplyOperation();
                this.addChildToParent(multiplyOperation);
                this.pushToContainer(multiplyOperation);
            } else if (children.get(1) instanceof ICSSParser.SubstractOperationContext) {
                SubtractOperation multiplyOperation = new SubtractOperation();
                this.addChildToParent(multiplyOperation);
                this.pushToContainer(multiplyOperation);
            }
        }
    }

    @Override
    public void enterSelectorSeperator(ICSSParser.SelectorSeperatorContext ctx) {
        super.enterSelectorSeperator(ctx);
        this.addChildToParent(new SelectorSeperator());
    }

    @Override
    public void enterIfClause(ICSSParser.IfClauseContext ctx) {
        // Remove all parents till the parent is a stylerule or ifclause
        while (true) {
            ASTNode parent = this.currentContainer.peek();
            if (parent instanceof Stylerule || parent instanceof IfClause) {
                break;
            }
            this.currentContainer.pop();
        }
        IfClause ifClause = new IfClause();
        this.addChildToParent(ifClause);
        this.pushToContainer(ifClause);
    }

    @Override
    public void enterElseClause(ICSSParser.ElseClauseContext ctx) {
        while (true) {
            ASTNode parent = this.currentContainer.peek();
            if (parent instanceof IfClause) {
                break;
            }
            this.currentContainer.pop();
        }

        ElseClause elseClause = new ElseClause();
        this.currentContainer.pop();
        this.addChildToParent(elseClause);
        this.pushToContainer(elseClause);
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
