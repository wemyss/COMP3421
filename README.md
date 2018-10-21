# COMP3421 Assignment 2

## Abstract
"Non cross OS compatible Red Dead Redemption themed Game Engine!"

Released two years ahead of the long awaited sequel, Red Dead Redemption 2, this engine sits at the forefront of graphics technology, with support for fully customised sparse terrain generation, Lindenmayer parallel tree volumes, 3D convex vertex buffers and much more. The graphics pipeline of this engine unlike a conventional game engine, with a complex light vector computation forward rendered pass that is later combined with a physically based global illumination pass and a deferred ambient occulsion shader pass to render a final composit. 

Using the engine's unique ability to be highly functional and efficient at rendering static 3D scenes, NcOScRDRt Game Engine, outperforms a typical engine in the most common scenario - a static scene. This is achieved by working as close to the core of OpenGL as possible, by limiting the number of avaliable vertex buffers objects, and by enhancing run time performance using a JVM and ensuring the use of highly stable and performant Java code (JOGL).

If you wish to use the NcOScRDRt Game Engine as part of your assignment, please consider donating to _0x3f17f1962B36e491b30A40b2405849e597Ba5FB5_.

## Features implemented
- **Terrain**
   - Mesh generation
   - Interpolating altitudes
   - Textured
- **Trees**
   - Simple cactus trees (cylinder & sphere)
   - Textured
   - L-tree
      - iterations can be changed in the `JSON` file
      - base rule can be changed in the `JSON` file
- **Roads**
   - Bezier curve road mesh
   - Road goes up and down hills
   - Textured
- **Avatar**
   - Simple avatar
   - Can switch between 3rd person & 1st
   - Textured
- **Other**
   - Board of "Spirit"
   - Uses VBO & shaders
   - Textured
- **Lighting**
   - Simple lighting
   - Face normals for game objects
   - Sunrise <--> Sunset mode (sun angle and color)

## Key Mapping
- `UP`: move forward
- `DOWN`: move backward
- `LEFT`: rotate view left
- `RIGHT`: rotate view right
- `A`: sidestep along X axis
- `D`: sidestep along X axis
- `1`: switch to 1st person view
- `3`: switch to 3rd person view
- `,`: move sun towards sunrise
- `.`: move sun towards sunset

## Contributors
- Kirsten Hendriks, z5018670
- Sam Wemyss, z5019350
