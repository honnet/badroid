package io.tdl.badroid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

class PlayButton extends Button
{
  int         sound;
  int         streamID;
  boolean     isPlaying;
  boolean     is1stPlay;
  int         loopMode;

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
    this.sound     = 0;
    this.streamID  = 0;
    this.isPlaying = false;
    this.is1stPlay = true;
    this.loopMode  = Badroid.LOOP_OFF;
  }
}