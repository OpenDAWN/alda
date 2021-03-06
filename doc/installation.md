# Installing Alda

* [Mac OS X / Linux](#mac-os-x--linux)
* [Windows](#windows)
* [Editor Plugins](#editor-plugins)

## Mac OS X / Linux

The executable file `alda` in the `bin` directory of this repository is a standalone executable script that can be run from anywhere. It will retrieve the latest release version of Alda and run it, passing along any command-line arguments you give it.

* To install Alda, simply copy the `alda` script from this repo into any directory in your `$PATH`, e.g. `/bin` or `/usr/local/bin`:

        curl https://raw.githubusercontent.com/alda-lang/alda/master/bin/alda -o /usr/local/bin/alda && chmod +x /usr/local/bin/alda

* This script requires the Clojure build tool [Boot](http://www.boot-clj.com), so you will need to have that installed as well. Mac OS X users with [Homebrew](https://github.com/homebrew/homebrew) can run `brew install boot-clj` to install Boot. Otherwise, [see here](https://github.com/boot-clj/boot#install) for more details about installing Boot.

Once you've completed the steps above, you'll be able to run `alda` from any working directory. Running the command `alda` by itself will display the help text.

### Updating Alda

Alda comes in two pieces: the Alda library (the part that does all the work) and the `alda` start script.

The start script will rarely need to be updated, but if you ever do need to get the latest version, you can do so by running the following command:

    alda script > /usr/local/bin/alda

The Alda library will keep itself updated on your computer each time you run it.

## Windows

The `alda` script doesn't seem to work for Windows users. If you're a Windows power user, [please feel free to weigh in on this issue](https://github.com/alda-lang/alda/issues/48). Until we have that sorted out, there is a workaround:

1. Install [Boot](https://github.com/boot-clj/boot#windows).
2. Clone this repo and `cd` into it.
3. You can now run `boot alda -x "<cmd> <args>"` while you are in this directory.

Examples:

* `boot alda -x repl` to start the Alda REPL
* `boot alda -x "play --code 'piano: c d e f g'"`

Caveats:

* It's more typing.
* It only works if you're in the Alda repo folder.
* Unlike the `alda` script, running the `boot alda` task will not automatically update Alda; you will have to do so manually by running `git pull`.
* If the command you're running is longer than one word, you must wrap it in double quotes -- see the examples above.

## Editor Plugins

For the best experience when editing Alda score files, install the Alda file-type plugin for your editor of choice.

> Don't see a plugin for your favorite editor? Write your own and add it here! :)

- [Sublime Text](https://github.com/archimedespi/sublime-alda)
- [Atom](https://github.com/MadcapJake/language-alda)
- [Vim](https://github.com/daveyarwood/vim-alda)

