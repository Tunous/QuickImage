# QuickImage

Simple application for quick creation of image links.

# Features

- Integration with android share menu
- Automatic copy to clipboard
- History of all uploaded images

# Build

- Register an application on Imgur [here](https://api.imgur.com/oauth2/addclient).
    - You can enter into the linked form whatever you want.
- Create a `gradle.properties` file with the following content:

```
IMGUR_API_CLIENT_ID="<Your Client ID goes here>"
```

- Build using Gradle

```
./gradlew assembleDebug
```

# Screenshots

<img src="art/upload-history.png" width="200px" />
<img src="art/notification.png" width="200px" />

# License

```
Copyright 2017 Łukasz Rutkowski

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
