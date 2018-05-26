# godot-local-notification

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg?longCache=true&style=flat-square)](https://github.com/xsellier/godotlocalnotification)
[![Godot Engine](https://img.shields.io/badge/GodotEngine-2.1-orange.svg?longCache=true&style=flat-square)](https://github.com/godotengine/godot)
[![LICENCE](https://img.shields.io/badge/License-MIT-green.svg?longCache=true&style=flat-square)](https://github.com/xsellier/godotlocalnotification/blob/master/LICENSE)

Android local notification for Godot Engine 2.1, including:

* Schedule a notification
* Cancel a notification

# Usage

## Compilation

[Prerequisites documentation](http://docs.godotengine.org/en/2.1/development/compiling/compiling_for_android.html)

```sh
export CXX=g++
export CC=gcc

# This one is optional
export SCRIPT_AES256_ENCRYPTION_KEY=YOUR_ENCRYPTION_KEY

# Place where the NDK/SDK are
export ANDROID_HOME=/usr/lib/android-sdk
export ANDROID_NDK_ROOT=/usr/lib/android-sdk/ndk-bundle

# Godot engine source directory
cd ./godot

scons -j2 CXX=$CXX CC=$CC platform=android tools=no target=debug
scons -j2 CXX=$CXX CC=$CC platform=android tools=no target=debug android_arch=x86
scons -j2 CXX=$CXX CC=$CC platform=android tools=no target=release
scons -j2 CXX=$CXX CC=$CC platform=android tools=no target=release android_arch=x86

cd platform/android/java
./gradlew clean
./gradlew build
cd -
```

## Edit Godot engine settings

### Using compiled templates

`Export` > `Android` > `Custom Package`, change fields `Debug` and `Release` to use the compiled android's templates (`bin` directory).

#### Loading the module

Edit `engine.cfg` and add an `android` part as following:

```ini
[android]
modules="org/godotengine/godot/GodotLocalNotification"
```

#### Initializing the module using GDScript

Here is an example

```python
extends Node

onready var godot_local_notification = Globals.get_singleton('GodotLocalNotification')

func _ready():
  if OS.get_name() == 'Android' and godot_local_notification != null:
    godot_local_notification.init(get_instance_ID())
  else:
    godot_local_notification = null

func schedule_local_notification(title, message, delay, notification_id):
  if godot_local_notification != null:
    godot_local_notification.schedule_local_notification(title, message, delay, notification_id)

func cancel_local_notification(notification_id):
  if godot_local_notification != null:
    godot_local_notification.cancel_local_notification(notification_id)
```

# License

[See LICENSE file](./LICENSE)
