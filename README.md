# My Personal Project

## A subtitle

A *bulleted* list:
- item 1
- item 2
- item 3

An example of text with **bold** and *italic* fonts.  

## Crossy Road
  My application will be a Java implementation of the popular game crossy
road, where you try to cross a road without getting hit by cars and try
to get as far as you can. I might consider adding some of these features 
into my game. Anyone can use my application since it is a pretty simple 
game that anyone can play.

  This project is of interest to me because I enjoy coding and playing games
and crossy road is a game that I used to play a lot as a kid so I think it 
would be fun to code it.

Potential features:
- press W to move forward and press A and D to move to the left and right
- play with different characters and add new characters to list of characters
to choose from
- cars spawn and move from left to right of screen, game over if touched
- save and load games

## User Stories
- As a user, I want to be able to create characters with an image representation
and add multiple characters to a list of characters
- As a user, I want to be able to view and choose a character from the list of 
characters and play the game with chosen character
- As a user, I want to be able to pause, save, and load games
- As a user, I want to be given the option to save my entire game when I quit a game, and to save
my list of characters and high scores when I quit the application
- As a user, I want to be given the option to load a previous game when I choose play
- As a user, I want to be able to view and save my high scores
- As a user, I want to be able to move around in the game


# Instructions for Grader

- You can generate the first required action related to the user story "adding multiple Xs to a Y" by 
clicking the "characters" button from the Menu page and clicking the "add character" button which shows a 
window where you can input the name and size of your character. To finish adding the character, click the
"add character" button. Flip through the JPanel to the end to see the character you just added.
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by
clicking the "characters" button from the Menu page and clicking the "remove" button of a character. 
Doing this will remove the character from the JPanel.
- You can locate my visual component by clicking the "play" button from the Menu page or by clicking the 
"characters" button from the Menu page and adding a character if there aren't already characters in the JPanel.
- You can save the state of my application by clicking the "quit" button from the Menu page, and a window will
open asking you to select the components of the game you want to save. 
- You can reload the state of my application by opening the application and a window will open asking you
to select the components of the game you want to load. Only the components that aren't empty will show up and 
be able to be selected.

# Phase 4: Task 2
character added: gregor
character added: felix
character added: firas
character added: rajrupa
character added: florence
character added: karina
character removed: gregor
character removed: felix

# Phase 4: Task 3
If I had more time to work on the project, I would have a lot of refactoring to do due to many 
questionable design choices I made. First of all, I can use the Singleton pattern to decrease coupling. 
I can make my CrossyRoad class a singleton because there is only ever one of them in the program at any
time. I could also do the same with my MenuPanel, CharactersPanel and HighScoresPanel class. Looking 
at my UML diagram, I notice that some classes have a lot of fields of other classes which is suspicious
design since it leads to coupling. If I had more time, I would try to address this problem by decreasing
my dependencies. 