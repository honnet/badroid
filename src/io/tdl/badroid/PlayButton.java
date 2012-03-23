package io.tdl.badroid;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.Button;

class PlayButton extends Button
{
  boolean     isLooping;
  MediaPlayer mediaPlayer;

  // TODO getters & setters !!!
  
  public PlayButton(Context context)
  {
    super(context);
    init();
  }

  public PlayButton(Context context, AttributeSet attributeSet)
  {
    super(context, attributeSet);
    init();
  }
  
  private void init()
  {
    this.isLooping   = false;
    this.mediaPlayer = new MediaPlayer();
  }
}
