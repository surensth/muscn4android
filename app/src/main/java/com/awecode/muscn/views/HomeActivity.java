package com.awecode.muscn.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.awecode.muscn.R;
import com.awecode.muscn.model.CountDownTime;
import com.awecode.muscn.model.http.fixtures.FixturesResponse;
import com.awecode.muscn.model.http.fixtures.Result;
import com.awecode.muscn.util.Util;
import com.awecode.muscn.util.countdown_timer.CountDownTimer;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.daysTextView)
    TextView mDaysTextView;
    @BindView(R.id.hoursTextView)
    TextView mHoursTextView;
    @BindView(R.id.minsTextView)
    TextView mMinsTextView;
    @BindView(R.id.secsTextView)
    TextView mSecsTextView;
    @BindView(R.id.competitionBetweenTextView)
    TextView mCompetitionBetweenTextView;
    @BindView(R.id.competitionNameVenueTextView)
    TextView mCompetitionNameVenueTextView;
    @BindView(R.id.broadCastChannelTextView)
    TextView mBroadCastChannelTextView;
    @BindView(R.id.dateTimeTextView)
    TextView mDateTimeTextView;
    private CountDownTimer mCountDownTimer;
    private long mTimeDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureCountDownTimer();
        showProgressView("Loading fixture...");
        requestFixturesList();
    }

    private void requestFixturesList() {
        Observable<FixturesResponse> call = mApiInterface.getFixtures();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FixturesResponse>() {
                    @Override
                    public void onCompleted() {
                        showContentView();

                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorView(e.getMessage());
                    }

                    @Override
                    public void onNext(FixturesResponse fixturesResponse) {
                        configureFixtureView(fixturesResponse.getResults().get(0));
                    }
                });
    }

    private void configureFixtureView(Result result) {
        String opponentName = result.getOpponent().getName();
        Boolean isHomeGame = result.getIsHomeGame();
        //configure broadcast channel name
        configureBroadCastChannelView(result.getBroadcastOn());
        //configure countdown timer
        configureCountDownTimer(result.getDatetime());

        //configure game between team names
        if (isHomeGame)
            mCompetitionBetweenTextView.setText("Manchester United\nvs.\n" + opponentName);
        else
            mCompetitionBetweenTextView.setText(opponentName + "\nvs.\nManchester United");

        //configure venue name
        mCompetitionNameVenueTextView.setText("English Premier League\n" + result.getVenue());


    }

    private void configureCountDownTimer(String dateStr) {
        //2015-08-08T11:45:00Z //yyyy-MM-dd'T'HH:mm:ssZZ
        dateStr="2016-09-22T23:45:00Z";
        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = myFormat.parse(dateStr);

            long timeDiff = date.getTime()-new Date().getTime();
            mCountDownTimer.setTime(timeDiff);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void configureBroadCastChannelView(Object broadCastOn) {
        if (broadCastOn != null && !TextUtils.isEmpty(String.valueOf(broadCastOn)))
            mBroadCastChannelTextView.setText(String.valueOf(broadCastOn));
        else
            mBroadCastChannelTextView.setVisibility(View.GONE);
    }

    private void configureCountDownTimer() {
        mCountDownTimer = new CountDownTimer();
        mCountDownTimer.setOnTimerListener(new CountDownTimer.TimerListener() {
            @Override
            public void onTick(long millisUntilFinished, CountDownTime countDownTime) {


                Log.v(TAG, "time ticked val: " + Util.getTwoDigitNumber(countDownTime.getDays()) + ":" + Util.getTwoDigitNumber(countDownTime.getHours()) + ":" + Util.getTwoDigitNumber(countDownTime.getMinutes())
                        + ":" + Util.getTwoDigitNumber(countDownTime.getSeconds()));
                mDaysTextView.setText(Util.getTwoDigitNumber(countDownTime.getDays()));
                mHoursTextView.setText(Util.getTwoDigitNumber(countDownTime.getHours()));
                mMinsTextView.setText(Util.getTwoDigitNumber(countDownTime.getMinutes()));
                mSecsTextView.setText(Util.getTwoDigitNumber(countDownTime.getSeconds()));
            }

            @Override
            public void onFinish() {
            }

        });

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
