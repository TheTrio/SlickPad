package com.company;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.*;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorHtml{
    public static String code;
    CodeArea codeArea = new CodeArea();
    Stage primaryStage = new Stage();
    static String[] KEYWORDS = {
            "accept", "accept-charset", "accesskey", "action", "align",
            "alt", "async", "autocomplete", "autofocus", "autoplay",
            "bgcolor", "border", "challenge", "charset", "checked",
            "cite", "class", "color", "cols", "colspan",
            "content", "contenteditable", "contextmenu", "controls", "coords",
            "data", "data-*", "datetime", "default", "defer",
            "dir", "dirname", "disabled", "download", "draggable",
            "dropzone", "enctype", "for", "form", "formaction",
            "headers", "height", "hidden", "high", "href",
            "hreflang", "http-equiv", "id", "ismap", "keytype","kind",
            "label", "lang", "list", "loop","low", "manifest", "max", "maxlength",
            "media", "method", "min", "multiple", "muted", "name", "novalidate",
            "onabort", "onafterprint", "onbeforeprint", "onbeforeunload",
            "onblur", "oncanplay", "onplaythrough", "onchange", "onclick",
            "oncontextmenu", "oncopy", "oncuechange", "oncut", "ondblclick",
            "ondrag", "ondragend", "ondragenter", "ondragleave", "ondragover",
            "ondragstart", "ondrop", "ondurationchange", "onemptied",
            "onended", "onerror", "onfocus", "onhashchange", "oninput", "oninvalid",
            "onkeydown", "onkeypress", "onkeyup", "onload", "onloadeddata",
            "onloadstart", "onmousedown", "onmousemover", "onmouseout", "onmouseover",
            "onmouseup", "onmousewheel", "onoffline", "ononline", "onpagehide", "onpageshow",
            "onpaste", "onpause", "onplay", "src", "width", "height", "value", "function"
    };




    private static final String KEYWORD_PATTERN = "(?i)\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN =  "<([^\\s\\\\]|\\\\.)*>" + "|" + "<([^\\s]+)";
    private static final String ORG_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";


    private static final String COMMENT_PATTERN = "<!--([^<]|\\\\.)*-->";

    private static final Pattern PATTERN = Pattern.compile(
                    "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                            + "|(?<ORG>" + ORG_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"

    );

    private static String sampleCode = "";

    private String string;
    int i=0,j=0;
    public void start() {
        Popup popup = new Popup();
        codeArea.setPopupWindow(popup);
        codeArea.setPopupAlignment(PopupAlignment.SELECTION_BOTTOM_CENTER);
        codeArea.setPopupAnchorOffset(new Point2D(0, 0));
        VBox vBox = new VBox(codeArea);
        VBox.setVgrow(codeArea, Priority.ALWAYS);
        codeArea.setStyle("-fx-font-size: 15pt;");
        codeArea.setOnKeyTyped(e -> {
            if (e.getCharacter().equals("\"")) {
                codeArea.insertText(codeArea.getCaretPosition(), "\"");
                codeArea.moveTo(codeArea.getCaretPosition()-1);
                e.consume();
            }
            if (e.getCharacter().equals("(")) {
                codeArea.insertText(codeArea.getCaretPosition(), ")");
                codeArea.moveTo(codeArea.getCaretPosition()-1);
                e.consume();
            }

            if (e.getCharacter().equals("{")) {
                codeArea.insertText(codeArea.getCaretPosition(), "}");
                codeArea.moveTo(codeArea.getCaretPosition()-1);

                e.consume();
            }
            if(e.getCharacter().equals("[")){
                codeArea.insertText(codeArea.getCaretPosition(), "]");
                codeArea.moveTo(codeArea.getCaretPosition()-1);
                e.consume();
            }
            if(e.getCharacter().equals("<")){
                codeArea.insertText(codeArea.getCaretPosition(), ">");
                codeArea.moveTo(codeArea.getCaretPosition()-1);
                e.consume();
            }


        });
        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
int f = 0;
            VBox vBox1 = new VBox();
            String spaces = "";
            @Override
            public void handle(KeyEvent e) {

                if(e.getCode()==KeyCode.ENTER && e.isControlDown()){
                    for(String commonString : KEYWORDS){
                        String words = codeArea.getSelectedText();
                        if(commonString.toLowerCase().contains(words.toLowerCase())){
                            System.out.println(words);
                            if(words==null || words.equals(" ") || words.matches("\\s+") || codeArea.getSelectedText().isEmpty()){
                                f = 0;
                                System.out.println("Word is null");
                            }else {
                                f++;
                                words = "";
                                Button button = new Button(commonString);
                                button.setOnAction(err->{
                                    codeArea.replaceText(codeArea.getSelection().getStart(), codeArea.getSelection().getEnd(), commonString);
                                    popup.getContent().removeAll(vBox1);
                                    vBox1.getChildren().clear();
                                    popup.hide();
                                });
                                vBox1.getChildren().add(button);
                            }
                        }else {

                        }
                    }

                    if(f==0){
                        Button bb = new Button("No Result");
                        bb.setOnAction(errr-> {
                            popup.getContent().remove(vBox1);
                            vBox1.getChildren().remove(bb);
                            popup.hide();
                        });
                        vBox1.getChildren().add(bb);



                    }
                    e.consume();
                    popup.getContent().add(vBox1);
                    popup.show(primaryStage);
                }else if(e.getCode()==KeyCode.ENTER){
                    int offset = codeArea.getCaretPosition();
                    TwoDimensional.Position pos = codeArea.offsetToPosition(offset, null);
                    int paralast = pos.getMajor();

                    for(int i=0;i<codeArea.getParagraph(paralast).length();i++){
                        if(codeArea.getParagraph(paralast).charAt(i)==' '){
                            spaces+=codeArea.getParagraph(paralast).charAt(i);
                        }else {
                            break;
                        }
                    }
                    e.consume();
                    codeArea.insertText(codeArea.getCaretPosition(), "\n" + spaces);
                    spaces= "";


                }
                if(e.getCode()==KeyCode.BACK_SPACE){
                    if(popup.isShowing()){
                        popup.hide();
                    }
                    int offset = codeArea.getCaretPosition();
                    TwoDimensional.Position pos = codeArea.offsetToPosition(offset, null);


                    if(codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("{}")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor()-1) == '}'){
                            System.out.println(codeArea.getCaretPosition());
                            codeArea.replaceText(codeArea.getCaretPosition()-2, codeArea.getCaretPosition(), "");
                            e.consume();
                        }
                    }
                    if(codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("[]")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor()-1) == ']'){
                            System.out.println(codeArea.getCaretPosition());
                            codeArea.replaceText(codeArea.getCaretPosition()-2, codeArea.getCaretPosition(), "");
                            e.consume();
                        }
                    }
                    if(codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().contains("()")) {
                        if (codeArea.getParagraphs().get(codeArea.getCurrentParagraph()).getText().charAt(pos.getMinor()-1) == ')'){
                            System.out.println(codeArea.getCaretPosition());
                            codeArea.replaceText(codeArea.getCaretPosition()-2, codeArea.getCaretPosition(), "");
                            e.consume();
                        }
                    }





                }
                System.out.println(e.getCode());
                if (e.getCode() == KeyCode.TAB) {
                    String s = "    ";
                    codeArea.insertText(codeArea.getCaretPosition(), s);
                    e.consume();
                }


