package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.graphics.ResourceHandler;
import lombok.Getter;

public class OptionsButton extends Button{

    @Getter
    private int index;
    private String[] possibilities;

    public OptionsButton(float x, float y, float width, float height, String[] possibilities, int startIndex) {
        super(x, y, width, height, possibilities[startIndex]);

        this.index = startIndex;
        this.possibilities = possibilities;

        setEvent(this::change);
    }

    @Override
    public void init() {
        super.init();

        texture = ResourceHandler.getTextures().get("optionsbutton");
    }

    private void change(){
        index++;
        if (index == possibilities.length) index = 0;

        setText(possibilities[index]);
    }


}
