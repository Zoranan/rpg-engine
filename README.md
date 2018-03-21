# RPG-Engine
This project is a "simple" rpg engine with a Construction Set (CS) for editing the data files.

The project currently loads most images from paths stored in the "sprites.xml" file. 
Item and Entity creation systems are working now, so all Items and entities are dynamically created at runtime.

## Dependencies
This project requires JDOM for the XML loading.
JDOM can be downloaded here: http://www.jdom.org/downloads/
You just need to extract the "jdom-2.0.6.jar" from the zip, and make sure it's linked in the build path

## Using The Construction Set
The CS requires a "Root Directory" to be set before it will open. Currently, you can not create a new directory. 
The root directory is where all the game assets you will be editing with the CS are stored. In this case, the Root 
is the "res" folder within our "Simple RPG Engine" folder.

### To Do
GE = Game Engine, 
CS = Construction Set
- (GE) Create generic Status Effects in the game engine, that can be customized in the CS
- (GE) Set up basic NPC creation in game worlds (No behaviors or stats yet)
~~- (CS) Move "Edit" and "New" buttons inside the XmlExplorer and use an interface for custom actions~~
- ~~(CS) Streamline the creation of CS tabs with a custom class, and move all tabs to the new format~~
- (CS) Get the NPC editor working
- (CS) Create a Stat Editor, to define what stats each Mob in game will have
- (CS) Create a Status Effect editor (requires Stat Editor)
- (CS) Add Stats to NPC editor, and allow values to be set per stat (will need a new type of CompountComponent to handle this)
- (CS) Add a preview for the Models tab
- (CS) Add a preview for the Races tab
- (CS) Add a preview for the NPC tab

Much, much more