int ij = 0;
                int end = 0;
         if(e.getCode()==KeyCode.ENTER && e.isShiftDown()){
             System.out.println("I am here");
                    string = codeArea.getText(codeArea.getCurrentParagraph()-1);
             System.out.println(string);
                    if(string.contains("<") && string.contains(">")){
                        if(!string.contains("/")) {
                            System.out.println(string);
                            string = string.replaceAll("<", "");
                            string = string.replaceAll(">", "");
                            string = string + " ";
                            while(string.charAt(end)!=' '){
                                System.out.println(string.charAt(end));

                                end++;
                            }
                            System.out.println(string);
                            string = string.substring(0, end);
                            System.out.println(string);
                            string = "</" + string + ">";
                            codeArea.insertText(codeArea.getCaretPosition(),string);
                    }
                }


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
        get.setOnAction(e->{
            giveText();
        });
        File.getItems().add(get);
        mb.getMenus().addAll(File);
        bp.setTop(mb);
        bp.setCenter(vBox);
        Scene scene = new Scene(bp, 760,400);
        scene.getStylesheets().add("java-keywords.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Programmer Window");
        primaryStage.show();
    }

    private void giveText() {
        Main.writeText(codeArea.getText());
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("ORG") !=null ? "org":
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("COMMENT") != null ? "comment" :
                                        matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :

                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    public void setText(String data) {
        if(data.trim().length()>0){
            sampleCode = data;
        }else
            sampleCode = String.join("\n",
                    "<html>" ,
                    "</html>"
            );
    }
}