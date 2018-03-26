package dev.zoranan.rpgengine.audio;

import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

import dev.zoranan.rpgengine.util.Assets;

public class SoundPlayer implements Runnable{
	private Thread thread;
	private boolean running = false;
	private boolean play = false;
	
	private AudioInputStream ais;
	//private Clip clip;
	private HashMap <String, Clip> persistentSounds;
	private ArrayList<String> toPlayOnce;	//Arraylist of sound clips
	private ArrayList<String> toStart;	//ArrayList of sound IDs
	
	
	
	//Create the thread, and start it
	public SoundPlayer() 
	{
		persistentSounds = new HashMap <String, Clip>();
		toPlayOnce = new ArrayList<String>();
		toStart = new ArrayList<String>();
		thread = new Thread(this);
		this.start();
	}
	
	//Start our audio thread
	public void start()
	{
		if (thread == null && !running)
			thread = new Thread (this);
		
		if (!running)
		{
			running = true;
			thread.start();
		}
	}
	
	//Plays / Restarts the sound
	public void playOnce(String soundID)
	{
		toPlayOnce.add(soundID);
	}
	
	//starts playing the next sound in our sound que
	private void startPlayOnce(String soundID)
	{
		try
		{
			String path = Assets.getSound(soundID).getChildText("src");
			ais = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource(path));
			Clip clip = AudioSystem.getClip();
			clip.addLineListener(new LineListener(){
				//Close the line when we are done
				@Override
				public void update(LineEvent event) 
				{
					if (event.getType() == Type.STOP)
						clip.close();
				}

			});
			clip.open(ais);
			clip.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Plays / Restarts a sound
	public void playPersistent(String soundID)
	{
		toStart.add(soundID);
	}
	
	private void startPlayPersistent(String soundID)
	{
		if (persistentSounds.get(soundID) == null)
			try
			{
				String path = Assets.getSound(soundID).getChildText("src");
				ais = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource(path));
				Clip persistentClip = AudioSystem.getClip();
				persistentClip.open(ais);
				persistentSounds.put(soundID, persistentClip);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		
		persistentSounds.get(soundID).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	//Closes out the sound
	public void stopSound(String soundID)
	{
		persistentSounds.get(soundID).stop();
		persistentSounds.get(soundID).close();
		persistentSounds.remove(soundID);
	}
	
	//Stop all sounds
	public void stopAll()
	{
		for (String s : persistentSounds.keySet())
		{
			stopSound(s);
		}
	}

	
	//Need a volatile int to keep the thread from sleeping
	//We are using a separate thread for audio
	private volatile int i;
	@Override
	public void run() 
	{	
		while (running)
		{
			//int i = 0;
			//Play SFX
			if ((i = toPlayOnce.size()) > 0)
			{
				
				startPlayOnce (toPlayOnce.get(i-1));	//Loads and plays a file in our que anonymously
				toPlayOnce.remove(i-1);
			}
			
			//Play music / ambient
			if ((i = toStart.size()) > 0)
			{
				startPlayPersistent(toStart.get(i-1));
				toStart.remove(i-1);
			}
					
		}
	}
}
