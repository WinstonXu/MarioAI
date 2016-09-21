
package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
/**
 * 
 * @author Winston
 *My version of the Mario Agent
 *Does several checks about the blocks in front, above and below the character to make decisions about what to do next
 */

public class MyAgent extends BasicMarioAIAgent implements Agent
{

	public MyAgent()
	{
		super("MyAgent");
		reset();
	}

	// Does (row, col) contain an enemy?   
	public boolean hasEnemy(int row, int col) {
		return enemies[row][col] != 0;
	}

	// Is (row, col) empty?  
	//Considers coins empty spaces now-as long as enemy is not on it
	public boolean isEmpty(int row, int col) {
		if(levelScene[row][col] == 0){
			return true;
		}
		else if(levelScene[row][col] == 2 && !hasEnemy(row,col)){
			return true;
		}
		else{
			return false;
		}
	}


	// Display Mario's view of the world
	public void printObservation() {
		System.out.println("**********OBSERVATIONS**************");
		for (int i = 0; i < mergedObservation.length; i++) {
			for (int j = 0; j < mergedObservation[0].length; j++) {
				if (i == mergedObservation.length / 2 && j == mergedObservation.length / 2) {
					System.out.print("M ");
				}
				else if (hasEnemy(i, j)) {
					System.out.print("E ");
				}
				else if (!isEmpty(i, j)) {
					System.out.print("B ");
				}
				else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println("************************");
	}

	// Actually perform an action by setting a slot in the action array to be true
	public boolean[] getAction()
	{

		action[Mario.KEY_LEFT] = false;
		action[Mario.KEY_RIGHT] = false;
		action[Mario.KEY_SPEED] = false;
		action[Mario.KEY_DOWN] = false;
		//Mario on ground and enemy/ block in front-cannot drop
		if(needsJump() && isMarioOnGround && !safeToDrop()){
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_RIGHT] = true;
		}
		//Mario is on ground-enemies above/in front; can drop down
		else if(needsJump() && safeToDrop() && isMarioOnGround){
			action[Mario.KEY_JUMP] = !isMarioOnGround;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_RIGHT] = true;
		}
		//Mario is in the process of jumping-extend jump because of enemies below
		else if(!isMarioOnGround && !safeToDrop()){
			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_RIGHT] = true;
			action[Mario.KEY_SPEED] = true;
		}
		//Mario is in air and enemies approaching-Just drop down-try to avoid enemies
		else if(!isMarioOnGround && needsJump() && safeToDrop()){
			action[Mario.KEY_DOWN] = true;
		}
		//All clear condition
		else{
			action[Mario.KEY_RIGHT] = true;
		}
		printObservation();
		return action;
	}
	/**
	 * Description: Uses isEmpty method to see if blocks in front of Mario have enemies or obstacles
	 * @param none
	 * @return boolean
	 */
	private boolean needsJump(){

		for(int i = 10; i < 15; i++){
			for(int j = 9; j > 6; j--){
				if(!isEmpty(j, i)){
					return true;
				}
			}
		}	
		return false;
	}
	/**
	 * Description: Uses isEmpty method to see if blocks in front and below Mario to see if Mario can keep running forward
	 * and drop down
	 * @param none
	 * @return boolean
	 */
	private boolean safeToDrop(){

		if(isEmpty(10,10)){
			for(int i = 11; i < 19; i++){
				if(!isEmpty(i,10)){
					for(int j = 11; j < 15; j++){
						if(hasEnemy(i, j)){
							return false;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	// Do the processing necessary to make decisions in getAction
	public void integrateObservation(Environment environment)
	{
		super.integrateObservation(environment);
		levelScene = environment.getLevelSceneObservationZ(2);
	}

	// Clear out old actions by creating a new action array
	public void reset()
	{
		action = new boolean[Environment.numberOfKeys];
	}
}
