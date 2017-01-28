package com.company;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.*;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor {
    public static String code;
    CodeArea codeArea = new CodeArea();
    Stage primaryStage = new Stage();
    static String[] KEYWORDS = {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "System",
            "BufferedReader", "InputStreamReader"
    };


    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );
    private String sampleCode = "";
    private String string;
    int i = 0, j = 0;
    private String words = "";

    public void start() {
        Popup popup = new Popup();
        codeArea.setPopupWindow(popup);
        codeArea.setPopupAlignment(PopupAlignment.SELECTION_BOTTOM_CENTER);
        codeArea.setPopupAnchorOffset(new Point2D(0, 0));
        VBox vBox = new VBox(codeArea);
        VBox.setVgrow(codeArea, Priority.ALWAYS);
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
        /*codeArea.setOnMousePressed(e -> {
            System.out.println("");
            if (e.isPrimaryButtonDown()) {

            } else {
                Stage stage = new Stage();
                TextField input = new TextField();
                VBox vb = new VBox();
                stage.setX(e.getSceneX());
                stage.setY(e.getSceneY());
                vb.getChildren().addAll(input);
                stage.setScene(new Scene(vb,300,78));
                stage.initStyle(StageStyle.UNDECORATED);
                String[] words = {
                        "abstract", "assert", "boolean", "break", "byte",
                        "case", "catch", "char", "class", "const",
                        "continue", "default", "do", "double", "else",
                        "enum", "extends", "final", "finally", "float",
                        "for", "goto", "if", "implements", "import",
                        "instanceof", "int", "interface", "long", "native",
                        "new", "package", "private", "protected", "public",
                        "return", "short", "static", "strictfp", "super",
                        "switch", "synchronized", "this", "throw", "throws",
                        "transient", "try", "void", "volatile", "while","System"
                };
                TextFields.bindAutoCompletion(input, words);
                input.setOnKeyPressed(er->{
                    if(er.getCode()== KeyCode.ENTER){
                        stage.close();
                        codeArea.insertText(codeArea.getCaretPosition(), input.getText());
                    }
                });
                stage.show();
            }
        });

        */
        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            int f = 0;
            String spaces = "";
            VBox vBox1 = new VBox();

            @Override
            public void handle(KeyEvent e) {
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

                if (e.getCode() == KeyCode.SPACE && e.isControlDown()) {
                    int offset = codeArea.getCaretPosition();
                    TwoDimensional.Position pos = codeArea.offsetToPosition(offset, null);
                    int position = codeArea.getCaretPosition();
                    int end = position;
                    int caret = pos.getMinor();
                    caret--;
                    System.out.println("Caret is at " + caret);
                    String textString = "";
                    String codeAreaText = codeArea.getParagraph(codeArea.getCurrentParagraph()).getText();
                    boolean doSomething = true;
                    while (codeAreaText.charAt(caret) != ' ') {
                        textString += codeAreaText.charAt(caret);
                        caret--;
                        end--;
                    }
                    textString = new StringBuilder(textString).reverse().toString();
                    System.out.println(codeArea.getText().substring(end, position));
                    System.out.println("Start was " + end + " and end was " + position);
                    if (textString.startsWith("BufferedRe")) {
                        codeArea.replaceText(end, position, "BufferedReader read = new BufferedReader(input);");
                    } else if (textString.startsWith("InputStr")) {
                        codeArea.replaceText(end, position, "InputStreamReader input = new InputStreamReader(System.in);");
                    } else if (textString.equalsIgnoreCase("SOPLN")) {
                        codeArea.replaceText(end, position, "System.out.println();");
                        codeArea.moveTo(codeArea.getCaretPosition() - 2);
                    } else if (textString.equalsIgnoreCase("sout")) {
                        codeArea.replaceText(end, position, "System.out.println();");
                        codeArea.moveTo(codeArea.getCaretPosition() - 2);
                    } else if (textString.equalsIgnoreCase("SOP")) {
                        codeArea.replaceText(end, position, "System.out.print();");
                        codeArea.moveTo(codeArea.getCaretPosition() - 2);
                    } else if (textString.startsWith("for")) {
                        Pattern pattern = Pattern.compile("\\d*,\\d*,\\d*");
                        Matcher matcher = pattern.matcher(textString);

                        if (matcher.find()) {
                            String s = matcher.group(0);
                            System.out.println(s);
                            pattern = null;
                            matcher = null;
                            pattern = Pattern.compile("\\d*,");
                            matcher = pattern.matcher(s);
                            String s2 = "";
                            String s1 = "";
                            String s3 = "";
                            if (matcher.find()) {
                                s1 = matcher.group(0);
                                System.out.println(s1);
                            } else {

                            }
                            pattern = null;
                            matcher = null;
                            pattern = Pattern.compile(",\\d*,");
                            matcher = pattern.matcher(s);
                            if (matcher.find()) {
                                s2 = matcher.group(0);
                                System.out.println(s2);
                            } else {

                            }

                            pattern = null;
                            matcher = null;
                            pattern = Pattern.compile(",\\d*$");
                            matcher = pattern.matcher(s);
                            if (matcher.find()) {
                                s3 = matcher.group(0);
                                System.out.println(s3);
                            } else {

                            }
                            s1 = s1.replace(",", "");
                            s2 = s2.replace(",", "");
                            s3 = s3.replace(",", "");

                            codeArea.replaceText(end, position, "for(int i=" + s1 + ";i<=" + s2 + ";i=i+" + s3 + ")" + " {}");
                            codeArea.moveTo(codeArea.getCaretPosition() - 1);
                        } else {
                            codeArea.replaceText(end, position, "for(int i=1;i<=10;i++) {}");
                            codeArea.moveTo(codeArea.getCaretPosition() - 1);
                        }
                    }


                }
                if (e.getCode() == KeyCode.ENTER && e.isControlDown()) {
                    for (String commonString : KEYWORDS) {
                        String words = codeArea.getSelectedText();
                        if (commonString.contains(words)) {
                            System.out.println(words);
                            if (words == null || words.equals(" ") || words.matches("\\s+") || codeArea.getSelectedText().isEmpty()) {
                                f = 0;
                                System.out.println("Word is null");
                            } else {
                                f++;
                                words = "";
                                Button button = new Button(commonString);
                                button.setOnAction(err -> {
                                    System.out.println(codeArea.getCaretPosition());
                                    codeArea.replaceText(codeArea.getSelection().getStart(), codeArea.getSelection().getEnd(), commonString);
                                    vBox1.getChildren().removeAll(button);
                                    popup.getContent().removeAll(vBox1);
                                    popup.hide();
                                });
                                vBox1.getChildren().add(button);
                            }
                        } else {

                        }
                    }

                    if (f == 0) {
                        Button bb = new Button("No Result");
                        bb.setOnAction(errr -> {
                            popup.getContent().remove(vBox1);
                            vBox1.getChildren().remove(bb);
                            popup.hide();
                        });
                        vBox1.getChildren().add(bb);


                    }

                    popup.getContent().add(vBox1);
                    popup.show(primaryStage);
                } else if (e.getCode() == KeyCode.ENTER) {
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

                if (e.getCode() == KeyCode.BACK_SLASH && e.isControlDown() && e.isShiftDown()) {
                    String tempText = codeArea.getSelectedText();
                    codeArea.replaceText(codeArea.getSelection().getStart(), codeArea.getSelection().getEnd(), "/*" + tempText + "*/");
                }

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
        bp.setCenter(vBox);
        Scene scene = new Scene(bp, 760, 400);
        codeArea.setId("CodeArea");
        scene.getStylesheets().add("java-keywords.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Programmer Window");
        primaryStage.show();
    }

    public void setText(String data) {
        if (data.trim().length() > 0) {
            sampleCode = data;
        } else
            sampleCode = String.join("\n",
                    "class Apples{",
                    "    public static void main(String args[]){",
                    "       //Type Your Code Here",
                    "   }",
                    "}"
            );
    }

    private void giveText() {
        Main.writeText(codeArea.getText());
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
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


}