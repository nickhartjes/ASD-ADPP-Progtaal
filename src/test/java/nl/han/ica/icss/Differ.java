package nl.han.ica.icss;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.checker.SemanticError;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.LinkedList;

public class Differ {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE = "\u001B[37m";

    static public void diffMatch(String file, String expected, String generated) {
        DiffMatchPatch dmp = new DiffMatchPatch();
        StringBuilder stringBuilder = new StringBuilder();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(expected, generated);
        dmp.diffCleanupSemantic(diff);

        stringBuilder.append("File: \n");
        stringBuilder.append(file);
        stringBuilder.append("Expected: \n");
        stringBuilder.append(expected);
        stringBuilder.append("\n\n");
        stringBuilder.append("Generated: \n");
        stringBuilder.append(generated);
        stringBuilder.append("\n\n");
        stringBuilder.append("Diff: \n");
        stringBuilder.append(ANSI_RESET);
        for (DiffMatchPatch.Diff item : diff) {
            switch (item.operation) {
                case INSERT:
                    stringBuilder.append(ANSI_GREEN);
                    break;
                case DELETE:
                    stringBuilder.append(ANSI_RED);
                    break;
                default:
                    stringBuilder.append(ANSI_WHITE);
            }
            stringBuilder.append(item.text);
        }
        stringBuilder.append(ANSI_RESET);
        stringBuilder.append("\n");
        System.out.println(stringBuilder.toString());
    }

    static public void printErrors(Pipeline pipeline){
        for (String error : pipeline.getErrors()){
            System.out.println(error);
        }
    }
}
