package com.utils.rxandroid;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nick Unuchek on 29.05.2017.
 */

public class RxNetworkUtils {
    private static final String TAG = RxNetworkUtils.class.getSimpleName();

    private List<Observable.Transformer<?, ?>> transformers;
    @Nullable private BaseView baseView;

    public static RxNetworkUtils builder() {
        return new RxNetworkUtils();
    }

    public static RxNetworkUtils builder(BaseView baseView) {
        return new RxNetworkUtils(baseView);
    }

    private RxNetworkUtils() {
        this.transformers = new ArrayList<>();
    }

    private RxNetworkUtils(@Nullable BaseView baseView) {
        this.transformers = new ArrayList<>();
        this.baseView = baseView;
    }

    public <T> Observable.Transformer<T, T> build() {
        return tObservable -> {
            for (int i = 0; i < transformers.size(); i++) {
                //noinspection unchecked
                tObservable = tObservable.compose(
                        (Observable.Transformer<T, T>) transformers.get(i)
                );
            }
            return tObservable;
        };
    }

    public RxNetworkUtils async() {
        transformers.add(observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        );
        return this;
    }

    public RxNetworkUtils sync() {
        transformers.add(observable -> observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
        );
        return this;
    }

    public RxNetworkUtils io() {
        transformers.add(observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        );
        return this;
    }

    @NonNull
    public RxNetworkUtils errorSkip() {
        transformers.add(observable -> observable
                .onErrorResumeNext(throwable -> {
                    return Observable.empty();
                }));
        return this;
    }

    @NonNull
    public RxNetworkUtils errorHandler() {
        transformers.add(observable -> observable
                .onErrorResumeNext(throwable -> {
//                    if (throwable instanceof HttpException) {
//                        String body = "";
//                        HttpException he = (HttpException) throwable;
//                        Log.i(TAG, "code: " + he.code());
//                        try {
//                            body = he.response().errorBody().string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        Response response = gson.fromJson(body, Response.class);
//                        Throwable err = response.getError() != null ?
//                                new Throwable(response.getError()) : throwable;
//                        return Observable.error(err);
//                    }
                    throwable.printStackTrace();
                    return Observable.empty();
                }));
        return this;
    }

    @NonNull
    public RxNetworkUtils onErrorResumeNext(Func1<Throwable, Observable<?>> throwableObservableFunc1) {
        transformers.add(observable -> observable
                .onErrorResumeNext(throwableObservableFunc1));
        return this;
    }

