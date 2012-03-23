/*
TODO: long click => setLooping(true);
http://developer.android.com/reference/android/view/View.html#setLongClickable(boolean)
*/
package io.tdl.badroid;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Badroid extends Activity
{
  static final String TAG     =  "Badroid";
  static final int    PAD_N   =  4;    // pad number
  static final int[]  BUTTONS = {R.id.button0, R.id.button1,
                                 R.id.button2, R.id.button3};
  static final int[]  SOUNDS  = {R.raw.sound0, R.raw.sound1,
                                 R.raw.sound2, R.raw.sound3};
  PlayButton[]        button  = new PlayButton[PAD_N];
  boolean             isMuted = false;
  AudioManager        audio;

  @Override public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    initViews();
    initAudio();
  }

  private void initViews()
  {
    View.OnClickListener myClickListener = new View.OnClickListener()
      { public void onClick(View v){clickHandle(v);} };

    for (int i=0; i<PAD_N; i++)
    {
      button[i] = (PlayButton) findViewById(BUTTONS[i]);
      button[i].setOnClickListener(myClickListener);
      button[i].setText(getString(R.string.play_str));
    }
  }

  private void initAudio()
  {
    audio = (AudioManager) /*Badroid.this.*/getSystemService(Context.AUDIO_SERVICE);
    button[0].isLooping = button[1].isLooping = true; // TODO with long click

    for (int i=0; i<PAD_N; i++)
    {
      try
      {
        /* TODO use popup to load sounds from sdcard, example:
http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/media/MediaPlayerDemo_Audio.html */
        button[i].mediaPlayer = MediaPlayer.create(this, SOUNDS[i]);
        button[i].mediaPlayer.prepareAsync();
        Log.v(TAG, "sound " + i + " out of " + PAD_N + ": loaded.");
      }
      catch (IllegalStateException e)
      {
        Log.e(TAG, "!!! FAILED TO PREPARE SAMPLE " + i + " !!!");
        e.printStackTrace();
      }
    }
  }

  private void clickHandle(View v)
  {
    PlayButton sound = (PlayButton)v;

    sound.setText(getString(sound.mediaPlayer.isPlaying() ?
                            R.string.play_str :
                            R.string.stop_str));

    if (sound.mediaPlayer.isPlaying())
    {
      stop(sound);
      if (!sound.isLooping)
        play(sound); // TODO test delay
    }
    else
      play(sound);
  }

  private void play(PlayButton sound)
  {
    try
    {
      sound.mediaPlayer.start();
      sound.mediaPlayer.setLooping(sound.isLooping); // TODO long click choice
    }
    catch (IllegalStateException e)
    {
      Log.e(TAG, "!!! ERROR WITH SAMPLE " +
                 sound.mediaPlayer.getAudioSessionId() + " !!!");
      e.printStackTrace();
    }
  }

  private void stop(PlayButton sound)
  {
    try
    {
      sound.mediaPlayer.stop();
      sound.mediaPlayer.prepareAsync();
    }
    catch (IllegalStateException e)
    {
      Log.e(TAG, "!!! ERROR WITH SAMPLE " +
                 sound.mediaPlayer.getAudioSessionId() + " !!!");
      e.printStackTrace();
    }
  }

  public void toggleClick(View v)
  {
    Log.d(TAG, "mute state: " + (isMuted? "OFF" : "ON"));

    audio.setStreamMute(AudioManager.STREAM_MUSIC, (isMuted? false : true)); 
    isMuted = !isMuted;
  }

  @Override protected void onDestroy()
  {
    super.onDestroy();
//*
    for (int i=0; i<PAD_N; i++)
      if (button[i].mediaPlayer != null)
      {
        button[i].mediaPlayer.release();
        button[i].mediaPlayer = null;
      }
//*/
  }

}
