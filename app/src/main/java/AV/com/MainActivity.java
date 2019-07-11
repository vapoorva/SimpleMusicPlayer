package AV.com;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button playButton;
    private SeekBar positionBar, volumeBar;
    private TextView elapsedTimeLabel, remaniningTimeLabel;
    private MediaPlayer mediaPlayer;
    private int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.playButton);//play button
        positionBar = (SeekBar) findViewById(R.id.positionBar);//position of song
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);//volume button
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);//elapsed time
        remaniningTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);//remaining time

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.files);
        mediaPlayer.setLooping(true);//to loop
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f, 0.5f);
        totalTime = mediaPlayer.getDuration();

        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNumber = progress / 100f;
                mediaPlayer.setVolume(volumeNumber, volumeNumber);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mediaPlayer != null) {
                    try {
                        Message message = new Message();
                        message.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);
            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remaniningTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String TimeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 100 % 60;
        TimeLabel = min + ":";
        if (sec < 10)
            TimeLabel += "0";
        TimeLabel += sec;
        return TimeLabel;
    }

    public void playBtnClick(View view) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playButton.setBackgroundResource(R.drawable.stop);
        } else {
            mediaPlayer.pause();
            playButton.setBackgroundResource(R.drawable.play);
        }
    }
}

