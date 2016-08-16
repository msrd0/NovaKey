package viviano.cantu.novakey.controller.touch;

import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;

/**
 * Created by Viviano on 6/12/2016.
 */
public interface TouchHandler {

    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event current touch event
     * @param controller controller used for context
     * @param manager used this to switch handler
     * @return true to continue action, false otherwise
     */
    boolean handle(MotionEvent event, Controller controller, HandlerManager manager);
}
