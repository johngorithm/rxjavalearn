package com.jxw.rxjavalearn.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jxw.rxjavalearn.R;
import com.jxw.rxjavalearn.model.Task;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CompositeDisposable disposable = new CompositeDisposable();
    private Task[] list = new Task[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list[0] = new Task("Call my baby", true, 3);
        list[1] = new Task("Walk the dog", false, 2);
        list[2] = new Task("Make my bed", true, 1);
        list[3] = new Task("Unload the dishwasher", false, 0);
        list[4] = new Task("Make dinner", true, 5);

        subscribeToAnObject();
    }

    public Observable<Task> getTaskObservable() {
        return Observable
                .fromArray(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void subscribeToAnObject() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
