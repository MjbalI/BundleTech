# BundleTech
Adds interactions between dispensers and bundles.  

![An extremely high-quality rendition of a dispenser block in isometric view, with a leather pouch sitting slightly closer and to the right](/src/main/resources/assets/bundletech/icon.png "No AI used here cr*phead")

### If a dispenser "uses" a bundle:
If the dispenser is not facing into a container:
   * The dispenser will empty the bundle, shooting out a stack of each contained item type in the same manner it would shoot an item normally.
   * If the bundle is empty upon activation, it will remain in the dispenser.  

If the dispenser is facing into a container:
   * The dispenser will try to fill the bundle using the first occupied slot in the container
     * This is equivalent to a player grabing the bundle and clicking with it over the first slot in the container.
   * Nothing will happen if the bundle is full or the container is empty.
   * Will pull from only the appropriate slots for sided containers (e.g. furnaces)



\* Note: This is my first mod, and I haven't worked in Java much before. Please yell at me about things being written poorly under the issues tab (as long as the criticism is actually constructive). 