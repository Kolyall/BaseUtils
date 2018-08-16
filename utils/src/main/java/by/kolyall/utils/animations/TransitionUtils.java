package by.kolyall.utils.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UserToken on 18.04.2017.
 */

public class TransitionUtils {
    private static int MAX_IMAGE_SIZE = (1024 * 1024);
    static Animator mergeAnimators(Animator animator1, Animator animator2) {
        if (animator1 == null) {
            return animator2;
        } else if (animator2 == null) {
            return animator1;
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animator1, animator2);
            return animatorSet;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Transition mergeTransitions(Transition... transitions) {
        int count = 0;
        int nonNullIndex = -1;
        for (int i = 0; i < transitions.length; i++) {
            if (transitions[i] != null) {
                count++;
                nonNullIndex = i;
            }
        }
        if (count == 0) {
            return null;
        }
        if (count == 1) {
            return transitions[nonNullIndex];
        }
        TransitionSet transitionSet = new TransitionSet();
        for (int i = 0; i < transitions.length; i++) {
            if (transitions[i] != null) {
                transitionSet.addTransition(transitions[i]);
            }
        }
        return transitionSet;
    }
    /**
     * Get a copy of bitmap of given drawable, return null if intrinsic size is zero
     */
    public static Bitmap createDrawableBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        float scale = Math.min(1f, ((float)MAX_IMAGE_SIZE) / (width * height));
        if (drawable instanceof BitmapDrawable && scale == 1f) {
            // return same bitmap if scale down not needed
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int bitmapWidth = (int) (width * scale);
        int bitmapHeight = (int) (height * scale);
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Rect existingBounds = drawable.getBounds();
        int left = existingBounds.left;
        int top = existingBounds.top;
        int right = existingBounds.right;
        int bottom = existingBounds.bottom;
        drawable.setBounds(0, 0, bitmapWidth, bitmapHeight);
        drawable.draw(canvas);
        drawable.setBounds(left, top, right, bottom);
        return bitmap;
    }
    /**
     * Creates a Bitmap of the given view, using the Matrix matrix to transform to the local
     * coordinates. <code>matrix</code> will be modified during the bitmap creation.
     *
     * <p>If the bitmap is large, it will be scaled uniformly down to at most 1MB size.</p>
     * @param view The view to create a bitmap for.
     * @param matrix The matrix converting the view local coordinates to the coordinates that
     *               the bitmap will be displayed in. <code>matrix</code> will be modified before
     *               returning.
     * @param bounds The bounds of the bitmap in the destination coordinate system (where the
     *               view should be presented. Typically, this is matrix.mapRect(viewBounds);
     * @return A bitmap of the given view or null if bounds has no width or height.
     */
    public static Bitmap createViewBitmap(View view, Matrix matrix, RectF bounds) {
        Bitmap bitmap = null;
        int bitmapWidth = Math.round(bounds.width());
        int bitmapHeight = Math.round(bounds.height());
        if (bitmapWidth > 0 && bitmapHeight > 0) {
            float scale = Math.min(1f, ((float)MAX_IMAGE_SIZE) / (bitmapWidth * bitmapHeight));
            bitmapWidth *= scale;
            bitmapHeight *= scale;
            matrix.postTranslate(-bounds.left, -bounds.top);
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.concat(matrix);
            view.draw(canvas);
        }
        return bitmap;
    }

    public static void removeSharedElementEnterTransitionEndListenerCompat(Window window, SimpleTransitionListener listener) {
        if (!AndroidVersionUtil.isGreaterThanL()) {
            return;
        }
        window.getSharedElementEnterTransition().removeListener(listener);
    }

    public static class MatrixEvaluator implements TypeEvaluator<Matrix> {
        float[] mTempStartValues = new float[9];
        float[] mTempEndValues = new float[9];
        Matrix mTempMatrix = new Matrix();
        @Override
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            startValue.getValues(mTempStartValues);
            endValue.getValues(mTempEndValues);
            for (int i = 0; i < 9; i++) {
                float diff = mTempEndValues[i] - mTempStartValues[i];
                mTempEndValues[i] = mTempStartValues[i] + (fraction * diff);
            }
            mTempMatrix.setValues(mTempEndValues);
            return mTempMatrix;
        }
    }

    @Nullable
    public static Transition findTransition(@NonNull TransitionSet set, @NonNull Class<? extends Transition> clazz) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < set.getTransitionCount(); i++) {
                Transition transition = set.getTransitionAt(i);
                if (transition.getClass() == clazz) {
                    return transition;
                }
                if (transition instanceof TransitionSet) {
                    Transition child = findTransition((TransitionSet) transition, clazz);
                    if (child != null) return child;
                }
            }
        }
        return null;
    }

    @Nullable
    public static  Transition findTransition(
            @NonNull TransitionSet set,
            @NonNull Class<? extends Transition> clazz,
            @IdRes int targetId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < set.getTransitionCount(); i++) {
                Transition transition = set.getTransitionAt(i);
                if (transition.getClass() == clazz) {
                    if (transition.getTargetIds().contains(targetId)) {
                        return transition;
                    }
                }
                if (transition instanceof TransitionSet) {
                    Transition child = findTransition((TransitionSet) transition, clazz, targetId);
                    if (child != null) return child;
                }
            }
        }
        return null;
    }

    public static List<Boolean> setAncestralClipping(@NonNull View view, boolean clipChildren) {
        return setAncestralClipping(view, clipChildren, new ArrayList<Boolean>());
    }

    private static List<Boolean> setAncestralClipping(
            @NonNull View view, boolean clipChildren, List<Boolean> was) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            was.add(group.getClipChildren());
            group.setClipChildren(clipChildren);
        }
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            setAncestralClipping((ViewGroup) parent, clipChildren, was);
        }
        return was;
    }

    public static void restoreAncestralClipping(@NonNull View view, List<Boolean> was) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            group.setClipChildren(was.remove(0));
        }
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            restoreAncestralClipping((ViewGroup) parent, was);
        }
    }


    public static void setSharedElementEnterTransitionEndListenerCompat(Window window, final SimpleTransitionListener listener) {
        if (!AndroidVersionUtil.isGreaterThanL()) {
            return;
        }
        window.getSharedElementEnterTransition().addListener(listener);
    }

    public static class SimpleTransitionListener implements Transition.TransitionListener {

        @Override
        public void onTransitionStart(Transition transition) {
        }

        @Override
        public void onTransitionEnd(Transition transition) {
        }

        @Override
        public void onTransitionCancel(Transition transition) {
        }

        @Override
        public void onTransitionPause(Transition transition) {
        }

        @Override
        public void onTransitionResume(Transition transition) {
        }
    }
}
