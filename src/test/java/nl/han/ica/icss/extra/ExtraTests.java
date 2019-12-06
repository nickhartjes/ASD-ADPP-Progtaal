package nl.han.ica.icss.extra;

import nl.han.ica.icss.Differ;
import nl.han.ica.icss.Pipeline;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExtraTests {

    private Pipeline parseCheck(final Path path, final String astExpected, boolean isValid) throws IOException {
        final String inputTest = Files.readString(path);
        final Pipeline pipeline = new Pipeline();
        pipeline.parseString(inputTest);
        boolean check = pipeline.check();
        boolean isChecked = pipeline.isChecked();
        Differ.printErrors(pipeline);
        Differ.diffMatch(inputTest, astExpected, pipeline.getAST().toString());
        assertEquals(astExpected, pipeline.getAST().toString());
        assertTrue(pipeline.isParsed());
        assertEquals(isValid, check);
        assertEquals( isValid, isChecked);
        return pipeline;
    }

    private void parseCheckTransformGenerate(final Path path, final String astExpected, final String cssExpected) throws IOException {
        final Pipeline pipeline = parseCheck(path, astExpected, true);
        Differ.printErrors(pipeline);
        pipeline.transform();
        System.out.println(pipeline.generate());
        assertTrue(pipeline.isTransformed());
        assertTrue(pipeline.getErrors().isEmpty());
        assertEquals(cssExpected, pipeline.generate());
        assertTrue(pipeline.getErrors().isEmpty());
    }

    @Test
    void testEX01T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("EX01-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector a|][SelectorSeperator , |][TagSelector p|][Declaration|[Property: (width)|][Pixel literal (10)|]]]]";
        final String cssExpected = "a, p {\n" +
                "  width: 10px;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }

    @Test
    void testEX01T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("EX01-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector a|][SelectorSeperator , |][TagSelector p|][SelectorSeperator , |][IdSelector #class|][SelectorSeperator , |][ClassSelector .perfect|][Declaration|[Property: (width)|][Pixel literal (10)|]]]]";
        final String cssExpected = "a, p, #class, .perfect {\n" +
                "  width: 10px;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }

    @Test
    void testEX02T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("EX02-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (UseLinkColor)|[VariableReference (UseLinkColor)|][Bool Literal (FALSE)|]][Stylerule|[TagSelector a|][If_Clause|[VariableReference (UseLinkColor)|][Declaration|[Property: (color)|][Color literal (#000000)|]]][Else_Clause|[Declaration|[Property: (color)|][Color literal (#111111)|]]]]]";
        final String cssExpected = "a {\n" +
                "  color: #111111;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }

    @Test
    void testEX02T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("EX02-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (UseLinkColor)|[VariableReference (UseLinkColor)|][Bool Literal (TRUE)|]][Stylerule|[TagSelector a|][If_Clause|[VariableReference (UseLinkColor)|][Declaration|[Property: (color)|][Color literal (#000000)|]]][Else_Clause|[Declaration|[Property: (color)|][Color literal (#111111)|]]]]]";
        final String cssExpected = "a {\n" +
                "  color: #000000;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }



}
