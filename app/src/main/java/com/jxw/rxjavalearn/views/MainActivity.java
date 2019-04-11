package com.jxw.rxjavalearn.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jxw.rxjavalearn.R;
import com.jxw.rxjavalearn.model.Task;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CompositeDisposable disposable = new CompositeDisposable();
    /* package */ Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        task = new Task("Take Lunch", false, 5);

        getTaskObservable().subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
                Log.d(TAG, "ON-SUBSCRIBE IS CALLED");
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "ON-ERROR IS CALLED");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "ON-COMPLETE IS CALLED");
            }
        });
    }

    public Observable<Task> getTaskObservable() {
        return Observable
                .create(new ObservableOnSubscribe<Task>() {
                    @Override
                    public void subscribe(ObservableEmitter<Task> emitter) throws Exception {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(task);
                            emitter.onComplete();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