    public RxNetworkUtils progressBar() {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .doOnSubscribe(baseView::showProgressBar)
                    .doOnError(throwable -> baseView.hideProgressBar())
                    .doOnCompleted(baseView::hideProgressBar)
                    .doOnTerminate(baseView::hideProgressBar)
                    .doAfterTerminate(baseView::hideProgressBar))
            ;
        }
        return this;
    }

    public RxNetworkUtils progressOn(HasProgress hasProgress) {
        if (hasProgress != null) {
            transformers.add(observable -> observable
                    .doOnSubscribe(hasProgress::showProgress)
                    .doOnError(throwable -> hasProgress.hideProgress())
                    .doOnCompleted(hasProgress::hideProgress)
                    .doOnTerminate(hasProgress::hideProgress)
                    .doAfterTerminate(hasProgress::hideProgress))
            ;
        }
        return this;
    }

    public RxNetworkUtils disable(Disableable disableable) {
        if (disableable != null) {
            transformers.add(
                    observable -> observable
                            .doOnSubscribe(() -> disableable.setEnabled(false))
                            .doOnError(throwable -> disableable.setEnabled(true))
                            .doOnCompleted(() -> disableable.setEnabled(true))
                            .doOnTerminate(() -> disableable.setEnabled(true))
                            .doAfterTerminate(() -> disableable.setEnabled(true))
            );
        }
        return this;
    }

    public RxNetworkUtils nonClickable(NonClickable clickable) {
        if (clickable != null) {
            transformers.add(
                    observable -> observable
                            .doOnSubscribe(() -> clickable.setClickable(false))
                            .doOnError(throwable -> clickable.setClickable(true))
                            .doOnCompleted(() -> clickable.setClickable(true))
                            .doOnTerminate(() -> clickable.setClickable(true))
                            .doAfterTerminate(() -> clickable.setClickable(true))
            );
        }
        return this;
    }

    public RxNetworkUtils progressMenuItem() {
        if (baseView != null && baseView instanceof HasProgressMenuItem) {
            transformers.add(observable -> observable
                    .doOnSubscribe(((HasProgressMenuItem) baseView)::showProgressMenuItem)
                    .doOnError(throwable -> ((HasProgressMenuItem) baseView).hideProgressMenuItem())
                    .doOnCompleted(((HasProgressMenuItem) baseView)::hideProgressMenuItem)
                    .doOnTerminate(((HasProgressMenuItem) baseView)::hideProgressMenuItem)
                    .doAfterTerminate(((HasProgressMenuItem) baseView)::hideProgressMenuItem))
            ;
        }
        return this;
    }

    public RxNetworkUtils shimmerProgress() {
        if (baseView != null && baseView instanceof HasShimmerView) {
            transformers.add(observable -> observable
                    .doOnSubscribe(((HasShimmerView) baseView)::showShimmerAdapter)
                    .doOnError(throwable -> ((HasShimmerView) baseView).hideShimmerAdapter())
                    .doOnCompleted(((HasShimmerView) baseView)::hideShimmerAdapter)
                    .doOnTerminate(((HasShimmerView) baseView)::hideShimmerAdapter)
                    .doAfterTerminate(((HasShimmerView) baseView)::hideShimmerAdapter))
            ;
        }
        return this;
    }

    public RxNetworkUtils progressDialog() {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .doOnSubscribe(baseView::showProgressDialog)
                    .doOnError(throwable -> baseView.hideProgressDialog())
                    .doOnCompleted(baseView::hideProgressDialog));
        }
        return this;
    }

    public RxNetworkUtils progressDialog(@StringRes int titleResId, @StringRes int messageResId) {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .doOnSubscribe(() -> baseView.showProgressDialog(titleResId, messageResId))
                    .doOnTerminate(baseView::hideProgressDialog));
        }
        return this;
    }

    public RxNetworkUtils errorToast() {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .doOnError(baseView::showErrorToast));
        }
        return this;
    }

    public RxNetworkUtils errorView() {
        if (baseView != null && baseView instanceof HasErrorView) {
            transformers.add(observable -> observable
                    .doOnSubscribe(() -> ((HasErrorView) baseView).hideErrorView())
                    .doOnError(((HasErrorView) baseView)::showErrorView));
        }
        return this;
    }

    public RxNetworkUtils errorSnackBar() {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .doOnError(baseView::showErrorSnackBar));
        }
        return this;
    }

    public RxNetworkUtils errorPopupDialog() {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .doOnError(baseView::showErrorPopupDialog));
        }
        return this;
    }

    public RxNetworkUtils retry(int maxRetryCount, long delay, TimeUnit unit) {
        transformers.add(observable -> observable
                .retryWhen(RxNetworkUtils.exponentialBackoff(maxRetryCount, delay, unit))
        );
        return this;
    }

    /*
     * retry subscribe to observable one time after internet connected action
     * */
    public RxNetworkUtils retryOnInternetConnection() {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .retryWhen(source -> source
                            .flatMap(throwable -> Observable.interval(2, TimeUnit.SECONDS)
                                    .map(aLong -> baseView.isInternetConnected())
                                    .filter(isInternetConnected -> isInternetConnected)
                                    .take(1)
                            )
                    )
            );
        }
        return this;
    }

    public <T> RxNetworkUtils doOnNext(Action1<T> onNext) {
        if (baseView != null) {
            transformers.add(new Observable.Transformer<T, T>() {
                @Override
                public Observable<T> call(Observable<T> observable) {
                    return observable
                            .doOnNext(onNext);
                }
            });
        }
        return this;
    }

    public <T> RxNetworkUtils doOnError(Action1<Throwable> doOnError) {
        if (baseView != null) {
            transformers.add(new Observable.Transformer<T, T>() {
                @Override
                public Observable<T> call(Observable<T> observable) {
                    return observable
                            .doOnError(doOnError);
                }
            });
        }
        return this;
    }

    public RxNetworkUtils doOnCompleted(Action0 action0) {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .doOnCompleted(action0));
        }
        return this;
    }

    public RxNetworkUtils doOnSubscribe(Action0 action0) {
        if (baseView != null) {
            transformers.add(observable -> observable
                    .doOnSubscribe(action0));
        }
        return this;
    }

    public static Func1<Observable<? extends Throwable>, Observable<?>> exponentialBackoff(
            int maxRetryCount, long delay, TimeUnit unit) {
        return errors -> errors
                .zipWith(Observable.range(1, maxRetryCount), (error, retryCount) -> retryCount)
                .flatMap(retryCount -> Observable.timer((long) Math.pow(delay, retryCount), unit));
    }

    public interface BaseView extends HasProgressBar, HasProgressDialog, HasErrorView {

        void showErrorPopupDialog(Throwable throwable);

        void showErrorSnackBar(Throwable throwable);

        void showErrorToast(Throwable throwable);

        boolean isInternetConnected();

    }

    public interface HasProgressMenuItem {

        void showProgressMenuItem();

        void hideProgressMenuItem();

    }

    public interface HasProgressBar {

        void showProgressBar();

        void hideProgressBar();

    }

    public interface HasProgress {

        void showProgress();

        void hideProgress();

    }

    public interface Disableable {

        void setEnabled(boolean enabled);

        boolean isEnabled();

    }
    public interface NonClickable {

        void setClickable(boolean clickable);

        boolean isClickable();

    }

    public interface HasErrorView {

        void showErrorView(Throwable throwable);

        void hideErrorView();

    }

    public interface HasProgressDialog {

        void showProgressDialog();

        void showProgressDialog(@StringRes int titleResId, @StringRes int messageResId);

        void hideProgressDialog();

    }

    public interface HasShimmerView extends BaseView {
        void showShimmerAdapter();

        void hideShimmerAdapter();
    }

    /* Usage
    *  Observable<Integer> invite = mAPI.invite()
    *  .compose(
                NetworkUtils.builder(this)
                        .async()
                        .progressBar()
                        .errorToast()
                        .build())
                .subscribe(
                        it -> Log.i(TAG, "onClickTalkWithFriend it: " + it.toString()),
                        Throwable::printStackTrace,
                        () -> Log.d(TAG, "onClickTalkWithFriend() called")
                );
    * */
}
