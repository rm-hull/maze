# maze

Maze generator and solver, served up with functional Clojure & Noir, and
front-end fun in ClojureScript with rendering in HTML5 canvas. See 
http://maze.destructuring-bind.org for work-in-progress. 

## TODO

* Solver algorithm

* View implementation

* Client-side scripting for rendering maze & animating backtracking path

* Roll the ball round the maze & against the clock -- device orientation 
  and motion API integration

## Implementation details

The core generator algorithm loosely based on the pseudo-code presented at
http://www.mazeworks.com/mazegen/mazetut/ and the ruby code at 
http://blade.nagaokaut.ac.jp/cgi-bin/scat.rb/ruby/ruby-talk/141783, but is
purely functional in nature (i.e. no mutating state). In order to generate
random pathways, a visitor higher-order function is required when the 
generator is called.

The generated mazes are random, and each is a perfect maze, in-as-much as 
there is always a path between every two points, for example:

    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
    |   |       |               |           |           |           |           |   |
    +   +   +---+   +   +---+   +   +---+   +   +   +---+   +   +   +---+   +   +   +
    |   |       |   |   |           |           |   |       |   |           |       |
    +   +---+   +   +   +---+---+---+---+---+---+---+   +---+   +---+---+---+---+   +
    |       |       |                       |       |   |       |                   |
    +---+   +   +---+---+---+---+---+---+   +   +   +   +   +---+   +---+---+   +---+
    |       |       |           |       |       |       |   |       |       |       |
    +   +---+---+   +---+   +   +   +   +---+---+---+---+   +---+---+   +   +---+   +
    |           |       |   |   |   |   |       |       |               |   |   |   |
    +---+---+   +---+   +   +   +   +   +   +   +---+   +---+---+---+---+   +   +   +
    |       |   |           |       |       |   |       |       |           |       |
    +   +   +   +   +---+---+---+---+---+---+   +   +   +   +---+   +---+---+   +---+
    |   |   |   |   |           |                   |   |   |       |       |       |
    +   +---+   +---+   +---+   +   +---+---+---+---+   +   +   +---+   +   +---+   +
    |   |       |       |       |   |           |           |       |   |   |       |
    +   +   +---+   +---+---+   +---+   +---+   +---+---+---+---+   +   +---+   +---+
    |   |   |               |       |   |   |       |               |   |       |   |
    +   +   +---+---+---+   +---+   +   +   +---+   +   +---+---+---+   +   +---+   +
    |   |               |       |       |       |   |               |       |       |
    +   +---+---+---+   +---+   +---+---+---+   +   +---+---+   +   +   +---+   +   +
    |               |           |   |           |   |       |   |   |   |       |   |
    +   +   +---+---+---+---+---+   +   +   +---+   +   +   +---+   +   +   +---+   +
    |   |                           |   |       |   |   |           |   |       |   |
    +   +---+---+---+---+---+---+   +   +---+   +   +   +---+---+---+   +   +   +   +
    |       |                   |           |   |   |       |   |       |   |   |   |
    +---+   +---+   +---+---+   +---+---+---+   +   +   +   +   +   +---+---+   +   +
    |   |       |       |               |       |   |   |   |       |           |   |
    +   +---+   +---+   +   +---+---+   +   +---+   +---+   +   +---+---+   +---+   +
    |       |       |   |   |       |   |           |       |               |       |
    +   +---+---+   +   +---+   +   +   +---+---+---+   +---+---+---+---+---+   +---+
    |           |   |           |   |           |               |           |       |
    +   +   +---+   +   +---+---+   +---+---+   +   +---+---+   +---+---+   +---+   +
    |   |       |   |   |       |   |       |   |   |       |   |               |   |
    +   +---+   +   +   +   +   +   +   +---+   +   +   +   +   +   +---+---+   +   +
    |       |       |       |   |   |       |   |       |   |   |   |           |   |
    +---+   +---+---+---+---+---+   +   +   +   +   +---+---+   +   +---+   +---+   +
    |   |   |                       |   |       |   |       |   |       |       |   |
    +   +   +---+   +---+---+---+---+   +---+---+---+   +   +   +   +   +---+---+   +
    |               |                                   |       |   |               |
    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+

## Usage

Run locally in development mode with `lein run` and browse to http://localhost:8080.
Note, requires leiningen 2.0

## References

* http://christian-helle.blogspot.co.uk/2012/03/accessing-accelerometer-from-html5-and.html

* http://menscher.com/teaching/woaa/examples/html5_accelerometer.html 

* http://blade.nagaokaut.ac.jp/cgi-bin/scat.rb/ruby/ruby-talk/141783
 
* http://www.mazeworks.com/mazegen/mazetut/
