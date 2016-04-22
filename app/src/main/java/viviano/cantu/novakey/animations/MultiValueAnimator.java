package viviano.cantu.novakey.animations;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Interpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viviano on 1/22/2016.
 */
public class MultiValueAnimator<K> extends ValueAnimator {

    /**
     * Static method which MUST be used to initialize a MultiValueAnimator
     *
     * @return A the equivalent of ValueAnimator.ofFloat(0, 1)
     */
    public static <K> MultiValueAnimator<K> create() {
        MultiValueAnimator<K> anim = new MultiValueAnimator<>();
        anim.setFloatValues(0, 1);
        return anim;
    }

    private Map<K, InterpolatorData> mInterpolatorData;//builder map

    private Map<K, DelayableInterpolator> mInterpolators;//filled with delayable interpolators
    private MultiUpdateListener mUpdateListener;

    /**
     * Constructor initializes data and adds an update listener which calls the
     * MultiUpdateListener methods onValueUpdate() & onAllUpdate()
     *
     */
    public MultiValueAnimator() {
        super();
        mInterpolatorData = new HashMap<>();

        this.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) getAnimatedValue();
                if (mUpdateListener != null) {

                    for (Map.Entry<K, DelayableInterpolator> e : mInterpolators.entrySet()) {
                        float actual = e.getValue().getInterpolation(v);
                        mUpdateListener.onValueUpdate(animation, actual, e.getKey());
                    }
                    mUpdateListener.onAllUpdate(animation, v);
                }
            }
        });
    }

    /**
     * Determines total duration of animation & sets DelayInterpolators
     * Then starts the animation
     */
    @Override
    public void start() {
        long totalDuration = 0;
        //add up all the durations
        for (Map.Entry<K, InterpolatorData> e : mInterpolatorData.entrySet()) {
            InterpolatorData id = e.getValue();
            long test = id.delay + id.duration;
            if (test > totalDuration)
                totalDuration = test;
        }
        setDuration(totalDuration);

        //set actual map
        mInterpolators = new HashMap<>();
        for (Map.Entry<K, InterpolatorData> e : mInterpolatorData.entrySet()) {
            InterpolatorData id = e.getValue();
            mInterpolators.put(e.getKey(),
                    new DelayableInterpolator(id.delay, id.duration,
                            totalDuration, id.interpolator));
        }
        super.start();
    }

    /**
     * Adds a new interpolator which will be used to create its corresponding DelayInterpolator
     *
     * @param key key corresponding to this interpolator
     * @param timeInterpolator interpolator for DelayInterpolator
     * @param delay delay of specific value animation
     * @param duration duration of specific value animation
     */
    public void addInterpolator(K key, TimeInterpolator timeInterpolator, long delay, long duration) {
        mInterpolatorData.put(key, new InterpolatorData(timeInterpolator, delay, duration));
    }

    // holds interpolator data until start
    private class InterpolatorData {
        TimeInterpolator interpolator;
        long delay, duration;

        public InterpolatorData(TimeInterpolator interpolator, long delay, long duration) {
            this.interpolator = interpolator;
            this.delay = delay;
            this.duration = duration;
        }
    }


    /**
     * Sets update multi update listener
     *
     * @param updateListener listener to set
     */
    public void setMultiUpdateListener(MultiUpdateListener updateListener) {
        mUpdateListener = updateListener;
    }

    /**
     * Update Listener that contains callback methods which users of MultiValueAnimator
     * will use to specify values
     */
    public interface MultiUpdateListener<K> {
        /**
         * Callback method that will be called for each different value which is being animated.
         *
         * @param animator this animator
         * @param value If a specific value is given a delay, the value will be 0 until the delay
         *              is reached. If a specific value is done animating, the value will be 1
         *              until the whole animation is finished
         * @param key key of the specific value animatios
         */
        void onValueUpdate(ValueAnimator animator, float value, K key);

        /**
         * Callback method that will be called at the end of each update.
         * Will be called after all the onValueUpdate() methods are called
         *
         * @param animator this animator
         * @param value current animated value from 0 - 1 depending on given interpolator
         */
        void onAllUpdate(ValueAnimator animator, float value);
    }
}
