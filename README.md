# MilkVr-UMS-BatchMVRL
Create MilkVR .mvrl files from the Universal Media Server (Windows)

Note: This was a quick and easy project. Improvements can definitely be made, but functionality is there. This app parses HTML from the UMS web interface to locate all video files.

This application requires Universal Media Server and the web interface running.

Universal Media Server should also have transcoding disabled to allow seeking functionality
  - in UMS android devices can be detected as chromeCast. Disable chromeCast renderer in UMS to avoid this, or ensure proper android rendered is used. Restart of UMS may be needed 

Required files:
  
  1. MilkVrBatch-DLNA.jar
  2. dlna.config
  3. createMVRL.cmd

Steps:

1. Ensure UMS is running + web interface
2. connect to UMS web interface. Default address= http://\<serverIP\>:9001/
3. Browse until you find the desired media folder (example: http://\<serverIP\>:9001/browse/2918)
4. modify dlna.config file as needed.
    - webPort is the server port used to access the web interface (default 9001)
    - dlnaPort is the server port used to play files over dlna (default 5001)
    - folderUrl = desired media folder (example: http://\<serverIP\>:9001/browse/2918)
5. run createMVRL.cmd
6. mvrl folder is created
7. copy new .mvrl files from mvrl folder to MilkVR folder on android
8. Start MilkVR and run .mvrl files from sideloaded 
