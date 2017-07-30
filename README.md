# abacus
[![Build Status](https://travis-ci.org/DanilaFe/abacus.svg?branch=master)](https://travis-ci.org/DanilaFe/abacus)

Summer project for NWAPW.
Created by Arthur Drobot, Danila Fedorin and Riley Jones.

## Project Description
Abacus is a calculator built with extensibility and usability in mind. It provides a plugin interface, via Java, as Lua proves too difficult to link up to the Java core. The description of the internals of the project can be found on the wiki page.

## Current State
Abacus is being built for the Northwest Advanced Programming Workshop, a 3 week program in which students work in teams to complete a single project, following principles of agile development. Because of its short timeframe, Abacus is not even close to completed state. Below is a list of the current features and problems.
- [x] Basic number class
- [x] Implementation of basic functions 
- [x] Implementation of `exp`, `ln`, `sqrt` using the basic functions and Taylor Series
- [x] Plugin loading from JAR files
- [x] Regular expression pattern construction and matching
- [x] Infix and postfix operators
- [ ] __Correct__ handling of postfix operators (`12+!3` parses to `12!+3`, which is wrong)
- [ ] User-defined precision
 
## Project Proposal
>There is currently no calculator that is up to par with a sophisticated programmer's needs. The standard system ones are awful, not respecting the order of operations and having only a few basic functions programmed into them. The web ones are tied to the Internet and don't function offline. Physical ones like the TI-84 come close in terms of functionality, but they make the user have to switch between the computer and the device. 
>
>My proposal is a more ergonomic calculator for advanced users. Of course, for a calculator, being able to do the actual math is a requirement. However, in this project I also would like to include other features that would make it much more pleasant to use. The first of these features is a wide collection of built in functions, designed with usefulness and consistency in mind. The second is scripting capabilities - most simply using Lua and its provided library. By allowing the users to script in a standardized language that isn't TI-BASIC, the calculator could simplify a variety of tasks and not have to clutter up the default provided functions with overly specific things. Lastly, it's important for the calculator to have a good design that doesn't get in the way of its use, on the two major desktop platforms (macOS and Windows). 
>
>With these features I believe that this is a calculator that I would use (and frequently find myself wanting to use). It also seems to have a diverse array of tasks, such as UI design, implementing the math functions to be fast and optimized (fast inverse square root, anyone?), parsing code, and working with Lua integration.
