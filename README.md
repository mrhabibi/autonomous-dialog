[ ![Download](https://api.bintray.com/packages/mrhabibi/maven/persistent-dialog/images/download.svg) ](https://bintray.com/mrhabibi/maven/persistent-dialog/_latestVersion)

# Persistent Dialog
A wrapper for building independent and persistent dialog with many features as an alternative to AlertDialog and DialogFragment on Android.

## Comparation

So, here it is, what you are looking for, the straight comparation between Persistent Dialog with native Alert Dialog or native Dialog Fragment:

|                                                  | AlertDialog | Dialog Fragment | Persistent Dialog |
|--------------------------------------------------|-------------|-----------------|-------------------|
| Saving state ability                             |      X      |        V        |         V         |
| Persistent after rotation                        |      X      |        V        |         V         |
| No leak while dismissing                         |      X      |        V        |         V         |
| Can be shown without FragmentManager             |      V      |        X        |         V         |
| Can be shown using ContextWrapper                |      V      |        X        |         V         |
| Won't get IllegalStateException when dismissing  |      V      |        X        |         V         |
| Dismissing asynchronously                        |      X      |        X        |         V         |
| Can be shown independently                       |      X      |        X        |         V         |
| Can be bonded with Activity for advanced styling |      X      |        X        |         V         |
| Single dialog checking & handling (Singleton)    |      X      |        X        |         V         |

## Installation

### Gradle

Add this line in your `build.gradle` file:

```
   compile 'com.mrhabibi:persistent-dialog:1.0.3'
```

This library is hosted in the [JCenter repository](https://bintray.com/mrhabibi/maven), so you have to ensure that the repository is included:

```
buildscript {
   repositories {
      jcenter()
   }
}
```



## Contributions

Feel free to create issues and pull requests.

## License

```
Persistent Dialog library for Android
Copyright (c) 2017 Muhammad Rizky Habibi (http://github.com/mrhabibi).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```