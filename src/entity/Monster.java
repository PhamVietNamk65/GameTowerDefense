package entity;

import static utils.Constants.Direction.*;

import java.awt.Rectangle;

public class Monster {
    protected float x, y;
	protected Rectangle bounds;
	protected int health;
	protected int maxHealth;
	protected int ID;
	protected int enemyType;
	protected int lastDir;
	protected boolean alive = true;
	private int pathIndex = 0;
	private EnemyState state = EnemyState.WALK;
	private int direction = 2;
	private int aniIndex = 0;
	protected float xOffset, yOffset;

	public Monster(float x, float y, int ID, int enemyType) {
		this.x = x;
		this.y = y;
		this.ID = ID;
		this.enemyType = enemyType;
		bounds = new Rectangle((int) x, (int) y, 32, 32);
		lastDir = -1;
		setStartHealth();
	}

	private void setStartHealth() {
		health = utils.Constants.Monsters.GetStartHealth(enemyType);
		maxHealth = health;
	}

	public void hurt(int dmg){
		this.health -=dmg;
		if(health <= 0){
			alive = false;
			state = EnemyState.DEATH;
		}
	}

	public void move(float speed, int dir) {
		lastDir = dir;
		switch (dir) {
		case LEFT:
			this.x -= speed;
			break;
		case UP:
			this.y -= speed;
			break;
		case RIGHT:
			this.x += speed;
			break;
		case DOWN:
			this.y += speed;
			break;
		}

		updateHitBox();
	}

	private void updateHitBox(){
		bounds.x = (int)x;
		bounds.y = (int)y;
	}

	public int getAniIndex() {
    	return aniIndex;
	}

	public void setAniIndex(int aniIndex) {
    	this.aniIndex = aniIndex;
	}

	public EnemyState getState(){
    	return state;
	}

	public void setState(EnemyState state){
    	this.state = state;
	}

	public int getPathIndex() {
    	return pathIndex;
	}

	public void nextPath() {
    	pathIndex++;
	}

	public void setPos(float x, float y) {
		// Don't use this one for moving the enemy.
		this.x = x;
		this.y = y;
	}

	public float getHealthBarFloat() {
		return health / (float) maxHealth;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public int getHealth() {
		return health;
	}

	public int getID() {
		return ID;
	}

	public int getEnemyType() {
		return enemyType;
	}

	public int getLastDir() {
		return lastDir;
	}

	public boolean IsAlive(){
		return alive;
	}
	
	public void setDirection(int right){
		this.direction = right;
	}

	public int getDirection(){
		return direction;
	}

	public void createOffset() {
        int maxOffset = 15; // Độ lệch tối đa trong ô 64x64
        java.util.Random r = new java.util.Random();
        
        // Random từ -15 đến 15
        this.xOffset = r.nextInt(maxOffset * 2) - maxOffset;
        this.yOffset = r.nextInt(maxOffset * 2) - maxOffset;
    }

    public float getxOffset() { return xOffset; }
    public float getyOffset() { return yOffset; }
}
