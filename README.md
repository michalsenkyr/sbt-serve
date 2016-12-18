# sbt-serve

WARNING: This project is still in its pre-alpha stage and is not ready for general usage.

Auto-plugin for easy serving of static files.

Simply reads a source directory and serves its contents.

## Usage

1. Add the plugin to your project (in `plugins/serve.sbt`):

```scala
addSbtPlugin("com.github.michalsenkyr" % "sbt-serve" % "1.0.0-SNAPSHOT")
```

2. Enable the plugin in your `build.sbt`:

```scala
enablePlugins(SbtServePlugin)
```

3. Run `sbt serve`

## Configuration

New project settings:

- `port in serve` - Port on which to serve the files (default 8000)
- `sourceDirectory in serve` - Directory to serve (default src/main/web)

sbt-serve uses [Spray.io](https://spray.io) internally. Its Akka actor system name is "sbt-serve"