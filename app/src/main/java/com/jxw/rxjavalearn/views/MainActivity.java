package com.jxw.rxjavalearn.views;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.jxw.rxjavalearn.R;
import com.jxw.rxjavalearn.data.DataSource;
import com.jxw.rxjavalearn.model.Task;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> stringObservable = Observable
                .fromIterable(DataSource.createTasks())
                .map(extractDescFunction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        stringObservable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }



    Function<Task, String> extractDescFunction = new Function<Task, String>() {
        @Override
        public String apply(Task task) {
            return task.getDescription();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
