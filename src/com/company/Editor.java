package com.company;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.util.function.IntFunction;
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
            "transient", "try", "void", "volatile", "while", "String"

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
    private static final String CHARACTER_PATTERN = "\\b([A-Za-z]*)\\(.*\\)";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<CHAR>" + CHARACTER_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
                    + "|(?<CONST>" + CONST_PATTERN + ")"
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
        IntFunction<Node> numberFactory = LineNumberFactory.get(codeArea);
        IntFunction<Node> arrowFactory = new ArrowFactory(codeArea.currentParagraphProperty());
        IntFunction<Node> graphicFactory = line -> {
            HBox hbox = new HBox(
                    numberFactory.apply(line),
                    arrowFactory.apply(line));
            hbox.setAlignment(Pos.CENTER_LEFT);
            return hbox;
        };
        codeArea.setParagraphGraphicFactory(graphicFactory);
        VBox vBox = new VBox(codeArea);
        VBox.setVgrow(codeArea, Priority.ALWAYS);
        codeArea.setOnKeyTyped(e -> {
            if (e.getCharacter().equals("\"")) {
                codeArea.insertText(codeArea.getCaretPosition(), "\"");
                codeArea.moveTo(codeArea.getCaretPosition() - 1);
                e.consume();
            }
            else if(e.getCharacter().equals("<")){
                codeArea.insertText(codeArea.getCaretPosition(), ">");
                codeArea.moveTo(codeArea.getCaretPosition() - 1);
                e.consume();
            }
            else if (e.getCharacter().equals("(")) {
                codeArea.insertText(codeArea.getCaretPosition(), ")");
                codeArea.moveTo(codeArea.getCaretPosition() - 1);
                e.consume();
            }

            else if (e.getCharacter().equals("{")) {
                codeArea.insertText(codeArea.getCaretPosition(), "}");
                codeArea.moveTo(codeArea.getCaretPosition() - 1);

                e.consume();
            }
            else if (e.getCharacter().equals("[")) {
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
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor() - 1) == '{') {
                            if(codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor()) == '}') {
                                System.out.println("Trying new");
                                codeArea.replaceText(codeArea.getCaretPosition() - 1, codeArea.getCaretPosition()+1, "");
                                e.consume();
                            }
                        }
                    }else if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("[]")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor() - 1) == '[') {
                            if(codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor()) == ']') {
                                System.out.println("Trying new");
                                codeArea.replaceText(codeArea.getCaretPosition() - 1, codeArea.getCaretPosition()+1, "");
                                e.consume();
                            }
                        }
                    }else if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("\"\"")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor() - 1) == '"') {
                            if(codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor()) == '"') {
                                System.out.println("Trying new");
                                codeArea.replaceText(codeArea.getCaretPosition() - 1, codeArea.getCaretPosition()+1, "");
                                e.consume();
                            }
                        }
                    }else if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("()")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor() - 1) == '(') {
                            if(codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor()) == ')') {
                                System.out.println("Trying new");
                                codeArea.replaceText(codeArea.getCaretPosition() - 1, codeArea.getCaretPosition()+1, "");
                                e.consume();
                            }
                        }
                    }else if(codeArea.getParagraph(codeArea.getCurrentParagraph()).getText().trim().isEmpty()){
                        codeArea.replaceText(codeArea.getCaretPosition()-codeArea.getParagraph(codeArea.getCurrentParagraph()).getText().length(), codeArea.getCaretPosition(), "");
                    }else if(codeArea.getParagraph(codeArea.getCurrentParagraph()).getText().endsWith(" ")){
                        String text = codeArea.getParagraph(codeArea.getCurrentParagraph()).getText();
                        int ch = 0;
                        for(int i=text.length()-1;i>=0;i--){
                            if(text.charAt(i)!=' '){
                                break;
                            }else
                                ch++;
                        }
                        codeArea.replaceText(codeArea.getCaretPosition()-ch+1, codeArea.getCaretPosition(), "");
                    }

                }
                if (e.getCode() == KeyCode.TAB) {
                    String s = "    ";
                    codeArea.insertText(codeArea.getCaretPosition(), s);
                    e.consume();
                }

                if(e.getCode()==KeyCode.A && e.isControlDown() && e.isShiftDown()){
                    codeArea.selectLine();
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
                    try {
                        while (codeAreaText.charAt(caret) != ' ') {
                            textString += codeAreaText.charAt(caret);
                            caret--;
                            end--;
                        }
                    }catch (StringIndexOutOfBoundsException erradsk){
                        caret++;
                    }
                    textString = new StringBuilder(textString).reverse().toString();
                    System.out.println(codeArea.getText().substring(end, position));
                    System.out.println("Start was " + end + " and end was " + position);
                    if(textString.equals("class")){
                        codeArea.replaceText(end, position,String.join("\n",
                                "class Apples{",
                                "    public static void main(String args[]){",
                                "       //Type Your Code Here",
                                "    }",
                                "}"
                        ));
                    }
                    else if(textString.equals("main")){
                        codeArea.replaceText(end, position, String.join("\n", "public static void main(String args[]){",
                                                                              "\n    }"
                                ));
                    }else if (textString.startsWith("BufferedRe")) {
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
                    "    }",
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
                            matcher.group("CHAR") !=null ? "charac" :
                            matcher.group("NUMBER") !=null ? "num" :
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


}