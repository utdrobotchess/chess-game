Chess Game App
========
This repository contains the source code developed for the Chessgame
Application to be used in Robotchess demos. The Robotchess project is developed
and hosted by the University of Texas at Dallas.

![Alt text](/resources/chessboard.jpg?raw=true)

For more information about the UTDallas Robotchess project, visit our website
at http://www.utdallas.edu/robotchess.

Contents
--------
* lib/
  - Contains java libraries needed to compile the app.

* log/
  - Contains the log4j configuration file as well as the .log files that are
generated when the app is run.

* resources/
  - Contains pictures for the app as well as the github repo.

* src/edu/utdallas/robotchess
  - Contains all source code for the app

* test/edu/utdallas/robotchess
  - Contains the source code for the unit tests for the app.

* build.xml
  - makefile for the Ant build system that our app uses. For more information
about Ant: http://ant.apache.org

Running the app
--------
If you are familiar with Ant, then you should be able to understand how to run
the app by inspecting the build.xml. Regardless, to run the app you need to
clone this repo, cd to this directory with some kind of shell, and enter 'ant run'.
You must have the ant command line tool installed to do this.

Issues
--------
We track issues using the github issue tracker. If you feel that something is
missing or broken, please visit our issues site: (https://github.com/utdrobotchess/chessbot/issues).
Reading the open issues here may help you understand why something isn't
working the way you expect it to.
