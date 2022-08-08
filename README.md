# dualgraph

Dualgraph is a Java application that solves graph algorithms.
The project is related to the bachelor's thesis on the topic "Development of an application for the visualization of graph algorithms".
Application offers graph editor (user can create/modify graphs) and graph algorithms can be applied to any graph which user has made.


Project implementation supports adding new custom graph algorithms.
Custom algorithm classes must be extended from parent class *Algorithm* and must be stored in package "sk.dualnexon.dualgraph.lib.algorithm", so the reflection system can easily find all algorithm classes and automatically fill them into the menu of available algorithms for user.

The application has already been presented on final exams, so I can flag the project as finished, but because of the lack of good graph algorithm multi-tool apps I'll continue with this project in my spare time to make great graph algorithm tool for people.

For people reading my bachelor thesis and checking the source code - the last commit related to bachelor thesis can be found [here](https://github.com/DualNeXoN/dualgraph/commit/f4809f4795be8cdbd70fbf82f3ec9c06c9c5e403).
