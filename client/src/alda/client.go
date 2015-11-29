package main

import (
	"fmt"
	"os"

	"github.com/codegangsta/cli"
)

func main() {
	app := cli.NewApp()
	app.Name = "alda"
	app.Version = "0.14.2"
	app.Usage = "a music language for musicians ♬ ♪"

	// global options
	app.Flags = []cli.Flag{
		cli.BoolFlag{
			Name:  "V, verbose",
			Usage: "Show verbose output",
		},
		cli.IntFlag{
			Name:  "p, port",
			Value: 27713,
			Usage: "Specify the port of the Alda server to use",
		},
		cli.IntFlag{
			Name:  "b, pre-buffer",
			Value: 0,
			Usage: "The number of milliseconds of lead time for buffering",
		},
		cli.IntFlag{
			Name:  "B, post-buffer",
			Value: 1000,
			Usage: "The number of milliseconds to wait after playing the score, before exiting",
		},
		cli.BoolFlag{
			Name:  "s, stock",
			Usage: "Use the default MIDI soundfont of your JVM, instead of FluidR3",
		},
	}

	// alda commands
	app.Commands = []cli.Command{
		{
			Name:  "server",
			Usage: "Manage Alda servers",
			Subcommands: []cli.Command{
				{
					Name:    "start",
					Aliases: []string{"up"},
					Usage:   "Start an Alda server",
					Action: func(c *cli.Context) {
						fmt.Println("starting server on port", c.GlobalInt("port"))
					},
				},
				{
					Name:    "stop",
					Aliases: []string{"down"},
					Usage:   "Stop the Alda server",
					Action: func(c *cli.Context) {
						fmt.Println("stopping server on port", c.GlobalInt("port"))
					},
				},
				{
					Name:    "restart",
					Aliases: []string{"downup"},
					Usage:   "Stop and restart the Alda server",
					Action: func(c *cli.Context) {
						fmt.Println("restarting server on port", c.GlobalInt("port"))
					},
				},
			},
		},
		{
			Name:  "version",
			Usage: "Display the version of the Alda server",
			Action: func(c *cli.Context) {
				fmt.Println("getting version of server running on port", c.GlobalInt("port"))
			},
		},
		{
			Name:  "play",
			Usage: "Evaluate and play Alda code",
			Flags: []cli.Flag{
				cli.StringFlag{
					Name:  "f, file",
					Usage: "Read Alda code from a file",
				},
				cli.StringFlag{
					Name:  "c, code",
					Usage: "Supply Alda code as a string",
				},
				cli.StringFlag{
					Name:  "F, from",
					Usage: "A time marking or marker from which to start playback",
				},
				cli.StringFlag{
					Name:  "T, to",
					Usage: "A time marking or marker at which to end playback",
				},
			},
			Action: func(c *cli.Context) {
				file := c.String("file")
				code := c.String("code")
				validateFileAndCodeArgs(file, code)

				if file != "" {
					// send file as input stream (ddliu/go-httpclient can do this)
				} else {
					// send code directly
				}

				fmt.Println("playing code: ", code)
			},
		},
		{
			Name:  "parse",
			Usage: "Display the result of parsing Alda code",
			Flags: []cli.Flag{
				cli.StringFlag{
					Name:  "f, file",
					Usage: "Read Alda code from a file",
				},
				cli.StringFlag{
					Name:  "c, code",
					Usage: "Supply Alda code as a string",
				},
				cli.BoolFlag{
					Name:  "l, lisp",
					Usage: "Return the resulting Clojure (alda.lisp) code",
				},
				cli.BoolFlag{
					Name:  "m, map",
					Usage: "Evaluate the score and return the resulting score map",
				},
			},
			Action: func(c *cli.Context) {
				file := c.String("file")
				code := c.String("code")
				validateFileAndCodeArgs(file, code)

				lispcode := c.Bool("lisp")
				scoremap := c.Bool("map")
				endpoint := validateLispAndMapFlags(lispcode, scoremap)

				if file != "" {
					// send file as input stream (ddliu/go-httpclient can do this)
				} else {
					// send code directly
				}

				fmt.Println("parsing code:", code)
				fmt.Println("endpoint:", endpoint)
			},
		},
	}

	app.Run(os.Args)
	os.Exit(0)
}

func validateFileAndCodeArgs(file string, code string) {
	if file == "" && code == "" {
		fmt.Fprintf(os.Stderr, "You must supply a string or a file containing Alda code.")
		os.Exit(1)
	}

	if file != "" && code != "" {
		fmt.Fprintf(os.Stderr, "You must either supply a --code or a --file argument, not both.")
		os.Exit(1)
	}
}

func validateLispAndMapFlags(lispcode bool, scoremap bool) string {
	if lispcode && scoremap {
		fmt.Fprintf(os.Stderr, "You must include either the --lisp or --map flag, not both.")
		os.Exit(1)
		return ""
	} else if lispcode {
		return "/parse/lisp"
	} else if scoremap {
		return "/parse/map"
	} else {
		return "/parse"
	}
}
