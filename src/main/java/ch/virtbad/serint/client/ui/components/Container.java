package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.ui.Context;
import ch.virtbad.serint.client.ui.components.base.Component;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Container extends Component {

    private boolean initialized;
    private List<Component> children;

    @Setter
    private boolean visible = true;

    public Container() {
        children = new ArrayList<>();
    }

    public void addComponent(Component component){
        if (initialized) { // Initialize components if needed
            component.setContext(context);
            component.init();
        }

        children.add(component);
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        for (Component child : children) {
            child.setContext(context);
        }
    }

    @Override
    public void init() {
        initialized = true;

        for (Component child : children) {
            child.init();
        }
    }

    @Override
    public void update(float delta) {
        if (!visible) return;
        for (Component child : children) {
            child.update(delta);
        }
    }

    @Override
    public void draw() {
        if (!visible) return;
        for (Component child : children) {
            child.draw();
        }
    }
}
