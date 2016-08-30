
package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;


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
	public boolean isEmpty(int row, int col) {
		return (levelScene[row][col] == 0);
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
		action[Mario.KEY_JUMP] = !isMarioAbleToJump && !isMarioOnGround;
		if(detectEnemy()){
//			System.out.println("Enemy detected");
			printObservation();
			return action;
		}
		else if(detectGap()){
//			System.out.println("Blocks");
			printObservation();
			return action;
		}
		
//		action[Mario.KEY_LEFT] = false;
//		action[Mario.KEY_RIGHT] = false;
//		action[Mario.KEY_JUMP] = !isMarioAbleToJump && !isMarioOnGround;
//		if((lookForward() && lookUp())){
//			action[Mario.KEY_LEFT] = true;
//		}
//		else if (!isEmpty(8,9) && !isEmpty(9,10)){
//			action[Mario.KEY_LEFT] = true;
//		}
//		else if(lookForward()){
//			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
//			action[Mario.KEY_RIGHT] = true;
//		}
		else{
			action[Mario.KEY_RIGHT] = true;
		}
		printObservation();
		System.out.println();
		System.out.println(action[Mario.KEY_LEFT]+ " "+ action[Mario.KEY_JUMP]+ " "
		+action[Mario.KEY_RIGHT]+" "+  isMarioAbleToJump+ " "+isMarioOnGround);
		System.out.println();
		return action;
	}
	
//	private boolean lookForward(){
//		
//		for(int i = 10; i < 13; i++){
//			if(isEmpty(9, i)){
//				return true;
//			}
//		}	
//		return false;
//	}
//	
//	private boolean lookUp(){
//		
//		for(int i = 6; i < 8; i++){
//			for(int j = 9; j < 12; j++){
//				if(hasEnemy(i, j)){
//					return true;
//				}
//			}
//		}
//		return false;
//	}
	
	private boolean detectEnemy(){
		for(int i = 10; i < 13; i++){
			if(hasEnemy(9,i)){
				action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
				action[Mario.KEY_RIGHT] = true;
				return true;
			}
		}
//		//Trying to catch enemies above
//		for(int i = 4; i < 10; i++){
//			for(int j = 11; j < 15; j++){
//				if(hasEnemy(i,j)){
//					action[Mario.KEY_RIGHT] = true;
//					return true;
//				}
//			}
//		}
		
		return false;
	}
	
	private boolean detectGap(){
		if(!isEmpty(9,10) || isEmpty(10,10)){
			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_RIGHT] = true;
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
