package viviano.cantu.novakey.settings;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.elements.buttons.Button;
import viviano.cantu.novakey.settings.widgets.BtnPreview;
import viviano.cantu.novakey.settings.widgets.ButtonAddView;

/**
 * Created by Viviano on 6/22/2015.
 */
public class ButtonPreference extends DialogPreference {

    private Context context;

    private BtnPreview preview;
    private ButtonAddView[] addBtns;

    public ButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setDialogLayoutResource(R.layout.button_layout);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        this.context = context;
    }

    @Override
    public void onBindDialogView(View view){
        super.onBindDialogView(view);
        preview = (BtnPreview)view.findViewById(R.id.preview);
        addBtns = new ButtonAddView[3];
        for (int i=1; i<=3; i++) {
            int resId = view.getResources().getIdentifier("add" + i, "id", context.getPackageName());
            final int j = i-1;
            addBtns[j] = (ButtonAddView)view.findViewById(resId);
            addBtns[j].setRadius(view.getResources().getDimension(R.dimen.btn_preview_rad));
            addBtns[j].enableAddIcon();
            addBtns[j].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        preview.addBtn(addBtns[j].btnShape());
                    }
                    return false;
                }
            });
        }
        updateShape(Button.CIRCLE);//at the start

        //shape picker
        ButtonAddView circle = (ButtonAddView)view.findViewById(R.id.circle);
        circle.setRadius(view.getResources().getDimension(R.dimen.btn_preview_rad));
        circle.setShape(Button.CIRCLE | Button.MEDIUM);
        circle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    updateShape(Button.CIRCLE);
                }
                return true;
            }
        });

        ButtonAddView arc = (ButtonAddView)view.findViewById(R.id.arc);
        arc.setRadius(view.getResources().getDimension(R.dimen.btn_preview_rad));
        arc.setShape(Button.ARC | Button.MEDIUM);
        arc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    updateShape(Button.ARC);
                }
                return true;
            }
        });
    }

    public void updateShape(int shape) {
        for (int i=0; i<3; i++) {
            int sh = (shape& Button.SHAPE)+((i+1)*16);
            addBtns[i].setShape(sh);
        }
    }
}
