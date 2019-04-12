package com.jxw.rxjavalearn.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jxw.rxjavalearn.R;
import com.jxw.rxjavalearn.model.Task;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        taskList.add(new Task("Call my baby", true, 3));
        taskList.add(new Task("Walk the dog", false, 2));
        taskList.add(new Task("Make my bed", true, 1));
        taskList.add(new Task("Unload the dishwasher", false, 0));
        taskList.add(new Task("Make dinner", true, 5));

        subscribeToAnObject();
    }

    public Observable<Task> getTaskObservable() {
        return Observable
                .fromIterable(taskList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void subscribeToAnObject() {
        getTaskObservable().subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
                Log.d(TAG, "onSubscribe: is called");
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: is called", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: is called");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
