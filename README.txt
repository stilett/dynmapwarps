Compilation
===========
cd <dynmapwarps directory>
ant

DynmapWarps.jar has been created in out/ directory.


Installation
============
1. Place DynmapWarps.jar to plugins/ folder
2. Merge the contents of dynmap folder with plugins/dynmap
3. Put the following lines to plugins/dynmap/configuration.txt
  - class: org.dynmap.ClientComponent
    type: warps


Configuration
=============
Configuration is located in plugins/DynmapWarps/config.yml. It is automatically created after the first run.

disable-homes: true  -- do not show homes in the map
disable-warps: true  -- do not show warps in the map

