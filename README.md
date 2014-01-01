# maze

Maze generator and solver, served up with functional Clojure & Noir, and
front-end fun in ClojureScript with rendering using HTML5 canvas. See here
for some variations:

* http://maze.destructuring-bind.org - default maze generator.

* http://maze.destructuring-bind.org?draw=path - default maze generator & solver.

* http://maze.destructuring-bind.org?draw=snake&cell-size=6&count=5 - snake solver.

* http://maze.destructuring-bind.org?draw=snail&cell-size=6 - snail-trails.

[Works on Chromium, Firefox, iPad - haven't tried the others - feedback welcome]

## TODO

* Follow the mouse clicks - when in snake or trails mode, clicking/tapping
  on the maze will cause the path to be re-evaluated with the end point at
  that mouse click.

* iPad game: roll a ball round the maze & against the clock -- device 
  orientation and motion API integration

## Implementation details

The core generator algorithm loosely based on the pseudo-code presented at
http://www.mazeworks.com/mazegen/mazetut/ and the ruby code at 
http://blade.nagaokaut.ac.jp/cgi-bin/scat.rb/ruby/ruby-talk/141783, but is
purely functional in nature (i.e. no mutating state). In order to generate
random pathways, a visitor higher-order function is required when the 
generator is called.

The generated mazes are random, and each is a perfect maze, in-as-much as 
there is always a path between every two points and there are no islands, 
for example:

    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
    | # |           |               |                   |                   |       |
    +   +---+---+   +   +---+   +   +---+   +---+---+   +---+---+   +---+   +---+   +
    | #   #   # |   |   |       |       |   |       |               |   |       |   |
    +---+---+   +   +   +   +---+---+   +   +   +---+---+---+---+---+   +---+   +   +
    |       | # |       |       |       |   |               |               |       |
    +   +   +   +---+---+---+   +---+   +   +---+---+   +   +   +---+---+   +---+   +
    |   |   | #   # | #   # |       |       |       |   |   |   |               |   |
    +   +   +---+   +   +   +   +   +---+---+   +   +---+   +   +   +---+---+---+   +
    |   |       | #   # | # |   |               |       |   |   |                   |
    +   +---+   +---+---+   +   +---+---+---+---+---+   +   +   +---+---+   +---+---+
    |   |       | #   #   # |           |   |           |       |       |           |
    +   +---+---+   +---+---+---+---+   +   +   +   +---+---+---+   +   +---+---+---+
    |           | #   # | #   # |           |   |   |               |               |
    +   +---+   +---+   +   +   +---+---+---+   +---+   +---+---+---+---+   +---+   +
    |       |       | #   # | # | #   #   # |                   |       |       |   |
    +---+   +---+   +---+---+   +   +---+   +   +---+---+---+---+   +   +---+---+   +
    |   |       |           | #   # |   | # |       |       |       |           |   |
    +   +   +---+   +   +---+---+---+   +   +---+---+   +   +   +---+---+---+   +   +
    |   |   |       |   |       | #   #   # |       |   |       |       |       |   |
    +   +   +   +---+---+   +   +   +---+---+   +   +   +   +---+   +   +   +---+   +
    |       |               |   | # |           |       |   |       |   |   |       |
    +   +---+---+---+---+---+   +   +   +---+   +---+---+---+   +---+   +   +---+   +
    |   |           |           | # |   |   |               |       |   |           |
    +   +   +---+   +   +---+---+   +   +   +---+---+---+   +---+   +   +---+---+---+
    |   |   |       |       | #   # |   |       | #   # |           |               |
    +   +   +---+---+---+   +   +---+   +   +   +   +   +---+---+---+---+---+---+   +
    |       |               | # |   |       |   | # | #   #   #   #   #   # |       |
    +   +---+   +---+---+---+   +   +---+---+   +   +---+---+---+---+---+   +   +   +
    |   |       | #   #   #   # |               | #   # |   |           | # |   |   |
    +---+   +---+   +---+---+---+---+---+---+---+---+   +   +   +   +   +   +   +   +
    |       | #   # |     #   #   #   #   #   #   #   # |       |   |   | # |   |   |
    +   +---+   +---+---+   +---+---+---+---+---+---+---+---+---+   +   +   +   +---+
    | #   # | # |     #   # |               |       |               |   | # |       |
    +   +   +   +   +   +---+   +---+---+   +   +   +   +---+---+---+---+   +---+   +
    | # | #   # |   | # |       |               |       |       | #   #   # | #   # |
    +   +---+---+---+   +   +   +---+   +---+---+---+---+   +   +   +---+---+   +   +
    | #   # | #   #   # |   |       |       |       |       |   | # |     #   # | # |
    +---+   +   +---+---+   +---+   +---+---+   +   +   +---+   +   +---+   +---+   +
    |     #   # |               |               |           |     #   #   # |     # |
    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+

## Usage

Run locally in development mode with `lein run` and browse to http://localhost:8080.
Note, requires leiningen 2.0

## References

* http://christian-helle.blogspot.co.uk/2012/03/accessing-accelerometer-from-html5-and.html

* http://menscher.com/teaching/woaa/examples/html5_accelerometer.html 

* http://blade.nagaokaut.ac.jp/cgi-bin/scat.rb/ruby/ruby-talk/141783
 
* http://www.mazeworks.com/mazegen/mazetut/

* http://lukevanderhart.com/2011/09/30/using-javascript-and-clojurescript.html



[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/rm-hull/maze/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

