Edited MyAgent’s isEmpty method. Now looks for coins as well and does not consider those spaces to be full-unless there is an enemy on top of the coin as well.

Added in several cases for Mario to consider when running through stage-mainly he tries to avoid enemies without jumping if possible.
Mario will also shoot fireballs/ sprint as well if enemies are in front of him.

Can reliably pass easiest difficulty level but that’s it. However, when there are many enemies in front several blocks high, especially flying enemies that move up and down, Mario doesn’t really know how to avoid them. Also problems controlling jump height, sometimes jumps too far and lands into pits. This might be an issue with the order of my conditions.