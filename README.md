# RPG-Engine
This project is a "simple" rpg engine with a Construction Set (CS) for editing the data files.

The project currently loads most images from paths stored in the "sprites.xml" file. 
Item and Entity creation systems are working now, so all Items and entities are dynamically created at runtime.

# Dependencies
This project requires JDOM for the XML loading.
JDOM can be downloaded here: http://www.jdom.org/downloads/
You just need to extract the "jdom-2.0.6.jar" from the zip, and make sure it's linked in the build path

# Using The Construction Set
The CS requires a "Root Directory" to be set before it will open. Currently, you can not create a new directory. 
The root directory is where all the game assets you will be editing with the CS are stored. In this case, the Root 
is the "res" folder within our "Simple RPG Engine" folder.

# To Do
- Create generic status effects in the game engine, that can be customized in the CS.
- Create a global variables XML file, to remove many hard coded and repeated sections (Mainly in skeletons and limbs)
- Get the NPC editor working in CS
- Make all editors in the CS able to preview existing XML nodes (graphically how they show up in game)
- Make all editors able to EDIT their associated XML nodes

Much, much more
