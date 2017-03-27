package com.guangbiao.spiderweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SpiderView mSpiderView;
    private SeekBar mSeekBar1;
    private SeekBar mSeekBar2;
    private SeekBar mSeekBar3;
    private SeekBar mSeekBar4;
    private SeekBar mSeekBar5;
    private SeekBar mSeekBar6;

    int[] ids={R.id.seekBar1,R.id.seekBar2,R.id.seekBar3,R.id.seekBar4,R.id.seekBar5,R.id.seekBar6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpiderView = (SpiderView) findViewById(R.id.spiderView);
        mSeekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        mSeekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        mSeekBar4 = (SeekBar) findViewById(R.id.seekBar4);
        mSeekBar5 = (SeekBar) findViewById(R.id.seekBar5);
        mSeekBar6 = (SeekBar) findViewById(R.id.seekBar6);

        for (int i = 0; i < mSpiderView.getData().size(); i++) {
            double value=mSpiderView.getData().get(i);
            ((SeekBar)findViewById(ids[i])).setProgress((int) value);
        }

        mSeekBar1.setOnSeekBarChangeListener(this);
        mSeekBar2.setOnSeekBarChangeListener(this);
        mSeekBar3.setOnSeekBarChangeListener(this);
        mSeekBar4.setOnSeekBarChangeListener(this);
        mSeekBar5.setOnSeekBarChangeListener(this);
        mSeekBar6.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        for (int i = 0; i < ids.length; i++) {
            if (ids[i]==seekBar.getId()){
                List<Double> data = mSpiderView.getData();
                data.set(i, (double) progress);
                mSpiderView.setData(data);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
