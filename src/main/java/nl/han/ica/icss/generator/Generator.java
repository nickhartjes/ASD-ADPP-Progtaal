package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;

import java.util.Stack;

public class Generator {

	private Stack<ASTNode> currentContainer;

	public String generate(AST ast) {
		this.currentContainer = new Stack<>();
		this.currentContainer.push(ast.root);
		return get(ast.root);
	}

	private String get(ASTNode node){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(node.getCssString(this.currentContainer.peek()));

		for(ASTNode x :node.getChildren()){
			this.currentContainer.push(x);
			stringBuilder.append(this.get(this.currentContainer.peek()));
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}
}
