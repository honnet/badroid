/*
TODO: long click => setLoop(sound[i], loopMode[i] = !loopMode[i]);
http://developer.android.com/reference/android/view/View.html#setLongClickable(boolean)
*/
package io.tdl.badroid;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Badroid extends Activity
{
  static final String  TAG         =  "Badroid";
  static final int     PAD_N       =  4;    // pad number
  static final float   FULL        =  1.0f; // play volume
  static final int     LOOP_OFF    =  0;
  static final int     LOOP_ON     = -1;
  static final int[]   BUTTONS     = {R.id.button0, R.id.button1,
                                      R.id.button2, R.id.button3};
  SoundPool     soundPool   = new SoundPool(PAD_N, 3, 0);
  PlayButton[]  button      = new PlayButton[PAD_N];
  boolean       isMuted     = false;
  AudioManager  audio;

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
    String name;
    AssetManager assetManager = getAssets();
    AssetFileDescriptor fileDescriptor;
    audio = (AudioManager) Badroid.this.getSystemService(Context.AUDIO_SERVICE);
    button[0].loopMode = button[1].loopMode = LOOP_ON; // TODO with long click

    for (int i=0; i<PAD_N; i++)
    {
      name = "sound" + String.valueOf(i) + ".ogg"; // in "assets" folder
      try
      {
        fileDescriptor  = assetManager.openFd(name);
        button[i].sound = soundPool.load(fileDescriptor, 1); // 1 = default
        Log.v(TAG, "sound " + i + " out of " + PAD_N + ": loaded.");
      }
      catch (IOException e)
      {
        Log.e(TAG, "!!! FAILED TO LOAD SAMPLE " + name + " !!!");
        e.printStackTrace();
      }
    }
  }

  private void clickHandle(View v)
  {
    PlayButton channel = (PlayButton)v;

    if ( channel.loopMode == LOOP_ON )
    {
      channel.setText(getString(channel.isPlaying ?
                                R.string.play_str :
                                R.string.stop_str));
      if (channel.isPlaying)
        stop(channel);
      else // isPlaying == false
        play(channel);
      
      channel.isPlaying = !channel.isPlaying;
    }
    else // LOOP_OFF:
    {
      if (!channel.is1stPlay)
        stop(channel);
      play(channel);
    }
  }
  
  public void toggleClick(View v)
  {
    Log.d(TAG, "toggle button state: " + (isMuted? "OFF" : "ON"));
    
    audio.setStreamMute(AudioManager.STREAM_MUSIC, (isMuted? false : true)); 
    isMuted = !isMuted;
  }
  
  private void play(PlayButton channel)
  {
    channel.streamID = soundPool.play(channel.sound,
                                      FULL,FULL, 0,
                                      channel.loopMode, 1);
    if (channel.streamID == 0)
      Log.e(TAG, "!!! FAILED TO READ SAMPLE " + channel.streamID + " !!!");

    channel.is1stPlay = false;
  }

  private void stop(PlayButton channel)
  {
    soundPool.stop(channel.streamID);
  }
}
