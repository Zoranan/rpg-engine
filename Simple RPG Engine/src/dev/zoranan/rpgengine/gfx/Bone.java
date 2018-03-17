package dev.zoranan.rpgengine.gfx;

public class Bone {
	public float x;
	public float y;
	public float width;
	public float height;
	public float rotation;
	
	public Bone ()
	{
		set(0,0,0,0,0);
	}
	
	public Bone (float x, float y, float w, float h, float r)
	{
		set (x, y, w, h, r);
	}
	
	public void set(float x, float y, float w, float h, float r)
	{
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.rotation = r;
	}
	
	//Returns a new bone with the combined values of this, and the passed in bone
	public Bone add(Bone b)
	{
		return new Bone (x + b.x, y + b.y, width + b.width, height + b.height, rotation + b.rotation);
	}
	
	public Bone difference(Bone b)
	{
		return new Bone (b.x - x, b.y - y, b.width - width, b.height - height, b.rotation - rotation);
	}
	
	//Returns a new bone divided by the number of frames
	public Bone divide(float frames)
	{
		return new Bone(x / frames, y / frames, width / frames, height / frames, rotation / frames);
	}
}
