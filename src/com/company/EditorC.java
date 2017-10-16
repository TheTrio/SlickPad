package com.company;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.fxmisc.richtext.*;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorC {
    public static String code;
    CodeArea codeArea = new CodeArea();

    Stage primaryStage = new Stage();
    static String[] KEYWORDS = {
            "auto", "double", "include","int", "struct", "break", "else", "long", "switch", "case",
            "enum", "register", "typedef", "char", "extern", "return", "union", "const",
            "float", "short", "unsigned", "continue", "for", "signed", "void", "default",
            "goto", "sizeof", "volatile", "do", "if", "static", "while", "printf"
    };


    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String NUMBER_PATTERN = "\\d";
    private static final String CONST_PATTERN  = "\\b[A-Z].*?\\b";
    private static final String CHARACTER_PATTERN = "'[.*]'";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
                    + "|(?<CHAR>" + CHARACTER_PATTERN + ")"
                    + "|(?<CONST>" + CONST_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"

    );

    private static String sampleCode = String.join("\n", new String[]{
            "#include <stdio.h>",
            "int main(){",
            "    printf(\"Hello SlickPad\");",
            "    return 0;",
            "}",

    });
    int i = 0, j = 0;

    private String string;

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("NUMBER") !=null ? "num" :
                                    matcher.group("CHAR") !=null ? "charac" :
                                            matcher.group("CONST") !=null ? "const" :
                                                    matcher.group("PAREN") != null ? "paren" :
                                                            matcher.group("BRACE") != null ? "brace" :
                                                                    matcher.group("BRACKET") != null ? "bracket" :
                                                                            matcher.group("SEMICOLON") != null ? "semicolon" :
                                                                                    matcher.group("STRING") != null ? "string" :
                                                                                            matcher.group("COMMENT") != null ? "comment" :
                                                                                                    null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public void setText(String data) {
        if (data.trim().length() > 0) {
            sampleCode = data;
        } else
            sampleCode = String.join("\n",
                    "#include <stdio.h>",
                    "int main(){",
                    "    printf(\"Hello World\");",
                    "    return 0;",
                    "}"
            );
    }

    private void giveText() {
        Main.writeText(codeArea.getText());
    }

    public void start() {

        codeArea.setId("CodeArea");
        codeArea.setOnKeyTyped(e -> {
            if (e.getCharacter().equals("\"")) {
                codeArea.insertText(codeArea.getCaretPosition(), "\"");
                codeArea.moveTo(codeArea.getCaretPosition() - 1);
                e.consume();
            }
            if (e.getCharacter().equals("(")) {
                codeArea.insertText(codeArea.getCaretPosition(), ")");
                codeArea.moveTo(codeArea.getCaretPosition() - 1);
                e.consume();
            }

            if (e.getCharacter().equals("{")) {
                codeArea.insertText(codeArea.getCaretPosition(), "}");
                codeArea.moveTo(codeArea.getCaretPosition() - 1);

                e.consume();
            }
            if (e.getCharacter().equals("[")) {
                codeArea.insertText(codeArea.getCaretPosition(), "]");
                codeArea.moveTo(codeArea.getCaretPosition() - 1);
                e.consume();
            }


        });
        codeArea.setStyle("-fx-font-size: 15pt;");
        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            String spaces = "";

            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.ENTER) {
                    int offset = codeArea.getCaretPosition();
                    TwoDimensional.Position pos = codeArea.offsetToPosition(offset, null);
                    int paralast = pos.getMajor();

                    for (int i = 0; i < codeArea.getParagraph(paralast).length(); i++) {
                        if (codeArea.getParagraph(paralast).charAt(i) == ' ') {
                            spaces += codeArea.getParagraph(paralast).charAt(i);
                        } else {
                            break;
                        }
                    }
                    e.consume();
                    codeArea.insertText(codeArea.getCaretPosition(), "\n" + spaces);
                    spaces = "";


                }
                if (e.getCode() == KeyCode.BACK_SPACE) {
                    int offset = codeArea.getCaretPosition();
                    TwoDimensional.Position pos = codeArea.offsetToPosition(offset, null);


                    if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("{}")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor() - 1) == '}') {
                            System.out.println(codeArea.getCaretPosition());
                            codeArea.replaceText(codeArea.getCaretPosition() - 2, codeArea.getCaretPosition(), "");
                            e.consume();
                        }
                    }
                    if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("[]")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor() - 1) == ']') {
                            System.out.println(codeArea.getCaretPosition());
                            codeArea.replaceText(codeArea.getCaretPosition() - 2, codeArea.getCaretPosition(), "");
                            e.consume();
                        }
                    }
                    if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("()")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor() - 1) == ')') {
                            System.out.println(codeArea.getCaretPosition());
                            codeArea.replaceText(codeArea.getCaretPosition() - 2, codeArea.getCaretPosition(), "");
                            e.consume();
                        }
                    }


                }
                if (e.getCode() == KeyCode.TAB) {
                    String s = "    ";
                    codeArea.insertText(codeArea.getCaretPosition(), s);
                    e.consume();
                }



/*                if(e.getCode()==KeyCode.ENTER && e.isControlDown()){

                    string = codeArea.getText(codeArea.getCurrentParagraph());
                    if(string.contains("<") && string.contains(">")){
                        if(!string.contains("/")) {
                            string = string.replaceAll("<", "");
                            string = string.replaceAll(">", "");
                            string = "\n" + "\n" + "</" + string + ">";

                            codeArea.insertText(codeArea.getCaretPosition(),string);

                    }
                }
                */

                //}
            }
        });
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .subscribe(change -> {
                    codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
                });
        codeArea.replaceText(0, 0, sampleCode);
        BorderPane bp = new BorderPane();
        MenuBar mb = new MenuBar();
        Menu File = new Menu("File");
        MenuItem get = new MenuItem("Transfer Code To Editor");
        get.setOnAction(e -> {
            giveText();
        });
        File.getItems().add(get);
        mb.getMenus().addAll(File);
        bp.setTop(mb);
        bp.setCenter(codeArea);
        Scene scene = new Scene(bp, 760, 400);
        scene.getStylesheets().add("java-keywords.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Programmer Window");
        primaryStage.show();
    }
}