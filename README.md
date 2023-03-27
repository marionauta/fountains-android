# Fountains Android

Android app to search for drinking fountains nearby.

## KMM

This project contains a [Kotlin Multiplatform Mobile][kmm] library to share
kotlin code with the [iOS app][ios]. You don't need to do anything for Android.
To compile the library for iOS, just run on the root folder:

```sh
./gradlew :WaterFountains:assembleXCFramework
```

[kmm]: https://kotlinlang.org/lp/mobile/
[ios]: https://github.com/marionauta/fountains-ios
