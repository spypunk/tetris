# tetris - Just another Tetris™ clone ![alt tag](src/main/resources/img/icons/icon.png)

[![Release](https://img.shields.io/badge/latest%20release-1.13.0-brightgreen.svg)](https://github.com/spypunk/tetris/releases/tag/1.13.0) [![Build Status](https://travis-ci.org/spypunk/tetris.svg?branch=master)](https://travis-ci.org/spypunk/tetris) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4d05e805b7ad406a82f10e7900fb497a)](https://www.codacy.com/app/spypunk/tetris?utm_source=github.com&utm_medium=referral&utm_content=spypunk/tetris&utm_campaign=Badge_Grade) [![Download tetris](https://img.shields.io/sourceforge/dt/spypunk-tetris.svg)](https://sourceforge.net/projects/spypunk-tetris/files/latest/download) [![License](http://www.wtfpl.net/wp-content/uploads/2012/12/wtfpl-badge-4.png)](http://www.wtfpl.net/) [![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/fold_left.svg?style=social&label=Follow)](https://twitter.com/spypunkk)

## How it looks ?

![alt tag](img/screenshot_start.png)

![alt tag](img/screenshot.png)

![alt tag](img/screenshot_pause.png)

![alt tag](img/screenshot_gameover.png)

![alt tag](img/screenshot_name_input.png)

![alt tag](img/screenshot_score_table.png)

## How to build it ?

You will need a Java JDK 8+ and maven 3+.

Execute **mvn clean package assembly:single** to build the release package.

# How to run it ?
```
mvn clean

mvn package

mvn exec:java

```

## How to play ?

- SPACE - Start a new game

- LEFT - Move the current shape to the left

- RIGHT - Move the current shape to the right

- DOWN - Move the current shape down

- UP - Rotate the current shape clockwise

- CTRL - "Hard drop" current shape

- P - Pause the current game

- H - Show the high scores

- M - Mute sound

- PAGE UP - Increase the volume

- PAGE DOWN - Decrease the volume

## What about license ?

This project is licensed under the WTFPL (Do What The Fuck You Want To Public License, Version 2)

[![WTFPL](http://www.wtfpl.net/wp-content/uploads/2012/12/logo-220x1601.png)](http://www.wtfpl.net/)

Copyright © 2016-2017 spypunk [spypunk@gmail.com](mailto:spypunk@gmail.com)

This work is free. You can redistribute it and/or modify it under the terms of the Do What The Fuck You Want To Public License, Version 2, as published by Sam Hocevar. See the COPYING file for more details.
