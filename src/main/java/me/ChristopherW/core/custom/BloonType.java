package me.ChristopherW.core.custom;

import me.ChristopherW.core.entity.Texture;
import me.ChristopherW.process.Game;

public enum BloonType {
	RED(1,1, Game.RED), 
	BLUE(1, 1.4f, Game.BLUE), 
	GREEN(1,1.8f, Game.GREEN),
	YELLOW(1,3.2f, Game.YELLOW),
	PINK(1,3.5f, Game.PINK), 
	BLACK(1, 1.8f, Game.BLACK), 
	WHITE(1, 2f, Game.WHITE),
	CERAMIC(10,2.5f, Game.CERAMIC),
	MOAB(200,1f, Game.MOAB);
	
	public int health;
	public float speed;
	public Texture color;
	
	// constructor
	BloonType(int health, float speed, Texture color){
		this.health = health;
		// speed relative to red bloon (1)
		this.speed = speed;
		this.color = color;
	}

	public static BloonType getTypeFromHealth(int health) {
		for (int i = 0; i < values().length; i++) {
			BloonType t = values()[i];
			if(t.health == health)
			return t;
		}
		return null;
	}
}
