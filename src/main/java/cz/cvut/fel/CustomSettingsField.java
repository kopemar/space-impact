package cz.cvut.fel;

import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class CustomSettingsField {
    private static final int PADDING = 3;
    private String hint;
    private int x;
    private int y;
    private int height;
    private TextField textField;
    private Group group;
    private Text hintText;
    private Rectangle hintBg = new Rectangle();
    private Color bg;

    CustomSettingsField(String lblText, String hint, int x, int y, int height, int lblWidth, Group group, Color bg) {
        this.hint = hint;
        this.x = x;
        this.y = y;
        this.height = height;
        this.group = group;
        this.bg = bg;
        Text label = new Text(x, y + Math.floorDiv(height, 2), lblText);

        label.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> showHint(true));
        label.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> showHint(false));
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> focusOnTextField());

        this.textField = new TextField();
        textField.setLayoutX(x + lblWidth);
        textField.setLayoutY(y);

        group.getChildren().add(label);
        group.getChildren().add(textField);
    }

    public String getHint() {
        return hint;
    }

    TextField getTextField() {
        return textField;
    }

    private void showHint(boolean show) {
        if (show) {
            hintText = new Text(x, y+height, hint);
            hintBg.setX(x - PADDING);
            hintBg.setY(y+height*0.5+PADDING*2);
            hintBg.setWidth(hintText.getLayoutBounds().getWidth() + PADDING*2);
            hintBg.setHeight(hintText.getLayoutBounds().getHeight() + PADDING*2);
            hintBg.setFill(bg);
            hintBg.setStroke(Color.rgb(100, 100, 100));

            group.getChildren().addAll(hintBg, hintText);
        }
        else {
            group.getChildren().removeAll(hintText, hintBg);
        }
    }

    private void focusOnTextField() {
        textField.requestFocus();
    }

}
