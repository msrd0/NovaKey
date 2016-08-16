package viviano.cantu.novakey.controller.actions.typing;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.model.NovaKeyModel;

/**
 * Created by Viviano on 6/20/2016.
 */
public class EnterAction implements Action<Void> {
    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     *
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        InputConnection ic = ime.getCurrentInputConnection();
        if (ic == null)
            return null;
        EditorInfo ei = ime.getCurrentInputEditorInfo();
        int imeAction = ei.imeOptions & EditorInfo.IME_MASK_ACTION;

        if (imeAction == EditorInfo.IME_ACTION_NONE ||
                imeAction == EditorInfo.IME_ACTION_UNSPECIFIED)
            control.fire(new InputAction('\n'));
        else
            ic.performEditorAction(imeAction);

        control.fire(new UpdateShiftAction());
        return null;
    }
}
